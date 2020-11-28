// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.util.Utils;

public class HeadWithEyeBlink extends AbstractPart {
    private final Anima head;

    public HeadWithEyeBlink(Group parent) {
        super(parent);

        head = new Anima("animation/013_head_eye_blink/head_eye_blink_000.png",
            Utils.buildSeqNames("animation/013_head_eye_blink/head_eye_blink_", 0, 20, ".png"))
            .setFps(100);
        head.addTo(root);
    }

    public void blink() {
        head.play();
    }
}
