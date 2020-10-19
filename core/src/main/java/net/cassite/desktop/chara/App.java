// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
import net.cassite.desktop.chara.manager.FontManager;
import net.cassite.desktop.chara.model.Model;
import net.cassite.desktop.chara.util.Consts;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Scheduled;
import net.cassite.desktop.chara.util.Utils;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import vproxybase.dns.Resolver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
    private final Bars bars;
    private final ContextMenu contextMenu = new ContextMenu();
    private final Menu characterMenu = new Menu(I18nConsts.characterMenu.get()[0]);

    private final Group mouseCircle = new Group();

    public App(Stage primaryStage, Scene scene, Pane rootPane, Pane rootScalePane, Scale scale) {
        assert Logger.debug("new App(...)");

        // load configs and write into fields
        {
            Boolean chatFeatureEnabled = ConfigManager.get().getChatFeatureEnabled();
            if (chatFeatureEnabled != null) {
                this.messageDisabled = !chatFeatureEnabled;
            }
            Boolean activeInteractionEnabled = ConfigManager.get().getActiveInteractionEnabled();
            if (activeInteractionEnabled != null) {
                this.allowActiveInteraction = activeInteractionEnabled;
            }
            // note: alwaysOnTop is loaded in init() method
        }

        // build graphics

        this.scene = scene;
        this.rootPane = rootPane;
        this.rootScalePane = rootScalePane;

        Group barGroup = new Group();
        this.bars = new Bars(barGroup);
        this.rootPane.getChildren().add(barGroup);

        root = new Group();
        this.rootScalePane.getChildren().add(root);

        this.scale = scale;

        this.chara = Global.model.construct(new Model.ConstructParams(getAppCallback(), root, characterMenu));

        this.primaryStage = new StageTransformer(primaryStage,
            chara.data().imageWidth, chara.data().imageHeight,
            chara.data().minX, chara.data().imageWidth - chara.data().maxX,
            chara.data().minY,
            chara.data().imageHeight - chara.data().maxY,
            Consts.CHARA_TOTAL_ABSOLUTE_MARGIN_TOP
        );
        this.primaryStage.getStage().focusedProperty().addListener((observable, oldValue, newValue) -> {
            assert Logger.debug("primary stage focused: " + newValue);
            if (newValue != null) {
                StageUtils.primaryStageFocused = newValue;
            }
        });

        this.inputBox = new InputBox(rootPane, rootScalePane);
        this.inputBox.setPrefWidth(Consts.INPUT_WIDTH);
        this.inputBox.setPrefHeight(Consts.INPUT_HEIGHT);
        this.inputBox.setFont(Font.font(FontManager.getFontFamily(), Consts.INPUT_FONT_SIZE));
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
            setGlobalScreen(false);
            ConfigManager.get().setLastTimestamp(System.currentTimeMillis());
            ConfigManager.saveNow();
            try {
                Resolver.getDefault().stop();
            } catch (IOException ignore) {
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

        // mouse circle
        Circle mouseCircleOuter = new Circle(mouseCircleRadius);
        mouseCircleOuter.setFill(new Color(1, 1, 1, 0.5));
        Circle mouseCircleInner = new Circle(mouseCircleInnerRadius);
        mouseCircleInner.setFill(new Color(1, 1, 1, 0.5));
        mouseCircle.setMouseTransparent(true);
        mouseCircle.getChildren().addAll(mouseCircleOuter, mouseCircleInner);
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

        // chara config
        if (!chara.data().messageSupported) {
            messageDisabled = true;
        }
        if (!chara.data().activeInteractionSupported) {
            allowActiveInteraction = false;
        }

        // stage config
        primaryStage.getStage().initStyle(StageStyle.TRANSPARENT);
        { // load alwaysOnTop from config
            Boolean alwaysOnTop = ConfigManager.get().getAlwaysOnTop();
            if (alwaysOnTop == null) {
                alwaysOnTop = true;
            }
            primaryStage.getStage().setAlwaysOnTop(alwaysOnTop);
        }
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
        rootPane.setOnMousePressed(dragHandler);
        rootPane.setOnMouseDragged(dragHandler);
        rootScalePane.setOnMouseClicked(this::click);
        rootPane.setOnMouseMoved(this::jfxMouseMove);
        rootPane.setOnMouseExited(this::jfxMouseLeave);
        NativeMouseListenerUtils.setOnMouseMoved(this::mouseMove);
        rootScalePane.setLayoutY(primaryStage.getAddAbsoluteTop());

        // menu
        CheckMenuItem messageEnableItem = new CheckMenuItem(I18nConsts.enableChatFeatureItem.get()[0]);
        messageEnableItem.setSelected(!messageDisabled);
        if (!chara.data().messageSupported) {
            messageEnableItem.setDisable(true);
        }
        messageEnableItem.setOnAction(e -> {
            messageDisableOrEnable();
            messageEnableItem.setSelected(!messageDisabled);
        });
        CheckMenuItem alwaysOnTopItem = new CheckMenuItem(I18nConsts.alwaysOnTopItem.get()[0]);
        alwaysOnTopItem.setSelected(primaryStage.getStage().isAlwaysOnTop());
        alwaysOnTopItem.setOnAction(e -> {
            setOrUnsetAlwaysTop();
            alwaysOnTopItem.setSelected(primaryStage.getStage().isAlwaysOnTop());
        });
        CheckMenuItem activeInteractionItem = new CheckMenuItem(I18nConsts.activeInteractionItem.get()[0]);
        activeInteractionItem.setSelected(allowActiveInteraction);
        if (!chara.data().activeInteractionSupported) {
            activeInteractionItem.setDisable(true);
        }
        activeInteractionItem.setOnAction(e -> {
            activeInteractionEnableOrDisable();
            activeInteractionItem.setSelected(allowActiveInteraction);
        });
        MenuItem screenshotItem = new MenuItem(I18nConsts.screenshotItem.get()[0]);
        screenshotItem.setOnAction(e -> {
            var img = root.snapshot(new SnapshotParameters(),
                new WritableImage(
                    chara.data().maxX - chara.data().minX,
                    chara.data().maxY - chara.data().minY));
            Clipboard clipboard = Clipboard.getSystemClipboard();
            var content = new ClipboardContent();
            content.putImage(img);
            content.putString(Global.model.name() + "-" + System.currentTimeMillis() + ".png");
            clipboard.setContent(content);
            Alert.alert(I18nConsts.screenshotSavedInClipboard.get()[0]);
        });
        Menu systemMenu = new Menu(I18nConsts.systemMenu.get()[0]);
        MenuItem showVersionsItem = new MenuItem(I18nConsts.showVersionsItem.get()[0]);
        showVersionsItem.setOnAction(e -> showVersions());
        MenuItem exitItem = new MenuItem(I18nConsts.exitMenuItem.get()[0]);
        exitItem.setOnAction(e -> StageUtils.closePrimaryStage());
        systemMenu.getItems().addAll(showVersionsItem, exitItem);
        contextMenu.getItems().addAll(
            messageEnableItem,
            alwaysOnTopItem,
            activeInteractionItem,
            screenshotItem,
            characterMenu,
            systemMenu);
        scene.setOnContextMenuRequested(e -> contextMenu.show(rootPane, e.getScreenX(), e.getScreenY()));

        // key
        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);

        // group config
        root.setLayoutX(-primaryStage.getCutLeft());
        root.setLayoutY(-primaryStage.getCutTop());

        // scale config
        scale.setX(initialWidth / chara.data().imageWidth);
        scale.setY(initialHeight / chara.data().imageHeight);

        // bar
        calculateBarPosition();

        // input box
        calculateInputBoxPosition();
    }

    public void ready() {
        chara.ready(new Chara.ReadyParams());
    }

    private AppCallback getAppCallback() {
        return new AppCallback() {
            @Override
            public void setCharaPoints(CharaPoints points) {
                Platform.runLater(() -> bars.setCharaPoints(points));
            }

            @Override
            public void showMessage(Words words) {
                Platform.runLater(() -> App.this.showMessage(words));
            }

            @Override
            public void clearAllMessages() {
                Platform.runLater(App.this::clearAllMessages);
            }

            @Override
            public void activeInteraction(Runnable cb) {
                if (allowActiveInteraction) {
                    cb.run();
                }
            }

            @Override
            public void clickNothing(double x, double y) {
                Platform.runLater(() -> App.this.clickNothing(x, y));
            }

            @Override
            public void moveWindow(double deltaX, double deltaY) {
                Platform.runLater(() -> App.this.moveWindow(deltaX, deltaY));
            }

            @Override
            public void setDraggable(boolean draggable) {
                Platform.runLater(() -> App.this.windowIsDraggable = draggable);
            }

            @Override
            public void setGlobalScreen(boolean globalScreen) {
                Platform.runLater(() -> {
                    setGlobalScreenFromChara = globalScreen;
                    if (!globalScreen) {
                        App.this.setGlobalScreen(false);
                    }
                    // let the mouse move event trigger globalScreen registration
                });
            }

            @Override
            public void setAlwaysShowBar(boolean alwaysShowBar) {
                Platform.runLater(() -> bars.setAlwaysShowBar(alwaysShowBar));
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

    private boolean windowIsDraggable = true;

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
            if (!windowIsDraggable) {
                assert Logger.debug("not draggable ...");
                return;
            }
            assert Logger.debug("mouse dragged ...");

            primaryStage.setAbsoluteX(e.getScreenX() - this.oldScreenX + this.oldStageX);
            primaryStage.setAbsoluteY(e.getScreenY() - this.oldScreenY + this.oldStageY);

            primaryStage.saveConfig();

            chara.dragged();

            calculatePositions();
        }
    }

    private void click(MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }
        // hide menu
        if (contextMenu.isShowing()) {
            contextMenu.hide();
            return; // do not do other things because the user simply wants to close the menu
        }

        double x = e.getX();
        double y = e.getY();
        x += primaryStage.getCutLeft();
        y += primaryStage.getCutTop();
        assert Logger.debug("clicked at (" + x + "," + y + ") <= (" + e.getSceneX() + "," + e.getSceneY() + ")");

        chara.click(x, y);
    }

    private Scheduled deregisterGlobalScreenAfterMouseLeaveScheduledFuture;
    private boolean setGlobalScreenFromChara = true;

    private boolean mouseCircleIsShown = false;
    private static final int mouseCircleRadius = 25;
    private static final int mouseCircleInnerRadius = 5;

    private void mouseCircleMove(double sceneX, double sceneY) {
        mouseCircle.setLayoutX(sceneX);
        mouseCircle.setLayoutY(sceneY);
        if (sceneX < mouseCircleRadius ||
            sceneX > primaryStage.getStage().getWidth() - mouseCircleRadius ||
            sceneY < mouseCircleRadius ||
            sceneY > primaryStage.getStage().getHeight() - mouseCircleRadius) {
            // should hide
            if (mouseCircleIsShown) {
                mouseCircleIsShown = false;
                rootPane.getChildren().remove(mouseCircle);
            }
        } else {
            if (!mouseCircleIsShown) {
                mouseCircleIsShown = true;
                rootPane.getChildren().add(mouseCircle);
            }
        }
    }

    private void mouseCircleHide() {
        if (mouseCircleIsShown) {
            mouseCircleIsShown = false;
            rootPane.getChildren().remove(mouseCircle);
        }
    }

    private void jfxMouseMove(MouseEvent e) {
        var foo = deregisterGlobalScreenAfterMouseLeaveScheduledFuture;
        deregisterGlobalScreenAfterMouseLeaveScheduledFuture = null;
        if (foo != null) {
            foo.cancel();
        }

        if (setGlobalScreenFromChara) {
            setGlobalScreen(true);
        } else {
            // should alert events by jfx
            double x = primaryStage.getImageXBySceneX(e.getX());
            double y = primaryStage.getImageYBySceneY(e.getY());
            mouseMove(x, y);
            mouseCircleMove(e.getX(), e.getY());
        }
    }

    private void jfxMouseLeave(MouseEvent e) {
        if (!GlobalScreen.isNativeHookRegistered()) {
            // should alert events by jfx
            double x = primaryStage.getImageXBySceneX(e.getX());
            double y = primaryStage.getImageYBySceneY(e.getY());
            mouseLeave(x, y);
        }
    }

    private boolean mouseLeaves = true;

    private void mouseMove(NativeMouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        x -= primaryStage.getAbsoluteX();
        y -= primaryStage.getAbsoluteY();
        x = primaryStage.getImageXBySceneX(x);
        y = primaryStage.getImageYBySceneY(y);

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

        if (mouseLeaves) {
            return;
        }

        mouseCircleMove(e.getX() - primaryStage.getAbsoluteX(), e.getY() - primaryStage.getAbsoluteY());
        mouseMove(x, y);
    }

    private void mouseMove(double x, double y) {
        chara.mouseMove(x, y);

        // show somethings
        double realY = (y - primaryStage.getCutTop()) * primaryStage.getScaleRatio() + primaryStage.getAddAbsoluteTop();
        // show bar
        {
            if (realY < Consts.CHARA_TOTAL_ABSOLUTE_MARGIN_TOP) {
                bars.doShowBar();
            } else {
                bars.doHideBar();
            }
        }
        // show input box
        if (!messageDisabled) {
            // calculate whether to show input box
            if (realY > inputBox.getLayoutY() - Consts.INPUT_SHOW_Y_DELTA) {
                inputBox.show();
            } else {
                inputBox.hide();
            }
        }
    }

    private void mouseLeave(double x, double y) {
        assert Logger.debug("mouse leave at (" + x + "," + y + ")");
        mouseCircleHide();
        chara.mouseLeave();

        var foo = deregisterGlobalScreenAfterMouseLeaveScheduledFuture;
        if (foo != null) {
            // this should not happen, but if happens, cancel the old one
            foo.cancel();
        }
        deregisterGlobalScreenAfterMouseLeaveScheduledFuture =
            ThreadUtils.get().scheduleFX(() -> setGlobalScreen(false), 10, TimeUnit.SECONDS);

        // hide input box
        inputBox.hide();
        bars.doHideBar();
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

    private void clearAllMessages() {
        if (messageStage == null) {
            return;
        }

        messageStage.clearAllMessages();
    }

    private double getMiddleXOfScreen() {
        return primaryStage.getAbsoluteX() + (chara.data().topMiddleX - primaryStage.getCutLeft()) * primaryStage.getScaleRatio();
    }

    private void calculatePositions() {
        calculateBarPosition();
        calculateInputBoxPosition();
        calculateMessageStagePosition();
    }

    private void calculateBarPosition() {
        var middle = (chara.data().topMiddleX - primaryStage.getCutLeft()) * primaryStage.getScaleRatio();
        bars.barGroup.setLayoutX(middle - Consts.BAR_WIDTH / 2D);
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

        x -= primaryStage.getCutLeft();
        x *= primaryStage.getScaleRatio();

        y -= primaryStage.getCutTop();
        y *= primaryStage.getScaleRatio();
        y += primaryStage.getAddAbsoluteTop();

        x += primaryStage.getAbsoluteX();
        y += primaryStage.getAbsoluteY();
        messageStage.click(x, y);
    }

    private void moveWindow(double deltaX, double deltaY) {
        primaryStage.setAbsoluteX(primaryStage.getAbsoluteX() + deltaX * primaryStage.getScaleRatio());
        primaryStage.setAbsoluteY(primaryStage.getAbsoluteY() + deltaY * primaryStage.getScaleRatio());
    }

    private void setGlobalScreen(boolean globalScreen) {
        if (globalScreen) {
            if (GlobalScreen.isNativeHookRegistered()) {
                return;
            }
            Logger.info("register global screen");
            try {
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException e) {
                Logger.error("register global screen native hook failed", e);
            }
        } else {
            if (!GlobalScreen.isNativeHookRegistered()) {
                return;
            }
            Logger.info("deregister global screen");
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ignore) {
            }
        }
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
        assert Logger.debug("key pressed: " + e.getCode());
        chara.keyPressed(e);

        if (Global.debugFeatures) {
            if (e.isControlDown() || e.isMetaDown()) {
                if (e.getCode() == KeyCode.C) {
                    // copy, get debug info
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    ClipboardContent content = new ClipboardContent();
                    if (chara.getDebugInfo(content)) {
                        clipboard.setContent(content);
                    }
                } else if (e.getCode() == KeyCode.V) {
                    // paste, feed debug message
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    chara.takeDebugMessage(clipboard);
                }
            }
        }
    }

    private void keyReleased(KeyEvent e) {
        assert Logger.debug("key released: " + e.getCode());
        chara.keyReleased(e);
    }

    private void showVersions() {
        Alert.alert("" +
            "code: " + Utils.verNum2Str(Consts.VERSION_NUM) + "\n" +
            "model: " + Utils.verNum2Str(Global.modelVersion) +
            "");
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
        ConfigManager.get().setAlwaysOnTop(primaryStage.getStage().isAlwaysOnTop());
    }

    private boolean messageDisabled = false;

    private void messageDisableOrEnable() {
        if (!chara.data().messageSupported) {
            return;
        }
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
        ConfigManager.get().setChatFeatureEnabled(!messageDisabled);
    }

    private boolean allowActiveInteraction = false;

    private void activeInteractionEnableOrDisable() {
        if (!chara.data().activeInteractionSupported) {
            return;
        }
        allowActiveInteraction = !allowActiveInteraction;
        if (allowActiveInteraction) {
            Alert.alert(I18nConsts.activeInteractionEnabled.get()[0]);
        } else {
            Alert.alert(I18nConsts.activeInteractionDisabled.get()[0]);
        }
        ConfigManager.get().setActiveInteractionEnabled(allowActiveInteraction);
    }
}
