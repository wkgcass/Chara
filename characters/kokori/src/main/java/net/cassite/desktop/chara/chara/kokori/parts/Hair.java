// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.util.Utils;

public class Hair extends AbstractPart {
    private final Anima hairDumb;
    private final Anima hair;

    public Hair(Group parent) {
        super(parent);

        hairDumb = new Anima("animation/hair_dumb/hair_dumb_000.png",
            Utils.buildSeqNames("animation/hair_dumb/hair_dumb_", 0, 40, ".png"));
        hair = new Anima("animation/hair_main/hair_main_000.png",
            Utils.buildSeqNames("animation/hair_main/hair_main_", 0, 40, ".png"));

        hair.addTo(root);
        hairDumb.addTo(root);
    }

    public void swing() {
        hair.play();
        hairDumb.play();
    }
}
