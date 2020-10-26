// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

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
