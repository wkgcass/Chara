// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import net.cassite.desktop.chara.chara.elithya.join.LanternJoin;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.TimeBasedAnimationHelper;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaConsts;

public class AbstractChainRotate extends AbstractPart {
    private final Rotate rotate;

    public AbstractChainRotate(Group parent, ElithyaConsts elithyaConsts) {
        super(parent);

        rotate = new Rotate(0, elithyaConsts.chainRotatePivotX, elithyaConsts.chainRotatePivotY);
        root.getTransforms().add(rotate);
    }

    private final TimeBasedAnimationHelper swingAnimation = new TimeBasedAnimationHelper(0, this::update);
    private int state;
    protected double swingDirection;

    private void update(double percentage) {
        if (state == 0) {
            updateBegin(percentage);
        } else if (state == 1) {
            updateSlow(percentage);
        } else {
            updateFinish(percentage);
        }
    }

    private void updateBegin(double percentage) {
        if (percentage < 0.2) {
            updateSwingUpPositive(percentage * 5, LanternJoin.rotateDegree1);
        } else if (percentage < 0.4) {
            updateSwingDownNegative((percentage - 0.2) * 5, LanternJoin.rotateDegree1);
        } else if (percentage < 0.6) {
            updateSwingUpNegative((percentage - 0.4) * 5, LanternJoin.rotateDegree2);
        } else if (percentage < 0.8) {
            updateSwingDownPositive((percentage - 0.6) * 5, LanternJoin.rotateDegree2);
        } else {
            updateSwingUpPositive((percentage - 0.8) * 5, LanternJoin.rotateDegree3);
        }
    }

    private void updateSwingUpPositive(double percentage, double degree) {
        rotate.setAngle((-Math.pow(percentage - 1, 2) + 1) * degree * swingDirection);
    }

    private void updateSwingDownNegative(double percentage, double degree) {
        rotate.setAngle((1 - Math.pow(percentage, 2)) * degree * swingDirection);
    }

    private void updateSwingUpNegative(double percentage, double degree) {
        rotate.setAngle((-Math.pow(percentage - 1, 2) + 1) * degree * swingDirection * -1);
    }

    private void updateSwingDownPositive(double percentage, double degree) {
        rotate.setAngle((1 - Math.pow(percentage, 2)) * degree * swingDirection * -1);
    }

    private void updateSlow(double percentage) {
        if (percentage < 0.5) {
            updateSwingDownNegative(percentage * 2, LanternJoin.rotateDegree3);
        } else {
            updateSwingUpNegative((percentage - 0.5) * 2, LanternJoin.rotateDegree4);
        }
    }

    private void updateFinish(double percentage) {
        if (percentage < 0.5) {
            rotate.setAngle((0.5 * Math.pow(2 * percentage, 2) - 1) * LanternJoin.rotateDegree4 * swingDirection);
        } else {
            rotate.setAngle((-0.5 * Math.pow(2 * (percentage - 0.5) - 1, 2)) * LanternJoin.rotateDegree4 * swingDirection);
        }
    }

    private static final double syncRatio = 1.25;

    protected void animateRotate(Runnable cb) {
        state = 0;
        swingAnimation.setDuration((int) Math.ceil(50 * 1000 / LanternJoin.beginFps * syncRatio));
        swingAnimation.setFinishCallbackOnce(() -> {
            state = 1;
            swingAnimation.setDuration((int) Math.ceil(20 * 1000 / LanternJoin.slowFps * syncRatio));
            swingAnimation.setFinishCallbackOnce(() -> {
                state = 2;
                swingAnimation.setDuration((int) Math.ceil(10 * 1000 / LanternJoin.finishFps * syncRatio));
                swingAnimation.setFinishCallbackOnce(cb).play();
            }).play();
        }).play();
    }
}
