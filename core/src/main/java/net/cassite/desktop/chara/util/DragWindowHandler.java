// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DragWindowHandler extends DragHandler implements EventHandler<MouseEvent> {
    public DragWindowHandler(Stage stage) {
        super((xy) -> {
                stage.setX(xy[0]);
                stage.setY(xy[1]);
            },
            () -> new double[]{stage.getX(), stage.getY()});
    }
}
