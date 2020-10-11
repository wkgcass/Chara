// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.personality;

import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.RateLimiter;
import net.cassite.desktop.chara.util.Utils;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class KokoriPersonality {
    private final AppCallback appCallback;
    private double bondPoint = 0.6;
    private ScheduledFuture<?> autoBondIncreasingFuture = null;

    public KokoriPersonality(AppCallback appCallback) {
        this.appCallback = appCallback;
        Double bondPoint = ConfigManager.get().getBondPoint();
        if (bondPoint != null) {
            this.bondPoint = bondPoint;
            appCallback.setBondPoint(bondPoint, 0);
        }
        rescheduleAutoBondPointIncreasingDecreasing();
    }

    private void rescheduleAutoBondPointIncreasingDecreasing() {
        if (autoBondIncreasingFuture != null) {
            autoBondIncreasingFuture.cancel(true);
        }
        autoBondIncreasingFuture = ThreadUtils.get().scheduleFX(() -> {
            autoBondIncreasingFuture = null;
            if (bondPoint > 0.8) {
                incBondPoint(-0.002);
            } else {
                incBondPoint(0.002);
            }
            rescheduleAutoBondPointIncreasingDecreasing();
        }, 10, TimeUnit.MINUTES);
    }

    private void incBondPoint(double delta) {
        var old = bondPoint;
        bondPoint += delta;
        if (bondPoint > 1) {
            bondPoint = 1;
        } else if (bondPoint < 0) {
            bondPoint = 0;
        }
        ConfigManager.get().setBondPoint(bondPoint);
        appCallback.setBondPoint(bondPoint, old);
    }

    private long getPositiveInteractionMinInterval() {
        return (long) ((1 - bondPoint) * 4 * 1000 + 4000);
    }

    private double getPositiveTransToNegativeThreshold() {
        return 4 * bondPoint + 3;
    }

    private boolean positiveInteraction() {
        return positiveInteraction(1);
    }

    private long positiveInteractionTimeRecordForCoolDown = -1;
    private int positiveInteractionCountAfterLast = 0;

    private boolean positiveInteraction(int n) {
        long current = System.currentTimeMillis();
        ++positiveInteractionCountAfterLast;
        if (n == 1 && current - positiveInteractionTimeRecordForCoolDown < getPositiveInteractionMinInterval()) {
            assert Logger.debug("positiveInteractionCountAfterLast = " + positiveInteractionCountAfterLast + ", bondPoint=" + bondPoint);
            if (positiveInteractionCountAfterLast >= getPositiveTransToNegativeThreshold()) {
                positiveInteractionTimeRecordForCoolDown = current;
                incBondPoint(-0.005);
                Logger.info("positive interaction transits to negative, current: " + bondPoint);
            } else {
                assert Logger.debug("positive interaction does nothing");
            }
            return false;
        }
        positiveInteractionTimeRecordForCoolDown = current;
        positiveInteractionCountAfterLast = 0;
        incBondPoint(0.002 * n);
        Logger.info("positive interaction * " + n + " increases bond, current: " + bondPoint);
        return true;
    }

    private boolean normalInteraction() {
        boolean ret = true;

        if (Utils.random(0.0005 * bondPoint + 0.0015)) {
            incBondPoint(0.001);
            Logger.info("normal interaction rand to positive, current: " + bondPoint);
        } else if (Utils.random(0.001)) {
            incBondPoint(-0.001);
            Logger.info("normal interaction rand to negative, current: " + bondPoint);
            ret = false;
        } else {
            assert Logger.debug("normal interaction does nothing");
        }

        return ret;
    }

    private void negativeInteraction() {
        negativeInteraction(1);
    }

    private long lastNegativeInteractionTime = -1;
    private long negativeInteractionCountAfterLast = 0;

    private void negativeInteraction(int n) {
        // prevent accidental touch
        ++negativeInteractionCountAfterLast;
        long current = System.currentTimeMillis();
        if (current - lastNegativeInteractionTime > 24 * 1000) {
            lastNegativeInteractionTime = current;
            negativeInteractionCountAfterLast = 1;
            assert Logger.debug("negative anti-accidental-touch count " +
                negativeInteractionCountAfterLast);
            return;
        }
        if (negativeInteractionCountAfterLast <= 4) {
            assert Logger.debug("negative anti-accidental-touch count " +
                negativeInteractionCountAfterLast);
            return;
        }

        incBondPoint(-0.02 * n);
        Logger.info("negative interaction * " + n + ", current: " + bondPoint);
        lastNegativeInteractionTime = -1;
    }

    public boolean touchHair() {
        preTouch();
        return positiveInteraction();
    }

    public void touchEye() {
        preTouch();
        normalInteraction();
    }

    public void touchFace() {
        preTouch();
        if (bondPoint >= 0.7) {
            assert Logger.debug("touch face as positive interaction");
            positiveInteraction();
        } else {
            assert Logger.debug("touch face as normal interaction");
            normalInteraction();
        }
    }

    public boolean touchRune() {
        preTouch();
        return normalInteraction();
    }

    public void touchCloth() {
        preTouch();
        if (normalInteraction()) {
            normalMessage();
        }
    }

    public void touchBreast() {
        preTouch();
        if (bondPoint > 0.8) {
            if (Utils.random(0.2)) {
                Logger.info("touch breast rand to positive");
                positiveInteraction();
            } else if (Utils.random(0.1)) {
                Logger.info("touch breast rand to negative");
                negativeInteraction();

                appCallback.showMessage(KokoriWords.doNotTouchThere.select());
            } else {
                Logger.info("touch breast rand to normal");
                normalInteraction();

                appCallback.showMessage(KokoriWords.flirt().select());
            }
        } else if (bondPoint > 0.45) {
            assert Logger.debug("touch breast as negative * 1");
            negativeInteraction();

            appCallback.showMessage(KokoriWords.doNotTouchThere.select());
        } else {
            assert Logger.debug("touch breast as negative * 5");
            negativeInteraction(5);

            appCallback.showMessage(KokoriWords.doNotTouchThere.select());
        }
    }

    public void touchArm() {
        preTouch();
        normalInteraction();
    }

    public int touchCrotch() {
        preTouch();
        if (bondPoint >= 0.9) {
            if (Utils.random(0.1)) {
                Logger.info("touch crotch rand to positive * 20");
                positiveInteraction(20);

                appCallback.showMessage(KokoriWords.wantSex.select());
                return 1;
            } else {
                Logger.info("touch crotch rand to negative");
                negativeInteraction();

                appCallback.showMessage(KokoriWords.doNotTouchThere.select());
            }
        } else if (bondPoint >= 0.6) {
            assert Logger.debug("touch crotch as negative");
            negativeInteraction();

            appCallback.showMessage(KokoriWords.doNotTouchThere.select());
        } else {
            assert Logger.debug("touch crotch as negative * 10");
            negativeInteraction(10);

            appCallback.showMessage(KokoriWords.doNotTouchThere.select());
            return -1;
        }
        return 0;
    }

    public int touchLeg() {
        preTouch();
        if (bondPoint >= 0.85) {
            assert Logger.debug("touch leg as positive");
            positiveInteraction();
            return 1;
        } else if (bondPoint >= 0.65) {
            if (Utils.random(0.1)) {
                Logger.info("touch leg rand to positive");
                positiveInteraction();
                return 1;
            } else if (Utils.random(0.4)) {
                Logger.info("touch leg rand to negative");
                negativeInteraction();

                appCallback.showMessage(KokoriWords.doNotTouchLeg.select());
                return -1;
            } else {
                Logger.info("touch leg rand to normal");
                normalInteraction();

                appCallback.showMessage(KokoriWords.flirt().select());
                return 0;
            }
        } else {
            assert Logger.debug("touch leg as negative");
            negativeInteraction();

            appCallback.showMessage(KokoriWords.doNotTouchLeg.select());
            return -1;
        }
    }

    public void touchOther() {
        preTouch();
        if (normalInteraction()) {
            normalMessage();
        }
    }

    private void preTouch() {
        rescheduleAutoBondPointIncreasingDecreasing();
    }

    private void normalMessage() {
        if (!messageRateLimit()) {
            return;
        }
        if (bondPoint >= 0.85) {
            if (Utils.random(0.3)) {
                appCallback.showMessage(KokoriWords.highIntimacyConversations().select());
            } else {
                appCallback.showMessage(KokoriWords.normalConversations().select());
            }
        } else {
            appCallback.showMessage(KokoriWords.normalConversations().select());
        }
    }

    private final RateLimiter messageRateLimiter = new RateLimiter(
        20 * 1000, 3, // 3 times in 20 seconds
        4 * 1000, 1 // 1 time in 4 seconds
    );

    private boolean messageRateLimit() {
        return messageRateLimiter.request();
    }

    public double getBondPoint() {
        return bondPoint;
    }

    public void setBondPoint(double value) {
        incBondPoint(value - bondPoint);
    }
}
