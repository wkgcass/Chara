// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.join;

import net.cassite.desktop.chara.chara.elithya.parts.LegLeft;
import net.cassite.desktop.chara.chara.elithya.parts.LegRight;
import net.cassite.desktop.chara.util.Utils;

public class LegJoin {
    private final LegLeft legLeft;
    private final LegRight legRight;

    public LegJoin(LegLeft legLeft, LegRight legRight) {
        this.legLeft = legLeft;
        this.legRight = legRight;
    }

    public int getStockingsState() {
        return legLeft.getStockingState();
    }

    public int getShoesState() {
        return legLeft.getShoeState();
    }

    public void showPurpleStockings() {
        legLeft.showPurpleStocking();
        legRight.showPurpleStocking();
    }

    public void showBlackStockings() {
        legLeft.showBlackStocking();
        legRight.showBlackStocking();
    }

    public void showNoneStockings() {
        legLeft.showNoneStocking();
        legRight.showNoneStocking();
    }

    public void showPurpleShoes() {
        legLeft.showPurpleShoe();
        legRight.showPurpleShoe();
    }

    public void showBlackShoes() {
        legLeft.showBlackShoe();
        legRight.showBlackShoe();
    }

    public void playLeft(Runnable cb) {
        legLeft.play(() -> {
            if (Utils.random(0.75)) {
                if (Utils.random(0.5)) {
                    legLeft.setFps(28 + 7);
                } else {
                    legLeft.setFps(28);
                }
            }
            cb.run();
        });
    }

    public void stopLeft() {
        legLeft.setFps(28);
        legLeft.stop();
    }

    public void playRight(Runnable cb) {
        legRight.play(() -> {
            if (Utils.random(0.75)) {
                if (Utils.random(0.5)) {
                    legRight.setFps(28 + 7);
                } else {
                    legRight.setFps(28);
                }
            }
            cb.run();
        });
    }

    public void stopRight() {
        legRight.setFps(28);
        legRight.stop();
    }
}
