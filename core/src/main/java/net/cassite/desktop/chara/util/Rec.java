// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

/**
 * Integer rectangle
 */
public class Rec {
    public final int x1;
    public final int y1;
    public final int x2;
    public final int y2;

    /**
     * Construct the integer rectangle
     *
     * @param x1 top-left x
     * @param y1 top-left y
     * @param x2 bot-right x
     * @param y2 bot-right y
     */
    public Rec(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Check whether this rectangle contains the coordinate.
     *
     * @param x x
     * @param y y
     * @return true if contains, false otherwise
     */
    public boolean contains(double x, double y) {
        return x1 <= x && x <= x2 && y1 <= y & y <= y2;
    }
}
