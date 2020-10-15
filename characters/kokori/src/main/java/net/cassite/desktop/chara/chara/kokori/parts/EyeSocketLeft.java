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

    private boolean isClosed = false;

    @Override
    public void close(Runnable cb) {
        if (isClosed) {
            cb.run();
            return;
        }
        isClosed = true;

        anima.resetTo(0).setEndFrame(10).setPauseCallbackOnce(cb).play(100);
    }

    @Override
    public void open() {
        if (!isClosed) {
            return;
        }
        isClosed = false;

        anima.resetTo(10).play(100);
    }
}
