// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.GaussianBlur;
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
import net.cassite.desktop.chara.manager.PluginManager;
import net.cassite.desktop.chara.model.Model;
import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.util.*;
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
            Boolean mouseIndicatorEnabled = ConfigManager.get().getMouseIndicatorEnabled();
            if (mouseIndicatorEnabled != null) {
                this.mouseIndicatorEnabled = mouseIndicatorEnabled;
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
            e.consume();

            boolean[] closed = {false};
            Runnable shutdown = () -> ThreadUtils.get().runOnFX(() -> {
                if (closed[0]) {
                    return;
                }
                closed[0] = true;
                animateShutdown();
            });

            int timeout = chara.shutdown(shutdown);
            if (timeout == 0) {
                shutdown.run();
                return;
            }
            ThreadUtils.get().scheduleFX(shutdown, timeout, TimeUnit.MILLISECONDS);
        });

        // iconified hook
        primaryStage.iconifiedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                primaryStage.setIconified(false);
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

    private void animateShutdown() {
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(0);
        rootPane.setEffect(blur);
        double x = rootScalePane.getLayoutX();
        double y = rootScalePane.getLayoutY();
        double s = scale.getX();
        double w = rootScalePane.getWidth();
        double h = rootScalePane.getHeight();
        double ws = w * s;
        double hs = h * s;
        new TimeBasedAnimationHelper(500, percentage -> {
            double ss = s * (1 - percentage / 4);
            scale.setX(ss);
            scale.setY(ss);
            double wss = w * ss;
            double hss = h * ss;
            rootScalePane.setLayoutX(x + (ws - wss) / 2);
            rootScalePane.setLayoutY(y + (hs - hss) / 1.2);

            primaryStage.getStage().setOpacity(1 - percentage);
            blur.setRadius(percentage * primaryStage.getStage().getWidth() / 20);
        }).setFinishCallback(() -> {
            chara.release();
            PluginManager.get().release();
            ThreadUtils.get().shutdownNow();
            if (messageStage != null) {
                messageStage.release();
            }
            Alert.shutdown();
            setGlobalScreen(false);
            ConfigManager.get().setLastTimestamp(System.currentTimeMillis());
            ConfigManager.saveNow();
            try {
                Resolver.getDefault().stop();
            } catch (IOException ignore) {
            }
            primaryStage.getStage().hide();
            if (StageUtils.primaryTemporaryStage != null) {
                Logger.info("hide primary temporary stage");
                StageUtils.primaryTemporaryStage.hide();
            }
        }).play();
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
        MenuItem toBackItem = new MenuItem(I18nConsts.toBackItem.get()[0]);
        toBackItem.setDisable(primaryStage.getStage().isAlwaysOnTop());
        toBackItem.setOnAction(e -> {
            if (primaryStage.getStage().isAlwaysOnTop()) return;
            primaryStage.getStage().toBack();
        });
        CheckMenuItem alwaysOnTopItem = new CheckMenuItem(I18nConsts.alwaysOnTopItem.get()[0]);
        alwaysOnTopItem.setSelected(primaryStage.getStage().isAlwaysOnTop());
        alwaysOnTopItem.setOnAction(e -> {
            setOrUnsetAlwaysTop();
            alwaysOnTopItem.setSelected(primaryStage.getStage().isAlwaysOnTop());
            toBackItem.setDisable(primaryStage.getStage().isAlwaysOnTop());
        });
        CheckMenuItem mouseIndicatorItem = new CheckMenuItem(I18nConsts.mouseIndicatorItem.get()[0]);
        mouseIndicatorItem.setSelected(mouseIndicatorEnabled);
        mouseIndicatorItem.setOnAction(e -> {
            toggleMouseIndicator();
            mouseIndicatorItem.setSelected(mouseIndicatorEnabled);
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
        MenuItem snapshotItem = new MenuItem(I18nConsts.snapshotItem.get()[0]);
        snapshotItem.setOnAction(e -> {
            var img = root.snapshot(new SnapshotParameters(),
                new WritableImage(
                    chara.data().maxX - chara.data().minX,
                    chara.data().maxY - chara.data().minY));
            Clipboard clipboard = Clipboard.getSystemClipboard();
            var content = new ClipboardContent();
            content.putImage(img);
            content.putString(Global.model.name() + "-" + System.currentTimeMillis() + ".png");
            clipboard.setContent(content);
            Alert.alert(I18nConsts.snapshotSavedInClipboard.get()[0]);
        });
        Menu systemMenu = new Menu(I18nConsts.systemMenu.get()[0]);
        MenuItem showVersionsItem = new MenuItem(I18nConsts.showVersionsItem.get()[0]);
        showVersionsItem.setOnAction(e -> showVersions());
        CheckMenuItem showIconOnTaskbarItem = new CheckMenuItem(I18nConsts.showItemOnTaskbarItem.get()[0]);
        showIconOnTaskbarItem.setSelected(ConfigManager.get().getShowIconOnTaskbar());
        showIconOnTaskbarItem.setOnAction(e -> {
            boolean b = !ConfigManager.get().getShowIconOnTaskbar();
            ConfigManager.get().setShowIconOnTaskbar(b);
            showIconOnTaskbarItem.setSelected(b);
            Alert.alert(I18nConsts.applyAfterReboot.get()[0]);
        });
        Menu pluginMenu = new Menu(I18nConsts.pluginMenu.get()[0]);
        for (Plugin plugin : PluginManager.get().getPlugins()) {
            MenuItem pluginItem = new MenuItem(plugin.name() + ": " + Utils.verNum2Str(plugin.version()));
            pluginItem.setOnAction(e -> plugin.clicked());
            pluginMenu.getItems().add(pluginItem);
        }
        if (PluginManager.get().getPlugins().isEmpty()) {
            pluginMenu.setDisable(true);
        }
        MenuItem exitItem = new MenuItem(I18nConsts.exitMenuItem.get()[0]);
        exitItem.setOnAction(e -> StageUtils.closePrimaryStage());
        if (Utils.isWindows()) {
            systemMenu.getItems().addAll(showVersionsItem, showIconOnTaskbarItem, pluginMenu, exitItem);
        } else {
            systemMenu.getItems().addAll(showVersionsItem, pluginMenu, exitItem);
        }
        contextMenu.getItems().addAll(
            messageEnableItem,
            alwaysOnTopItem,
            toBackItem,
            mouseIndicatorItem,
            activeInteractionItem,
            snapshotItem,
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
        // construct message stage
        messageStage = new MessageStage(primaryStage);
        messageStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            assert Logger.debug("message stage focused: " + newValue);
            if (newValue != null) {
                StageUtils.messageStageFocused = newValue;
            }
        });
        messageStage.setAlwaysOnTop(primaryStage.getStage().isAlwaysOnTop());
        calculateMessageStagePosition();
        EventBus.publish(Events.MessageStageReady, messageStage);

        // primary stage is ready
        EventBus.publish(Events.PrimaryStageReady, primaryStage);

        // call chara.ready
        chara.ready(new Chara.ReadyParams());
        EventBus.publish(Events.CharacterReady, chara);
    }

    private AppCallback getAppCallback() {
        var appCallback = new AppCallback() {
            @Override
            public void setCharaPoints(CharaPoints points) {
                ThreadUtils.get().runOnFX(() -> bars.setCharaPoints(points));
            }

            @Override
            public void showMessage(Words words) {
                ThreadUtils.get().runOnFX(() -> App.this.showMessage(words));
            }

            @Override
            public void clearAllMessages() {
                ThreadUtils.get().runOnFX(App.this::clearAllMessages);
            }

            @Override
            public void activeInteraction(Runnable cb) {
                if (allowActiveInteraction) {
                    cb.run();
                }
            }

            @Override
            public void clickNothing(double x, double y) {
                ThreadUtils.get().runOnFX(() -> App.this.clickNothing(x, y));
            }

            @Override
            public void moveWindow(double anchorX, double deltaX, double anchorY, double deltaY) {
                ThreadUtils.get().runOnFX(() -> App.this.moveWindow(anchorX, deltaX, anchorY, deltaY));
            }

            @Override
            public double[] getWindowPosition() {
                return new double[]{primaryStage.getAbsoluteX(), primaryStage.getAbsoluteY()};
            }

            @Override
            public void setDraggable(boolean draggable) {
                ThreadUtils.get().runOnFX(() -> App.this.windowIsDraggable = draggable);
            }

            @Override
            public void setGlobalScreen(boolean globalScreen) {
                ThreadUtils.get().runOnFX(() -> {
                    setGlobalScreenFromChara = globalScreen;
                    if (!globalScreen) {
                        App.this.setGlobalScreen(false);
                    }
                    // let the mouse move event trigger globalScreen registration
                });
            }

            @Override
            public void setAlwaysShowBar(boolean alwaysShowBar) {
                ThreadUtils.get().runOnFX(() -> bars.setAlwaysShowBar(alwaysShowBar));
            }
        };
        EventBus.publish(Events.AppCallbackReady, appCallback);
        return appCallback;
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
        EventBus.publish(Events.PrimaryStageResized, null);

        primaryStage.saveConfig();

        calculatePositions();
        mouseCircleHide();
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
            EventBus.publish(Events.PrimaryStageMoved, null);

            primaryStage.saveConfig();

            chara.dragged();

            calculatePositions();
        }
    }

    private static final double[] MOUSE_CLICK_EVENT_UTIL_OBJECT = new double[2];

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
        MOUSE_CLICK_EVENT_UTIL_OBJECT[0] = x;
        MOUSE_CLICK_EVENT_UTIL_OBJECT[1] = y;
        EventBus.publish(Events.MouseClickedImagePosition, MOUSE_CLICK_EVENT_UTIL_OBJECT);
    }

    private Scheduled deregisterGlobalScreenAfterMouseLeaveScheduledFuture;
    private boolean setGlobalScreenFromChara = true;

    private boolean mouseIndicatorEnabled = true;
    private boolean mouseCircleIsShown = false;
    private static final int mouseCircleRadius = 25;
    private static final int mouseCircleInnerRadius = 5;

    private void mouseCircleMove(double sceneX, double sceneY) {
        if (!mouseIndicatorEnabled) {
            return; // do not show the mouse if disabled
        }
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

    private void toggleMouseIndicator() {
        mouseIndicatorEnabled = !mouseIndicatorEnabled;
        ConfigManager.get().setMouseIndicatorEnabled(mouseIndicatorEnabled);
        if (!mouseIndicatorEnabled) {
            mouseCircleHide();
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
        }
        if (!GlobalScreen.isNativeHookRegistered()) {
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
        double realY = primaryStage.getSceneYByImageY(y);
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
    private int colorHash = 0;

    private void showMessage(Words words) {
        if (messageDisabled) {
            Logger.info("message is disabled");
            return;
        }

        if (messageStage == null) {
            return; // not constructed yet
        }
        int colorHash = this.colorHash++;
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

    private double getTopMiddleXOfScreen() {
        return primaryStage.getAbsoluteX() + primaryStage.getSceneXByImageX(chara.data().topMiddleX);
    }

    private void calculatePositions() {
        calculateBarPosition();
        calculateInputBoxPosition();
        calculateMessageStagePosition();
    }

    private void calculateBarPosition() {
        var middle = primaryStage.getSceneXByImageX(chara.data().topMiddleX);
        bars.barGroup.setLayoutX(middle - Consts.BAR_WIDTH / 2D);
    }

    private void calculateInputBoxPosition() {
        var middle = primaryStage.getSceneXByImageX(chara.data().bottomMiddleX);
        inputBox.setLayoutX(middle - Consts.INPUT_WIDTH / 2D);
        var stageH = primaryStage.getStage().getHeight();
        inputBox.setLayoutY(stageH - Consts.INPUT_MARGIN_BOTTOM - Consts.INPUT_HEIGHT);
    }

    private void calculateMessageStagePosition() {
        if (messageStage == null) {
            return;
        }

        var middleXOfScreen = getTopMiddleXOfScreen();
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

        var y = primaryStage.getAbsoluteY() + primaryStage.getSceneYByImageY(chara.data().messageAtMinY);
        messageStage.setY(y);
    }

    private void clickNothing(double x, double y) {
        if (messageStage == null) {
            return;
        }
        if (!messageStage.isShowing()) {
            return;
        }

        x = primaryStage.getSceneXByImageX(x);
        y = primaryStage.getSceneYByImageY(y);

        x += primaryStage.getAbsoluteX();
        y += primaryStage.getAbsoluteY();
        messageStage.click(x, y);
    }

    private void moveWindow(double anchorX, double deltaX, double anchorY, double deltaY) {
        primaryStage.setAbsoluteX(anchorX + deltaX * primaryStage.getScaleRatio());
        primaryStage.setAbsoluteY(anchorY + deltaY * primaryStage.getScaleRatio());
        EventBus.publish(Events.PrimaryStageMoved, null);
    }

    private void setGlobalScreen(boolean globalScreen) {
        if (globalScreen) {
            if (!Global.globalScreenEnabled) {
                return;
            }
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
        chara.takeMessage(text);
        EventBus.publish(Events.MessageTaken, text);
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
            "model: " + Utils.verNum2Str(Global.modelVersion) + "\n" +
            "vproxy: " + vproxybase.util.Version.VERSION + "\n" +
            "vjson: " + vjson.util.VERSION.VERSION + "\n" +
            "jnativehook: " + Consts.JNATIVEHOOK_VERSION +
            "");
    }

    private void setOrUnsetAlwaysTop() {
        if (primaryStage.getStage().isAlwaysOnTop()) {
            assert Logger.debug("always on top disabled");
            primaryStage.getStage().setAlwaysOnTop(false);
            if (messageStage != null) {
                messageStage.setAlwaysOnTop(false);
            }
        } else {
            assert Logger.debug("always on top enabled");
            primaryStage.getStage().setAlwaysOnTop(true);
            if (messageStage != null) {
                messageStage.setAlwaysOnTop(true);
            }
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
        } else {
            assert Logger.debug("message disable");
            messageDisabled = true;
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
