// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.model.vpwsagent;

import javafx.scene.image.Image;

public class Resources {
    public final Image idleImage;
    public final Image launchingImage;
    public final Image runningImage;

    public Resources(Image idleImage, Image launchingImage, Image runningImage) {
        this.idleImage = idleImage;
        this.launchingImage = launchingImage;
        this.runningImage = runningImage;
    }
}
