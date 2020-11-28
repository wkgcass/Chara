// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Static;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaConsts;

public class Lantern extends AbstractChainRotate {
    public Lantern(Group parent, ElithyaConsts elithyaConsts) {
        super(parent, elithyaConsts);

        Static lantern = new Static("static/044_lantern.PNG");
        lantern.addTo(root);
    }

    private boolean isPlaying = false;

    public void swingRight(Runnable cb) {
        if (isPlaying) {
            return;
        }
        isPlaying = true;

        // swing right
        swingDirection = -1;
        animateRotate(() -> {
            isPlaying = false;
            cb.run();
        });
    }

    public void swingLeft(Runnable cb) {
        if (isPlaying) {
            return;
        }
        isPlaying = true;

        // swing left
        swingDirection = 1;
        animateRotate(() -> {
            isPlaying = false;
            cb.run();
        });
    }
}
