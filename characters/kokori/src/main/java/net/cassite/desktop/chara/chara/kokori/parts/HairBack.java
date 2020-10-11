// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.util.Utils;

public class HairBack extends AbstractPart {
    private final Anima anima;

    public HairBack(Group parent) {
        super(parent);
        anima = new Anima("animation/hair_back/hair_back_000.png",
            Utils.buildSeqNames("animation/hair_back/hair_back_", 0, 40, ".png"));
        anima.addTo(root);
    }

    public void swing() {
        anima.play();
    }
}
