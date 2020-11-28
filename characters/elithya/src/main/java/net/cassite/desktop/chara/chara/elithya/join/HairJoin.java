// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.join;

import net.cassite.desktop.chara.chara.elithya.parts.*;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;

public class HairJoin {
    private final HairDumb dumb;
    private final HairFront front;
    private final HairSideLeft left;
    private final HairSideRight right;
    private final HairBack back;

    public HairJoin(HairDumb dumb,
                    HairFront front,
                    HairSideLeft left,
                    HairSideRight right,
                    HairBack back) {
        this.dumb = dumb;
        this.front = front;
        this.left = left;
        this.right = right;
        this.back = back;
    }

    public void play() {
        Runnable[] callbacks = ElithyaUtils.synchronize(5, this::play);
        dumb.play(callbacks[0]);
        front.play(callbacks[1]);
        left.play(callbacks[2]);
        right.play(callbacks[3]);
        back.play(callbacks[4]);
    }
}
