// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.chara.elithya.util.LoopingAnima;
import net.cassite.desktop.chara.chara.parts.AbstractPart;

public class CloakMid extends AbstractPart {
    private final LoopingAnima cloakMid;

    public CloakMid(Group parent) {
        super(parent);

        cloakMid = ElithyaUtils.buildLoopingAnima("animation/037_cloak_mid/cloak_mid_", 10, ".png");
        cloakMid.setFps(24);
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
        cloakMid.addTo(root);
    }

    public void hide() {
        if (!isShown) {
            return;
        }
        isShown = false;
        cloakMid.removeFrom(root);
    }

    public void play(Runnable cb) {
        cloakMid.play(cb);
    }
}
