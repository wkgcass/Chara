// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.chara.elithya.util.LoopingAnima;
import net.cassite.desktop.chara.chara.parts.AbstractPart;

public class HatBack extends AbstractPart {
    private final LoopingAnima hatBack;

    public HatBack(Group parent) {
        super(parent);

        hatBack = ElithyaUtils.buildLoopingAnima("animation/041_hat_back/hat_back_", 10, ".png");
        hatBack.setFps(24);
    }

    private boolean isShown = false;

    public void show() {
        if (isShown) {
            return;
        }
        isShown = true;
        hatBack.addTo(root);
    }

    public void hide() {
        if (!isShown) {
            return;
        }
        isShown = false;
        hatBack.removeFrom(root);
    }

    public void play(Runnable cb) {
        hatBack.play(cb);
    }
}
