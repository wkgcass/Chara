// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.util.Utils;

public class EyeSocketLeft extends AbstractPart implements EyeSocket {
    private final Anima anima;

    public EyeSocketLeft(Group parent) {
        super(parent);
        anima = new Anima("animation/eye_socket_left/left_eye_000.png",
            Utils.buildSeqNames("animation/eye_socket_left/left_eye_", 0, 20, ".png"));
        anima.addTo(root);
    }

    @Override
    public void blink() {
        anima.play(100);
    }
}
