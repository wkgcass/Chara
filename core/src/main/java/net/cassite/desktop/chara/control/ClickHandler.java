// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.control;

/**
 * Utility interface for handling clicking areas in a general way
 */
@FunctionalInterface
public interface ClickHandler {
    /**
     * Should be called on clicking events
     *
     * @param x x position
     * @param y y position
     */
    void click(double x, double y);
}
