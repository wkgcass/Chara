// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.control;

import javafx.scene.robot.Robot;
import net.cassite.desktop.chara.graphic.HZ;
import net.cassite.desktop.chara.graphic.TimeBasedAnimationHelper;
import net.cassite.desktop.chara.graphic.Updatable;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;

import java.util.Arrays;
import java.util.List;

public class SleepIndicator {
    private final Robot robot;
    private final List<SleepIndicatorStage> stages;
    private boolean isShown = false;
    private boolean enabled = false;

    public SleepIndicator(Robot robot, FgoClickConsts consts) {
        this.robot = robot;

        var sin = Math.sin(Math.PI / 3) * consts.sleepIndicatorRadius;
        var cos = Math.cos(Math.PI / 3) * consts.sleepIndicatorRadius;
        var r = consts.sleepIndicatorRadius;

        var stage0to60 = new SleepIndicatorStage(consts,
            0, -r,
            sin, r - cos);
        var stage60to120 = new SleepIndicatorStage(consts,
            cos, -sin,
            r - cos, 2 * sin);
        var stage120to180 = new SleepIndicatorStage(consts,
            0, cos,
            sin, r - cos);
        var stage180to240 = new SleepIndicatorStage(consts,
            -sin, cos,
            sin, r - cos);
        var stage240to300 = new SleepIndicatorStage(consts,
            -r, -sin,
            r - cos, 2 * sin);
        var stage300to360 = new SleepIndicatorStage(consts,
            -sin, -r,
            sin, r - cos);
        stages = Arrays.asList(stage0to60, stage60to120, stage120to180, stage180to240, stage240to300, stage300to360);
    }

    private final TimeBasedAnimationHelper animation = new TimeBasedAnimationHelper(0, this::update).setFinishCallback(this::finish);
    private final Updatable updatePosition = this::updatePosition;

    public void show(int millis) {
        if (!enabled) {
            return;
        }
        for (SleepIndicatorStage s : stages) {
            s.setDegree(360);
        }
        animation.setDuration(millis);
        animation.play();

        if (!isShown) {
            for (SleepIndicatorStage stage : stages) {
                stage.doShow();
            }
            HZ.get().register(updatePosition);
        }
        isShown = true;
    }

    private double lastMouseX = 0;
    private double lastMouseY = 0;

    private void updatePosition(long ts) {
        double x = robot.getMouseX();
        double y = robot.getMouseY();
        if (x == lastMouseX && y == lastMouseY) {
            return;
        }
        lastMouseX = x;
        lastMouseY = y;
        for (SleepIndicatorStage s : stages) {
            s.setPosition(x, y);
        }
    }

    private void update(double percentage) {
        for (SleepIndicatorStage s : stages) {
            s.setDegree(360 * (1 - percentage));
        }
    }

    private void finish() {
        hide();
    }

    public void hide() {
        if (!isShown) {
            return;
        }
        isShown = false;
        for (SleepIndicatorStage stage : stages) {
            stage.hide();
        }
        HZ.get().deregister(updatePosition);
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        if (!enabled) {
            return;
        }
        enabled = false;
        hide();
    }
}
