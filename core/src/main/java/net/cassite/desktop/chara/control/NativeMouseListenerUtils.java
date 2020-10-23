// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.control;

import javafx.application.Platform;
import net.cassite.desktop.chara.util.Logger;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class NativeMouseListenerUtils implements NativeMouseInputListener {
    private static final NativeMouseInputListener instance = new NativeMouseListenerUtils();
    private static final List<Consumer<NativeMouseEvent>> mouseMoveHandlers = new LinkedList<>();

    private NativeMouseListenerUtils() {
    }

    public static NativeMouseInputListener get() {
        return instance;
    }

    public static void setOnMouseMoved(Consumer<NativeMouseEvent> h) {
        mouseMoveHandlers.add(h);
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        // ignore
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        // ignore
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        // ignore
    }

    private volatile boolean isHandlingMouseMovedEvent = false;
    private volatile NativeMouseEvent lastHoldingMouseMovedEvent = null;

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        lastHoldingMouseMovedEvent = e;
        if (isHandlingMouseMovedEvent) {
            assert Logger.debug("currently handling mouse event, hold this event");
            return;
        }
        isHandlingMouseMovedEvent = true;
        Platform.runLater(() -> {
            NativeMouseEvent last;
            while ((last = lastHoldingMouseMovedEvent) != null) {
                lastHoldingMouseMovedEvent = null;
                for (var h : mouseMoveHandlers) {
                    h.accept(last);
                }
            }
            isHandlingMouseMovedEvent = false;
        });
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        // ignore
    }
}
