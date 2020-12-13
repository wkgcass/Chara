// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.coordinate;

import javafx.scene.input.MouseEvent;

public class Point {
    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(MouseEvent e) {
        this(e.getX(), e.getY());
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
