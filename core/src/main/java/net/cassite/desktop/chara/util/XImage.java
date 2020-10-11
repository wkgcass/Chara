// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import javafx.scene.image.Image;

public class XImage {
    public final long time;
    public final int x;
    public final int y;
    public final Image image;

    public XImage(long time, int x, int y, Image image) {
        this.time = time;
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public double getWidth() {
        return image.getWidth();
    }

    public double getHeight() {
        return image.getHeight();
    }
}
