// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.chara.elithya.util.LoopingAnima;
import net.cassite.desktop.chara.chara.parts.AbstractPart;

public class HairBack extends AbstractPart {
    private final LoopingAnima hairBack;

    public HairBack(Group parent) {
        super(parent);

        hairBack = ElithyaUtils.buildLoopingAnima("animation/040_hair_back/hair_back_", 15, ".png");
        hairBack.setFps(24);
        hairBack.addTo(root);
    }

    public void play(Runnable cb) {
        hairBack.play(cb);
    }
}
