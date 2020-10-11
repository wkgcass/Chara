// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.util.Utils;

public class LegLeft extends AbstractPart {
    private final Anima anima;

    public LegLeft(Group parent) {
        super(parent);
        anima = new Anima(
            "animation/leg_left/leg_left_008.png",
            Utils.buildSeqNames("animation/leg_left/leg_left_", 8, 54, ".png")
        );
        anima.addTo(root);
    }

    public void tighten() {
        anima.play();
    }
}
