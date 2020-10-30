// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.personality;

import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.CharaPoints;
import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.model.kokori.KokoriConsts;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.RateLimiter;
import net.cassite.desktop.chara.util.Scheduled;
import net.cassite.desktop.chara.util.Utils;

import java.util.concurrent.TimeUnit;

public class KokoriPersonality {
    public static final double INITIAL_BOND_POINT = 0.6;

    private static final double AUTO_BOND_POINT_INCR = 0.002;
    private static final double AUTO_BOND_POINT_DECR = -0.002;
    private static final double AUTO_DESIRE_POINT_INCR = 0.001;
    private static final double POSITIVE_TRANS_TO_NEGATIVE_BOND_POINT_DECR = -0.005;
    private static final double POSITIVE_BOND_POINT_INCR_BASE = 0.002;
    private static final double POSITIVE_DESIRE_POINT_INCR = 0.005;
    private static final double NORMAL_POSITIVE_BOND_POINT_INCR = 0.001;
    private static final double NORMAL_POSITIVE_DESIRE_POINT_INCR = 0.001;
    private static final double NORMAL_NEGATIVE_BOND_POINT_DECR = -0.001;
    private static final double NORMAL_NEGATIVE_DESIRE_POINT_DECR = -0.001;
    private static final double NEGATIVE_BOND_POINT_DECR_BASE = -0.02;
    private static final double NEGATIVE_DESIRE_POINT_DECR = -0.02;

    private long getPositiveInteractionMinInterval() {
        return (long) ((1 - bondPoint) * 4 * 1000 + 4000);
    }

    private double getPositiveTransToNegativeThreshold() {
        return 4 * bondPoint + 3;
    }

    private final KokoriConsts kokoriConsts;
    private final AppCallback appCallback;
    private double bondPoint = INITIAL_BOND_POINT;
    private double desirePoint = 0;
    private Scheduled autoBondIncreasingFuture = null;

    public KokoriPersonality(KokoriConsts kokoriConsts, AppCallback appCallback) {
        this.kokoriConsts = kokoriConsts;
        this.appCallback = appCallback;
        Double bondPoint = ConfigManager.get().getBondPoint();
        if (bondPoint != null) {
            this.bondPoint = bondPoint;
        }
        Double desirePoint = ConfigManager.get().getDesirePoint();
        if (desirePoint != null) {
            this.desirePoint = desirePoint;
        }

        // set values based on the time that the program terminates
        long now = System.currentTimeMillis();
        long lastTs = ConfigManager.get().getLastTimestamp();
        long timeDelta = now - lastTs;
        // set bondPoint
        // bond point reduces when you don't contact her
        if (this.bondPoint > INITIAL_BOND_POINT) {
            final long oneMonth = 30L * 24 * 3600 * 1000;
            final long twoDays = 2L * 24 * 3600 * 1000;
            if (timeDelta > oneMonth) {
                this.bondPoint = INITIAL_BOND_POINT;
            } else if (timeDelta > twoDays) {
                this.bondPoint = INITIAL_BOND_POINT + (this.bondPoint - INITIAL_BOND_POINT) * ((double) timeDelta) / (oneMonth - twoDays);
            }
        }
        // set desirePoint
        // desire point reduces when you are not watching her
        if (this.desirePoint >= kokoriConsts.veryHighDesirePoint) {
            if (timeDelta > 3600 * 1000) {
                this.desirePoint = 0.4;
            }
        }
        appCallback.setCharaPoints(new CharaPoints(this.bondPoint, 0, this.desirePoint, 0));
        rescheduleAutoCharaPointsIncreasingDecreasing();
    }

    private void rescheduleAutoCharaPointsIncreasingDecreasing() {
        if (autoBondIncreasingFuture != null) {
            autoBondIncreasingFuture.cancel();
        }
        autoBondIncreasingFuture = ThreadUtils.get().scheduleFX(() -> {
            autoBondIncreasingFuture = null;
            double incBondDelta;
            double incDesireDelta;
            if (bondPoint > 0.8) {
                incBondDelta = AUTO_BOND_POINT_DECR;
            } else {
                incBondDelta = AUTO_BOND_POINT_INCR;
            }
            if (desirePoint > 0.8 || desirePoint < 0.2) {
                incDesireDelta = 0;
            } else {
                incDesireDelta = AUTO_DESIRE_POINT_INCR;
            }
            incPoints(incBondDelta, incDesireDelta);
            rescheduleAutoCharaPointsIncreasingDecreasing();
        }, 10, TimeUnit.MINUTES);
    }

    public void incPoints(double bondDelta, double desireDelta) {
        var bondOld = bondPoint;
        bondPoint += bondDelta;
        if (bondPoint > 1) {
            bondPoint = 1;
        } else if (bondPoint < 0) {
            bondPoint = 0;
        }
        var desireOld = desirePoint;
        desirePoint += desireDelta;
        if (desirePoint > 1) {
            desirePoint = 1;
        } else if (desirePoint < 0) {
            desirePoint = 0;
        }
        appCallback.setCharaPoints(new CharaPoints(bondPoint, bondOld, desirePoint, desireOld));
    }

    private long positiveInteractionTimeRecordForCoolDown = -1;
    private int positiveInteractionCountAfterLast = 0;

    private boolean positiveInteraction(int n, Runnable triggerCB, Runnable negativeCB) {
        long current = System.currentTimeMillis();
        ++positiveInteractionCountAfterLast;
        if (n == 1 && current - positiveInteractionTimeRecordForCoolDown < getPositiveInteractionMinInterval()) {
            assert Logger.debug("positiveInteractionCountAfterLast = " + positiveInteractionCountAfterLast + ", bondPoint=" + bondPoint);
            if (positiveInteractionCountAfterLast >= getPositiveTransToNegativeThreshold()) {
                positiveInteractionTimeRecordForCoolDown = current;
                if (negativeCB == null) {
                    incPoints(POSITIVE_TRANS_TO_NEGATIVE_BOND_POINT_DECR, 0);
                } else {
                    negativeCB.run();
                }
                Logger.info("positive interaction transits to negative, current: " + bondPoint);
            } else {
                assert Logger.debug("positive interaction does nothing");
            }
            return false;
        }
        positiveInteractionTimeRecordForCoolDown = current;
        positiveInteractionCountAfterLast = 0;
        if (triggerCB == null) {
            incPoints(POSITIVE_BOND_POINT_INCR_BASE * n, POSITIVE_DESIRE_POINT_INCR);
        } else {
            triggerCB.run();
        }
        Logger.info("positive interaction * " + n + " increases bond, current: " + bondPoint);
        return true;
    }

    private boolean normalInteraction(Runnable positiveCB, Runnable negativeCB, Runnable nothingCB) {
        boolean ret = true;

        if (Utils.random(0.0005 * bondPoint + 0.0015)) {
            if (positiveCB == null) {
                incPoints(NORMAL_POSITIVE_BOND_POINT_INCR, NORMAL_POSITIVE_DESIRE_POINT_INCR);
            } else {
                positiveCB.run();
            }
            Logger.info("normal interaction rand to positive, current: " + bondPoint);
        } else if (Utils.random(0.001)) {
            if (negativeCB == null) {
                incPoints(NORMAL_NEGATIVE_BOND_POINT_DECR, NORMAL_NEGATIVE_DESIRE_POINT_DECR);
            } else {
                negativeCB.run();
            }
            Logger.info("normal interaction rand to negative, current: " + bondPoint);
            ret = false;
        } else {
            assert Logger.debug("normal interaction does nothing");
            if (nothingCB != null) {
                nothingCB.run();
            }
        }

        return ret;
    }

    private long lastNegativeInteractionTime = -1;
    private long negativeInteractionCountAfterLast = 0;

    private void negativeInteraction(int n, Runnable triggerCB) {
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

        if (triggerCB == null) {
            incPoints(NEGATIVE_BOND_POINT_DECR_BASE * n, NEGATIVE_DESIRE_POINT_DECR * n);
        } else {
            triggerCB.run();
        }
        Logger.info("negative interaction * " + n + ", current: " + bondPoint);
        lastNegativeInteractionTime = -1;
    }

    public boolean touchHair() {
        preTouch();
        return positiveInteraction(1, null, null);
    }

    public void touchEye() {
        preTouch();
        normalInteraction(null, null, null);
    }

    public void touchFace() {
        preTouch();
        if (bondPoint >= 0.7) {
            assert Logger.debug("touch face as positive interaction");
            positiveInteraction(1, null, null);
        } else {
            assert Logger.debug("touch face as normal interaction");
            normalInteraction(null, null, null);
        }
    }

    public boolean touchRune() {
        preTouch();
        return normalInteraction(null, null, null);
    }

    public void touchCloth() {
        preTouch();
        if (normalInteraction(null, null, null)) {
            showNormalMessage();
        }
    }

    public void touchBreast() {
        preTouch();
        if (bondPoint > 0.8) {
            if (Utils.random(0.2)) {
                Logger.info("touch breast rand to positive");
                positiveInteraction(1, null, null);
            } else if (Utils.random(0.1)) {
                Logger.info("touch breast rand to negative");
                negativeInteraction(1, null);

                appCallback.showMessage(KokoriWords.doNotTouchThere.select());
            } else {
                Logger.info("touch breast rand to normal");
                normalInteraction(
                    () ->
                        incPoints(NORMAL_POSITIVE_BOND_POINT_INCR, POSITIVE_DESIRE_POINT_INCR),
                    () ->
                        incPoints(NORMAL_NEGATIVE_BOND_POINT_DECR, 0),
                    () ->
                        incPoints(0, POSITIVE_DESIRE_POINT_INCR));

                appCallback.showMessage(KokoriWords.flirt().select());
            }
        } else if (bondPoint > 0.45) {
            assert Logger.debug("touch breast as negative * 1");
            negativeInteraction(1, null);

            appCallback.showMessage(KokoriWords.doNotTouchThere.select());
        } else {
            assert Logger.debug("touch breast as negative * 5");
            negativeInteraction(5, null);

            appCallback.showMessage(KokoriWords.doNotTouchThere.select());
        }
    }

    public void touchArm() {
        preTouch();
        normalInteraction(null, null, null);
    }

    public int touchCrotch() {
        preTouch();
        if (bondPoint >= 0.9) {
            if (desirePoint >= kokoriConsts.veryHighDesirePoint) {
                Logger.info("touch crotch as positive");
                double originalDesirePoint = desirePoint;
                positiveInteraction(20,
                    () ->
                        incPoints(POSITIVE_BOND_POINT_INCR_BASE * 20, -1 /* reduce all */),
                    () ->
                        incPoints(POSITIVE_TRANS_TO_NEGATIVE_BOND_POINT_DECR, POSITIVE_DESIRE_POINT_INCR));

                if (Global.r18features) {
                    if (originalDesirePoint == 1) {
                        appCallback.showMessage(KokoriR18Words.cannotRestrain.select());
                    } else {
                        appCallback.showMessage(KokoriR18Words.wantSex.select());
                    }
                } else {
                    appCallback.showMessage(KokoriWords.happyWords.select());
                }
                return 1;
            } else {
                Logger.info("touch crotch rand to negative");
                negativeInteraction(1, () ->
                    incPoints(NEGATIVE_BOND_POINT_DECR_BASE, POSITIVE_DESIRE_POINT_INCR));

                appCallback.showMessage(KokoriWords.doNotTouchThere.select());
            }
        } else if (bondPoint >= 0.6) {
            assert Logger.debug("touch crotch as negative");
            negativeInteraction(1, () ->
                incPoints(NEGATIVE_BOND_POINT_DECR_BASE, POSITIVE_DESIRE_POINT_INCR));

            appCallback.showMessage(KokoriWords.doNotTouchThere.select());
        } else {
            assert Logger.debug("touch crotch as negative * 10");
            negativeInteraction(10, null);

            appCallback.showMessage(KokoriWords.doNotTouchThere.select());
            return -1;
        }
        return 0;
    }

    public int touchLeg() {
        preTouch();
        if (bondPoint >= 0.85) {
            assert Logger.debug("touch leg as positive");
            positiveInteraction(1, null, null);
            return 1;
        } else if (bondPoint >= 0.65) {
            if (Utils.random(0.1)) {
                Logger.info("touch leg rand to positive");
                positiveInteraction(1, null, null);
                return 1;
            } else if (Utils.random(0.4)) {
                Logger.info("touch leg rand to negative");
                negativeInteraction(1, null);

                appCallback.showMessage(KokoriWords.doNotTouchLeg.select());
                return -1;
            } else {
                Logger.info("touch leg rand to normal");
                normalInteraction(null, null, null);

                appCallback.showMessage(KokoriWords.flirt().select());
                return 0;
            }
        } else {
            assert Logger.debug("touch leg as negative");
            negativeInteraction(1, null);

            appCallback.showMessage(KokoriWords.doNotTouchLeg.select());
            return -1;
        }
    }

    public void touchOther() {
        preTouch();
        if (normalInteraction(null, null, null)) {
            showNormalMessage();
        }
    }

    private void preTouch() {
        rescheduleAutoCharaPointsIncreasingDecreasing();
    }

    private final RateLimiter messageRateLimiter = new RateLimiter(
        3 * 60 * 1000, 1, // 1 time in 3 minutes
        10 * 60 * 1000, 3 // 3 times in 10 minutes
    );

    public void showNormalMessage() {
        if (!messageRateLimiter.request()) {
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

    public double getBondPoint() {
        return bondPoint;
    }

    public double getDesirePoint() {
        return desirePoint;
    }

    public void setBondPoint(double value) {
        incPoints(value - bondPoint, 0);
    }

    public void setDesirePoint(double value) {
        incPoints(0, value - desirePoint);
    }
}
