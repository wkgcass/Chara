// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.util.Utils;

public class Quiver extends AbstractPart {
    private final Anima anima;

    public Quiver(Group parent) {
        super(parent);
        anima = new Anima("animation/quiver/quiver_000.png",
            Utils.buildSeqNames("animation/quiver/quiver_", 0, 30, ".png"));
        anima.addTo(root);
    }

    public void shake() {
        anima.play();
    }
}
