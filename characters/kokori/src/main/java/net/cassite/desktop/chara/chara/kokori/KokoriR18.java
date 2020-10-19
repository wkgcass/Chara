// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.paint.Color;
import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.chara.Chara;
import net.cassite.desktop.chara.chara.kokori.i18n.KokoriR18I18n;
import net.cassite.desktop.chara.chara.kokori.personality.KokoriPersonality;
import net.cassite.desktop.chara.chara.kokori.personality.KokoriR18Words;
import net.cassite.desktop.chara.chara.kokori.util.Consts;
import net.cassite.desktop.chara.graphic.TimeBasedAnimationHelper;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.model.kokori.KokoriConsts;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.RateLimiter;
import net.cassite.desktop.chara.util.Utils;

public class KokoriR18 {
    private final Kokori kokori;
    private final KokoriConsts kokoriConsts;
    private final AppCallback appCallback;
    private final KokoriPersonality personality;

    private final MenuItem wantHMenuItem = new MenuItem(KokoriR18I18n.wantHMenuItem.get()[0]);
    private final MenuItem useLovePotionMenuItem = new MenuItem(KokoriR18I18n.useLovePotionMenuItem.get()[0]);
    private final ColorInput orgasmFlashColorInput = new ColorInput();
    private final Blend orgasmFlashEffect = new Blend();

    private boolean isAnimatingSex = false;
    private boolean needAnimateSexLater = false;
    private long sexStartTime = -1;

    private static final int orgasmFlashRed = 0xff;
    private static final int orgasmFlashGreen = 0xff;
    private static final int orgasmFlashBlue = 0xff;

    public KokoriR18(Kokori kokori) {
        this.kokori = kokori;
        this.kokoriConsts = kokori.kokoriConsts;
        this.appCallback = kokori.appCallback;
        this.personality = kokori.personality;

        orgasmFlashColorInput.setX(0);
        orgasmFlashColorInput.setY(0);
        orgasmFlashColorInput.setWidth(kokoriConsts.imageWidth);
        orgasmFlashColorInput.setHeight(kokoriConsts.imageHeight);
        orgasmFlashColorInput.setPaint(new Color(orgasmFlashRed / 255D, orgasmFlashGreen / 255D, orgasmFlashBlue / 255D, 1));
        orgasmFlashEffect.setTopInput(orgasmFlashColorInput);
        orgasmFlashEffect.setMode(BlendMode.SRC_ATOP);
    }

    public void initCharacterMenu(Menu characterMenu) {
        if (Global.r18features) {
            wantHMenuItem.setOnAction(e -> menuWantH());
            useLovePotionMenuItem.setOnAction(e -> menuUseLovePotion());
            characterMenu.getItems().addAll(wantHMenuItem);
        }
    }

    public void ready(@SuppressWarnings("unused") Chara.ReadyParams params) {
        int n = ConfigManager.get().getIntValue(Consts.LOVE_POTION_COUNT);
        if (n > 0) {
            useLovePotionMenuItem.setText(getLovePotionMenuItemText(n));
            if (!kokori.characterMenu.getItems().contains(useLovePotionMenuItem)) {
                kokori.characterMenu.getItems().add(useLovePotionMenuItem);
            }
        }
    }

    public void addLovePotion() {
        if (!Global.r18features) {
            return;
        }
        if (ConfigManager.get().getIntValue(Consts.LOVE_POTION_COUNT) >= Consts.MAX_LOVE_POTION_COUNT) {
            assert Logger.debug("too many love potion");
            return;
        }
        int n = ConfigManager.get().incIntValue(Consts.LOVE_POTION_COUNT, 1);
        if (n > 0) {
            useLovePotionMenuItem.setText(getLovePotionMenuItemText(n));
            if (!kokori.characterMenu.getItems().contains(useLovePotionMenuItem)) {
                kokori.characterMenu.getItems().add(useLovePotionMenuItem);
            }
        }
    }

    private String getLovePotionMenuItemText(int n) {
        if (n > 1) {
            return KokoriR18I18n.useLovePotionMenuItem.get()[0] + " * " + n;
        } else {
            return KokoriR18I18n.useLovePotionMenuItem.get()[0];
        }
    }

    public void startHavingSex() {
        if (kokori.state != Kokori.State.R18_SEX) {
            sexStartTime = System.currentTimeMillis();
        }
        kokori.state = Kokori.State.R18_SEX;
        appCallback.setGlobalScreen(false);
        appCallback.setDraggable(false);

        var added = animateSex(() -> {
            if (personality.getDesirePoint() < 1) {
                // not reached yet
                return;
            }

            var startTime = sexStartTime;
            var now = System.currentTimeMillis();
            var deltaTime = now - startTime;
            Logger.info("sex interaction took " + deltaTime + "ms");
            // record
            ConfigManager.get().incIntValue(Consts.SEX_TOTAL_TIME, (int) deltaTime);
            ConfigManager.get().incIntValue(Consts.SEX_COUNT, 1);

            kokori.state = Kokori.State.AFTER_SEX;
            sexStartTime = -1;
            appCallback.clearAllMessages();

            var lovePotionUsed = ConfigManager.get().getBoolValue(Consts.LOVE_POTION_USED);
            ConfigManager.get().setBoolValue(Consts.LOVE_POTION_USED, false);

            orgasmFlash(() -> {
                boolean badEnd = false;

                if (deltaTime < (65_000 + (lovePotionUsed ? 5_000 : 0))) {
                    appCallback.showMessage(KokoriR18Words.finishWantMore.select());
                    if (!lovePotionUsed) {
                        personality.incPoints(0.1, 0);
                    }
                    // record
                    ConfigManager.get().incIntValue(Consts.ORGASM_COUNT, 1);
                } else {
                    if (lovePotionUsed && personality.getBondPoint() < 0.85) {
                        appCallback.showMessage(KokoriR18Words.finishBadWithLovePotion.select());
                        personality.incPoints(-0.3, -1);
                        badEnd = true;
                        // record
                        ConfigManager.get().incIntValue(Consts.BAD_SEX_COUNT, 1);
                    } else {
                        appCallback.showMessage(KokoriR18Words.finish.select());
                        // reset desire point
                        personality.setDesirePoint(0);
                        // record
                        ConfigManager.get().incIntValue(Consts.NORMAL_SEX_COUNT, 1);
                    }
                }

                // do reset
                kokori.eyeLeft.zoom(1);
                kokori.eyeLeft.move(kokoriConsts.eyeLeftOriginalX, kokoriConsts.eyeLeftOriginalY);
                kokori.eyeRight.zoom(1);
                kokori.eyeRight.move(kokoriConsts.eyeRightOriginalX, kokoriConsts.eyeRightOriginalY);
                kokori.redCheek.show();
                if (badEnd) {
                    kokori.eyeLeft.removeHighlight();
                    kokori.eyeRight.removeHighlight();
                    kokori.legLeft.tighten(() -> {
                    });
                    kokori.armRight.tighten();
                    kokori.mouth.toSad();
                } else {
                    kokori.resetHighlight();
                    kokori.legLeft.loose();
                    kokori.armRight.moveToDefaultPosition();
                    kokori.mouth.toHappy();
                }

                // resume
                Utils.delay("finish-sex", 5_000, () -> {
                    appCallback.setGlobalScreen(true);
                    appCallback.setDraggable(true);

                    // reset again
                    kokori.legLeft.loose();
                    kokori.armRight.moveToDefaultPosition();
                    kokori.resetHighlight();
                    kokori.resetCheek();
                    kokori.resetMouth();
                    kokori.resetCharaPointsRelated();
                    kokori.state = Kokori.State.NORMAL;
                });
            });
        });
        if (added) {
            assert Logger.debug("start animating sex");
        } else {
            assert Logger.debug("already animating sex");
        }
    }

    private void orgasmFlash(Runnable cb) {
        // orgasm flash: simulate some porn games
        // wait --> white-shortly --> wait-1s --> white-shortly*2 --> wait-1s --> white-long --> end
        // 1
        Utils.delayNoRecord(500, () -> { // wait
            kokori.root.setEffect(orgasmFlashEffect);
            // 2
            Utils.delayNoRecord(200, () -> { // showing shortly
                kokori.root.setEffect(null);
                // 3
                Utils.delayNoRecord(800, () -> { // wait
                    kokori.root.setEffect(orgasmFlashEffect);
                    // 4
                    Utils.delayNoRecord(100, () -> { // showing shortly 1st
                        kokori.root.setEffect(null);
                        // 5
                        Utils.delayNoRecord(100, () -> { // short delay
                            kokori.root.setEffect(orgasmFlashEffect);
                            // 6
                            Utils.delayNoRecord(100, () -> { // showing shortly 2st
                                kokori.root.setEffect(null);
                                // 7
                                Utils.delayNoRecord(800, () -> { // wait
                                    kokori.root.setEffect(orgasmFlashEffect);
                                    // 8
                                    Utils.delayNoRecord(500, () ->
                                        // 9
                                        new TimeBasedAnimationHelper(2000, p ->
                                            orgasmFlashColorInput.setPaint(
                                                new Color(orgasmFlashRed / 255D, orgasmFlashGreen / 255D, orgasmFlashBlue / 255D, 1 - p * p * p)
                                            )
                                        ).setFinishCallback(() -> {
                                            kokori.root.setEffect(null);
                                            orgasmFlashColorInput.setPaint(
                                                new Color(orgasmFlashRed / 255D, orgasmFlashGreen / 255D, orgasmFlashBlue / 255D, 1)
                                            );
                                            cb.run();
                                        }).play() // -9
                                    ); // -8
                                }); // -7
                            }); // -6
                        }); // -5
                    }); // -4
                }); // -3
            }); // -2
        }); // -1
    }

    public boolean animateSex(Runnable cb) {
        if (isAnimatingSex) {
            needAnimateSexLater = true;
            return false;
        }
        isAnimatingSex = true;
        doAnimateSex(cb);
        return true;
    }

    private void doAnimateSex(Runnable cb) {
        kokori.redCheek.show();
        var p = personality.getDesirePoint();
        // high light
        if (p < 0.1) {
            kokori.eyeLeft.addHighlight();
            kokori.eyeRight.addHighlight();
        } else {
            kokori.eyeLeft.removeHighlight();
            kokori.eyeRight.removeHighlight();
        }
        // eye position
        if ((0.3 < p && p < 0.5) || (0.8 < p && p < 0.9)) {
            kokori.eyeLeft.close();
            kokori.eyeRight.close();
        } else if (p > 0.5) {
            kokori.eyeLeft.open();
            kokori.eyeRight.open();
            kokori.eyeLeft.move(kokoriConsts.eyeLeftOriginalX, kokoriConsts.eyeLeftYMin);
            kokori.eyeRight.move(kokoriConsts.eyeRightOriginalX, kokoriConsts.eyeRightYMin);
        } else {
            kokori.eyeLeft.open();
            kokori.eyeRight.open();
            kokori.eyeLeft.move(kokoriConsts.eyeLeftOriginalX, kokoriConsts.eyeLeftOriginalY);
            kokori.eyeRight.move(kokoriConsts.eyeRightOriginalX, kokoriConsts.eyeRightOriginalY);
        }
        // eye zoom
        if (p < 0.6) {
            kokori.eyeLeft.zoom(1);
            kokori.eyeRight.zoom(1);
        } else {
            kokori.eyeLeft.zoom(0.85);
            kokori.eyeRight.zoom(0.85);
        }
        // mouth
        if (p < 0.1) {
            kokori.mouth.toSad();
        } else if (p < 0.4) {
            kokori.mouth.startAnimatingOpenAndShut();
        } else {
            kokori.mouth.stopAnimatingOpenAndShut();
            kokori.mouth.toOpen();
        }
        // arm right
        if (p < 0.2 || (0.9 < p && p < 0.95)) {
            kokori.armRight.protectCrotch();
        } else {
            kokori.armRight.tighten();
        }
        // leg left
        if (p < 0.7) {
            kokori.legLeft.tighten(() -> {
            });
        } else {
            kokori.legLeft.loose();
        }

        final double moveMax = kokoriConsts.sexMoveMax;
        double[] alreadyMoved = {0};
        final int durationMax = 800;
        final int durationMin = 400;
        int duration = (int) (durationMax - (durationMax - durationMin) * personality.getDesirePoint());
        new TimeBasedAnimationHelper(duration / 2, percentage -> {
            double targetPosition = moveMax * percentage;
            double shouldMove = targetPosition - alreadyMoved[0];
            alreadyMoved[0] = targetPosition;
            appCallback.moveWindow(0, -shouldMove);
        }).setFinishCallback(() ->
            new TimeBasedAnimationHelper(duration / 2, percentage -> {
                double targetPosition = moveMax * (1 - percentage);
                double shouldMove = alreadyMoved[0] - targetPosition;
                alreadyMoved[0] = targetPosition;
                appCallback.moveWindow(0, shouldMove);
            }).setFinishCallback(() -> {
                if (needAnimateSexLater) {
                    needAnimateSexLater = false;
                    doAnimateSex(cb);
                } else {
                    isAnimatingSex = false;
                    cb.run();
                }
            }).play()
        ).play();
    }

    private final RateLimiter stateSexIncDesirePointRateLimiter = new RateLimiter(
        100, 2
    );

    public void clickStateSex(double x, double y) {
        double p = personality.getDesirePoint();
        if (p >= 1) {
            return;
        }
        if (!kokoriConsts.clickCrotchRec.contains(x, y)) {
            return;
        }
        if (stateSexIncDesirePointRateLimiter.request()) {
            final double incMax = 0.005;
            final double incMin = 0.001;
            double inc = incMax - (incMax - incMin) * personality.getDesirePoint();
            personality.incPoints(0, inc);
        }
        double screamProbability;
        if (personality.getDesirePoint() < 0.3) {
            screamProbability = 0.3;
        } else if (personality.getDesirePoint() < 0.5) {
            screamProbability = 0.5;
        } else if (personality.getDesirePoint() < 0.7) {
            screamProbability = 0.7;
        } else {
            screamProbability = 1;
        }
        if (Utils.random(screamProbability)) {
            sexScream();
        }
        startHavingSex();

        // animate
        kokori.hair.swing();
        kokori.hairSide.swing();
        kokori.hairBack.swing();
        kokori.dressFront.flutter();
        kokori.dressBack.flutter();
        kokori.quiver.shake();
        kokori.armRight.hideRune();
        kokori.armRight.hideArrow();
        kokori.armLeft.hideBow();
    }

    private void sexScream() {
        if (personality.getDesirePoint() > 0.95) {
            appCallback.showMessage(KokoriR18Words.scream4.select());
        } else if (personality.getDesirePoint() > 0.9) {
            if (Utils.random(0.1)) {
                appCallback.showMessage(KokoriR18Words.scream1.select());
            } else if (Utils.random(0.15)) {
                appCallback.showMessage(KokoriR18Words.scream2.select());
            } else if (Utils.random(0.2)) {
                appCallback.showMessage(KokoriR18Words.scream3.select());
            } else {
                appCallback.showMessage(KokoriR18Words.scream4.select());
            }
        } else if (personality.getDesirePoint() > 0.6) {
            if (Utils.random(0.2)) {
                appCallback.showMessage(KokoriR18Words.scream1.select());
            } else if (Utils.random(0.3)) {
                appCallback.showMessage(KokoriR18Words.scream2.select());
            } else {
                appCallback.showMessage(KokoriR18Words.scream3.select());
            }
        } else if (personality.getDesirePoint() > 0.3) {
            if (Utils.random(0.4)) {
                appCallback.showMessage(KokoriR18Words.scream1.select());
            } else {
                appCallback.showMessage(KokoriR18Words.scream2.select());
            }
        } else {
            appCallback.showMessage(KokoriR18Words.scream1.select());
        }
    }

    public void menuWantH() {
        if (!Global.r18features) {
            return;
        }
        if (kokori.state != Kokori.State.NORMAL) {
            return;
        }

        if (kokori.preInteractionCheckFail()) {
            return;
        }

        var desire = personality.getDesirePoint();
        if (desire != 1) {
            var bond = personality.getBondPoint();
            if (bond < 0.3) {
                personality.incPoints(-0.05, -0.05);
                appCallback.showMessage(KokoriR18Words.reject1.select());
                return;
            }
            if (bond < 0.6) {
                personality.incPoints(-0.01, -0.01);
                appCallback.showMessage(KokoriR18Words.reject2.select());
                return;
            }
            if (bond < 0.85) {
                personality.incPoints(0.0001, 0.005);
                appCallback.showMessage(KokoriR18Words.reject3.select());
                return;
            }

            if (desire < 0.8) {
                personality.incPoints(0.0005, 0.01);
                appCallback.showMessage(KokoriR18Words.reject4.select());
                Utils.shortDelay(kokori.redCheek::show);
                Utils.delay("menu-want-h-dont-want-now", 1500, kokori::resetCheek);
                return;
            }
        }
        if (desire == 1) {
            appCallback.showMessage(KokoriR18Words.cannotRestrain.select());
        } else {
            appCallback.showMessage(KokoriR18Words.wantSex.select());
        }
        personality.setDesirePoint(0);
        Utils.shortDelay(kokori.redCheek::show);
        Utils.delay("menu-want-h", 1500, this::startHavingSex);
    }

    public void menuUseLovePotion() {
        if (!Global.r18features) {
            return;
        }
        if (kokori.state != Kokori.State.NORMAL) {
            return;
        }
        int lovePotionCountAfterUsing = ConfigManager.get().incIntValue(Consts.LOVE_POTION_COUNT, -1);
        if (lovePotionCountAfterUsing < 0) {
            ConfigManager.get().setIntValue(Consts.LOVE_POTION_COUNT, 0);
        }
        if (lovePotionCountAfterUsing <= 0) {
            kokori.characterMenu.getItems().remove(useLovePotionMenuItem);
        }
        if (lovePotionCountAfterUsing < 0) {
            return; // should not be used
        }

        useLovePotionMenuItem.setText(getLovePotionMenuItemText(lovePotionCountAfterUsing));

        // whether this can succeed
        final double minBond = 0.3; // < 0.3 will fail
        final double maxBond = 0.75; // > 0.75 will succeed
        double bond = personality.getBondPoint();
        double percentage;
        if (bond < minBond) {
            percentage = 0;
        } else if (bond > maxBond) {
            percentage = 1;
        } else {
            percentage = (bond - minBond) / (maxBond - minBond);
        }

        if (Utils.random(percentage)) {
            ConfigManager.get().setBoolValue(Consts.LOVE_POTION_USED, true);
            appCallback.showMessage(KokoriR18Words.succeededUsingLovePotion.select());
            personality.setDesirePoint(1);
        } else {
            appCallback.showMessage(KokoriR18Words.failedUsingLovePotion.select());
            personality.incPoints(-(maxBond - kokoriConsts.reallyReallyBadMood), -1);
        }
    }
}
