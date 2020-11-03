// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DragHandler implements EventHandler<MouseEvent> {
    private final Consumer<double[]> setter;
    private final Supplier<double[]> getter;
    private final Function<MouseEvent, double[]> offsetGetter;
    private double oldNodeX;
    private double oldNodeY;
    private double oldOffsetX;
    private double oldOffsetY;

    public DragHandler(Consumer<double[]> setter, Supplier<double[]> getter, Function<MouseEvent, double[]> offsetGetter) {
        this.setter = setter;
        this.getter = getter;
        this.offsetGetter = offsetGetter;
    }

    public DragHandler(Consumer<double[]> setter, Supplier<double[]> getter) {
        this.setter = setter;
        this.getter = getter;
        this.offsetGetter = e -> new double[]{e.getScreenX(), e.getScreenY()};
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
        var xy = getter.get();
        this.oldNodeX = xy[0];
        this.oldNodeY = xy[1];
        var offxy = offsetGetter.apply(e);
        oldOffsetX = offxy[0];
        oldOffsetY = offxy[1];
    }

    protected void dragged(MouseEvent e) {
        double[] xy = new double[2];
        var offxy = offsetGetter.apply(e);
        xy[0] = offxy[0] - this.oldOffsetX + this.oldNodeX;
        xy[1] = offxy[1] - this.oldOffsetY + this.oldNodeY;
        setter.accept(xy);
    }
}
