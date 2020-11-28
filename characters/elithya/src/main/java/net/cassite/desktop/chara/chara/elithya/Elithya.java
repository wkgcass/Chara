// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya;

import javafx.scene.Group;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.chara.Chara;
import net.cassite.desktop.chara.chara.elithya.chat.ElithyaChatbot;
import net.cassite.desktop.chara.chara.elithya.join.*;
import net.cassite.desktop.chara.chara.elithya.parts.*;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaConsts;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaI18nConsts;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaSavingConsts;
import net.cassite.desktop.chara.graphic.TimeBasedAnimationHelper;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Scheduled;
import net.cassite.desktop.chara.util.Utils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Elithya implements Chara {
    private final AppCallback appCallback;
    private final Menu menu;
    private final ElithyaConsts elithyaConsts;

    private final Group elithya;
    private final Data data;

    private final ElithyaChatbot chatbot;

    public final HatJoin hat;
    public final EyebrowLeft eyebrowLeft;
    public final EyebrowRight eyebrowRight;
    public final HairJoin hair;
    public final Nose nose;
    public final Mouth mouth;
    public final EyeJoin eyes;
    public final EyeWhiteLeft eyeWhiteLeft;
    public final EyeWhiteRight eyeWhiteRight;
    public final CloakJoin cloak;
    public final ArmLeft armLeft;
    public final Cloth cloth;
    public final ArmPitLeft armPitLeft;
    public final Neck neck;
    public final SkirtJoin skirt;
    public final LegJoin legs;
    public final ClothBack clothBack;
    public final ArmRightJoin armRight;
    public final Broom broom;
    public final LanternJoin lantern;
    public final Moon moon;

    private final Scheduled randomEventsScheduled;

    public Elithya(AppCallback appCallback, Group parent, Menu menu, ElithyaConsts elithyaConsts) {
        this.appCallback = appCallback;
        this.menu = menu;
        this.elithyaConsts = elithyaConsts;

        chatbot = new ElithyaChatbot(this, appCallback);

        moon = new Moon(parent);

        elithya = new Group();
        parent.getChildren().add(elithya);

        ChainBack chainBack = new ChainBack(elithya, elithyaConsts);
        Lantern lantern = new Lantern(elithya, elithyaConsts);
        broom = new Broom(elithya);
        ChainFront chainFront = new ChainFront(elithya, elithyaConsts);
        HatBack hatBack = new HatBack(elithya);
        HairBack hairBack = new HairBack(elithya);
        CloakBack cloakBack = new CloakBack(elithya);
        ArmUpperRight armUpperRight = new ArmUpperRight(elithya);
        CloakMid cloakMid = new CloakMid(elithya);
        ArmForeRight armForeRight = new ArmForeRight(elithya);
        HandRight handRight = new HandRight(elithya);
        WandLight wandLight = new WandLight(elithya);
        clothBack = new ClothBack(elithya);
        SkirtBack skirtBack = new SkirtBack(elithya);
        LegRight legRight = new LegRight(elithya);
        LegLeft legLeft = new LegLeft(elithya);
        SkirtFront skirtFront = new SkirtFront(elithya);
        neck = new Neck(elithya);
        armPitLeft = new ArmPitLeft(elithya);
        cloth = new Cloth(elithya);
        HairSideRight hairSideRight = new HairSideRight(elithya);
        armLeft = new ArmLeft(elithya);
        CloakFront cloakFront = new CloakFront(elithya);
        eyeWhiteRight = new EyeWhiteRight(elithya);
        eyeWhiteLeft = new EyeWhiteLeft(elithya);
        EyeRight eyeRight = new EyeRight(elithya, elithyaConsts);
        EyeLeft eyeLeft = new EyeLeft(elithya, elithyaConsts);
        HeadWithEyeBlink headWithEyeBlink = new HeadWithEyeBlink(elithya);
        mouth = new Mouth(elithya);
        nose = new Nose(elithya);
        HairSideLeft hairSideLeft = new HairSideLeft(elithya);
        HairFront hairFront = new HairFront(elithya);
        HairDumb hairDumb = new HairDumb(elithya);
        eyebrowRight = new EyebrowRight(elithya);
        eyebrowLeft = new EyebrowLeft(elithya);
        HatFront hatFront = new HatFront(elithya);

        hat = new HatJoin(hatFront, hatBack, elithyaConsts);
        hair = new HairJoin(hairDumb, hairFront, hairSideLeft, hairSideRight, hairBack);
        eyes = new EyeJoin(headWithEyeBlink, eyeLeft, eyeRight, elithyaConsts);
        cloak = new CloakJoin(cloakFront, cloakMid, cloakBack);
        skirt = new SkirtJoin(skirtFront, skirtBack);
        legs = new LegJoin(legLeft, legRight);
        armRight = new ArmRightJoin(wandLight, handRight, armForeRight, armUpperRight, elithyaConsts);
        this.lantern = new LanternJoin(chainFront, lantern, chainBack);

        data = new DataBuilder()
            .setImageWidth(elithyaConsts.imageWidth)
            .setImageHeight(elithyaConsts.imageHeight)
            .setMinWidth(elithyaConsts.minWidth)
            .setInitialWidth(elithyaConsts.initialWidth)
            .setMinX(0)
            .setMaxX(elithyaConsts.imageWidth)
            .setTopMiddleX(elithyaConsts.topMiddleX)
            .setBottomMiddleX(elithyaConsts.bottomMiddleX)
            .setMessageOffsetX(elithyaConsts.messageOffsetX)
            .setMessageAtMinY(elithyaConsts.messageAtMinY)
            .setMinY(0)
            .setMaxY(elithyaConsts.imageHeight)
            .build();

        randomEventsScheduled = ThreadUtils.get().scheduleAtFixedRateFX(this::randomEvents, 1_000, 3_000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void ready(ReadyParams readyParams) {
        appCallback.setAlwaysHideBar(true); // no points for this character
        double[] stageXY = appCallback.getWindowPosition();
        stageX = stageXY[0];
        stageY = stageXY[1];
        {
            if (ConfigManager.get().getBoolValue(ElithyaSavingConsts.showWandLight)) {
                armRight.showWandLight();
            }
        }
        {
            CheckMenuItem showHatItem = new CheckMenuItem(ElithyaI18nConsts.showHatMenuItem.get()[0]);
            if (ConfigManager.get().getBoolValue(ElithyaSavingConsts.hideHat)) {
                showHatItem.setSelected(false);
                hat.hide();
            } else {
                showHatItem.setSelected(true);
                hat.show();
            }
            showHatItem.setOnAction(e -> {
                boolean b = hat.isShown();
                ConfigManager.get().setBoolValue(ElithyaSavingConsts.hideHat, b);
                b = !b;
                showHatItem.setSelected(b);
                if (b) {
                    hat.show();
                } else {
                    hat.hide();
                }
            });
            menu.getItems().add(showHatItem);
        }
        {
            CheckMenuItem showCloakItem = new CheckMenuItem(ElithyaI18nConsts.showCloakMenuItem.get()[0]);
            if (ConfigManager.get().getBoolValue(ElithyaSavingConsts.hideCloak)) {
                showCloakItem.setSelected(false);
                cloak.hide();
            } else {
                showCloakItem.setSelected(true);
                cloak.show();
            }
            showCloakItem.setOnAction(e -> {
                boolean b = cloak.isShown();
                ConfigManager.get().setBoolValue(ElithyaSavingConsts.hideCloak, b);
                b = !b;
                showCloakItem.setSelected(b);
                if (b) {
                    cloak.show();
                } else {
                    cloak.hide();
                }
            });
            menu.getItems().add(showCloakItem);
        }
        {
            MenuItem stockingsMenu = new MenuItem(ElithyaI18nConsts.switchStockings.get()[0]);
            var circle = new Circle();
            circle.setRadius(5);
            var purple = new Color(0x9e / 255d, 0x6e / 255d, 0xa8 / 255d, 1);
            var black = new Color(0x45 / 255d, 0x45 / 255d, 0x45 / 255d, 1);
            var none = new Color(0xff / 255d, 0xc2 / 255d, 0xa4 / 255d, 1);
            stockingsMenu.setGraphic(circle);
            int stockingsColor = ConfigManager.get().getIntValue(ElithyaSavingConsts.stockingsColor);
            if (stockingsColor == 0 || stockingsColor == 1) {
                circle.setFill(purple);
                legs.showPurpleStockings();
            } else if (stockingsColor == 2) {
                circle.setFill(black);
                legs.showBlackStockings();
            } else {
                circle.setFill(none);
                legs.showNoneStockings();
            }
            stockingsMenu.setOnAction(e -> {
                int state = legs.getStockingsState();
                if (state == 0 || state == 1) {
                    ConfigManager.get().setIntValue(ElithyaSavingConsts.stockingsColor, 2);
                    circle.setFill(black);
                    legs.showBlackStockings();
                } else if (state == 2) {
                    ConfigManager.get().setIntValue(ElithyaSavingConsts.stockingsColor, 3);
                    circle.setFill(none);
                    legs.showNoneStockings();
                } else {
                    ConfigManager.get().setIntValue(ElithyaSavingConsts.stockingsColor, 1);
                    circle.setFill(purple);
                    legs.showPurpleStockings();
                }
            });
            menu.getItems().add(stockingsMenu);
        }
        {
            MenuItem shoesMenu = new MenuItem(ElithyaI18nConsts.switchShoes.get()[0]);
            var circle = new Circle();
            circle.setRadius(5);
            var purple = new Color(0x9e / 255d, 0x6e / 255d, 0xa8 / 255d, 1);
            var black = new Color(0x45 / 255d, 0x45 / 255d, 0x45 / 255d, 1);
            shoesMenu.setGraphic(circle);
            int shoesColor = ConfigManager.get().getIntValue(ElithyaSavingConsts.shoesColor);
            if (shoesColor == 0 || shoesColor == 1) {
                circle.setFill(purple);
                legs.showPurpleShoes();
            } else {
                circle.setFill(black);
                legs.showBlackShoes();
            }
            shoesMenu.setOnAction(e -> {
                int state = legs.getShoesState();
                if (state == 0 || state == 1) {
                    ConfigManager.get().setIntValue(ElithyaSavingConsts.shoesColor, 2);
                    circle.setFill(black);
                    legs.showBlackShoes();
                } else {
                    ConfigManager.get().setIntValue(ElithyaSavingConsts.shoesColor, 1);
                    circle.setFill(purple);
                    legs.showPurpleShoes();
                }
            });
            menu.getItems().add(shoesMenu);
        }
        {
            CheckMenuItem showMoonItem = new CheckMenuItem(ElithyaI18nConsts.showMoonMenuItem.get()[0]);
            if (ConfigManager.get().getBoolValue(ElithyaSavingConsts.hideMoon)) {
                showMoonItem.setSelected(false);
                moon.hide();
            } else {
                showMoonItem.setSelected(true);
                moon.show();
            }
            showMoonItem.setOnAction(e -> {
                boolean b = moon.isShown();
                ConfigManager.get().setBoolValue(ElithyaSavingConsts.hideMoon, b);
                b = !b;
                showMoonItem.setSelected(b);
                if (b) {
                    moon.show();
                } else {
                    moon.hide();
                }
            });
            menu.getItems().add(showMoonItem);
        }
        animateAll();
    }

    private void randomEvents() {
        if (Utils.random(0.8)) {
            eyes.blink();
            if (Utils.random(0.4)) {
                Utils.delay("double-blink", 250, eyes::blink);
            }
        }
    }

    private void animateAll() {
        animateHat();
        animateHair();
        animateCloak();
        animateSkirt();
        animatePosition();
    }

    private void animateHat() {
        hat.play();
        hat.animateHatPosition();
    }

    private void animateHair() {
        hair.play();
    }

    private void animateCloak() {
        cloak.play();
    }

    private void animateSkirt() {
        skirt.play();
    }

    private boolean mouseIn;
    private double mousePositionX;
    private double mousePositionY;

    @Override
    public void mouseMove(double x, double y) {
        mouseIn = true;
        mousePositionX = x;
        mousePositionY = y;
        eyes.track(x - elithya.getLayoutX(), y - elithya.getLayoutY());
        armRight.track(x - elithya.getLayoutX(), y - elithya.getLayoutY());
    }

    @Override
    public void mouseLeave() {
        mouseIn = false;
        eyes.reset();
        armRight.reset();
    }

    private double positionOffsetBeginX = 0;
    private double positionOffsetBeginY = 0;
    private Boolean positionIsMovingLeft;
    private Boolean positionIsMovingTop;
    private double positionOffsetEndX;
    private double positionOffsetEndY;
    private final TimeBasedAnimationHelper positionAnimation = new TimeBasedAnimationHelper(
        2000, this::updatePosition
    );

    private void animatePosition() {
        positionAnimation.setFinishCallback(() -> {
            positionOffsetBeginX = positionOffsetEndX;
            positionOffsetBeginY = positionOffsetEndY;
            animatePosition0();
        });
        animatePosition0();
    }

    @SuppressWarnings("DuplicatedCode")
    private void animatePosition0() {
        int minX = -elithyaConsts.moveLeftMax;
        int maxX = elithyaConsts.moveRightMax;
        int minY = -elithyaConsts.moveUpMax;
        int maxY = elithyaConsts.moveDownMax;

        boolean regardlessOfLeftRightDirection = false;
        boolean regardlessOfTopBottomDirection = false;
        if (Utils.random(0.5)) {
            if (Utils.random(0.75)) {
                regardlessOfLeftRightDirection = true;
            } else {
                regardlessOfTopBottomDirection = true;
            }
        }
        if (positionIsMovingLeft == null || regardlessOfLeftRightDirection) {
            positionOffsetEndX = ThreadLocalRandom.current().nextInt(maxX - minX + 1) + minX;
            positionIsMovingLeft = positionOffsetEndX < positionOffsetBeginX;
        } else if (positionIsMovingLeft) {
            positionOffsetEndX = ThreadLocalRandom.current().nextInt((int) (maxX - positionOffsetBeginX + 1)) + positionOffsetBeginX;
            positionIsMovingLeft = false;
        } else {
            positionOffsetEndX = ThreadLocalRandom.current().nextInt((int) (positionOffsetBeginX - minX + 1)) + minX;
            positionIsMovingLeft = true;
        }
        if (positionIsMovingTop == null || regardlessOfTopBottomDirection) {
            positionOffsetEndY = ThreadLocalRandom.current().nextInt(maxY - minY + 1) + minY;
            positionIsMovingTop = positionOffsetEndY < positionOffsetBeginY;
        } else if (positionIsMovingTop) {
            positionOffsetEndY = ThreadLocalRandom.current().nextInt((int) (maxY - positionOffsetBeginY + 1)) + positionOffsetBeginY;
            positionIsMovingTop = false;
        } else {
            positionOffsetEndY = ThreadLocalRandom.current().nextInt((int) (positionOffsetBeginY - minY + 1)) + minY;
            positionIsMovingTop = true;
        }

        if (Math.pow(positionOffsetEndX - positionOffsetBeginX, 2)
            + Math.pow(positionOffsetEndY - positionOffsetBeginY, 2)
            > Math.pow(elithyaConsts.lanternSwingDelta, 2)
            && (Math.abs(positionOffsetEndX - positionOffsetBeginX)
            > elithyaConsts.lanternSwingDeltaX)
            || (Math.abs(positionOffsetEndY - positionOffsetBeginY)
            > elithyaConsts.lanternSwingDeltaY)) {

            if (positionIsMovingLeft) {
                lantern.swingRight();
            } else {
                lantern.swingLeft();
            }

            animateLegs();
        }

        positionAnimation.play();
    }

    private void updatePosition(double percentage) {
        double deltax = positionOffsetEndX - positionOffsetBeginX;
        double deltay = positionOffsetEndY - positionOffsetBeginY;
        double rate;
        if (percentage < 0.5) {
            rate = 2 * Math.pow(percentage, 2); // 2x^2
        } else {
            rate = -2 * Math.pow(percentage - 1, 2) + 1; // -2(x-1)^2+1
        }
        deltax *= rate;
        deltay *= rate;
        elithya.setLayoutX(deltax + positionOffsetBeginX);
        elithya.setLayoutY(deltay + positionOffsetBeginY);

        if (mouseIn) {
            eyes.track(mousePositionX - elithya.getLayoutX(), mousePositionY - elithya.getLayoutY());
            armRight.track(mousePositionX - elithya.getLayoutX(), mousePositionY - elithya.getLayoutY());
        }
    }

    private double stageX;
    private double stageY;

    @Override
    public void dragged() {
        animateLegs();
        double[] stageXY = appCallback.getWindowPosition();
        double stageX = stageXY[0];
        double stageY = stageXY[1];
        Utils.delay("dragged", 50, () -> {
            boolean stageMoveLeft = stageX < this.stageX;
            boolean stageMoveUp = stageY < this.stageY;
            this.stageX = stageX;
            this.stageY = stageY;

            if (stageMoveUp) {
                assert Logger.debug("stage moves up");
                cloth.playInitialDown();
            } else {
                assert Logger.debug("stage moves down");
                cloth.playInitialUp();
            }
            if (stageMoveLeft) {
                assert Logger.debug("stage moves left");
                lantern.swingRight();
            } else {
                assert Logger.debug("stage move right");
                lantern.swingLeft();
            }
        });
    }

    private void animateLegs() {
        animateLegLeftCount = 0;
        animateLegLeft();
        animateLegRightCount = 0;
        animateLegRight();
    }

    private int animateLegLeftCount = 0;

    private void animateLegLeft() {
        legs.playLeft(() -> {
            if (++animateLegLeftCount > elithyaConsts.animateLegMaxCount) {
                legs.stopLeft();
            } else {
                animateLegLeft();
            }
        });
    }

    private int animateLegRightCount = 0;

    private void animateLegRight() {
        legs.playRight(() -> {
            if (++animateLegRightCount > elithyaConsts.animateLegMaxCount) {
                legs.stopRight();
            } else {
                animateLegRight();
            }
        });
    }

    @Override
    public void click(double x, double y) {
        if (armRight.wandLightIsShown()) {
            ConfigManager.get().setBoolValue(ElithyaSavingConsts.showWandLight, false);
            armRight.hideWandLight();
        } else {
            ConfigManager.get().setBoolValue(ElithyaSavingConsts.showWandLight, true);
            armRight.showWandLight();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // ignore
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ignore
    }

    @Override
    public Data data() {
        return data;
    }

    @Override
    public int shutdown(Runnable runnable) {
        return 0;
    }

    @Override
    public void release() {
        randomEventsScheduled.cancel();
    }

    @Override
    public void takeMessage(String s) {
        chatbot.takeMessage(s);
    }

    @Override
    public boolean getDebugInfo(ClipboardContent clipboardContent) {
        clipboardContent.putString("elithya");
        return true;
    }

    @Override
    public void takeDebugMessage(Clipboard clipboard) {
        String msg = clipboard.getString();
        if (msg == null || msg.isBlank()) {
            return;
        }
        appCallback.showMessage(msg);
    }
}
