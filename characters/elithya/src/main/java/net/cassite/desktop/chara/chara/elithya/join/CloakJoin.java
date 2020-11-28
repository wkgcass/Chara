// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.join;

import net.cassite.desktop.chara.chara.elithya.parts.CloakBack;
import net.cassite.desktop.chara.chara.elithya.parts.CloakFront;
import net.cassite.desktop.chara.chara.elithya.parts.CloakMid;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;

public class CloakJoin {
    private final CloakFront front;
    private final CloakMid mid;
    private final CloakBack back;

    public CloakJoin(CloakFront front, CloakMid mid, CloakBack back) {
        this.front = front;
        this.mid = mid;
        this.back = back;
    }

    public boolean isShown() {
        return front.isShown();
    }

    public void show() {
        front.show();
        mid.show();
        back.show();
    }

    public void hide() {
        front.hide();
        mid.hide();
        back.hide();
    }

    public void play() {
        Runnable[] callbacks = ElithyaUtils.synchronize(3, this::play);
        front.play(callbacks[0]);
        mid.play(callbacks[1]);
        back.play(callbacks[2]);
    }
}
