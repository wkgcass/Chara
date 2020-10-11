// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.util.Utils;

public class DressFront extends AbstractPart {
    private final Anima anima;

    public DressFront(Group parent) {
        super(parent);
        anima = new Anima(
            "animation/dress_front/dress_front_000.png",
            Utils.buildSeqNames("animation/dress_front/dress_front_", 0, 40, ".png")
        );
        anima.addTo(root);
    }

    public void flutter() {
        anima.play();
    }
}
