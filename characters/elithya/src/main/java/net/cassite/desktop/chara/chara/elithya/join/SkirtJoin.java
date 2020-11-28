// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.join;

import net.cassite.desktop.chara.chara.elithya.parts.SkirtBack;
import net.cassite.desktop.chara.chara.elithya.parts.SkirtFront;

public class SkirtJoin {
    private final SkirtFront front;
    private final SkirtBack back;

    public SkirtJoin(SkirtFront front, SkirtBack back) {
        this.front = front;
        this.back = back;
    }

    public void play() {
        front.play();
    }
}
