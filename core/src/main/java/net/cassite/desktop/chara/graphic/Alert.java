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
import net.cassite.desktop.chara.util.Utils;

import java.util.concurrent.ConcurrentHashMap;

public class Alert {
    private Alert() {
    }

    private static final int MARGIN_HORIZONTAL = 40;
    private static final int MARGIN_VERTICAL = 30;
    private static final int FONT_SIZE = 64;
    private static final double MAX_OPACITY = 0.65;

    private static final Object _VALUE_ = new Object();
    private static final ConcurrentHashMap<Stage, Object> showingStages = new ConcurrentHashMap<>();

    public static void alert(String msg) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> alert(msg));
            return;
        }
        Text foo = new Text(msg);
        foo.setFont(new Font(FONT_SIZE));
        var w = foo.getLayoutBounds().getWidth();
        var h = foo.getLayoutBounds().getHeight();

        Label label = new Label(msg);
        label.setFont(new Font(FONT_SIZE));
        label.setLayoutX(MARGIN_HORIZONTAL);
        label.setLayoutY(MARGIN_VERTICAL);
        label.setTextFill(new Color(1, 1, 1, 1));

        Stage stage = new Stage();
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
        int msgLen = msg.getBytes().length;
        if (msgLen > 30) {
            duration += (msgLen - 30) * 50;
        }
        final int fDuration = duration;

        showingStages.put(stage, _VALUE_);
        stage.show();
        new TimeBasedAnimationHelper(500, 50 / HZ.UNIT,
            p -> stage.setOpacity(p * MAX_OPACITY))
            .setFinishCallback(() ->
                Utils.delayNoRecord(fDuration, () ->
                    new TimeBasedAnimationHelper(500, 50 / HZ.UNIT,
                        p -> stage.setOpacity(MAX_OPACITY - p * MAX_OPACITY))
                        .setFinishCallback(() -> {
                            showingStages.remove(stage);
                            stage.hide();
                        })
                        .play()
                )
            )
            .play();

        pane.setOnMouseClicked(e -> {
            showingStages.remove(stage);
            stage.hide();
        });
    }

    public static void shutdown() {
        showingStages.keySet().forEach(Stage::hide);
        showingStages.clear();
    }
}
