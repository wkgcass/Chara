// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.chara.elithya.util.LoopingAnima;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;

public class CloakFront extends AbstractPart {
    private final LoopingAnima cloakFront;
    private final Static shadowOnCloth;

    public CloakFront(Group parent) {
        super(parent);

        shadowOnCloth = new Static("static/022_001_clock_front_shadow_on_cloth.PNG");
        cloakFront = ElithyaUtils.buildLoopingAnima("animation/020_cloak_front/cloak_front_", 10, ".png");
        cloakFront.setFps(24);
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
        shadowOnCloth.addTo(root);
        cloakFront.addTo(root);
    }

    public void hide() {
        if (!isShown) {
            return;
        }
        isShown = false;
        shadowOnCloth.removeFrom(root);
        cloakFront.removeFrom(root);
    }

    public void play(Runnable cb) {
        cloakFront.play(cb);
    }
}
