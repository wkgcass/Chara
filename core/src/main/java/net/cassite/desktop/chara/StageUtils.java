// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class StageUtils {
    public static boolean primaryStageFocused = true;
    public static Stage primaryStage;

    private StageUtils() {
    }

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

    public static boolean closePrimaryStage() {
        var foo = primaryStage;
        if (foo == null) {
            return false;
        }
        Platform.runLater(() -> foo.fireEvent(new WindowEvent(null, WindowEvent.WINDOW_CLOSE_REQUEST)));
        return true;
    }
}
