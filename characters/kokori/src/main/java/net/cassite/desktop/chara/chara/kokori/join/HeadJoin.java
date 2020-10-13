// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.join;

import javafx.scene.transform.Rotate;
import net.cassite.desktop.chara.chara.kokori.parts.*;
import net.cassite.desktop.chara.graphic.TimeBasedAnimationHelper;
import net.cassite.desktop.chara.model.kokori.KokoriConsts;

public class HeadJoin {
    private final KokoriConsts kokoriConsts;

    private final Hair hair;

    private final Rotate headRotate;
    private final Rotate hairSideLeftRotate;
    private final Rotate hairSideRightRotate;
    private final Rotate hairBackRotate;

    private int state = 0; // 0:default, 1:left, 2:right

    private final TimeBasedAnimationHelper animationHelper = new TimeBasedAnimationHelper(
        300, this::update
    );

    public HeadJoin(KokoriConsts kokoriConsts, Head head, Hair hair, HairSide hairSide, HairBack hairBack,
                    EyeJoin eyeLeft, EyeJoin eyeRight, Mouth mouth, RedCheek redCheek) {
        this.kokoriConsts = kokoriConsts;
        this.hair = hair;

        headRotate = new Rotate(0, kokoriConsts.headJoin_headRotate_x, kokoriConsts.headJoin_headRotate_y);
        hairSideLeftRotate = new Rotate(0, kokoriConsts.headJoin_hairSideLeftRotate_x, kokoriConsts.headJoin_hairSideLeftRotate_y);
        hairSideRightRotate = new Rotate(0, kokoriConsts.headJoin_hairSideRightRotate_x, kokoriConsts.headJoin_hairSideRightRotate_y);
        hairBackRotate = new Rotate(0, kokoriConsts.headJoin_hairBackRotate_x, kokoriConsts.headJoin_hairBackRotate_y);

        head.getRoot().getTransforms().add(headRotate);
        hair.getRoot().getTransforms().add(headRotate);
        hairSide.getLeftGroup().getTransforms().addAll(headRotate, hairSideLeftRotate);
        hairSide.getRightGroup().getTransforms().addAll(headRotate, hairSideRightRotate);
        hairBack.getRoot().getTransforms().addAll(headRotate, hairBackRotate);
        handleEye(eyeLeft);
        handleEye(eyeRight);
        mouth.getRoot().getTransforms().add(headRotate);
        redCheek.getRoot().getTransforms().add(headRotate);
    }

    private void handleEye(EyeJoin eye) {
        eye.eye.getRoot().getTransforms().add(headRotate);
        eye.eyebrow.getRoot().getTransforms().add(headRotate);
        eye.eyeSocket.getRoot().getTransforms().add(headRotate);
    }

    public void tiltToLeft() {
        if (state == 1) {
            return;
        }
        state = 1;
        targetAngle = kokoriConsts.headJoin_tiltToLeft_targetAngle;
        play();
        hair.swing();
    }

    public void tiltToRight() {
        if (state == 2) {
            return;
        }
        state = 2;
        targetAngle = kokoriConsts.headJoin_tiltToRight_targetAngle;
        play();
        hair.swing();
    }

    public void toDefaultPosition() {
        if (state == 0) {
            return;
        }
        state = 0;
        targetAngle = 0;
        play();
        hair.swing();
    }

    private double startAngle = 0;
    private double targetAngle = 0;

    private void play() {
        startAngle = headRotate.getAngle();
        animationHelper.play();
    }

    private void update(double percentage) {
        double angle = (targetAngle - startAngle) * percentage + startAngle;
        headRotate.setAngle(angle);
        hairSideRightRotate.setAngle(-angle);
        hairSideLeftRotate.setAngle(-angle);
        hairBackRotate.setAngle(-angle * 0.8);
    }

    public int getState() {
        return state;
    }
}
