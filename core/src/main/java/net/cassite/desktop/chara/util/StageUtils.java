// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.graphic.UStage;
import net.cassite.desktop.chara.i18n.I18nConsts;
import vproxybase.util.Tuple3;

/**
 * Stage utility
 */
public class StageUtils {
    /**
     * the primary stage is now focused
     */
    public static boolean primaryStageFocused = true;
    /**
     * the message stag is now focused
     */
    public static boolean messageStageFocused = false;
    /**
     * the primary stage
     */
    public static Stage primaryStage;
    /**
     * the primary temporary stage
     */
    public static Stage primaryTemporaryStage;

    private StageUtils() {
    }

    /**
     * Create a temporary stage which is {@link StageStyle#UTILITY} and transparent and far away from the screen.<br>
     * This can prevent from showing another window on taskbar when showing a secondary stage.<br>
     * <pre>
     * var tmp = StageUtils.createTransparentTemporaryUtilityStage();
     * stage.initOwner(tmp);
     * // ...
     * tmp.show();
     * stage.show();
     * </pre>
     *
     * @return the constructed stage
     */
    public static Stage createTransparentTemporaryUtilityStage() {
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        configureTransparentTemporaryUtilityStage(stage);
        return stage;
    }

    public static void configureTransparentTemporaryUtilityStage(Stage stage) {
        primaryStage.xProperty().addListener((ob, old, now) ->
            stage.setX(now.doubleValue() + 1));
        primaryStage.yProperty().addListener((ob, old, now) ->
            stage.setY(now.doubleValue() + 1));

        stage.focusedProperty().addListener((ob, old, now) -> {
            if (now) primaryStage.requestFocus();
        });

        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.setWidth(1);
        stage.setHeight(1);
        stage.setOpacity(0);
        stage.setX(primaryStage.getX() + 1);
        stage.setY(primaryStage.getY() + 1);

        Pane pane = new Pane();
        pane.setMouseTransparent(true);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
    }

    public static Tuple3<UStage, ProgressBar, Label> createLoadingBarStage() {
        UStage loadingStage = new UStage();
        loadingStage.setPaneWidth(600);
        loadingStage.setPaneHeight(80);
        loadingStage.setTitle(I18nConsts.LOADING.get()[0]);
        loadingStage.centerOnScreen();
        loadingStage.setIcon(Global.charaDefaultIcon);

        Pane pane = loadingStage.getRootPane();

        Label label = new Label();
        label.setLayoutX(10);
        label.setLayoutY(10);
        label.setPrefHeight(20);
        label.setText(I18nConsts.LOADING.get()[0]);
        pane.getChildren().add(label);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setLayoutX(10);
        progressBar.setLayoutY(40);
        progressBar.setPrefWidth(580);
        progressBar.setPrefHeight(20);
        progressBar.setProgress(0);
        pane.getChildren().add(progressBar);

        loadingStage.setOnCloseRequest(e -> {
            Platform.setImplicitExit(false);
            Logger.fatal("program exits while loading");
        });

        return new Tuple3<>(loadingStage, progressBar, label);
    }

    /**
     * Send {@link WindowEvent#WINDOW_CLOSE_REQUEST} to the primary stage
     *
     * @return true if the primary stage recorded and event sent, false otherwise
     */
    public static boolean closePrimaryStage() {
        var foo = primaryStage;
        if (foo == null) {
            return false;
        }
        Platform.runLater(() -> foo.fireEvent(new WindowEvent(null, WindowEvent.WINDOW_CLOSE_REQUEST)));
        return true;
    }
}
