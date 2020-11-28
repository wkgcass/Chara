// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.chara.elithya.util.LoopingAnima;
import net.cassite.desktop.chara.chara.parts.AbstractPart;

public class SkirtFront extends AbstractPart {
    private final LoopingAnima skirtFront;

    public SkirtFront(Group parent) {
        super(parent);

        skirtFront = ElithyaUtils.buildLoopingAnima("animation/025_skirt_front/skirt_front_", 10, ".png");
        skirtFront.setFps(24);
        skirtFront.addTo(root);
    }

    public void play() {
        skirtFront.play();
    }
}
