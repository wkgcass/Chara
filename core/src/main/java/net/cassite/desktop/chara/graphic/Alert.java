// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.cassite.desktop.chara.util.StageUtils;
import net.cassite.desktop.chara.manager.FontManager;
import net.cassite.desktop.chara.util.Utils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class Alert {
    private Alert() {
    }

    private static final int MARGIN_HORIZONTAL = 40;
    private static final int MARGIN_VERTICAL = 30;
    private static final int FONT_SIZE = 64;
    private static final double MAX_OPACITY = (
        Utils.isMac() ? 0.65 : (
            Utils.isWindows() ? 0.85 : (
                Utils.isLinux() ? 0.65 :
                    0.65)));
    private static final int MAX_ALERT_TEXT_HEIGHT = 768;

    private static final Object _VALUE_ = new Object();
    private static final ConcurrentHashMap<AlertStage, Object> showingStages = new ConcurrentHashMap<>();

    /**
     * Show alert message
     *
     * @param msg msg to show
     */
    public static void alert(String msg) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> alert(msg));
            return;
        }
        Text foo = new Text(msg);
        foo.setFont(Font.font(FontManager.getFontFamily(), FONT_SIZE));
        var w = foo.getLayoutBounds().getWidth();
        var h = foo.getLayoutBounds().getHeight();

        if (h > MAX_ALERT_TEXT_HEIGHT) {
            h = MAX_ALERT_TEXT_HEIGHT;
        }

        Label label = new Label(msg);
        label.setFont(Font.font(FontManager.getFontFamily(), FONT_SIZE));
        label.setLayoutX(MARGIN_HORIZONTAL);
        label.setLayoutY(MARGIN_VERTICAL);
        label.setTextFill(new Color(1, 1, 1, 1));

        Stage tmpStage = StageUtils.createTransparentTemporaryUtilityStage();

        Stage stage = new Stage();
        stage.initOwner(tmpStage);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setWidth(w + MARGIN_HORIZONTAL * 2);
        stage.setHeight(h + MARGIN_VERTICAL * 2);
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setOpacity(0);

        Pane pane = new Pane();
        pane.setBackground(Background.EMPTY);
        pane.getChildren().add(label);

        Scene scene = new Scene(pane);
        scene.setFill(new Color(0x44 / 255D, 0x44 / 255D, 0x44 / 255D, 1));

        stage.setScene(scene);

        int duration = 1500;
        int msgLen = msg.getBytes(StandardCharsets.UTF_8).length;
        if (msgLen > 30) {
            duration += (msgLen - 30) * 50;
        }

        AlertStage alertStage = new AlertStage(stage, tmpStage, duration);
        showingStages.put(alertStage, _VALUE_);
        alertStage.animateShow();

        pane.setOnMouseClicked(e -> alertStage.onClicked());
        scene.setOnScroll(e -> {
            double d = e.getDeltaY();
            pane.setLayoutY(pane.getLayoutY() + d);
        });
    }

    public static void shutdown() {
        showingStages.keySet().forEach(AlertStage::hide);
        showingStages.clear();
    }

    private static class AlertStage {
        final Stage stage;
        final Stage tmpStage;
        final int duration;

        boolean isHiding = false;
        boolean isHidden = false;
        boolean isClicked = false;

        AlertStage(Stage stage, Stage tmpStage, int duration) {
            this.stage = stage;
            this.tmpStage = tmpStage;
            this.duration = duration;

            tmpStage.show();
            stage.show();
        }

        void onClicked() {
            if (isClicked) {
                return; // already clicked
            }
            isClicked = true;
            animateHide();
        }

        void animateShow() {
            new TimeBasedAnimationHelper(500,
                p -> {
                    if (isClicked) {
                        return; // is clicked to hide, so the animation should pause, let it hide
                    }
                    stage.setOpacity(p * MAX_OPACITY);
                })
                .setFinishCallback(() -> {
                    if (isClicked) {
                        return; // is clicked to hide, so the animation should pause, let it hide
                    }
                    Utils.delayNoRecord(duration, this::animateHide);
                })
                .play();
        }

        void animateHide() {
            if (isHidden) {
                return; // is already hidden, so no need to run hiding animation here
            }
            if (isHiding) {
                return; // is going to hide, no need to handle here
            }
            // should run animation to hide the stage
            isHiding = true;
            new TimeBasedAnimationHelper(500,
                p -> stage.setOpacity(MAX_OPACITY - p * MAX_OPACITY))
                .setFinishCallback(this::hide)
                .play();
        }

        void hide() {
            isHiding = false;
            if (isHidden) {
                return; // already hidden
            }
            isHidden = true;

            showingStages.remove(this);
            stage.hide();
            tmpStage.hide();
        }
    }
}
