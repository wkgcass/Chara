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

    private boolean isTighten = false;

    public void loose() {
        if (!isTighten) {
            return;
        }
        isTighten = false;

        anima.resetTo(4).play();
    }

    public void tighten(Runnable cb) {
        if (isTighten) {
            cb.run();
            return;
        }
        isTighten = true;

        anima.resetTo(0).setEndFrame(4).setPauseCallbackOnce(cb).play();
    }
}
