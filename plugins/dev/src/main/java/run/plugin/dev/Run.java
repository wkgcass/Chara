// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package run.plugin.dev;

import javafx.application.Application;
import javafx.stage.Stage;
import net.cassite.desktop.chara.Main;

public class Run extends Application {
    @Override
    public void start(Stage primaryStage) {
        new Main().start(primaryStage);
    }
}
