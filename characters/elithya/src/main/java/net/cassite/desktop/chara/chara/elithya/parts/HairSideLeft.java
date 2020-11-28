// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.chara.elithya.util.LoopingAnima;
import net.cassite.desktop.chara.chara.parts.AbstractPart;

public class HairSideLeft extends AbstractPart {
    private final LoopingAnima hairSideLeft;

    public HairSideLeft(Group parent) {
        super(parent);

        hairSideLeft = ElithyaUtils.buildLoopingAnima("animation/007_hair_side_left/hair_side_left_", 15, ".png");
        hairSideLeft.setFps(24);
        hairSideLeft.addTo(root);
    }

    public void play(Runnable cb) {
        hairSideLeft.play(cb);
    }
}
