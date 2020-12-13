// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import javafx.stage.FileChooser;
import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.chara.Chara;
import net.cassite.desktop.chara.graphic.Alert;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Utils;
import net.cassite.desktop.chara.widget.fgoclick.control.BoundsSelector;
import net.cassite.desktop.chara.widget.fgoclick.control.OneButton;
import net.cassite.desktop.chara.widget.fgoclick.control.SleepIndicator;
import net.cassite.desktop.chara.widget.fgoclick.control.TwoButtons;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.Anchor;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.AnchorSelector;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.ConfirmStage;
import net.cassite.desktop.chara.widget.fgoclick.fgo.Configuration;
import net.cassite.desktop.chara.widget.fgoclick.robot.ExecutableAction;
import net.cassite.desktop.chara.widget.fgoclick.robot.HtmlRobot;
import net.cassite.desktop.chara.widget.fgoclick.robot.RobotRun;
import net.cassite.desktop.chara.widget.fgoclick.statem.Statem;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickI18nConsts;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FgoClick implements Chara {
    private final AppCallback appCallback;
    private final Group parent;
    private final FgoClickConsts consts;

    private final CheckMenuItem htmlRobotMenuItem;

    private final OneButton stoppedNotPreparedStage;
    private final TwoButtons stoppedPreparedStage;
    private final TwoButtons selectBoundsStage;
    private final OneButton preparingStage;
    private final OneButton preparedStage;
    private final TwoButtons confirmConfigurationStage;
    private final TwoButtons runningStage;
    private final TwoButtons pausedStage;
    private final Group[] allStages;

    private final Data data;
    private final Robot robot;
    private final SleepIndicator sleepIndicator;

    public FgoClick(AppCallback appCallback, Group parent, Menu characterMenu, FgoClickConsts consts) {
        this.appCallback = appCallback;
        this.parent = parent;
        this.consts = consts;

        this.data = new DataBuilder()
            .setImageWidth(consts.width)
            .setImageHeight(consts.height)
            .setMinWidth(consts.width)
            .setInitialWidth(consts.width)
            .setMinX(0)
            .setMaxX(consts.width)
            .setTopMiddleX(consts.width / 2)
            .setBottomMiddleX(consts.width / 2)
            .setMessageOffsetX(consts.width / 2 + 10)
            .setMessageAtMinY(consts.height / 2 - 10)
            .setMinY(0)
            .setMaxY(consts.height)
            .build();

        this.stoppedNotPreparedStage = new OneButton(consts,
            FgoClickI18nConsts.prepareButton.get()[0], FgoClickConsts.prepareButtonColor, this::prepare);
        this.stoppedPreparedStage = new TwoButtons(consts,
            FgoClickI18nConsts.rePrepareButton.get()[0], FgoClickConsts.prepareButtonColor, this::prepare,
            FgoClickI18nConsts.runButton.get()[0], FgoClickConsts.runButtonColor, this::run);
        this.selectBoundsStage = new TwoButtons(consts,
            FgoClickI18nConsts.cancelButton.get()[0], FgoClickConsts.cancelButtonColor, this::cancelSelectingBounds,
            FgoClickI18nConsts.okButton.get()[0], FgoClickConsts.okButtonColor, this::selectBounds);
        this.preparingStage = new OneButton(consts,
            FgoClickI18nConsts.cancelButton.get()[0], FgoClickConsts.cancelButtonColor, this::cancelPreparing);
        this.preparedStage = new OneButton(consts,
            FgoClickI18nConsts.runButton.get()[0], FgoClickConsts.runButtonColor, this::run);
        this.confirmConfigurationStage = new TwoButtons(consts,
            FgoClickI18nConsts.cancelButton.get()[0], FgoClickConsts.cancelButtonColor, this::cancelConfirming,
            FgoClickI18nConsts.okButton.get()[0], FgoClickConsts.okButtonColor, this::confirm);
        this.runningStage = new TwoButtons(consts,
            FgoClickI18nConsts.stopButton.get()[0], FgoClickConsts.stopButtonColor, this::stop,
            FgoClickI18nConsts.pauseButton.get()[0], FgoClickConsts.pauseButtonColor, () -> pause(() -> {
        }));
        this.pausedStage = new TwoButtons(consts,
            FgoClickI18nConsts.resumeButton.get()[0], FgoClickConsts.runButtonColor, this::resume,
            FgoClickI18nConsts.skipButton.get()[0], FgoClickConsts.skipButtonColor, () -> skip(() -> {
        }));
        allStages = new Group[]{
            stoppedNotPreparedStage,
            stoppedPreparedStage,
            selectBoundsStage,
            preparingStage,
            preparedStage,
            confirmConfigurationStage,
            runningStage,
            pausedStage,
        };

        this.robot = new Robot();
        this.sleepIndicator = new SleepIndicator(robot, consts);

        var sleepIndicatorCheckMenuItem = new CheckMenuItem(FgoClickI18nConsts.sleepIndicatorMenuItem.get()[0]);
        sleepIndicatorCheckMenuItem.setSelected(!ConfigManager.get().getBoolValue(FgoClickConsts.disableSleepIndicator));
        sleepIndicatorCheckMenuItem.setOnAction(e -> {
            var disabled = ConfigManager.get().getBoolValue(FgoClickConsts.disableSleepIndicator);
            disabled = !disabled;
            ConfigManager.get().setBoolValue(FgoClickConsts.disableSleepIndicator, disabled);
            sleepIndicatorCheckMenuItem.setSelected(!disabled);
            if (disabled) {
                sleepIndicator.disable();
            } else {
                sleepIndicator.enable();
            }
        });
        characterMenu.getItems().add(sleepIndicatorCheckMenuItem);

        var snapshotCheckMenuItem = new CheckMenuItem(FgoClickI18nConsts.snapshotMenuItem.get()[0]);
        snapshotCheckMenuItem.setSelected(ConfigManager.get().getBoolValue(FgoClickConsts.autoSnapshot));
        snapshotCheckMenuItem.setOnAction(e -> {
            var b = ConfigManager.get().getBoolValue(FgoClickConsts.autoSnapshot);
            b = !b;
            ConfigManager.get().setBoolValue(FgoClickConsts.autoSnapshot, b);
            snapshotCheckMenuItem.setSelected(b);
        });
        characterMenu.getItems().add(snapshotCheckMenuItem);

        htmlRobotMenuItem = new CheckMenuItem(FgoClickI18nConsts.htmlRobotMenuItem.get()[0]);
        htmlRobotMenuItem.setDisable(true);
        htmlRobotMenuItem.setSelected(false);
        htmlRobotMenuItem.setOnAction(e -> htmlRobotEnableDisable());
        characterMenu.getItems().add(htmlRobotMenuItem);

        var click100Times = new MenuItem(FgoClickI18nConsts.click100TimesMenuItem.get()[0]);
        click100Times.setOnAction(e -> clickManyTimes(100));
        characterMenu.getItems().add(click100Times);

        var click50Times = new MenuItem(FgoClickI18nConsts.click50TimesMenuItem.get()[0]);
        click50Times.setOnAction(e -> clickManyTimes(50));
        characterMenu.getItems().add(click50Times);

        resetToInitialStage();
    }

    private BoundsSelector boundsSelector;

    private void showBoundsSelector() {
        boundsSelector = new BoundsSelector();
        double w = ConfigManager.get().getDoubleValue(FgoClickConsts.boundsWidth);
        if (w == 0) w = consts.defaultBoundsWidth;
        double h = ConfigManager.get().getDoubleValue(FgoClickConsts.boundsHeight);
        if (h == 0) h = consts.defaultBoundsHeight;
        double x = ConfigManager.get().getDoubleValue(FgoClickConsts.boundsX);
        double y = ConfigManager.get().getDoubleValue(FgoClickConsts.boundsY);
        boundsSelector.setWidth(w);
        boundsSelector.setHeight(h);
        if (x == 0 && y == 0) {
            boundsSelector.centerOnScreen();
        } else {
            boundsSelector.setX(x);
            boundsSelector.setY(y);
        }
        boundsSelector.show();
    }

    private void prepare() {
        showBoundsSelector();
        removeAllStages();
        parent.getChildren().add(selectBoundsStage);

        // the buttons should not be draggable when preparing
        // it will be set to true after preparation done or canceled
        appCallback.setDraggable(false);

        // show message
        appCallback.clearAllMessages();
        appCallback.showMessage(FgoClickI18nConsts.selectBoundsHelpMessage, true);
        appCallback.showMessage(FgoClickI18nConsts.ensureGameScene, true);
    }

    private void cancelSelectingBounds() {
        boundsSelector.hide();
        boundsSelector = null;
        resetToInitialStage();
    }

    private void selectBounds() {
        appCallback.clearAllMessages();

        double x = boundsSelector.getX();
        double y = boundsSelector.getY();
        double w = boundsSelector.getWidth();
        double h = boundsSelector.getHeight();
        boundsSelector.hide();

        double oldX = ConfigManager.get().getDoubleValue(FgoClickConsts.boundsX);
        double oldY = ConfigManager.get().getDoubleValue(FgoClickConsts.boundsY);
        double oldW = ConfigManager.get().getDoubleValue(FgoClickConsts.boundsWidth);
        double oldH = ConfigManager.get().getDoubleValue(FgoClickConsts.boundsHeight);

        ConfigManager.get().setDoubleValue(FgoClickConsts.boundsX, x);
        ConfigManager.get().setDoubleValue(FgoClickConsts.boundsY, y);
        ConfigManager.get().setDoubleValue(FgoClickConsts.boundsWidth, w);
        ConfigManager.get().setDoubleValue(FgoClickConsts.boundsHeight, h);

        if (x != oldX || y != oldY || w != oldW || h != oldH) {
            // set prepared to false because the bounds are changed
            ConfigManager.get().setBoolValue(FgoClickConsts.prepared, false);
        }

        removeAllStages();
        parent.getChildren().add(preparingStage);

        // show anchorSelector
        anchorSelector = new AnchorSelector(robot, appCallback, consts, anchor -> {
            anchorSelector.hide();
            anchorSelector = null;
            try {
                anchor.calculate();
            } catch (Exception e) {
                Logger.error(FgoClickI18nConsts.invalidAnchors.get()[0], e);
                resetToInitialStage();
                return;
            }
            // succeeded
            anchor.save();
            ConfigManager.get().setBoolValue(FgoClickConsts.prepared, true);

            appCallback.clearAllMessages();
            appCallback.showMessage(FgoClickI18nConsts.readyToGoMessage);

            removeAllStages();
            parent.getChildren().add(preparedStage);
        });
        anchorSelector.show();
    }

    private AnchorSelector anchorSelector;

    private void cancelPreparing() {
        anchorSelector.hide();
        anchorSelector = null;
        resetToInitialStage();
    }

    private void run() {
        removeAllStages();
        parent.getChildren().add(confirmConfigurationStage);
        // should not be draggable when confirming
        // it will be set to true when canceled or confirmed
        appCallback.setDraggable(false);

        // show the stage with points confirm
        confirmStage = new ConfirmStage(consts, new Anchor(ConfigManager.get()).calculate(), robot);
        confirmStage.show();

        appCallback.clearAllMessages();
        appCallback.showMessage(FgoClickI18nConsts.clickConfirmStageToSwitchCoordinatesInDifferentScenes, true);
    }

    private ConfirmStage confirmStage;

    private void cancelConfirming() {
        confirmStage.hide();
        confirmStage = null;
        resetToInitialStage();
    }

    private void confirm() {
        // msg
        appCallback.clearAllMessages();
        // draggable
        appCallback.setDraggable(true);

        // hide
        confirmStage.hide();
        confirmStage = null;

        // select
        appCallback.clearAllMessages();
        appCallback.showMessage(true, FgoClickI18nConsts.chooseScriptFile.get()[0]);
        disableAllStages();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(FgoClickI18nConsts.chooseScriptFile.get()[0]);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
            "script file",
            "*.fgoclick"));
        File file = fileChooser.showOpenDialog(null);
        appCallback.clearAllMessages();
        enableAllStages();
        if (file == null) {
            appCallback.clearAllMessages();
            appCallback.showMessage(FgoClickI18nConsts.noFileSelected);
            resetToInitialStage();
            return;
        }

        Configuration configuration;
        List<ExecutableAction> executableActions;
        try {
            configuration = FgoClickUtils.deserialize(file);
            executableActions = new Statem(configuration).compile();
        } catch (Exception e) {
            Logger.error(FgoClickI18nConsts.invalidScriptFile.get()[0], e);
            resetToInitialStage();
            return;
        }
        Logger.info(configuration.getPreset().getSettings().toString());

        // run robot
        robotRun = new RobotRun(robot,
            new Anchor(ConfigManager.get()).calculate(),
            sleepIndicator,
            consts,
            configuration.getPreset().getSettings(),
            executableActions,
            () -> {
                /*
                 * see {@link #stop()}
                 */
                parent.setCursor(Cursor.DEFAULT);
                runningStage.setButton1Text(FgoClickI18nConsts.stopButton.get()[0]);
                runningStage.setButton2Text(FgoClickI18nConsts.pauseButton.get()[0]);
                sleepIndicator.hide();
                enableAllStages();
                // switch button
                resetToInitialStage();
            });
        Logger.info("wait for 2s before running");
        disableAllStages();
        Utils.delayNoRecord(2_000, () -> {
            enableAllStages();
            robotRun.run();
            htmlRobotMenuItem.setDisable(false);
            htmlRobotMenuItem.setSelected(htmlRobot != null);
        });

        // switch button
        removeAllStages();
        parent.getChildren().add(runningStage);
    }

    private RobotRun robotRun;
    private HtmlRobot htmlRobot;

    private void htmlRobotEnableDisable() {
        if (robotRun == null) {
            return; // cannot enable when robotRun not exists
        }
        if (htmlRobot == null) {
            htmlRobot = new HtmlRobot(robot, robotRun, this);
            try {
                htmlRobot.start(59080);
            } catch (IOException e) {
                Logger.error("failed to launch html robot on 59080", e);
                return;
            }
            Alert.alert(FgoClickI18nConsts.htmlRobotStartedOn59080.get()[0]);
        } else {
            htmlRobot.stop();
            htmlRobot = null;
        }
        htmlRobotMenuItem.setSelected(htmlRobot != null);
    }

    private void stop() {
        Logger.warn("stopping ...");
        runningStage.setButton1Text(FgoClickI18nConsts.stoppingButton.get()[0]);
        disableAllStages();
        parent.setCursor(Cursor.WAIT);
        robotRun.stop();
        if (htmlRobot != null) {
            htmlRobot.stop();
        }
        htmlRobot = null;
        htmlRobotMenuItem.setDisable(true);
        htmlRobotMenuItem.setSelected(false);
    }

    private boolean pausing = false;

    public void pause(Runnable cb) {
        if (pausing) {
            cb.run();
            return;
        }
        if (!parent.getChildren().contains(runningStage)) {
            Logger.warn("should not run 'pause' when running stage is not showing");
            cb.run();
            return;
        }
        pausing = true;
        Logger.warn("pausing ...");

        runningStage.setButton2Text(FgoClickI18nConsts.pausingButton.get()[0]);
        disableAllStages();
        robotRun.pause(() -> {
            sleepIndicator.hide();

            runningStage.setButton2Text(FgoClickI18nConsts.pauseButton.get()[0]);
            removeAllStages();
            parent.getChildren().add(pausedStage);
            enableAllStages();

            appCallback.clearAllMessages();
            appCallback.showMessage(true, robotRun.peekNextOp());

            pausing = false;
            cb.run();
        });
    }

    public void resume() {
        if (!parent.getChildren().contains(pausedStage)) {
            Logger.warn("should not run 'resume' when paused stage is not showing");
            return;
        }
        appCallback.clearAllMessages();
        robotRun.run();
        removeAllStages();
        parent.getChildren().add(runningStage);
    }

    private boolean skipping = false;

    public void skip(Runnable cb) {
        if (skipping) {
            cb.run();
            return;
        }
        if (!parent.getChildren().contains(pausedStage)) {
            Logger.warn("should not run 'skip' when paused stage is not showing");
            cb.run();
            return;
        }
        skipping = true;
        Logger.warn("skipping ...");

        disableAllStages();
        pausedStage.setButton2Text(FgoClickI18nConsts.skippingButton.get()[0]);
        robotRun.skip(() -> {
            enableAllStages();
            pausedStage.setButton2Text(FgoClickI18nConsts.skipButton.get()[0]);
            appCallback.clearAllMessages();
            appCallback.showMessage(true, robotRun.peekNextOp());

            skipping = false;
            cb.run();
        });
    }

    private void removeAllStages() {
        parent.getChildren().removeAll(allStages);
    }

    private void resetToInitialStage() {
        appCallback.clearAllMessages();
        removeAllStages();
        if (ConfigManager.get().getBoolValue(FgoClickConsts.prepared)) {
            parent.getChildren().add(stoppedPreparedStage);
        } else {
            parent.getChildren().add(stoppedNotPreparedStage);
        }
        appCallback.setDraggable(true);
    }

    private void disableAllStages() {
        for (var g : allStages) {
            g.setMouseTransparent(true);
        }
    }

    private void enableAllStages() {
        for (var g : allStages) {
            g.setMouseTransparent(false);
        }
    }

    private void clickManyTimes(int times) {
        Alert.alert(FgoClickI18nConsts.waitForTwoSecondsBeforeClicking.get()[0]);
        Logger.info("wait for 2s before clicking mouse for " + times + " times");
        Utils.delay("click-many-times", 2_000, () -> {
            Logger.info("begin to click");
            clickManyTimes0(times);
        });
    }

    private void clickManyTimes0(int times) {
        Utils.delay("do-click-many-times", 500, () -> {
            robot.mouseClick(MouseButton.PRIMARY);
            if (times == 0) {
                Logger.info("clicking finishes");
                Alert.alert(FgoClickI18nConsts.clickingFinishes.get()[0]);
                return;
            }
            clickManyTimes0(times - 1);
        });
    }

    @Override
    public void ready(ReadyParams params) {
        appCallback.setAlwaysHideBar(true);
        if (!ConfigManager.get().getBoolValue(FgoClickConsts.disableSleepIndicator)) {
            sleepIndicator.enable();
        }
    }

    @Override
    public void mouseMove(double x, double y) {
        // ignore
    }

    @Override
    public void mouseLeave() {
        // ignore
    }

    @Override
    public void dragged() {
        // ignore
    }

    @Override
    public void click(double x, double y) {
        // ignore
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
    public int shutdown(Runnable cb) {
        return 0;
    }

    @Override
    public void release() {
        if (boundsSelector != null) {
            boundsSelector.hide();
            boundsSelector = null;
        }
        if (confirmStage != null) {
            confirmStage.hide();
            confirmStage = null;
        }
        if (htmlRobot != null) {
            htmlRobot.stop();
            htmlRobot = null;
        }
    }

    @Override
    public void takeMessage(String msg) {
        // ignore
    }

    @Override
    public boolean getDebugInfo(ClipboardContent content) {
        FgoClickUtils.DebugImage image = FgoClickUtils.getLastDebugImage();
        if (image == null) {
            return false;
        }
        content.putImage(image.image);
        content.putString(image.msg);
        return true;
    }

    @Override
    public void takeDebugMessage(Clipboard clipboard) {
        // ignore
    }
}
