// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Div;
import net.cassite.desktop.chara.graphic.Static;
import net.cassite.desktop.chara.graphic.TimeBasedAnimationHelper;
import net.cassite.desktop.chara.model.kokori.KokoriConsts;

public class ArmLeft extends AbstractPart {
    private final KokoriConsts kokoriConsts;
    private final Rotate shoulderRotate;
    private final Rotate elbowRotate;
    private final Translate elbowTranslate;

    private final TimeBasedAnimationHelper animationHelper;

    private boolean bowShown = false;

    public ArmLeft(KokoriConsts kokoriConsts, Group parent) {
        super(parent);
        this.kokoriConsts = kokoriConsts;

        Static bow = new Static("static/039_bow.PNG");
        Static hand = new Static("static/040_hand_left.PNG");
        Static fore = new Static("static/041_arm_fore_left.PNG");
        Static upper = new Static("static/042_arm_upper_left.PNG");

        Div shoulderRotateGroup = new Div();
        root.getChildren().add(shoulderRotateGroup);

        upper.addTo(shoulderRotateGroup);

        Div elbowRotateGroup = new Div();
        shoulderRotateGroup.getChildren().add(elbowRotateGroup);

        fore.addTo(elbowRotateGroup);
        hand.addTo(elbowRotateGroup);
        bow.addTo(elbowRotateGroup);

        shoulderRotate = new Rotate(0, kokoriConsts.armLeft_shoulderRotate_x, kokoriConsts.armLeft_shoulderRotate_y);
        shoulderRotateGroup.getTransforms().add(shoulderRotate);
        elbowRotate = new Rotate(0, kokoriConsts.armLeft_elbowRotate_x, kokoriConsts.armLeft_elbowRotate_y);
        elbowTranslate = new Translate();
        elbowRotateGroup.getTransforms().addAll(elbowRotate, elbowTranslate);

        animationHelper = new TimeBasedAnimationHelper(400, this::update);
    }

    public void showBow() {
        if (bowShown) {
            return;
        }
        bowShown = true;
        targetShoulderAngle = kokoriConsts.armLeft_showBow_targetShoulderAngle;
        targetElbowAngle = kokoriConsts.armLeft_showBow_targetElbowAngle;
        targetElbowTranslateX = kokoriConsts.armLeft_showBow_targetElbowTranslateX;
        targetElbowTranslateY = kokoriConsts.armLeft_showBow_targetElbowTranslateY;
        play();
    }

    public void hideBow() {
        if (!bowShown) {
            return;
        }
        bowShown = false;
        targetShoulderAngle = 0;
        targetElbowAngle = 0;
        targetElbowTranslateX = 0;
        targetElbowTranslateY = 0;
        play();
    }

    public boolean bowIsShown() {
        return bowShown;
    }

    private double startShoulderAngle = 0;
    private double startElbowAngle = 0;
    private double startElbowTranslateX = 0;
    private double startElbowTranslateY = 0;
    private double targetShoulderAngle = 0;
    private double targetElbowAngle = 0;
    private double targetElbowTranslateX = 0;
    private double targetElbowTranslateY = 0;

    private void play() {
        startShoulderAngle = shoulderRotate.getAngle();
        startElbowAngle = elbowRotate.getAngle();
        startElbowTranslateX = elbowTranslate.getX();
        startElbowTranslateY = elbowTranslate.getY();
        animationHelper.play();
    }

    private void update(double angleRate) {
        shoulderRotate.setAngle((targetShoulderAngle - startShoulderAngle) * angleRate + startShoulderAngle);
        elbowRotate.setAngle((targetElbowAngle - startElbowAngle) * angleRate + startElbowAngle);
        elbowTranslate.setX((targetElbowTranslateX - startElbowTranslateX) * angleRate + startElbowTranslateX);
        elbowTranslate.setY((targetElbowTranslateY - startElbowTranslateY) * angleRate + startElbowTranslateY);
    }
}
