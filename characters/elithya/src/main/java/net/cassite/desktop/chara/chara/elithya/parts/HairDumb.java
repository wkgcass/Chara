// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.chara.elithya.util.LoopingAnima;
import net.cassite.desktop.chara.chara.parts.AbstractPart;

public class HairDumb extends AbstractPart {
    private final LoopingAnima hairDumb;

    public HairDumb(Group parent) {
        super(parent);

        hairDumb = ElithyaUtils.buildLoopingAnima("animation/005_hair_dumb/hair_dumb_", 10, ".png");
        hairDumb.setFps(24);
        hairDumb.addTo(root);
    }

    public void play(Runnable cb) {
        hairDumb.play(cb);
    }
}
