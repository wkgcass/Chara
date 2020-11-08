// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.join;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import net.cassite.desktop.chara.chara.kokori.parts.ArmForeRight;
import net.cassite.desktop.chara.chara.kokori.parts.ArmLeft;
import net.cassite.desktop.chara.chara.kokori.parts.ArmUpperRight;
import net.cassite.desktop.chara.chara.kokori.parts.HandRight;
import net.cassite.desktop.chara.graphic.TimeBasedAnimationHelper;
import net.cassite.desktop.chara.model.kokori.KokoriConsts;

public class ArmRightJoin {
    private final KokoriConsts kokoriConsts;

    private final ArmForeRight fore;
    private final HandRight hand;
    private final ArmLeft armLeft;

    private final Rotate shoulderRotate;
    private final Translate shoulderTranslate;
    private final Rotate elbowRotate;
    private final Translate elbowTranslate;

    private final TimeBasedAnimationHelper animationHelper;

    public ArmRightJoin(KokoriConsts kokoriConsts, ArmUpperRight upper, ArmForeRight fore, HandRight hand, ArmLeft armLeft) {
        this.kokoriConsts = kokoriConsts;
        this.hand = hand;
        this.fore = fore;
        this.armLeft = armLeft;

        shoulderRotate = new Rotate(0, kokoriConsts.armRightJoin_shoulderRotate_x, kokoriConsts.armRightJoin_shoulderRotate_y);
        shoulderTranslate = new Translate();
        elbowRotate = new Rotate(0, kokoriConsts.armRightJoin_elbowRotate_x, kokoriConsts.armRightJoin_elbowRotate_y);
        elbowTranslate = new Translate();

        upper.getRoot().getTransforms().addAll(shoulderRotate, shoulderTranslate);
        fore.getRoot().getTransforms().addAll(shoulderRotate, shoulderTranslate, elbowRotate, elbowTranslate);
        hand.getRoot().getTransforms().addAll(shoulderRotate, shoulderTranslate, elbowRotate, elbowTranslate);

        this.animationHelper = new TimeBasedAnimationHelper(300, this::update);
    }

    public void showArrow() {
        hand.showArrow();
    }

    public void showRune() {
        hand.showRune();
    }

    public void hideArrow() {
        hand.hideArrow();
    }

    public void hideRune() {
        hand.hideRune();
    }

    public boolean arrowIsVisible() {
        return hand.arrowIsVisible();
    }

    public boolean runeIsVisible() {
        return hand.runeIsVisible();
    }

    public void runeFlow() {
        hand.runeFlow();
    }

    public void protectCrotch() {
        targetShoulderAngle = kokoriConsts.armRightJoin_protectCrotch_targetShoulderAngle;
        targetShoulderDeltaX = 0;
        targetShoulderDeltaY = 0;
        targetElbowAngle = kokoriConsts.armRightJoin_protectCrotch_targetElbowAngle;
        targetElbowDeltaX = kokoriConsts.armRightJoin_protectCrotch_targetElbowDeltaX;
        targetElbowDeltaY = kokoriConsts.armRightJoin_protectCrotch_targetElbowDeltaY;
        animationHelper.setDuration(100);
        play();
    }

    public void moveToDefaultPosition() {
        moveToDefaultPosition(() -> {
        });
    }

    public void moveToDefaultPosition(Runnable cb) {
        targetShoulderAngle = 0;
        targetShoulderDeltaX = 0;
        targetShoulderDeltaY = 0;
        targetElbowAngle = 0;
        targetElbowDeltaX = 0;
        targetElbowDeltaY = 0;
        animationHelper.setDuration(300);
        animationHelper.setFinishCallbackOnce(cb);
        play();
    }

    public void tighten() {
        targetShoulderAngle = kokoriConsts.armRightJoin_tighten_targetShoulderAngle;
        targetShoulderDeltaX = 0;
        targetShoulderDeltaY = 0;
        targetElbowAngle = kokoriConsts.armRightJoin_tighten_targetElbowAngle;
        targetElbowDeltaX = kokoriConsts.armRightJoin_tighten_targetElbowDeltaX;
        targetElbowDeltaY = kokoriConsts.armRightJoin_tighten_targetElbowDeltaY;
        animationHelper.setDuration(100);
        play();
    }

    public void stretch(Runnable cb) {
        targetShoulderAngle = kokoriConsts.armRightJoin_stretch_targetShoulderAngle;
        targetShoulderDeltaX = kokoriConsts.armRightJoin_stretch_targetShoulderDeltaX;
        targetShoulderDeltaY = kokoriConsts.armRightJoin_stretch_targetShoulderDeltaY;
        targetElbowAngle = kokoriConsts.armRightJoin_protectCrotch_targetElbowAngle;
        targetElbowDeltaX = kokoriConsts.armRightJoin_protectCrotch_targetElbowDeltaX;
        targetElbowDeltaY = kokoriConsts.armRightJoin_protectCrotch_targetElbowDeltaY;
        animationHelper.setDuration(300);
        animationHelper.setFinishCallbackOnce(cb);
        play();
    }

    public void animateMoveToBack(Runnable cb) {
        if (isMovedToBack) {
            return;
        }
        isMovedToBack = true;
        stretch(() -> {
            moveToBack();
            moveToDefaultPosition(cb);
        });
    }

    public void animateMoveToFront(Runnable cb) {
        if (!isMovedToBack) {
            return;
        }
        isMovedToBack = false;
        stretch(() -> {
            moveToFront();
            moveToDefaultPosition(cb);
        });
    }

    private boolean isMovedToBack = false;

    public boolean isMovedToBack() {
        return isMovedToBack;
    }

    public void moveToBack() {
        isMovedToBack = true;
        fore.reposition(true, armLeft);
        hand.reposition(true, fore);
    }

    public void moveToFront() {
        isMovedToBack = false;
        fore.reposition(true, null);
        hand.reposition(true, fore);
    }

    private double startShoulderAngle = 0;
    private double startShoulderDeltaX = 0;
    private double startShoulderDeltaY = 0;
    private double startElbowAngle = 0;
    private double startElbowDeltaX = 0;
    private double startElbowDeltaY = 0;

    private double targetShoulderAngle = 0;
    private double targetShoulderDeltaX = 0;
    private double targetShoulderDeltaY = 0;
    private double targetElbowAngle = 0;
    private double targetElbowDeltaX = 0;
    private double targetElbowDeltaY = 0;

    private void play() {
        startShoulderAngle = shoulderRotate.getAngle();
        startShoulderDeltaX = shoulderTranslate.getX();
        startShoulderDeltaY = shoulderTranslate.getY();
        startElbowAngle = elbowRotate.getAngle();
        startElbowDeltaX = elbowTranslate.getX();
        startElbowDeltaY = elbowTranslate.getY();
        animationHelper.play();
    }

    private void update(double rate) {
        shoulderRotate.setAngle((targetShoulderAngle - startShoulderAngle) * rate + startShoulderAngle);
        shoulderTranslate.setX((targetShoulderDeltaX - startShoulderDeltaX) * rate + startShoulderDeltaX);
        shoulderTranslate.setY((targetShoulderDeltaY - startShoulderDeltaY) * rate + startShoulderDeltaY);
        elbowRotate.setAngle((targetElbowAngle - startElbowAngle) * rate + startElbowAngle);
        elbowTranslate.setX((targetElbowDeltaX - startElbowDeltaX) * rate + startElbowDeltaX);
        elbowTranslate.setY((targetElbowDeltaY - startElbowDeltaY) * rate + startElbowDeltaY);
    }
}
