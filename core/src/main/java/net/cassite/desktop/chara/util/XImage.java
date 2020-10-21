// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import javafx.scene.image.Image;

/**
 * The cut image with x and y offsets recorded
 */
public class XImage {
    /**
     * last modification time of the image
     */
    public final long time;
    /**
     * x offset should be moved relative to the original image
     */
    public final int x;
    /**
     * y offset should be moved relative to the original image
     */
    public final int y;
    /**
     * the cut image
     */
    public final Image image;

    public XImage(long time, int x, int y, Image image) {
        this.time = time;
        this.x = x;
        this.y = y;
        this.image = image;
    }

    /**
     * Get width of the cut image
     *
     * @return {@link Image#getWidth()}
     */
    public double getWidth() {
        return image.getWidth();
    }

    /**
     * Get height of the cut image
     *
     * @return {@link Image#getHeight()}
     */
    public double getHeight() {
        return image.getHeight();
    }
}
