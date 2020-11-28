// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.chara.elithya.util.LoopingAnima;
import net.cassite.desktop.chara.chara.parts.AbstractPart;

public class HairSideRight extends AbstractPart {
    private final LoopingAnima hairSideRight;

    public HairSideRight(Group parent) {
        super(parent);

        hairSideRight = ElithyaUtils.buildLoopingAnima("animation/034_hair_side_right/hair_side_right_", 15, ".png");
        hairSideRight.setFps(24);
        hairSideRight.addTo(root);
    }

    public void play(Runnable cb) {
        hairSideRight.play(cb);
    }
}
