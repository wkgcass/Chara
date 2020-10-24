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
        stage.initStyle(StageStyle.UTILITY);
        stage.setWidth(0);
        stage.setHeight(0);
        stage.setOpacity(0);
        stage.setX(Integer.MAX_VALUE);
        stage.setY(Integer.MAX_VALUE);

        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        stage.setScene(scene);

        return stage;
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
