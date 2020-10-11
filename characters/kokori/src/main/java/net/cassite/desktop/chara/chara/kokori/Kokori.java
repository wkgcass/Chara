// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori;

import javafx.application.Platform;
import javafx.scene.Group;
import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.AppCallbackDelegate;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.control.ClickHandler;
import net.cassite.desktop.chara.chara.Chara;
import net.cassite.desktop.chara.chara.kokori.chat.KokoriChatBot;
import net.cassite.desktop.chara.chara.kokori.join.ArmRightJoin;
import net.cassite.desktop.chara.chara.kokori.join.EyeJoin;
import net.cassite.desktop.chara.chara.kokori.join.HeadJoin;
import net.cassite.desktop.chara.chara.kokori.parts.*;
import net.cassite.desktop.chara.chara.kokori.personality.KokoriPersonality;
import net.cassite.desktop.chara.chara.kokori.personality.KokoriWords;
import net.cassite.desktop.chara.chara.kokori.special.DontWantToSeeYouException;
import net.cassite.desktop.chara.graphic.Alert;
import net.cassite.desktop.chara.i18n.I18nConsts;
import net.cassite.desktop.chara.special.ModelFileNotFoundException;
import net.cassite.desktop.chara.util.Consts;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Rec;
import net.cassite.desktop.chara.util.Utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("FieldCanBeLocal")
public class Kokori implements Chara {
    private static final Data data = new DataBuilder()
        .setImageWidth(CharaConsts.IMAGE_WIDTH)
        .setImageHeight(CharaConsts.IMAGE_HEIGHT)
        .setMinWidth(CharaConsts.MIN_WIDTH)
        .setInitialWidth(CharaConsts.INITIAL_WIDTH)
        .setMinX(CharaConsts.X_MIN)
        .setMaxX(CharaConsts.X_MAX)
        .setTopMiddleX(CharaConsts.X_TOP_MIDDLE)
        .setBottomMiddleX(CharaConsts.X_BOTTOM_MIDDLE)
        .setMessageOffsetX(CharaConsts.MSG_OFFSET_X)
        .setMessageAtMinY(CharaConsts.MSG_MIN_Y)
        .setMinY(CharaConsts.Y_MIN)
        .setMaxY(CharaConsts.Y_MAX)
        .build();

    public static final class CharaConsts {
        public static final int IMAGE_WIDTH = 1385;
        public static final int IMAGE_HEIGHT = 2400;

        public static final int MIN_WIDTH = 350;
        public static final int INITIAL_WIDTH = 415;

        public static final int X_MIN = 118;
        public static final int X_MAX = 1339;
        public static final int Y_MIN = 325;
        public static final int Y_MAX = 2333;

        public static final int X_TOP_MIDDLE = 680;
        public static final int X_BOTTOM_MIDDLE = 710;

        public static final int EYE_RIGHT_ORIGINAL_X = 629;
        public static final int EYE_RIGHT_ORIGINAL_Y = 600;

        public static final int EYE_RIGHT_X_MIN = 624;
        public static final int EYE_RIGHT_X_MAX = 634;

        public static final int EYE_RIGHT_Y_MIN = 596;
        public static final int EYE_RIGHT_Y_MAX = 605;

        public static final int EYE_LEFT_ORIGINAL_X = 745;
        public static final int EYE_LEFT_ORIGINAL_Y = 595;

        public static final int EYE_LEFT_X_MIN = 740;
        public static final int EYE_LEFT_X_MAX = 751;

        public static final int EYE_LEFT_Y_MIN = 592;
        public static final int EYE_LEFT_Y_MAX = 600;

        public static final int EYE_TRACK_X_MIN =
            EYE_RIGHT_ORIGINAL_X - Math.min(X_MAX - EYE_LEFT_ORIGINAL_X, EYE_RIGHT_ORIGINAL_X - X_MIN);
        public static final int EYE_TRACK_X_MAX =
            EYE_LEFT_ORIGINAL_X + Math.min(X_MAX - EYE_LEFT_ORIGINAL_X, EYE_RIGHT_ORIGINAL_X - X_MIN);
        public static final int EYE_TRACK_Y_MIN = Y_MIN - Consts.CHARA_TOTAL_MARGIN_TOP;
        public static final int EYE_TRACK_Y_MAX = 1355;

        public static final int MSG_OFFSET_X = X_TOP_MIDDLE - 450;
        public static final int MSG_MIN_Y = 650;

        public static final double BAD_MOOD = 0.3;
        public static final double REALLY_BAD_MOOD = 0.2;
        public static final double REALLY_REALLY_BAD_MOOD = 0.15;
    }

    private final Map<Rec, ClickHandler> clickHandlerMap = new LinkedHashMap<>();

    private final Group root = new Group();
    private final AppCallback appCallback;

    public final Hair hair;
    public final RedCheek redCheek;
    public final EyeJoin eyeRight;
    public final EyeJoin eyeLeft;
    public final Mouth mouth;
    public final Head head;
    public final HairSide hairSide;
    public final ArmRightJoin armRight;
    public final Bowknot bowknot;
    public final DressFront dressFront;
    public final NeckBreast neckBreast;
    public final LegLeft legLeft;
    public final LegRight legRight;
    public final DressBack dressBack;
    public final ArmLeft armLeft;
    public final Quiver quiver;
    public final HairBack hairBack;

    public final HeadJoin headJoin;

    private final KokoriPersonality personality;
    private final KokoriChatBot chatBot;

    public Kokori(AppCallback appCallback, Group parentPane) {
        this.appCallback = appCallback;

        root.setLayoutX(0);
        root.setLayoutY(0);
        parentPane.getChildren().add(root);

        var cbDelegate = new AppCallbackDelegate(appCallback) {
            @Override
            public void setBondPoint(double current, double previous) {
                Platform.runLater(() -> onBondPointChange());
                super.setBondPoint(current, previous);
            }
        };
        this.personality = new KokoriPersonality(cbDelegate);
        this.chatBot = new KokoriChatBot(this, personality, cbDelegate);

        hairBack = new HairBack(root);
        quiver = new Quiver(root);
        armLeft = new ArmLeft(root);
        ArmUpperRight armUpperRight = new ArmUpperRight(root);
        dressBack = new DressBack(root);
        legRight = new LegRight(root);
        legLeft = new LegLeft(root);
        neckBreast = new NeckBreast(root);
        dressFront = new DressFront(root);
        bowknot = new Bowknot(root);
        ArmForeRight armForeRight = new ArmForeRight(root);
        HandRight handRight = new HandRight(root);
        hairSide = new HairSide(root);
        EyeLeft eyeLeftPart = new EyeLeft(root);
        EyeRight eyeRightPart = new EyeRight(root);
        head = new Head(root);
        mouth = new Mouth(root);
        EyeSocketLeft eyeSocketLeft = new EyeSocketLeft(root);
        EyeSocketRight eyeSocketRight = new EyeSocketRight(root);
        redCheek = new RedCheek(root);
        hair = new Hair(root);
        EyebrowLeft eyebrowLeft = new EyebrowLeft(root);
        EyebrowRight eyebrowRight = new EyebrowRight(root);

        eyeLeft = new EyeJoin(eyeLeftPart, eyeSocketLeft, eyebrowLeft);
        eyeRight = new EyeJoin(eyeRightPart, eyeSocketRight, eyebrowRight);
        armRight = new ArmRightJoin(armUpperRight, armForeRight, handRight);
        headJoin = new HeadJoin(head, hair, hairSide, hairBack, eyeLeft, eyeRight, mouth, redCheek);

        // initiate handlers
        clickHandlerMap.put(new Rec(537, 380, 837, 566), this::clickHair);
        clickHandlerMap.put(new Rec(605, 576, 648, 609), this::clickEyeRight);
        clickHandlerMap.put(new Rec(719, 573, 762, 604), this::clickEyeLeft);
        clickHandlerMap.put(new Rec(584, 583, 781, 671), this::clickFace);
        clickHandlerMap.put(new Rec(718, 1115, 797, 1210), this::clickRune);
        clickHandlerMap.put(new Rec(133, 1338, 433, 1604), this::clickCloth);
        clickHandlerMap.put(new Rec(400, 1190, 629, 1402), this::clickCloth);
        clickHandlerMap.put(new Rec(891, 1311, 1206, 1566), this::clickCloth);
        clickHandlerMap.put(new Rec(758, 1238, 920, 1452), this::clickCloth);
        clickHandlerMap.put(new Rec(607, 995, 786, 1121), this::clickBowknot);
        clickHandlerMap.put(new Rec(586, 797, 822, 926), this::clickBreast);
        clickHandlerMap.put(new Rec(805, 768, 932, 1190), this::clickArmLeft);
        clickHandlerMap.put(new Rec(626, 1187, 743, 1464), this::clickCrotch);
        clickHandlerMap.put(new Rec(677, 1440, 910, 2276), this::clickLegLeft);
        clickHandlerMap.put(new Rec(572, 1395, 908, 2314), this::clickLeg);
        clickHandlerMap.put(new Rec(0, 1647, 581, 2400), this::clickNothing);
        clickHandlerMap.put(new Rec(903, 1580, 1385, 2400), this::clickNothing);
        clickHandlerMap.put(new Rec(0, 0, 479, 1197), this::clickNothing);
        clickHandlerMap.put(new Rec(898, 0, 1385, 1161), this::clickNothing);

        // initiate random events
        {
            // run every 3 seconds
            ThreadUtils.get().scheduleAtFixedRateFX(this::randomEvent,
                5 * 1000, 3 * 1000, TimeUnit.MILLISECONDS);
        }

        resetBondPointRelated();
    }

    private void onBondPointChange() {
        if (personality == null) {
            return; // not initialized yet
        }
        resetBondPointRelated();
        if (personality.getBondPoint() < CharaConsts.REALLY_BAD_MOOD) {
            Alert.alert(I18nConsts.bondPointTooLowWarning.get()[0]);
        }
    }

    private void resetBondPointRelated() {
        resetMouth();
        resetHighlight();
    }

    private void resetMouth() {
        if (mouth == null) {
            return; // not initialized yet
        }
        if (personality.getBondPoint() < CharaConsts.BAD_MOOD) {
            mouth.toSad();
        } else {
            mouth.toDefault();
        }
    }

    private void resetHighlight() {
        if (eyeLeft == null || eyeRight == null) {
            return; // not initialized yet
        }
        if (personality.getBondPoint() < CharaConsts.REALLY_BAD_MOOD) {
            eyeLeft.removeHighlight();
            eyeRight.removeHighlight();
        } else {
            eyeLeft.addHighlight();
            eyeRight.addHighlight();
        }
    }

    private boolean eyeTracked = false;

    @Override
    public void ready() {
        if (personality.getBondPoint() < CharaConsts.REALLY_REALLY_BAD_MOOD) {
            appCallback.showMessage(KokoriWords.reallyReallyBadMoodOpening.select());
        } else if (personality.getBondPoint() < CharaConsts.BAD_MOOD) {
            appCallback.showMessage(KokoriWords.badMoodOpening.select());
        } else {
            appCallback.showMessage(KokoriWords.opening().select());
        }
    }

    @Override
    public void mouseMove(double x, double y) {
        if (CharaConsts.EYE_TRACK_X_MIN <= x && x <= CharaConsts.EYE_TRACK_X_MAX &&
            CharaConsts.EYE_TRACK_Y_MIN <= y && y <= CharaConsts.EYE_TRACK_Y_MAX) {
            trackEye(x, y);
        } else {
            restoreEyePosition();
        }
    }

    private void trackEye(double x, double y) {
        var foo = eyeTracked;
        eyeTracked = true;
        if (!foo) {
            eyeLeft.blink();
            eyeRight.blink();
        }
        eyeLeft.track(x, y);
        eyeRight.track(x, y);
    }

    @Override
    public void mouseLeave() {
        restoreEyePosition();
    }

    @Override
    public void dragged() {
        hair.swing();
        hairSide.swing();
        hairBack.swing();
        quiver.shake();
        dressFront.flutter();
        dressBack.flutter();
    }

    private void restoreEyePosition() {
        var foo = eyeTracked;
        eyeTracked = false;
        if (!foo) {
            return;
        }
        eyeLeft.restorePosition();
        eyeRight.restorePosition();
        eyeLeft.blink();
        eyeRight.blink();
    }

    @Override
    public void click(double x, double y) {
        if (personality.getBondPoint() < CharaConsts.BAD_MOOD) {
            if (Utils.random(0.2)) {
                appCallback.showMessage(KokoriWords.dontWantToSeeYou.select());
                return;
            }
        }
        if (personality.getBondPoint() < CharaConsts.REALLY_BAD_MOOD) {
            if (Utils.random(0.3)) {
                Logger.fatal(KokoriWords.dontWantToSeeYou.select().get()[0], new DontWantToSeeYouException());
                return;
            }

            Alert.alert(I18nConsts.bondPointTooLowWarning.get()[0]);
        }
        if (personality.getBondPoint() < CharaConsts.REALLY_REALLY_BAD_MOOD) {
            if (Utils.random(0.3)) {
                ModelFileNotFoundException.moveModelFile();
                Logger.fatal(I18nConsts.modelFileNotFound.get()[0], new ModelFileNotFoundException());
                return;
            }
        }
        if (personality.getBondPoint() < CharaConsts.REALLY_BAD_MOOD) {
            // do not interact in this situation
            return;
        }

        // normal handling
        for (var entry : clickHandlerMap.entrySet()) {
            var rec = entry.getKey();
            var handler = entry.getValue();
            if (rec.x1 <= x && x <= rec.x2 && rec.y1 <= y && y <= rec.y2) {
                handler.click(x, y);
                return;
            }
        }
        clickOther(x, y);
    }

    @Override
    public Data data() {
        return data;
    }

    private void clickHair(double x, double y) {
        assert Logger.debug("click hair");
        hair.swing();
        hairSide.swing();
        hairBack.swing();

        if (personality.touchHair()) {
            appCallback.showMessage(KokoriWords.happy.select());
            if (Utils.random(0.2)) {
                Utils.shortDelay(mouth::toHappy);
                Utils.delay("touch-hair-happy", 3000, this::resetMouth);
            }
        }
    }

    private int eyeRightState = 0; // 0: normal, 1: changing color, 2: color selected

    private void clickEyeRight(double x, double y) {
        assert Logger.debug("click eye right");
        eyeRight.blink();
        if (eyeRightState == 0) {
            eyeRightState = 1;
            eyeRight.beginAnimatingPupilColor();
        } else if (eyeRightState == 1) {
            eyeRightState = 2;
            eyeRight.stopAnimatingPupilColor();
        } else {
            eyeRightState = 0;
            eyeRight.resetPupilColor();
        }

        personality.touchEye();
    }

    private int eyeLeftState = 0; // 0: normal, 1: changing color, 2: color selected

    private void clickEyeLeft(double x, double y) {
        assert Logger.debug("click eye left");
        eyeLeft.blink();
        if (eyeLeftState == 0) {
            eyeLeftState = 1;
            eyeLeft.beginAnimatingPupilColor();
        } else if (eyeLeftState == 1) {
            eyeLeftState = 2;
            eyeLeft.stopAnimatingPupilColor();
        } else {
            eyeLeftState = 0;
            eyeLeft.resetPupilColor();
        }

        personality.touchEye();
    }

    private void clickFace(double x, double y) {
        assert Logger.debug("click face");
        boolean redCheck = false;
        if (Utils.random(0.5)) {
            redCheek.show();
            Utils.delay("touch-face", 1500, redCheek::hide);

            redCheck = true;

            appCallback.showMessage(KokoriWords.shy.select());
        }

        if (headJoin.getState() == 0) {
            if (!redCheck || Utils.random(0.15)) {
                if (Utils.random(0.5)) {
                    headJoin.tiltToLeft();
                } else {
                    headJoin.tiltToRight();
                }
                Utils.delay("touch-face-head-tilt", 2000, headJoin::toDefaultPosition);
            }
        } else {
            headJoin.toDefaultPosition();
        }

        personality.touchFace();
    }

    private void clickRune(double x, double y) {
        assert Logger.debug("click rune");
        if (!armRight.runeIsVisible()) {
            assert Logger.debug("fall to click cloth because it's not visible");
            clickCloth(x, y);
        }
        armRight.runeFlow();
        Utils.delay("hide-rune", 1000, armRight::hideRune);

        if (personality.touchRune()) {
            appCallback.showMessage(KokoriWords.aboutRune.select());
        }
    }

    private void clickCloth(double x, double y) {
        assert Logger.debug("click cloth");
        dressFront.flutter();
        dressBack.flutter();

        personality.touchCloth();
    }

    private void clickBowknot(double x, double y) {
        assert Logger.debug("click bowknot");
        bowknot.flow();

        personality.touchCloth();
    }

    private void clickBreast(double x, double y) {
        assert Logger.debug("click breast");
        Utils.shortDelay(mouth::toSad);
        redCheek.show();
        armRight.tighten();
        armRight.hideRune();
        armRight.hideArrow();
        Utils.delay("touch-breast", 1500, () -> {
            resetMouth();
            redCheek.hide();
            armRight.moveToDefaultPosition();
        });

        personality.touchBreast();
    }

    private void clickArmLeft(double x, double y) {
        assert Logger.debug("click arm left");
        if (armLeft.bowIsShown()) {
            armLeft.hideBow();
            if (armRight.arrowIsVisible()) {
                quiver.shake();
            }
            armRight.hideArrow();
        } else {
            armLeft.showBow();
            armRight.hideRune();
            armRight.showArrow();
            quiver.shake();

            appCallback.showMessage(KokoriWords.showBow.select());
        }

        personality.touchArm();
    }

    private void clickCrotch(double x, double y) {
        assert Logger.debug("click crotch");

        int level = personality.touchCrotch();
        if (level >= 1) {
            Utils.shortDelay(mouth::toHappy);
            redCheek.show();
            legLeft.tighten();
            dressFront.flutter();
            dressBack.flutter();
            Utils.delay("click-crotch-happy", 1500, () -> {
                resetMouth();
                redCheek.hide();
            });
        } else {
            Utils.shortDelay(mouth::toSad);
            if (level > -1) {
                redCheek.show();
            }
            legLeft.tighten();
            armRight.protectCrotch();
            armRight.hideArrow();
            armRight.hideRune();
            dressFront.flutter();
            dressBack.flutter();
            Utils.delay("click-crotch", 1500, () -> {
                redCheek.hide();
                resetMouth();
                armRight.moveToDefaultPosition();
            });
            if (level <= -1) {
                if (Utils.random(0.3)) {
                    Utils.shortDelay(eyeLeft::removeHighlight);
                    Utils.shortDelay(eyeRight::removeHighlight);
                    Utils.delay("lose-highlight", 1500, this::resetHighlight);
                }
            }
        }
    }

    private void clickLegLeft(double x, double y) {
        assert Logger.debug("click leg left");
        legLeft.tighten();
        dressFront.flutter();
        dressBack.flutter();
        redCheek.show();

        int result = personality.touchLeg();
        if (result != 1) {
            Utils.shortDelay(mouth::toSad);
        }

        Utils.delay("click-leg-left", 1500, () -> {
            redCheek.hide();
            if (result != 1) {
                resetMouth();
            }
        });
    }

    private void clickLeg(double x, double y) {
        assert Logger.debug("click leg");
        redCheek.show();

        int result = personality.touchLeg();
        if (result != 1) {
            Utils.shortDelay(mouth::toSad);
        }

        Utils.delay("click-leg", 1500, () -> {
            redCheek.hide();
            if (result != 1) {
                resetMouth();
            }
        });
    }

    private void clickNothing(double x, double y) {
        assert Logger.debug("click nothing");
        appCallback.clickNothing(x, y);
    }

    private void clickOther(@SuppressWarnings("unused") double x, @SuppressWarnings("unused") double y) {
        assert Logger.debug("click other");
        eyeLeft.blink();
        eyeRight.blink();
        if (Utils.random(0.5)) {
            Utils.delay("double-blink", 250, () -> {
                eyeLeft.blink();
                eyeRight.blink();
            });
        }

        personality.touchOther();
    }

    private void randomEvent() {
        if (Utils.random(0.7)) {
            Utils.randomDelay(() -> {
                hair.swing();
                hairSide.swing();
                if (Utils.random(0.8)) {
                    hairBack.swing();
                }
            });
        }
        if (Utils.random(0.7)) {
            Utils.randomDelay(() -> {
                bowknot.flow();
                dressFront.flutter();
                dressBack.flutter();
            });
        }
        if (Utils.random(0.8)) {
            Utils.randomDelay(() -> {
                eyeLeft.blink();
                eyeRight.blink();
                if (Utils.random(0.4)) {
                    Utils.delay("double-blink", 250, () -> {
                        eyeLeft.blink();
                        eyeRight.blink();
                    });
                }
            });
        }
        if (Utils.random(0.6)) {
            Utils.randomDelay(armRight::runeFlow);
        }
        if (Utils.random(0.006)) {
            Utils.randomDelay(() -> {
                if (!armRight.runeIsVisible() && !armRight.arrowIsVisible()) {
                    armRight.showRune();
                }
            });
        }
    }

    @Override
    public void release() {
        // nothing to do
    }

    @Override
    public void takeMessage(String msg) {
        chatBot.takeMessage(msg);
    }
}
