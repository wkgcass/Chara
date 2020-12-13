// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.robot;

import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Utils;
import net.cassite.desktop.chara.widget.fgoclick.control.SleepIndicator;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.CalculatedAnchor;
import net.cassite.desktop.chara.widget.fgoclick.fgo.Settings;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickI18nConsts;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class RobotRun {
    private final Robot robot;
    private final CalculatedAnchor anchor;
    private final SleepIndicator sleepIndicator;
    private final FgoClickConsts consts;
    private final Settings settings;
    private final List<ExecutableAction> actions;
    private final Runnable stopCallback;

    public RobotRun(Robot robot,
                    CalculatedAnchor anchor,
                    SleepIndicator sleepIndicator,
                    FgoClickConsts consts,
                    Settings settings,
                    List<ExecutableAction> actions,
                    Runnable stopCallback) {
        this.robot = robot;
        this.anchor = anchor;
        this.sleepIndicator = sleepIndicator;
        this.consts = consts;
        this.settings = settings;
        this.stopCallback = stopCallback;

        this.actions = new LinkedList<>();
        {
            // battle actions
            this.actions.addAll(actions);
            // after battle
            this.actions.add(new DummyClick());
            this.actions.add(new ClickNext());
            if (settings.getUseApple() != null && !settings.getUseApple().equals("no")) {
                this.actions.add(new NextGameAndUseApple());
                this.actions.add(new ChooseSupportServant());
            } else {
                this.actions.add(new ClickDoNotUseApple());
                this.actions.add(new ClickLastGameAndCancelApple());
                this.actions.add(new ChooseSupportServant());
                this.actions.add(new ClickStartButton());
            }
            this.actions.add(new WaitForGameStarting());
        }
    }

    private boolean isRunning = false;
    private boolean isPaused = false;
    private RunningContext ctx;
    private ListIterator<ExecutableAction> actionIterator;

    private void clickTitle(Runnable cb) {
        Utils.delay("click-title", 100, () -> {
            double x = robot.getMouseX();
            double y = robot.getMouseY();
            robot.mouseMove(anchor.getStage().x + anchor.getTitle().x, anchor.getStage().y + anchor.getTitle().y);
            robot.mouseClick(MouseButton.PRIMARY);
            // restore position
            Utils.delay("click-title-restore", 50, () -> {
                robot.mouseMove(x, y);
                cb.run();
            });
        });
    }

    public void run() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        pausedCallback = null;
        skipCallback = null;
        if (!isPaused) {
            ctx = new RunningContext(robot, anchor, sleepIndicator, consts, settings, this::done);
            actionIterator = actions.listIterator();
        }
        isPaused = false;
        ctx.skipping = false;
        Logger.warn("robot begins");
        clickTitle(() -> {
            if (!executing) {
                done(false);
            }
        });
    }

    private boolean executing = false;

    private void done(boolean willBreak) {
        executing = false;

        if (isPaused) {
            Logger.warn("robot paused");
            if (firstPausedActionDone) {
                firstPausedActionDone = false;
                if (actionIterator.hasPrevious()) {
                    actionIterator.previous();
                }
            }
            Runnable pausedCallback = this.pausedCallback;
            this.pausedCallback = null;
            if (pausedCallback != null) {
                pausedCallback.run();
            }
            Runnable skipCallback = this.skipCallback;
            this.skipCallback = null;
            if (skipCallback != null) {
                skipCallback.run();
            }
            ctx.skipping = false;
            return;
        }
        if (willBreak) {
            Logger.warn("robot stops (break loop)");
            this.stopCallback.run();
            return;
        }

        RunningContext ctx = this.ctx;
        Iterator<ExecutableAction> actionIterator = this.actionIterator;
        if (!isRunning) {
            Logger.warn("robot stops");
            this.stopCallback.run();
            return;
        }
        if (!actionIterator.hasNext()) {
            Logger.warn("one round finished, continue running");
            this.actionIterator = actions.listIterator();
            done(false);
            return;
        }
        var a = actionIterator.next();
        executing = true;
        a.execute(ctx);
    }

    private Runnable skipCallback;

    public void skip(Runnable cb) {
        if (!isPaused) {
            return;
        }
        if (!actionIterator.hasNext()) {
            if (settings.isLoop()) {
                actionIterator = actions.listIterator();
            } else {
                return;
            }
        }
        var action = actionIterator.next();
        ctx.skipping = true;
        skipCallback = cb;
        action.execute(ctx);
    }

    public String peekNextOp() {
        if (!actionIterator.hasNext()) {
            if (settings.isLoop()) {
                return actions.get(0).toString();
            }
            return FgoClickI18nConsts.allOpsDone.get()[0];
        }
        String ret = actionIterator.next().toString();
        actionIterator.previous();
        return ret;
    }

    public String getLastOp() {
        if (!actionIterator.hasPrevious()) {
            return FgoClickI18nConsts.noOpYet.get()[0];
        }
        var ret = actionIterator.previous().toString();
        actionIterator.next();
        return ret;
    }

    public void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
        isPaused = false;
        pausedCallback = null;
        skipCallback = null;
        ctx.stopped = true;
        ctx = null;
        actionIterator = null;
    }

    private Runnable pausedCallback;
    private boolean firstPausedActionDone;

    public void pause(Runnable cb) {
        if (!isRunning) {
            cb.run();
            return;
        }
        if (isPaused) {
            cb.run();
            return;
        }
        isPaused = true;
        ctx.skipping = true;
        isRunning = false;
        pausedCallback = cb;
        skipCallback = null;
        firstPausedActionDone = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
