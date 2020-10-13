// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.join;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import net.cassite.desktop.chara.chara.kokori.parts.ArmForeRight;
import net.cassite.desktop.chara.chara.kokori.parts.ArmUpperRight;
import net.cassite.desktop.chara.chara.kokori.parts.HandRight;
import net.cassite.desktop.chara.graphic.TimeBasedAnimationHelper;
import net.cassite.desktop.chara.model.kokori.KokoriConsts;

public class ArmRightJoin {
    private final KokoriConsts kokoriConsts;

    private final HandRight hand;

    private final Rotate shoulderRotate;
    private final Rotate elbowRotate;
    private final Translate elbowTranslate;

    private final TimeBasedAnimationHelper animationHelper;

    public ArmRightJoin(KokoriConsts kokoriConsts, ArmUpperRight upper, ArmForeRight fore, HandRight hand) {
        this.kokoriConsts = kokoriConsts;
        this.hand = hand;

        shoulderRotate = new Rotate(0, kokoriConsts.armRightJoin_shoulderRotate_x, kokoriConsts.armRightJoin_shoulderRotate_y);
        elbowRotate = new Rotate(0, kokoriConsts.armRightJoin_elbowRotate_x, kokoriConsts.armRightJoin_elbowRotate_y);
        elbowTranslate = new Translate();

        upper.getRoot().getTransforms().add(shoulderRotate);
        fore.getRoot().getTransforms().addAll(shoulderRotate, elbowRotate, elbowTranslate);
        hand.getRoot().getTransforms().addAll(shoulderRotate, elbowRotate, elbowTranslate);

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
        targetElbowAngle = kokoriConsts.armRightJoin_protectCrotch_targetElbowAngle;
        targetElbowDeltaX = kokoriConsts.armRightJoin_protectCrotch_targetElbowDeltaX;
        targetElbowDeltaY = kokoriConsts.armRightJoin_protectCrotch_targetElbowDeltaY;
        animationHelper.setDuration(100);
        play();
    }

    public void moveToDefaultPosition() {
        targetShoulderAngle = 0;
        targetElbowAngle = 0;
        targetElbowDeltaX = 0;
        targetElbowDeltaY = 0;
        animationHelper.setDuration(300);
        play();
    }

    public void tighten() {
        targetShoulderAngle = kokoriConsts.armRightJoin_tighten_targetShoulderAngle;
        targetElbowAngle = kokoriConsts.armRightJoin_tighten_targetElbowAngle;
        targetElbowDeltaX = kokoriConsts.armRightJoin_tighten_targetElbowDeltaX;
        targetElbowDeltaY = kokoriConsts.armRightJoin_tighten_targetElbowDeltaY;
        animationHelper.setDuration(100);
        play();
    }

    private double startShoulderAngle = 0;
    private double startElbowAngle = 0;
    private double startElbowDeltaX = 0;
    private double startElbowDeltaY = 0;
    private double targetShoulderAngle = 0;
    private double targetElbowAngle = 0;
    private double targetElbowDeltaX = 0;
    private double targetElbowDeltaY = 0;

    private void play() {
        startShoulderAngle = shoulderRotate.getAngle();
        startElbowAngle = elbowRotate.getAngle();
        startElbowDeltaX = elbowTranslate.getX();
        startElbowDeltaY = elbowTranslate.getY();
        animationHelper.play();
    }

    private void update(double rate) {
        shoulderRotate.setAngle((targetShoulderAngle - startShoulderAngle) * rate + startShoulderAngle);
        elbowRotate.setAngle((targetElbowAngle - startElbowAngle) * rate + startElbowAngle);
        elbowTranslate.setX((targetElbowDeltaX - startElbowDeltaX) * rate + startElbowDeltaX);
        elbowTranslate.setY((targetElbowDeltaY - startElbowDeltaY) * rate + startElbowDeltaY);
    }
}
