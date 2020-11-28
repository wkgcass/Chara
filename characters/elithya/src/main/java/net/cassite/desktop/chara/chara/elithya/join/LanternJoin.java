// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.join;

import net.cassite.desktop.chara.chara.elithya.parts.ChainBack;
import net.cassite.desktop.chara.chara.elithya.parts.ChainFront;
import net.cassite.desktop.chara.chara.elithya.parts.Lantern;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;

public class LanternJoin {
    public static final double beginFps = 30;
    public static final double slowFps = 25;
    public static final double finishFps = 18;
    public static final double rotateDegree1 = 15;
    public static final double rotateDegree2 = 12;
    public static final double rotateDegree3 = 8;
    public static final double rotateDegree4 = 3;

    private final ChainFront front;
    private final Lantern lantern;
    private final ChainBack back;

    public LanternJoin(ChainFront front, Lantern lantern, ChainBack back) {
        this.front = front;
        this.lantern = lantern;
        this.back = back;
    }

    private boolean isPlaying = false;

    public void swingRight() {
        if (isPlaying) {
            return;
        }
        isPlaying = true;

        Runnable[] callbacks = ElithyaUtils.synchronize(3, () -> isPlaying = false);
        front.swingRight(callbacks[0]);
        lantern.swingRight(callbacks[1]);
        back.swingRight(callbacks[2]);
    }

    public void swingLeft() {
        if (isPlaying) {
            return;
        }
        isPlaying = true;

        Runnable[] callbacks = ElithyaUtils.synchronize(3, () -> isPlaying = false);
        front.swingLeft(callbacks[0]);
        lantern.swingLeft(callbacks[1]);
        back.swingLeft(callbacks[2]);
    }
}
