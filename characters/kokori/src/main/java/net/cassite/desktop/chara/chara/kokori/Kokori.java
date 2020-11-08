// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import net.cassite.desktop.chara.*;
import net.cassite.desktop.chara.chara.Chara;
import net.cassite.desktop.chara.chara.kokori.chat.KokoriChatBot;
import net.cassite.desktop.chara.chara.kokori.i18n.KokoriI18n;
import net.cassite.desktop.chara.chara.kokori.i18n.KokoriR18I18n;
import net.cassite.desktop.chara.chara.kokori.join.ArmRightJoin;
import net.cassite.desktop.chara.chara.kokori.join.EyeJoin;
import net.cassite.desktop.chara.chara.kokori.join.HeadJoin;
import net.cassite.desktop.chara.chara.kokori.parts.*;
import net.cassite.desktop.chara.chara.kokori.personality.KokoriPersonality;
import net.cassite.desktop.chara.chara.kokori.personality.KokoriR18Words;
import net.cassite.desktop.chara.chara.kokori.personality.KokoriWords;
import net.cassite.desktop.chara.chara.kokori.special.DontWantToSeeYouException;
import net.cassite.desktop.chara.chara.kokori.special.ModelFileNotFoundException;
import net.cassite.desktop.chara.chara.kokori.util.Consts;
import net.cassite.desktop.chara.control.ClickHandler;
import net.cassite.desktop.chara.graphic.Alert;
import net.cassite.desktop.chara.i18n.I18nConsts;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.model.kokori.KokoriConsts;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Rec;
import net.cassite.desktop.chara.util.Scheduled;
import net.cassite.desktop.chara.util.Utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("FieldCanBeLocal")
public class Kokori implements Chara {
    private final Map<Rec, ClickHandler> clickHandlerMap = new LinkedHashMap<>();

    final Group root = new Group();
    final AppCallback appCallback;
    final KokoriConsts kokoriConsts;
    private final Data data;

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

    final KokoriPersonality personality;
    private final KokoriChatBot chatBot;
    public final KokoriR18 r18;

    private final Scheduled randomEventsScheduledFuture;

    final Menu characterMenu;
    private final MenuItem aboutNameMenuItem = new MenuItem(KokoriI18n.aboutNameMenuItem.get()[0]);
    private final MenuItem conversationMenuItem = new MenuItem(KokoriI18n.conversationMenuItem.get()[0]);
    private final MenuItem thingsSheLikesMenuItem = new MenuItem(KokoriI18n.thingsSheLikesMenuItem.get()[0]);
    private final MenuItem thingsSheHatesMenuItem = new MenuItem(KokoriI18n.thingsSheHatesMenuItem.get()[0]);
    private final MenuItem[] bondStoriesMenuItems = new MenuItem[5];
    private final double[] bondStoriesRequiredBondPoints = new double[5];
    private final MenuItem expressionMenuItem = new MenuItem(KokoriI18n.expressionMenuItem.get()[0]);
    private final CheckMenuItem proposeMenuItem = new CheckMenuItem(KokoriI18n.proposeMenuItem.get()[0]);

    public Kokori(KokoriConsts kokoriConsts, AppCallback appCallback, Group parent, Menu characterMenu) {
        this.appCallback = appCallback;
        this.kokoriConsts = kokoriConsts;
        var dataBuilder = new DataBuilder()
            .setImageWidth(kokoriConsts.imageWidth)
            .setImageHeight(kokoriConsts.imageHeight)
            .setMinWidth(kokoriConsts.minWidth)
            .setInitialWidth(kokoriConsts.initialWidth)
            .setMinX(kokoriConsts.xMin)
            .setMaxX(kokoriConsts.xMax)
            .setTopMiddleX(kokoriConsts.xTopMiddle)
            .setBottomMiddleX(kokoriConsts.xBottomMiddle)
            .setMessageOffsetX(kokoriConsts.msgOffsetX)
            .setMessageAtMinY(kokoriConsts.msgMinY)
            .setMinY(kokoriConsts.yMin)
            .setMaxY(kokoriConsts.yMax);
        this.data = dataBuilder.build();

        root.setLayoutX(0);
        root.setLayoutY(0);
        parent.getChildren().add(root);

        var cbDelegate = new AppCallbackDelegate(appCallback) {
            @Override
            public void setCharaPoints(CharaPoints points) {
                Platform.runLater(() -> onCharaPointsChange());
                super.setCharaPoints(points);
            }
        };
        this.personality = new KokoriPersonality(kokoriConsts, cbDelegate);
        this.chatBot = new KokoriChatBot(this, personality, cbDelegate);
        this.r18 = new KokoriR18(this);

        // menu
        this.characterMenu = characterMenu;
        aboutNameMenuItem.setOnAction(e -> this.menuAboutName());
        conversationMenuItem.setOnAction(e -> this.conversation());
        thingsSheLikesMenuItem.setOnAction(e -> this.menuThingsSheLikes());
        thingsSheHatesMenuItem.setOnAction(e -> this.menuThingsSheHates());
        for (int i = 0; i < 5; ++i) {
            bondStoriesMenuItems[i] = new MenuItem(KokoriI18n.bondStoryMenuItem.get()[0] + " " + (i + 1));
            bondStoriesRequiredBondPoints[i] = (1 - KokoriPersonality.INITIAL_BOND_POINT) / 5 * (i + 1) + KokoriPersonality.INITIAL_BOND_POINT;
            final var ii = i;
            bondStoriesMenuItems[i].setOnAction(e -> this.menuBondStory(ii));
        }
        characterMenu.getItems().addAll(
            aboutNameMenuItem,
            conversationMenuItem,
            thingsSheLikesMenuItem,
            thingsSheHatesMenuItem
        );
        characterMenu.getItems().addAll(bondStoriesMenuItems);
        expressionMenuItem.setOnAction(e -> this.menuExpression());
        characterMenu.getItems().add(expressionMenuItem);
        proposeMenuItem.setOnAction(e -> this.propose());
        proposeMenuItem.setDisable(true);
        proposeMenuItem.setSelected(ConfigManager.get().getBoolValue(Consts.PROPOSING_ACCEPTED));
        r18.initCharacterMenu(characterMenu);

        hairBack = new HairBack(root);
        quiver = new Quiver(root);
        armLeft = new ArmLeft(kokoriConsts, root);
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
        EyeLeft eyeLeftPart = new EyeLeft(kokoriConsts, root);
        EyeRight eyeRightPart = new EyeRight(kokoriConsts, root);
        head = new Head(root);
        mouth = new Mouth(kokoriConsts, root);
        EyeSocketLeft eyeSocketLeft = new EyeSocketLeft(root);
        EyeSocketRight eyeSocketRight = new EyeSocketRight(root);
        redCheek = new RedCheek(root);
        hair = new Hair(root);
        EyebrowLeft eyebrowLeft = new EyebrowLeft(root);
        EyebrowRight eyebrowRight = new EyebrowRight(root);

        eyeLeft = new EyeJoin(eyeLeftPart, eyeSocketLeft, eyebrowLeft);
        eyeRight = new EyeJoin(eyeRightPart, eyeSocketRight, eyebrowRight);
        armRight = new ArmRightJoin(kokoriConsts, armUpperRight, armForeRight, handRight, armLeft);
        headJoin = new HeadJoin(kokoriConsts, head, hair, hairSide, hairBack, eyeLeft, eyeRight, mouth, redCheek);

        // initiate handlers
        clickHandlerMap.put(kokoriConsts.clickHairRec, this::clickHair);
        clickHandlerMap.put(kokoriConsts.clickEyeRightRec, this::clickEyeRight);
        clickHandlerMap.put(kokoriConsts.clickEyeLeftRec, this::clickEyeLeft);
        clickHandlerMap.put(kokoriConsts.clickFaceRec, this::clickFace);
        clickHandlerMap.put(kokoriConsts.clickRuneRec, this::clickRune);
        clickHandlerMap.put(kokoriConsts.clickClothRec1, this::clickCloth);
        clickHandlerMap.put(kokoriConsts.clickClothRec2, this::clickCloth);
        clickHandlerMap.put(kokoriConsts.clickClothRec3, this::clickCloth);
        clickHandlerMap.put(kokoriConsts.clickClothRec4, this::clickCloth);
        clickHandlerMap.put(kokoriConsts.clickBowknotRec, this::clickBowknot);
        clickHandlerMap.put(kokoriConsts.clickBreastRec, this::clickBreast);
        clickHandlerMap.put(kokoriConsts.clickArmLeftRec, this::clickArmLeft);
        clickHandlerMap.put(kokoriConsts.clickCrotchRec, this::clickCrotch);
        clickHandlerMap.put(kokoriConsts.clickLegLeftRec, this::clickLegLeft);
        clickHandlerMap.put(kokoriConsts.clickLegRec, this::clickLeg);
        clickHandlerMap.put(kokoriConsts.clickNothingRec1, this::clickNothing);
        clickHandlerMap.put(kokoriConsts.clickNothingRec2, this::clickNothing);
        clickHandlerMap.put(kokoriConsts.clickNothingRec3, this::clickNothing);
        clickHandlerMap.put(kokoriConsts.clickNothingRec4, this::clickNothing);

        // initiate random events
        {
            // run every 3 seconds
            randomEventsScheduledFuture = ThreadUtils.get().scheduleAtFixedRateFX(this::randomEvent,
                5 * 1000, 3 * 1000, TimeUnit.MILLISECONDS);
        }

        resetCharaPointsRelated();
    }

    private void onCharaPointsChange() {
        if (personality == null) {
            return; // not initialized yet
        }
        if (state != State.NORMAL) {
            return;
        }
        resetCharaPointsRelated();
        if (personality.getBondPoint() < kokoriConsts.reallyBadMood) {
            Alert.alert(KokoriI18n.bondPointTooLowWarning.get()[0]);
        }
    }

    public void resetAll() {
        resetCharaPointsRelated();
        resetMouth();
        resetCheek();
        resetHighlight();
        eyeLeft.restorePosition();
        eyeLeft.zoom(1);
        eyeRight.restorePosition();
        eyeRight.zoom(1);
        armRight.moveToDefaultPosition();
        armRight.animateMoveToFront(() -> {
        });
        armLeft.hideBow();
        legLeft.loose();
        headJoin.toDefaultPosition();
    }

    public void resetCharaPointsRelated() {
        resetMouth();
        resetCheek();
        resetHighlight();
        resetMenuItems();
        restoreEyePosition();
    }

    public void resetMouth() {
        if (mouth == null) {
            return; // not initialized yet
        }
        if (personality.getDesirePoint() == 1 && Global.r18Features()) {
            mouth.startAnimatingOpenAndShut();
        } else if (personality.getBondPoint() < kokoriConsts.badMood) {
            mouth.stopAnimatingOpenAndShut();
            mouth.toSad();
        } else {
            mouth.stopAnimatingOpenAndShut();
            mouth.toDefault();
        }
    }

    public void resetCheek() {
        if (redCheek == null) {
            return; // not initialized yet
        }
        if (Global.r18Features() && personality.getDesirePoint() >= kokoriConsts.veryHighDesirePoint) {
            redCheek.show();
        } else {
            redCheek.hide();
        }
    }

    public void resetHighlight() {
        if (eyeLeft == null || eyeRight == null) {
            return; // not initialized yet
        }
        if (personality.getDesirePoint() == 1 && Global.r18Features()) {
            eyeLeft.removeHighlight();
            eyeRight.removeHighlight();
        } else if (personality.getBondPoint() < kokoriConsts.reallyBadMood) {
            eyeLeft.removeHighlight();
            eyeRight.removeHighlight();
        } else {
            eyeLeft.addHighlight();
            eyeRight.addHighlight();
        }
    }

    private void resetMenuItems() {
        double bondPoint = personality.getBondPoint();
        thingsSheLikesMenuItem.setDisable(bondPoint < 0.7);
        for (int i = 0; i < bondStoriesMenuItems.length; ++i) {
            var menuItem = bondStoriesMenuItems[i];
            var required = bondStoriesRequiredBondPoints[i];
            menuItem.setDisable(bondPoint < required);
        }
        if (bondPoint < 0.85) {
            characterMenu.getItems().remove(proposeMenuItem);
        } else {
            if (!characterMenu.getItems().contains(proposeMenuItem)) {
                characterMenu.getItems().add(proposeMenuItem);
            }
        }
    }

    @Override
    public void ready(ReadyParams params) {
        if (personality.getBondPoint() < kokoriConsts.reallyReallyBadMood) {
            appCallback.showMessage(KokoriWords.reallyReallyBadMoodOpening.select());
        } else if (personality.getBondPoint() < kokoriConsts.badMood) {
            appCallback.showMessage(KokoriWords.badMoodOpening.select());
        } else {
            appCallback.showMessage(KokoriWords.opening().select());
        }

        r18.ready(params);
    }

    enum State {
        NORMAL,
        R18_SEX,
        AFTER_SEX,
    }

    State state = State.NORMAL;

    @Override
    public void mouseMove(double x, double y) {
        if (state != State.NORMAL) {
            return;
        }

        if (kokoriConsts.eyeTrackXMin <= x && x <= kokoriConsts.eyeTrackXMax &&
            kokoriConsts.eyeTrackYMin <= y && y <= kokoriConsts.eyeTrackYMax) {
            trackEye(x, y);
        } else {
            restoreEyePosition();
        }
    }

    private boolean eyeTracked = false;

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
        if (state != State.NORMAL) {
            return;
        }

        restoreEyePosition();
    }

    @Override
    public void dragged() {
        if (state != State.NORMAL) {
            return;
        }

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
        if (Global.r18Features() && personality.getDesirePoint() == 1) {
            lookAtLeftOrRight();
        } else {
            eyeLeft.restorePosition();
            eyeRight.restorePosition();
            eyeLeft.blink();
            eyeRight.blink();
        }
    }

    private void lookAtLeftOrRight() {
        if (Utils.random(0.5)) {
            double leftDelta = kokoriConsts.eyeLeftOriginalX - kokoriConsts.eyeLeftXMin;
            eyeLeft.move(kokoriConsts.eyeLeftOriginalX - leftDelta * 0.8, kokoriConsts.eyeLeftOriginalY);
            double rightDelta = kokoriConsts.eyeRightOriginalX - kokoriConsts.eyeRightXMin;
            eyeRight.move(kokoriConsts.eyeRightOriginalX - rightDelta * 0.8, kokoriConsts.eyeRightOriginalY);
        } else {
            double leftDelta = kokoriConsts.eyeLeftXMax - kokoriConsts.eyeLeftOriginalX;
            eyeLeft.move(kokoriConsts.eyeLeftOriginalX + leftDelta * 0.8, kokoriConsts.eyeLeftOriginalY);
            double rightDelta = kokoriConsts.eyeRightXMax - kokoriConsts.eyeRightOriginalX;
            eyeRight.move(kokoriConsts.eyeRightOriginalX + rightDelta * 0.8, kokoriConsts.eyeRightOriginalY);
        }
        eyeLeft.blink();
        eyeRight.blink();
    }

    @Override
    public void click(double x, double y) {
        if (state == State.R18_SEX) {
            r18.clickStateSex(x, y);
            return;
        }
        if (state != State.NORMAL) {
            return;
        }

        if (preInteractionCheckFail()) {
            return;
        }

        // normal handling
        for (var entry : clickHandlerMap.entrySet()) {
            var rec = entry.getKey();
            var handler = entry.getValue();
            if (rec.contains(x, y)) {
                handler.click(x, y);
                return;
            }
        }
        clickOther(x, y);
    }

    public boolean preInteractionCheckFail() {
        if (personality.getBondPoint() < kokoriConsts.badMood) {
            if (Utils.random(0.2)) {
                appCallback.showMessage(KokoriWords.dontWantToSeeYou.select());
                return true;
            }
        }
        if (personality.getBondPoint() < kokoriConsts.reallyBadMood) {
            if (Utils.random(0.3)) {
                Logger.fatal(KokoriWords.dontWantToSeeYou.select().get()[0], new DontWantToSeeYouException());
                return true;
            }

            Alert.alert(KokoriI18n.bondPointTooLowWarning.get()[0]);
        }
        if (personality.getBondPoint() < kokoriConsts.reallyReallyBadMood) {
            if (Utils.random(0.3)) {
                ModelFileNotFoundException.moveModelFile();
                Logger.fatal(I18nConsts.modelFileNotFound.get()[0], new ModelFileNotFoundException());
                return true;
            }
        }
        //noinspection RedundantIfStatement
        if (personality.getBondPoint() < kokoriConsts.reallyBadMood) {
            // do not interact in this situation
            return true;
        }
        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.isShiftDown() && e.isAltDown()) {
            switch (e.getCode()) {
                case H: // Happy
                    expressionHappy();
                    break;
                case S: // Shy
                    expressionShy();
                    break;
                case A: // sAd
                    expressionSad();
                    break;
                case D: // Disgust
                    expressionDisgust();
                    break;
                case R: // Reset
                    resetAll();
                    break;
                case P: // surPrised
                    expressionSurprised();
                    break;
                case Y: // Yandere
                    if (Global.r18Features()) {
                        expressionYandere();
                    }
                    break;
                case O: // Orgasm
                    if (Global.r18Features()) {
                        expressionOrgasm();
                    }
                    break;
            }
        }
        // do nothing
    }

    private void expressionHappy() {
        eyeLeft.addHighlight();
        eyeRight.addHighlight();
        redCheek.hide();
        mouth.toHappy();
    }

    private void expressionShy() {
        eyeLeft.addHighlight();
        eyeRight.addHighlight();
        redCheek.show();
        mouth.toSad();
    }

    private void expressionSad() {
        mouth.toSad();
    }

    private void expressionDisgust() {
        eyeLeft.removeHighlight();
        eyeRight.removeHighlight();
        redCheek.hide();
        mouth.toSad();
    }

    private void expressionSurprised() {
        eyeLeft.zoom(0.85);
        eyeRight.zoom(0.85);
        mouth.toOpen();
    }

    private void expressionYandere() {
        headJoin.tiltToLeft();
        eyeLeft.removeHighlight();
        eyeRight.removeHighlight();
        redCheek.show();
        mouth.toHappy();
        armRight.animateMoveToBack(armRight::tighten);
        legLeft.tighten(() -> {
        });
    }

    private void expressionOrgasm() {
        eyeLeft.zoom(0.85);
        eyeLeft.move(kokoriConsts.eyeLeftOriginalX, kokoriConsts.eyeLeftYMin);
        eyeLeft.removeHighlight();
        eyeRight.zoom(0.85);
        eyeRight.move(kokoriConsts.eyeRightOriginalX, kokoriConsts.eyeRightYMin);
        eyeRight.removeHighlight();
        mouth.toOpen();
        redCheek.show();
        armRight.protectCrotch();
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // do nothing
    }

    @Override
    public Data data() {
        return data;
    }

    @Override
    public int shutdown(Runnable cb) {
        return 0;
    }

    private void clickHair(double x, double y) {
        assert Logger.debug("click hair");
        hair.swing();
        hairSide.swing();
        hairBack.swing();

        if (personality.touchHair()) {
            if (Utils.random(0.5)) {
                appCallback.showMessage(KokoriWords.happy.select());
            } else {
                personality.showNormalMessage();
            }
            if (Utils.random(0.2)) {
                Utils.shortDelay(mouth::toHappy);
                Utils.delay("touch-hair-happy", 3000, this::resetMouth);
            }
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private void clickEyeRight(double x, double y) {
        assert Logger.debug("click eye right");
        eyeRight.blink();
        personality.touchEye();
    }

    @SuppressWarnings("DuplicatedCode")
    private void clickEyeLeft(double x, double y) {
        assert Logger.debug("click eye left");
        eyeLeft.blink();
        personality.touchEye();
    }

    private void clickFace(double x, double y) {
        assert Logger.debug("click face");
        boolean redCheck = false;
        if (Utils.random(0.5)) {
            Utils.shortDelay(redCheek::show);
            Utils.delay("touch-face", 1500, this::resetCheek);

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
            assert Logger.debug("fall to click rune because it's not visible");
            clickCloth(x, y);
            return;
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
        Utils.shortDelay(redCheek::show);
        armRight.tighten();
        armRight.hideRune();
        armRight.hideArrow();
        Utils.delay("touch-breast", 1500, () -> {
            resetMouth();
            resetCheek();
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
            Utils.shortDelay(redCheek::show);
            legLeft.tighten(legLeft::loose);
            dressFront.flutter();
            dressBack.flutter();
            if (Global.r18Features()) {
                Utils.delay("click-crotch-happy", 1500, r18::startHavingSex);
            } else {
                Utils.delay("click-crotch-happy", 1500, () -> {
                    resetMouth();
                    redCheek.hide();
                });
            }
        } else {
            Utils.shortDelay(mouth::toSad);
            if (level > -1) {
                Utils.shortDelay(redCheek::show);
            }
            if (level <= -1) {
                if (Utils.random(0.3)) {
                    Utils.shortDelay(eyeLeft::removeHighlight);
                    Utils.shortDelay(eyeRight::removeHighlight);
                } else {
                    Utils.shortDelay(redCheek::show);
                }
            }
            legLeft.tighten(legLeft::loose);
            armRight.protectCrotch();
            armRight.hideArrow();
            armRight.hideRune();
            dressFront.flutter();
            dressBack.flutter();
            Utils.delay("click-crotch", 1500, () -> {
                resetCheek();
                resetMouth();
                resetHighlight();
                armRight.moveToDefaultPosition();
            });
        }
    }

    private void clickLegLeft(double x, double y) {
        assert Logger.debug("click leg left");
        legLeft.tighten(legLeft::loose);
        dressFront.flutter();
        dressBack.flutter();
        Utils.shortDelay(redCheek::show);

        int result = personality.touchLeg();
        if (result != 1) {
            Utils.shortDelay(mouth::toSad);
        }

        Utils.delay("click-leg-left", 1500, () -> {
            resetCheek();
            if (result != 1) {
                resetMouth();
            }
        });
    }

    private void clickLeg(double x, double y) {
        assert Logger.debug("click leg");
        Utils.shortDelay(redCheek::show);

        int result = personality.touchLeg();
        if (result != 1) {
            Utils.shortDelay(mouth::toSad);
        }

        Utils.delay("click-leg", 1500, () -> {
            resetCheek();
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
        if (state != State.NORMAL) {
            return;
        }

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
            if (!armRight.runeIsVisible() && !armRight.arrowIsVisible()) {
                armRight.showRune();
            }
        }
        if (Utils.random(0.001)) {
            r18.addLovePotion();
        }
        if (Global.r18Features() && personality.getDesirePoint() == 1) {
            if (Utils.random(0.5)) {
                lookAtLeftOrRight();
            }
        }
        if (Utils.random(0.006)) {
            if (!ConfigManager.get().getBoolValue(Consts.PROPOSING_ACCEPTED) && personality.getBondPoint() >= 0.85) {
                proposeMenuItem.setDisable(false);
            }
        }
    }

    @Override
    public void release() {
        randomEventsScheduledFuture.cancel();
    }

    @Override
    public void takeMessage(String msg) {
        if (state != State.NORMAL) {
            if (!Global.debugFeatures() || !msg.startsWith("::")) {
                return;
            }
        }

        chatBot.takeMessage(msg);
    }

    @Override
    public boolean getDebugInfo(ClipboardContent content) {
        Logger.info("getting debug info");
        content.putString("Kokori: " +
            "state=" + state +
            ", bond=" + personality.getBondPoint() +
            ", desire=" + personality.getDesirePoint() +
            ", config=" + ConfigManager.get() +
            ", current=" + System.currentTimeMillis());
        return true;
    }

    @Override
    public void takeDebugMessage(Clipboard clipboard) {
        if (clipboard.hasString()) {
            String s = clipboard.getString();
            if (s != null && !s.isBlank()) {
                appCallback.showMessage(s);
            }
        }
    }

    private void menuAboutName() {
        appCallback.showMessage(KokoriWords.aboutName.select());
    }

    private void conversation() {
        if (preInteractionCheckFail()) {
            return;
        }
        appCallback.showMessage(KokoriWords.menuConversations().select());
    }

    private void menuThingsSheLikes() {
        if (preInteractionCheckFail()) {
            return;
        }
        if (personality.getBondPoint() < 0.7) {
            return;
        }
        if (personality.getBondPoint() < 0.8) {
            appCallback.showMessage(KokoriWords.thingsLikes1.select());
        } else if (personality.getBondPoint() < 0.85) {
            appCallback.showMessage(KokoriWords.thingsLikes2.select());
        } else {
            if (!ConfigManager.get().getBoolValue(Consts.PROPOSING_ACCEPTED)) {
                appCallback.showMessage(KokoriWords.thingsLikes3.select());
            } else {
                Utils.delay("things-likes-4", 20_000, this::resetAll);
                double r18probability = 0;
                if (Global.r18Features()) {
                    r18probability = 0.5;
                }
                if (!Utils.random(r18probability)) {
                    expressionYandere();
                    appCallback.showMessage(KokoriWords.thingsLikes4.select());
                } else {
                    eyeLeft.removeHighlight();
                    eyeRight.removeHighlight();
                    headJoin.tiltToLeft();
                    mouth.toHappy();
                    redCheek.show();
                    armRight.protectCrotch();
                    appCallback.showMessage(KokoriR18Words.thingsLikesR18.select());
                }
            }
        }
    }

    private void menuThingsSheHates() {
        var bond = personality.getBondPoint();
        if (bond < kokoriConsts.reallyBadMood) {
            appCallback.showMessage(KokoriWords.thingsHates1.select());
            return;
        }
        if (bond < kokoriConsts.badMood) {
            appCallback.showMessage(KokoriWords.thingsHates2.select());
            return;
        }
        if (bond < 0.7) {
            appCallback.showMessage(KokoriWords.thingsHates3.select());
            return;
        }
        if (!ConfigManager.get().getBoolValue(Consts.PROPOSING_ACCEPTED)) {
            appCallback.showMessage(KokoriWords.thingsHates4.select());
        } else {
            double r18probability = 0;
            if (Global.r18Features()) {
                r18probability = 0.5;
            }
            if (Utils.random(r18probability)) {
                appCallback.showMessage(KokoriWords.thingsHates5.select());
            } else {
                appCallback.showMessage(KokoriR18Words.thingsHatesR18.select());
            }
        }
    }

    private void menuBondStory(int lvl) {
        if (personality.getBondPoint() < KokoriPersonality.INITIAL_BOND_POINT) {
            return;
        }
        if (personality.getBondPoint() < bondStoriesRequiredBondPoints[lvl]) {
            return;
        }
        appCallback.showMessage(KokoriWords.bondStories[lvl].select());
    }

    private void menuExpression() {
        String manual = KokoriI18n.expressionManual.get()[0];
        if (Global.r18Features()) {
            manual += "\n" + KokoriR18I18n.r18ExpressionManual.get()[0];
        }
        Alert.alert(manual);
    }

    private void propose() {
        if (ConfigManager.get().getBoolValue(Consts.PROPOSING_ACCEPTED)) {
            return;
        }
        if (preInteractionCheckFail()) {
            return;
        }
        var bondPoint = personality.getBondPoint();
        if (bondPoint < 0.85) {
            return;
        }
        int cnt = ConfigManager.get().getIntValue(Consts.PROPOSING_COUNT);
        ConfigManager.get().setIntValue(Consts.PROPOSING_COUNT, cnt + 1);

        double probability = (bondPoint - 0.75) * cnt; // minimum 0.1 * 0
        if (Utils.random(probability)) {
            setProposeAccepted(true);
            redCheek.show();
            mouth.toHappy();
            Utils.delay("propose-accept", 10_000, () -> {
                resetCheek();
                resetMouth();
            });
            appCallback.showMessage(KokoriWords.acceptProposal.select());
        } else {
            proposeMenuItem.setDisable(true);
            redCheek.show();
            mouth.toSad();
            Utils.delay("propose-reject", 10_000, () -> {
                resetCheek();
                resetMouth();
            });
            appCallback.showMessage(KokoriWords.rejectProposal.select());
        }
    }

    public void setProposeAccepted(boolean accepted) {
        ConfigManager.get().setBoolValue(Consts.PROPOSING_ACCEPTED, accepted);
        if (accepted) {
            proposeMenuItem.setSelected(true);
            proposeMenuItem.setDisable(true);
        } else {
            proposeMenuItem.setSelected(false);
        }
    }

    public void setProposeMenuItemDisabled(boolean disabled) {
        if (!disabled) {
            if (ConfigManager.get().getBoolValue(Consts.PROPOSING_ACCEPTED)) {
                return;
            }
        }
        proposeMenuItem.setDisable(disabled);
    }
}
