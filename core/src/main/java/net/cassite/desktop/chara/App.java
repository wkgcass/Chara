// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.cassite.desktop.chara.chara.Chara;
import net.cassite.desktop.chara.control.NativeMouseListenerUtils;
import net.cassite.desktop.chara.graphic.*;
import net.cassite.desktop.chara.i18n.I18nConsts;
import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.util.Consts;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Utils;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;

public class App {
    private final StageTransformer primaryStage;
    private final Scene scene;
    private final Group root;
    private final Pane rootPane;
    private final Pane rootScalePane;
    private final Scale scale;
    private final InputBox inputBox;

    private final int MAX_WIDTH;
    private final int MAX_HEIGHT;

    private final Chara chara;
    private final ProgressBar bondBar;

    public App(Stage primaryStage, Scene scene, Pane rootPane, Pane rootScalePane, Scale scale) {
        assert Logger.debug("new App(...)");

        this.scene = scene;
        this.rootPane = rootPane;
        this.rootScalePane = rootScalePane;

        this.bondBar = new ProgressBar(0);
        this.bondBar.setLayoutX(Consts.BOND_BAR_MARGIN);
        this.bondBar.setLayoutY(Consts.BOND_BAR_MARGIN);
        this.bondBar.setPrefWidth(Consts.BOND_BAR_WIDTH);
        this.bondBar.setPrefHeight(Consts.BOND_BAR_HEIGHT);
        this.bondBar.setOpacity(0);
        this.rootScalePane.getChildren().add(bondBar);

        root = new Group();
        this.rootScalePane.getChildren().add(root);

        this.scale = scale;

        this.chara = Global.model.construct(getAppCallback(), root);

        this.primaryStage = new StageTransformer(primaryStage,
            chara.data().imageWidth, chara.data().imageHeight,
            chara.data().minX, chara.data().imageWidth - chara.data().maxX,
            chara.data().minY - (Consts.BOND_BAR_HEIGHT + Consts.BOND_BAR_MARGIN * 2),
            chara.data().imageHeight - chara.data().maxY
        );
        this.primaryStage.getStage().focusedProperty().addListener((observable, oldValue, newValue) -> {
            assert Logger.debug("primary stage focused: " + newValue);
            if (newValue != null) {
                StageUtils.primaryStageFocused = newValue;
            }
        });

        this.bondBar.setLayoutX(this.chara.data().topMiddleX - this.primaryStage.getCutLeft() - Consts.BOND_BAR_WIDTH / 2D);

        this.inputBox = new InputBox(rootPane, rootScalePane);
        this.inputBox.setPrefWidth(Consts.INPUT_WIDTH);
        this.inputBox.setPrefHeight(Consts.INPUT_HEIGHT);
        this.inputBox.setFont(new Font(Consts.INPUT_FONT_SIZE));
        this.inputBox.setOnKeyPressed(this::inputBoxKeyPressed);

        // register terminating hook
        primaryStage.setOnCloseRequest(e -> {
            Utils.cancelAllTimers();
            chara.release();
            ThreadUtils.get().shutdownNow();
            if (messageStage != null) {
                if (messageStage.isShowing()) {
                    messageStage.hide();
                }
            }
            Alert.shutdown();
            if (GlobalScreen.isNativeHookRegistered()) {
                try {
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException ignore) {
                }
            }
        });

        // calculate MAX_WIDTH and MAX_HEIGHT
        {
            double maxWidth = chara.data().imageWidth;
            double maxHeight = chara.data().imageHeight;

            var screens = Screen.getScreens();
            for (Screen s : screens) {
                var bounds = s.getBounds();
                if (bounds.getWidth() * 1.1 < maxWidth) {
                    maxWidth = bounds.getWidth() * 1.1;
                }
                if (bounds.getHeight() * 1.1 < maxHeight) {
                    maxHeight = bounds.getHeight() * 1.1;
                }
            }
            if (maxWidth / chara.data().imageWidth * chara.data().imageHeight < maxHeight) {
                this.MAX_WIDTH = (int) maxWidth;
                this.MAX_HEIGHT = (int) (maxWidth / chara.data().imageWidth * chara.data().imageHeight);
            } else {
                this.MAX_HEIGHT = (int) maxHeight;
                this.MAX_WIDTH = (int) (maxHeight / chara.data().imageHeight * chara.data().imageWidth);
            }
        }
    }

    public void init() {
        // load config
        double initialWidth = chara.data().initialWidth;
        double initialHeight = initialWidth / chara.data().imageWidth * chara.data().imageHeight;
        Double configRatio = ConfigManager.get().getCharacterRatio();
        if (configRatio != null) {
            initialWidth = (int) (chara.data().imageWidth * configRatio);
            initialHeight = (int) (chara.data().imageHeight * configRatio);
        }

        if (initialWidth > MAX_WIDTH) {
            initialWidth = MAX_WIDTH;
            initialHeight = MAX_HEIGHT;
        }

        // stage config
        primaryStage.getStage().initStyle(StageStyle.TRANSPARENT);
        primaryStage.getStage().setAlwaysOnTop(true);
        primaryStage.getStage().setResizable(false);
        primaryStage.scale(initialWidth / chara.data().imageWidth);
        Double configX = ConfigManager.get().getStageX();
        Double configY = ConfigManager.get().getStageY();
        if (configX != null && configY != null) {
            primaryStage.setAbsoluteX(configX);
            primaryStage.setAbsoluteY(configY);
        } else {
            primaryStage.centerOnScreen();
        }

        // pane config
        rootScalePane.setOnScroll(this::resize);
        var dragHandler = new DragWindowHandler();
        rootScalePane.setOnMousePressed(dragHandler);
        rootScalePane.setOnMouseDragged(dragHandler);
        rootScalePane.setOnMouseClicked(this::click);
        NativeMouseListenerUtils.setOnMouseMoved(this::mouseMove);

        // scene config
        scene.setOnKeyPressed(this::keyPressed);

        // group config
        root.setLayoutX(-primaryStage.getCutLeft());
        root.setLayoutY(-primaryStage.getCutTop());

        // scale config
        scale.setX(initialWidth / chara.data().imageWidth);
        scale.setY(initialHeight / chara.data().imageHeight);

        // input box
        calculateInputBoxPosition();

        // alert help message
        Alert.alert(I18nConsts.showMessageHelper.get()[0]);
    }

    public void ready() {
        chara.ready();
    }

    private AppCallback getAppCallback() {
        return new AppCallback() {
            @Override
            public void setBondPoint(double current, double previouse) {
                Platform.runLater(() -> App.this.setBondPoint(current, previouse));
            }

            @Override
            public void showMessage(Words words) {
                Platform.runLater(() -> App.this.showMessage(words));
            }

            @Override
            public void clickNothing(double x, double y) {
                Platform.runLater(() -> App.this.clickNothing(x, y));
            }
        };
    }

    private void resize(ScrollEvent e) {
        assert Logger.debug("resizing ...");

        double zoomFactor = 0.01;

        if (e.getDeltaY() < 0) {
            zoomFactor = -zoomFactor;
        }
        double oldRatio = primaryStage.getScaleRatio();
        double ratio = oldRatio + zoomFactor;

        if (chara.data().imageWidth * ratio > MAX_WIDTH) {
            ratio = ((double) MAX_WIDTH) / chara.data().imageWidth;
        }
        if (chara.data().imageWidth * ratio < chara.data().minWidth) {
            ratio = ((double) chara.data().minWidth) / chara.data().imageWidth;
        }

        if (Utils.doubleEquals(oldRatio, ratio, 0.0001)) {
            assert Logger.debug("size not changed");
            return;
        }

        scale.setX(ratio);
        scale.setY(ratio);
        double ex = e.getX();
        double ey = e.getY();
        ex += primaryStage.getCutLeft();
        ey += primaryStage.getCutTop();
        primaryStage.scaleAt(ex, ey, ratio);

        primaryStage.saveConfig();

        calculatePositions();
    }

    private class DragWindowHandler implements EventHandler<MouseEvent> {
        private double oldStageX;
        private double oldStageY;
        private double oldScreenX;
        private double oldScreenY;

        @Override
        public void handle(MouseEvent e) {
            if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
                pressed(e);
            } else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                dragged(e);
            }
        }

        private void pressed(MouseEvent e) {
            assert Logger.debug("mouse pressed ...");

            this.oldStageX = primaryStage.getAbsoluteX();
            this.oldStageY = primaryStage.getAbsoluteY();
            oldScreenX = e.getScreenX();
            oldScreenY = e.getScreenY();
        }

        private void dragged(MouseEvent e) {
            assert Logger.debug("mouse dragged ...");

            primaryStage.setAbsoluteX(e.getScreenX() - this.oldScreenX + this.oldStageX);
            primaryStage.setAbsoluteY(e.getScreenY() - this.oldScreenY + this.oldStageY);

            primaryStage.saveConfig();

            chara.dragged();

            calculatePositions();
        }
    }

    private void click(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        x += primaryStage.getCutLeft();
        y += primaryStage.getCutTop();
        assert Logger.debug("clicked at (" + x + "," + y + ") <= (" + e.getSceneX() + "," + e.getSceneY() + ")");

        chara.click(x, y);
    }

    private void mouseMove(NativeMouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        x -= primaryStage.getAbsoluteX();
        y -= primaryStage.getAbsoluteY();

        x /= primaryStage.getScaleRatio();
        y /= primaryStage.getScaleRatio();

        x += primaryStage.getCutLeft();
        y += primaryStage.getCutTop();
        chara.mouseMove(x, y);

        // show input box
        if (!messageDisabled) {
            // calculate whether to show input box
            double realY = (y - primaryStage.getCutTop()) * primaryStage.getScaleRatio();
            if (realY > inputBox.getLayoutY() - Consts.INPUT_SHOW_Y_DELTA) {
                inputBox.show();
            } else {
                inputBox.hide();
            }
        }

        // check mouse enter/leave
        double mx = e.getX();
        double my = e.getY();
        double px = primaryStage.getAbsoluteX();
        double py = primaryStage.getAbsoluteY();
        double w = primaryStage.getStage().getWidth();
        double h = primaryStage.getStage().getHeight();
        if (mx < px || mx > px + w || my < py || my > py + h) {
            // leaves
            if (!mouseLeaves) {
                mouseLeaves = true;
                mouseLeave(x, y);
            }
        } else {
            // enters
            mouseLeaves = false;
        }
    }

    private boolean mouseLeaves = true;

    private void mouseLeave(double x, double y) {
        assert Logger.debug("mouse leave at (" + x + "," + y + ")");
        chara.mouseLeave();

        // hide input box
        inputBox.hide();
    }

    private final TimeBasedAnimationHelper bondBarAnimationHelper = new TimeBasedAnimationHelper(
        250, this::animateBondBar);

    private double bondBarOpacityBegin = 0;
    private double bondBarOpacityTarget = 0;
    private double bondBarProgressBegin = 0;
    private double bondBarProgressTarget = 0;

    private void setBondPoint(double current, double previous) {
        if (current < 0) {
            current = 0;
        }
        if (current > 1) {
            current = 1;
        }
        if (previous < 0) {
            previous = 0;
        }
        if (previous > 1) {
            previous = 1;
        }
        if (current >= previous) {
            bondBar.setStyle("-fx-accent: #ff5263;");
        } else {
            bondBar.setStyle("-fx-accent: #4e4f5e;");
        }
        bondBarOpacityBegin = 0;
        bondBarOpacityTarget = 1;
        bondBarProgressBegin = previous;
        bondBarProgressTarget = previous;
        bondBarAnimationHelper.setDuration(250);

        final var previousFinal = previous;
        final var currentFinal = current;

        bondBarAnimationHelper
            .setFinishCallback(() -> {
                bondBarOpacityBegin = 1;
                bondBarOpacityTarget = 1;
                bondBarProgressBegin = previousFinal;
                bondBarProgressTarget = currentFinal;
                bondBarAnimationHelper.setDuration(1000);
                bondBarAnimationHelper.setFinishCallback(() -> {
                    bondBarOpacityBegin = 1;
                    bondBarOpacityTarget = 0;
                    bondBarProgressBegin = currentFinal;
                    bondBarProgressTarget = currentFinal;
                    bondBarAnimationHelper.setDuration(250);
                    bondBarAnimationHelper.setFinishCallback(null);
                    bondBarAnimationHelper.play();
                });
                bondBarAnimationHelper.play();
            });
        bondBarAnimationHelper.play();
    }

    private void animateBondBar(double percentage) {
        double opacity = (bondBarOpacityTarget - bondBarOpacityBegin) * percentage + bondBarOpacityBegin;
        bondBar.setOpacity(opacity);
        double progress = (bondBarProgressTarget - bondBarProgressBegin) * percentage + bondBarProgressBegin;
        bondBar.setProgress(progress);
    }

    private MessageStage messageStage = null;

    private void showMessage(Words words) {
        if (messageDisabled) {
            Logger.info("message is disabled");
            return;
        }

        if (messageStage == null) {
            messageStage = new MessageStage(primaryStage);
            messageStage.setAlwaysOnTop(primaryStage.getStage().isAlwaysOnTop());
            calculateMessageStagePosition();
        }
        int colorHash = (int) (Math.random() * 1000);
        int n = 0;
        for (String message : words.get()) {
            var msg = new InputMessage(message).setColor(colorHash);
            Utils.delayNoRecord(n * 1200, () -> messageStage.pushMessage(msg));
            ++n;
        }
    }

    private double getMiddleXOfScreen() {
        return primaryStage.getAbsoluteX() + (chara.data().topMiddleX - primaryStage.getCutLeft()) * primaryStage.getScaleRatio();
    }

    private void calculatePositions() {
        calculateInputBoxPosition();
        calculateMessageStagePosition();
    }

    private void calculateInputBoxPosition() {
        var middle = (chara.data().bottomMiddleX - primaryStage.getCutLeft()) * primaryStage.getScaleRatio();
        inputBox.setLayoutX(middle - Consts.INPUT_WIDTH / 2D);
        var stageH = primaryStage.getStage().getHeight();
        inputBox.setLayoutY(stageH - Consts.INPUT_MARGIN_BOTTOM - Consts.INPUT_HEIGHT);
    }

    private void calculateMessageStagePosition() {
        if (messageStage == null) {
            return;
        }

        var middleXOfScreen = getMiddleXOfScreen();
        var screen = primaryStage.getScreen();
        var screenBounds = screen.getBounds();
        var screenMid = screenBounds.getWidth() / 2;
        if (middleXOfScreen > screenMid) {
            messageStage.pointToRight();
        } else {
            messageStage.pointToLeft();
        }

        var offsetX = chara.data().messageOffsetX * primaryStage.getScaleRatio();
        if (messageStage.isPointingToRight()) {
            var maxX = middleXOfScreen - offsetX;
            messageStage.setX(maxX - Consts.MSG_STAGE_WIDTH);
        } else {
            messageStage.setX(middleXOfScreen + offsetX);
        }

        var y = primaryStage.getAbsoluteY() + ((chara.data().messageAtMinY - primaryStage.getCutTop()) * primaryStage.getScaleRatio());
        messageStage.setY(y);
    }

    private void clickNothing(double x, double y) {
        if (messageStage == null) {
            return;
        }
        if (!messageStage.isShowing()) {
            return;
        }

        x = primaryStage.calculateX(x);
        y = primaryStage.calculateY(y);
        x += primaryStage.getAbsoluteX();
        y += primaryStage.getAbsoluteY();
        messageStage.click(x, y);
    }

    private void inputBoxKeyPressed(KeyEvent e) {
        assert Logger.debug("input box key pressed");

        if (e.getCode() == KeyCode.ENTER) {
            String text = inputBox.getText();
            inputBox.clear();
            inputBox.requestFocus();
            if (text.isBlank()) {
                return;
            }
            inputBox.recordInputText(text);
            takeMessage(text);
        } else if (e.getCode() == KeyCode.UP) {
            inputBox.fillPreviousInputText();
        } else if (e.getCode() == KeyCode.DOWN) {
            inputBox.fillNextInputTextOrEmpty();
        }
    }

    private void takeMessage(String text) {
        // check special texts
        chara.takeMessage(text);
    }

    private void keyPressed(KeyEvent e) {
        assert Logger.debug("key pressed");
        if (e.isControlDown() && e.isShiftDown() && e.isMetaDown()) {

            if (e.getCode() == KeyCode.C) {
                messageDisableOrEnable();
            } else if (e.getCode() == KeyCode.T) {
                setOrUnsetAlwaysTop();
            } else if (e.getCode() == KeyCode.H) {
                showHelpMessage();
            }

        }
    }

    private void showHelpMessage() {
        Alert.alert(I18nConsts.showMessageHelper.get()[0]);
    }

    private void setOrUnsetAlwaysTop() {
        if (primaryStage.getStage().isAlwaysOnTop()) {
            assert Logger.debug("always on top disabled");
            primaryStage.getStage().setAlwaysOnTop(false);
            if (messageStage != null) {
                messageStage.setAlwaysOnTop(false);
            }
            Alert.alert(I18nConsts.alwaysOnTopDisabled.get()[0]);
        } else {
            assert Logger.debug("always on top enabled");
            primaryStage.getStage().setAlwaysOnTop(true);
            if (messageStage != null) {
                messageStage.setAlwaysOnTop(true);
            }
            Alert.alert(I18nConsts.alwaysOnTopEnabled.get()[0]);
        }
    }

    private boolean messageDisabled = false;

    private void messageDisableOrEnable() {
        if (messageDisabled) {
            assert Logger.debug("message enabled");
            messageDisabled = false;
            Alert.alert(I18nConsts.messageEnabled.get()[0]);
        } else {
            assert Logger.debug("message disable");
            messageDisabled = true;
            Alert.alert(I18nConsts.messageDisabled.get()[0]);
            inputBox.hide();
        }
    }
}
