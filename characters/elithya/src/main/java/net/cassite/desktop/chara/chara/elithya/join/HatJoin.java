// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.join;

import net.cassite.desktop.chara.chara.elithya.parts.HatBack;
import net.cassite.desktop.chara.chara.elithya.parts.HatFront;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.graphic.HZ;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaConsts;

public class HatJoin {
    private final HatFront hatFront;
    private final HatBack hatBack;
    private final ElithyaConsts elithyaConsts;

    public HatJoin(HatFront hatFront, HatBack hatBack, ElithyaConsts elithyaConsts) {
        this.hatFront = hatFront;
        this.hatBack = hatBack;
        this.elithyaConsts = elithyaConsts;

        double totalTime =
            elithyaConsts.hatMoveDownMax - elithyaConsts.hatDownAccelerateLen
                + elithyaConsts.hatDownAccelerateLen * 2d
                + elithyaConsts.hatDownAccelerateLen * 2d
                + elithyaConsts.hatMoveDownMax - elithyaConsts.hatDownAccelerateLen + elithyaConsts.hatMoveUpMax - elithyaConsts.hatUpAccelerateLen
                + elithyaConsts.hatUpAccelerateLen * 2d
                + elithyaConsts.hatUpAccelerateLen * 2d
                + elithyaConsts.hatMoveUpMax - elithyaConsts.hatUpAccelerateLen;
        p1 = 0 + (elithyaConsts.hatMoveDownMax - elithyaConsts.hatDownAccelerateLen) / totalTime;
        p2 = p1 + elithyaConsts.hatDownAccelerateLen * 2d / totalTime;
        p3 = p2 + elithyaConsts.hatDownAccelerateLen * 2d / totalTime;
        p4 = p3 + (elithyaConsts.hatMoveDownMax - elithyaConsts.hatDownAccelerateLen + elithyaConsts.hatMoveUpMax - elithyaConsts.hatUpAccelerateLen) / totalTime;
        p5 = p4 + (elithyaConsts.hatUpAccelerateLen) * 2d / totalTime;
        p6 = p5 + (elithyaConsts.hatUpAccelerateLen) * 2d / totalTime;
        p7 = p6 + (elithyaConsts.hatMoveUpMax - elithyaConsts.hatUpAccelerateLen) / totalTime;
    }

    public boolean isShown() {
        return hatFront.isShown();
    }

    public void show() {
        hatFront.show();
        hatBack.show();
    }

    public void hide() {
        hatFront.hide();
        hatBack.hide();
    }

    public void play() {
        Runnable[] callbacks = ElithyaUtils.synchronize(2, this::play);
        hatFront.play(callbacks[0]);
        hatBack.play(callbacks[1]);
    }

    private void setYPosition(double y) {
        hatFront.getRoot().setLayoutY(y);
        hatBack.getRoot().setLayoutY(y);
    }

    private boolean isAnimatingPosition = false;
    private long beginTs;
    private static final long animatingPositionTotalTime = 5000;

    private final double p1; // from 0, move down, uniform speed
    private final double p2; // from p1, move down, decelerate
    private final double p3; // from p2, move up, accelerate
    private final double p4; // from p3, move up, uniform speed
    private final double p5; // from p4, move up, decelerate
    private final double p6; // from p5, move down, accelerate
    private final double p7; // from p6, move down to 0, uniform speed

    public void animateHatPosition() {
        if (isAnimatingPosition) {
            return;
        }
        isAnimatingPosition = true;
        beginTs = System.currentTimeMillis();
        HZ.get().register(this::updatePosition);
    }

    private void updatePosition(long ts) {
        long t = (ts - beginTs) % animatingPositionTotalTime;
        double p = t / (double) animatingPositionTotalTime;

        if (p < p1) {
            setYPosition(
                (elithyaConsts.hatMoveDownMax - elithyaConsts.hatDownAccelerateLen) * p / p1
            );
        } else if (p < p2) {
            setYPosition(elithyaConsts.hatMoveDownMax - elithyaConsts.hatDownAccelerateLen
                + (elithyaConsts.hatDownAccelerateLen) * (-Math.pow((p - p1) / (p2 - p1) - 1, 2) + 1)
            );
        } else if (p < p3) {
            setYPosition(elithyaConsts.hatMoveDownMax -
                (elithyaConsts.hatDownAccelerateLen * Math.pow((p - p2) / (p3 - p2), 2))
            );
        } else if (p < p4) {
            setYPosition(elithyaConsts.hatMoveDownMax - elithyaConsts.hatDownAccelerateLen -
                (elithyaConsts.hatMoveDownMax - elithyaConsts.hatDownAccelerateLen + elithyaConsts.hatMoveUpMax - elithyaConsts.hatUpAccelerateLen)
                    * (p - p3) / (p4 - p3)
            );
        } else if (p < p5) {
            setYPosition(-(elithyaConsts.hatMoveUpMax - elithyaConsts.hatUpAccelerateLen) -
                (elithyaConsts.hatUpAccelerateLen * (
                    -Math.pow((p - p4) / (p5 - p4) - 1, 2) + 1
                ))
            );
        } else if (p < p6) {
            setYPosition(-elithyaConsts.hatMoveUpMax + elithyaConsts.hatUpAccelerateLen * Math.pow((p - p5) / (p6 - p5), 2));
        } else if (p < p7) {
            setYPosition(-(elithyaConsts.hatMoveUpMax - elithyaConsts.hatUpAccelerateLen) +
                (elithyaConsts.hatMoveUpMax - elithyaConsts.hatUpAccelerateLen) * (p - p6) / (p7 - p6));
        } else {
            setYPosition(0);
        }
    }
}
