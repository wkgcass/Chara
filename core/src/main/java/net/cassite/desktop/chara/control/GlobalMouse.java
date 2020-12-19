// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.control;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.robot.Robot;
import net.cassite.desktop.chara.graphic.HZ;
import net.cassite.desktop.chara.graphic.Updatable;

import java.util.LinkedList;
import java.util.List;

public class GlobalMouse {
    private static final GlobalMouse instance = new GlobalMouse();
    private static final List<EventHandler<MouseEvent>> mouseMoveHandlers = new LinkedList<>();

    private GlobalMouse() {
    }

    public static void setOnMouseMoved(EventHandler<MouseEvent> h) {
        mouseMoveHandlers.add(h);
    }

    public static boolean isRunning() {
        return instance.isRunning;
    }

    public static void enable() {
        instance.doEnable();
    }

    public static void disable() {
        instance.doDisable();
    }

    private volatile boolean isRunning = false;
    private final Robot robot = new Robot();
    private final Updatable updateMouse = this::updateMouse;

    private synchronized void doEnable() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        Platform.runLater(() -> HZ.get().register(updateMouse));
    }

    private synchronized void doDisable() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
        Platform.runLater(() -> HZ.get().deregister(updateMouse));
    }

    private long lastUpdateMouseTs = 0;
    private double lastMouseX = -1;
    private double lastMouseY = -1;

    private void updateMouse(long ts) {
        if (ts - lastUpdateMouseTs < 50) {
            return;
        }

        double x = robot.getMouseX();
        double y = robot.getMouseY();
        if (x == lastMouseX && y == lastMouseY) {
            // mouse not moved
            return;
        }

        lastUpdateMouseTs = ts;
        lastMouseX = x;
        lastMouseY = y;
        nativeMouseMoved(new MouseEvent(
            MouseEvent.MOUSE_MOVED,
            0, 0,
            x, y,
            MouseButton.NONE,
            0,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            null
        ));
    }

    private void nativeMouseMoved(MouseEvent e) {
        for (var h : mouseMoveHandlers) {
            h.handle(e);
        }
    }
}
