// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DragWindowHandler implements EventHandler<MouseEvent> {
    private final Stage stage;
    private double oldStageX;
    private double oldStageY;
    private double oldScreenX;
    private double oldScreenY;

    public DragWindowHandler(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(MouseEvent e) {
        if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
            pressed(e);
        } else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            dragged(e);
        }
    }

    private void pressed(MouseEvent e) {
        assert Logger.debug("mouse pressed ...");

        this.oldStageX = stage.getX();
        this.oldStageY = stage.getY();
        oldScreenX = e.getScreenX();
        oldScreenY = e.getScreenY();
    }

    protected void dragged(MouseEvent e) {
        assert Logger.debug("mouse dragged ...");
        stage.setX(e.getScreenX() - this.oldScreenX + this.oldStageX);
        stage.setY(e.getScreenY() - this.oldScreenY + this.oldStageY);
    }
}
