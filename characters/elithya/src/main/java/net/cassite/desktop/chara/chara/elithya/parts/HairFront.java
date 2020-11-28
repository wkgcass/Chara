// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.chara.elithya.util.LoopingAnima;
import net.cassite.desktop.chara.chara.parts.AbstractPart;

public class HairFront extends AbstractPart {
    private final LoopingAnima hairFront;

    public HairFront(Group parent) {
        super(parent);

        hairFront = ElithyaUtils.buildLoopingAnima("animation/006_hair_front/hair_front_", 15, ".png");
        hairFront.setFps(24);
        hairFront.addTo(root);
    }

    public void play(Runnable cb) {
        hairFront.play(cb);
    }
}
