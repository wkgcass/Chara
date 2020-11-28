// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.chara.elithya.util.LoopingAnima;
import net.cassite.desktop.chara.chara.parts.AbstractPart;

public class CloakBack extends AbstractPart {
    private final LoopingAnima cloakBack;

    public CloakBack(Group parent) {
        super(parent);

        cloakBack = ElithyaUtils.buildLoopingAnima("animation/039_cloak_back/cloak_back_", 10, ".png");
        cloakBack.setFps(24);
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
        cloakBack.addTo(root);
    }

    public void hide() {
        if (!isShown) {
            return;
        }
        isShown = false;
        cloakBack.removeFrom(root);
    }

    public void play(Runnable cb) {
        cloakBack.play(cb);
    }
}
