// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.util.Utils;

public class Bowknot extends AbstractPart {
    private final Anima anima;

    public Bowknot(Group parent) {
        super(parent);
        anima = new Anima("animation/bowknot/bowknot_000.png",
            Utils.buildSeqNames("animation/bowknot/bowknot_", 0, 40, ".png"));
        anima.addTo(root);
    }

    public void flow() {
        anima.play();
    }
}
