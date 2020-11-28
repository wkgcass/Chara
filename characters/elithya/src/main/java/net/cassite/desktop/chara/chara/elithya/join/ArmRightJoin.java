// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.join;

import javafx.scene.transform.Rotate;
import net.cassite.desktop.chara.chara.elithya.parts.ArmForeRight;
import net.cassite.desktop.chara.chara.elithya.parts.ArmUpperRight;
import net.cassite.desktop.chara.chara.elithya.parts.HandRight;
import net.cassite.desktop.chara.chara.elithya.parts.WandLight;
import net.cassite.desktop.chara.graphic.HZ;
import net.cassite.desktop.chara.graphic.Updatable;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaConsts;
import net.cassite.desktop.chara.util.Utils;

import java.util.Arrays;

public class ArmRightJoin {
    private final WandLight wandLight;
    private final HandRight hand;
    private final ArmForeRight fore;
    private final ArmUpperRight upper;
    private final ElithyaConsts elithyaConsts;

    private final Rotate rotateFore;
    private final Rotate rotateHand;

    // use armForeRightRotatePivot as origin
    private final double foreX;
    private final double foreY;

    private final double foreInit;
    private final double foreMin;
    private final double foreMax;
    private final double foreV;

    // use armForeRightRotatePivot as origin
    private final double handX;
    private final double handY;

    private final double handInit;
    private final double handMin;
    private final double handMax;
    private final double handV;

    private final Updatable updatable = this::update;

    public ArmRightJoin(WandLight wandLight, HandRight hand, ArmForeRight fore, ArmUpperRight upper, ElithyaConsts elithyaConsts) {
        this.wandLight = wandLight;
        this.hand = hand;
        this.fore = fore;
        this.upper = upper;
        this.elithyaConsts = elithyaConsts;

        foreX = elithyaConsts.handRightRotateX - elithyaConsts.armForeRightRotateX;
        foreY = elithyaConsts.handRightRotateY - elithyaConsts.armForeRightRotateY;
        foreInit = elithyaConsts.armForeRightInitDegree;
        foreMin = elithyaConsts.armForeRightMinDegree - 0.0001;
        foreMax = elithyaConsts.armForeRightMaxDegree + 0.0001;
        foreV = elithyaConsts.armForeRightAngularVelocity / HZ.UNIT;
        handX = elithyaConsts.wandTipX - elithyaConsts.armForeRightRotateX;
        handY = elithyaConsts.wandTipY - elithyaConsts.armForeRightRotateY;
        handInit = elithyaConsts.handRightInitDegree;
        handMin = elithyaConsts.handRightMinDegree - 0.0001;
        handMax = elithyaConsts.handRightMaxDegree + 0.0001;
        handV = elithyaConsts.handRightAngularVelocity / HZ.UNIT;

        foreCur = foreInit;
        handCur = handInit;

        rotateFore = new Rotate(foreInit, elithyaConsts.armForeRightRotateX, elithyaConsts.armForeRightRotateY);
        fore.getRoot().getTransforms().add(rotateFore);
        hand.getRoot().getTransforms().add(rotateFore);
        wandLight.getRoot().getTransforms().add(rotateFore);
        rotateHand = new Rotate(handInit, elithyaConsts.handRightRotateX, elithyaConsts.handRightRotateY);
        hand.getRoot().getTransforms().add(rotateHand);
        wandLight.getRoot().getTransforms().add(rotateHand);
    }

    public boolean wandLightIsShown() {
        return wandLight.isShown();
    }

    public void showWandLight() {
        wandLight.show();
    }

    public void hideWandLight() {
        wandLight.hide();
    }

    public void reset() {
        track0(elithyaConsts.wandTipX, elithyaConsts.wandTipY);
    }

    public void track(double x, double y) {
        if (x < elithyaConsts.eyeTrackBoundMinX || x > elithyaConsts.eyeTrackBoundMaxX
            || y < elithyaConsts.eyeTrackBoundMinY || y > elithyaConsts.eyeTrackBoundMaxY) {
            reset();
            return;
        }
        track0(x, y);
    }

    private void track0(double x, double y) {
        if (!startedTracking) {
            HZ.get().register(updatable);
        }
        startedTracking = true;
        // use armForeRightRotatePivot as origin
        targetX = x - elithyaConsts.armForeRightRotateX;
        targetY = y - elithyaConsts.armForeRightRotateY;
    }

    private double targetX;
    private double targetY;
    private double foreCur;
    private double handCur;
    private int skipTickCount = 0;
    private boolean startedTracking = false;

    private void update(long ts) {
        if (skipTickCount++ < elithyaConsts.armRightRotateSkipTick) {
            return;
        }
        skipTickCount = 0;

        double[] dis = new double[8];
        Arrays.fill(dis, Double.MAX_VALUE);
        double foreAdd = foreCur + foreV;
        double foreSub = foreCur - foreV;
        boolean foreAddOk = foreAdd < foreMax;
        boolean foreSubOk = foreSub > foreMin;
        double handAdd = handCur + handV;
        double handSub = handCur - handV;
        boolean handAddOk = handAdd < handMax;
        boolean handSubOk = handSub > handMin;
        if (foreAddOk)
            dis[0] = calcDistance(foreAdd, handCur);
        if (foreSubOk)
            dis[1] = calcDistance(foreSub, handCur);
        if (handAddOk)
            dis[2] = calcDistance(foreCur, handAdd);
        if (handSubOk)
            dis[3] = calcDistance(foreCur, handSub);
        if (foreAddOk && handAddOk)
            dis[4] = calcDistance(foreAdd, handAdd);
        if (foreSubOk && handAddOk)
            dis[5] = calcDistance(foreSub, handAdd);
        if (foreAddOk && handSubOk)
            dis[6] = calcDistance(foreAdd, handSub);
        if (foreSubOk && handSubOk)
            dis[7] = calcDistance(foreSub, handSub);

        int idx = -1;
        double lastDis = calcDistance(foreCur, handCur);
        for (int i = 0; i < dis.length; ++i) {
            if (lastDis > dis[i]) {
                lastDis = dis[i];
                idx = i;
            }
        }
        if (idx == -1) {
            HZ.get().deregister(updatable);
            startedTracking = false;
            return;
        }

        switch (idx) {
            case 0:
                apply(foreAdd, handCur);
                break;
            case 1:
                apply(foreSub, handCur);
                break;
            case 2:
                apply(foreCur, handAdd);
                break;
            case 3:
                apply(foreCur, handSub);
                break;
            case 4:
                apply(foreAdd, handAdd);
                break;
            case 5:
                apply(foreSub, handAdd);
                break;
            case 6:
                apply(foreAdd, handSub);
                break;
            case 7:
                apply(foreSub, handSub);
                break;
        }
    }

    private double calcDistance(double fore, double hand) {
        fore = fore * Math.PI / 180;
        double foreX = Math.cos(fore) * this.foreX - Math.sin(fore) * this.foreY;
        double foreY = Math.sin(fore) * this.foreX + Math.cos(fore) * this.foreY;

        double handX = Math.cos(fore) * this.handX - Math.sin(fore) * this.handY;
        double handY = Math.sin(fore) * this.handX + Math.cos(fore) * this.handY;
        handX -= foreX;
        handY -= foreY;

        hand = hand * Math.PI / 180;
        double handX2 = Math.cos(hand) * handX - Math.sin(hand) * handY;
        double handY2 = Math.sin(hand) * handX + Math.cos(hand) * handY;
        handX = handX2 + foreX;
        handY = handY2 + foreY;

        //noinspection UnnecessaryLocalVariable
        double ret = Math.sqrt(Math.pow(handX - targetX, 2) + Math.pow(handY - targetY, 2));
        // System.out.printf("fore=%.3f, hand=%.3f, target=(%.3f, %.3f), hand=(%.3f, %.3f), fore=(%.3f, %.3f), ret=%.3f\n",
        //     fore, hand, targetX, targetY, handX, handY, foreX, foreY, ret);
        return ret;
    }

    private void apply(double fore, double hand) {
        foreCur = fore;
        handCur = hand;
        if (Utils.doubleEquals(fore, foreInit, 0.0002)) {
            rotateFore.setAngle(foreInit);
        } else {
            rotateFore.setAngle(fore);
        }
        if (Utils.doubleEquals(hand, handInit, 0.0002)) {
            rotateHand.setAngle(handInit);
        } else {
            rotateHand.setAngle(hand);
        }
    }

    public void foreRotate(double degree) {
        rotateFore.setAngle(degree);
    }

    public void handRotate(double degree) {
        rotateHand.setAngle(degree);
    }
}
