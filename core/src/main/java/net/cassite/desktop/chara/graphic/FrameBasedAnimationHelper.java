// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.application.Platform;

public class FrameBasedAnimationHelper implements Updatable {
    public interface Update {
        void update(int frame);
    }

    private double fps;
    private final int totalFrameCount;
    private final Update updateFunc;
    private boolean playing;
    private int currentFrame;
    private int endFrame;
    private long lastUpdateMillis;

    private Runnable pauseCallbackOnce;

    public FrameBasedAnimationHelper(int totalFrameCount, Update updateFunc) {
        this.totalFrameCount = totalFrameCount;
        this.updateFunc = updateFunc;
        resetTo(-1);
    }

    public boolean isPlaying() {
        return playing;
    }

    public double getFps() {
        return fps;
    }

    public int getTotalFrameCount() {
        return totalFrameCount;
    }

    public FrameBasedAnimationHelper resetTo(int frame) {
        pauseCallbackOnce = null;
        pause();
        this.endFrame = totalFrameCount - 1;
        this.lastUpdateMillis = -1;
        if (frame == -1) {
            this.currentFrame = 0;
        } else {
            this.currentFrame = frame;
        }

        return this;
    }

    public FrameBasedAnimationHelper setPauseCallbackOnce(Runnable pauseCallbackOnce) {
        this.pauseCallbackOnce = pauseCallbackOnce;
        return this;
    }

    public FrameBasedAnimationHelper setEndFrame(int endFrame) {
        this.endFrame = endFrame;
        return this;
    }

    public void play(double fps) {
        if (this.currentFrame == this.endFrame) {
            var endFrame = this.endFrame;
            var pauseCallbackOnce = this.pauseCallbackOnce;
            resetTo(0);
            this.endFrame = endFrame;
            this.pauseCallbackOnce = pauseCallbackOnce;
        }
        this.fps = fps;
        this.playing = true;
        HZ.get().register(this);
    }

    public void pause() {
        playing = false;
        HZ.get().deregister(this);

        // run callback (once)
        var pauseCallbackLocal = this.pauseCallbackOnce;
        this.pauseCallbackOnce = null;
        if (pauseCallbackLocal != null) {
            Platform.runLater(pauseCallbackLocal);
        }
    }

    @Override
    public void update(long current) {
        if (!playing) {
            // no need to update when it's not playing
            return;
        }
        int frames = 0;
        if (lastUpdateMillis != -1) {
            long delta = current - lastUpdateMillis;
            frames = (int) (delta / (1000 / fps));
            if (frames == 0) {
                return;
            }
        }

        // update

        lastUpdateMillis = current;

        frames = currentFrame + frames;
        if (frames > endFrame) {
            frames = endFrame;
        }
        currentFrame = frames;
        updateFunc.update(frames);

        if (currentFrame == endFrame) {
            pause();
        }
    }
}
