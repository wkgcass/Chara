// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.chara.elithya.util.LoopingAnima;
import net.cassite.desktop.chara.chara.parts.AbstractPart;

public class HatFront extends AbstractPart {
    private final LoopingAnima hatFront;

    public HatFront(Group parent) {
        super(parent);

        hatFront = ElithyaUtils.buildLoopingAnima("animation/002_hat_front/hat_front_", 10, ".png");
        hatFront.setFps(24);
    }

    private boolean isShown = false;

    public boolean isShown() {
        return isShown;
    }

    public void show() {
        if (isShown) {
            return;
        }
        isShown = true;
        hatFront.addTo(root);
    }

    public void hide() {
        if (!isShown) {
            return;
        }
        isShown = false;
        hatFront.removeFrom(root);
    }

    public void play(Runnable cb) {
        hatFront.play(cb);
    }
}
