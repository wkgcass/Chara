// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.util;

import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.graphic.Div;

public class LoopingAnima {
    final Div parent = new Div();
    final Anima start;
    final Anima loop;
    final Anima stop;

    public LoopingAnima(Anima start, Anima loop, Anima stop) {
        this.start = start;
        this.loop = loop;
        this.stop = stop;
        start.addTo(parent);
    }

    private boolean isStarted = false;
    private boolean isPlaying = false;

    public void play() {
        play(null);
    }

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
        start.resetTo(-1).setPauseCallbackOnce(() -> {
            start.resetTo(-1);
            start.removeFrom(parent);
            loop.addTo(parent);
            loop(cb);
        }).play();
    }

    private void loop(Runnable cb) {
        loop.resetTo(-1).setPauseCallbackOnce(() -> {
            loop.resetTo(-1);
            if (cb == null) {
                loop(null);
            } else {
                isPlaying = false;
                cb.run();
            }
        }).play();
    }

    public void stop() {
        if (isPlaying) {
            return;
        }
        isPlaying = true;
        loop.removeFrom(parent);
        stop.addTo(parent);
        stop.resetTo(-1).setPauseCallbackOnce(() -> {
            stop.resetTo(-1);
            stop.removeFrom(parent);
            start.addTo(parent);
            isPlaying = false;
            isStarted = false;
        }).play();
    }

    public void setFps(double fps) {
        start.setFps(fps);
        loop.setFps(fps);
        stop.setFps(fps);
    }

    public void addTo(Div parent) {
        parent.getChildren().add(this.parent);
    }

    public void removeFrom(Div parent) {
        parent.getChildren().remove(this.parent);
    }
}
