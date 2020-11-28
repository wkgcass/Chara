// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.util;

import net.cassite.desktop.chara.graphic.Div;
import net.cassite.desktop.chara.graphic.FrameBasedAnimationHelper;

import java.util.Arrays;
import java.util.List;

public class JoinedLoopingAnima {
    private final List<LoopingAnima> animaList;

    private final FrameBasedAnimationHelper startAnimation;
    private final FrameBasedAnimationHelper loopAnimation;
    private final FrameBasedAnimationHelper stopAnimation;

    public JoinedLoopingAnima(int startFrames, int loopFrames, int stopFrames, LoopingAnima... loopingAnimas) {
        animaList = Arrays.asList(loopingAnimas);

        startAnimation = new FrameBasedAnimationHelper(startFrames, this::animateStart);
        loopAnimation = new FrameBasedAnimationHelper(loopFrames, this::animateLoop);
        stopAnimation = new FrameBasedAnimationHelper(stopFrames, this::animateStop);
    }

    private void animateStart(int frame) {
        for (var a : animaList) {
            a.start.resetTo(frame);
        }
    }

    private void animateLoop(int frame) {
        for (var a : animaList) {
            a.loop.resetTo(frame);
        }
    }

    private void animateStop(int frame) {
        for (var a : animaList) {
            a.stop.resetTo(frame);
        }
    }

    private boolean isStarted = false;
    private boolean isPlaying = false;

    public void play(Runnable cb) {
        if (isPlaying) {
            return;
        }
        isPlaying = true;
        if (isStarted) {
            loop(cb);
            return;
        }
        isStarted = true;
        resetStart();
        startAnimation.resetTo(-1).setPauseCallbackOnce(() -> {
            startAnimation.resetTo(-1);
            resetStart();
            removeStart();
            addLoop();
            loop(cb);
        }).play(startAnimation.getFps());
    }

    private void loop(Runnable cb) {
        resetLoop();
        loopAnimation.resetTo(-1).setPauseCallbackOnce(() -> {
            resetLoop();
            isPlaying = false;
            cb.run();
        }).play(loopAnimation.getFps());
    }

    public void stop() {
        if (isPlaying) {
            return;
        }
        isPlaying = true;
        removeLoop();
        addStop();
        resetStop();
        stopAnimation.resetTo(-1).setPauseCallbackOnce(() -> {
            stopAnimation.resetTo(-1);
            resetStop();
            removeStop();
            addStart();
            isPlaying = false;
            isStarted = false;
        }).play(stopAnimation.getFps());
    }

    private void resetStart() {
        for (var a : animaList) {
            a.start.resetTo(-1);
        }
    }

    private void resetLoop() {
        for (var a : animaList) {
            a.loop.resetTo(-1);
        }
    }

    private void resetStop() {
        for (var a : animaList) {
            a.stop.resetTo(-1);
        }
    }

    private void addStart() {
        for (var a : animaList) {
            a.start.addTo(a.parent);
        }
    }

    private void addLoop() {
        for (var a : animaList) {
            a.loop.addTo(a.parent);
        }
    }

    private void addStop() {
        for (var a : animaList) {
            a.stop.addTo(a.parent);
        }
    }

    private void removeStart() {
        for (var a : animaList) {
            a.start.removeFrom(a.parent);
        }
    }

    private void removeLoop() {
        for (var a : animaList) {
            a.loop.removeFrom(a.parent);
        }
    }

    private void removeStop() {
        for (var a : animaList) {
            a.stop.removeFrom(a.parent);
        }
    }

    public void setFps(double fps) {
        startAnimation.setFps(fps);
        loopAnimation.setFps(fps);
        stopAnimation.setFps(fps);
        for (var a : animaList) {
            a.setFps(fps);
        }
    }

    public void addTo(Div parent) {
        for (var a : animaList) {
            parent.getChildren().add(a.parent);
        }
    }

    public void removeFrom(Div parent) {
        for (var a : animaList) {
            parent.getChildren().remove(a.parent);
        }
    }
}
