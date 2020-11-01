// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.application.Platform;
import net.cassite.desktop.chara.ThreadUtils;

/**
 * A helper for framed based animation.<br>
 * You may set the fps and the callback function will tell you which frame you need to show.
 */
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

    /**
     * Constructor
     *
     * @param totalFrameCount how many frames the animation has
     * @param updateFunc      the callback function
     */
    public FrameBasedAnimationHelper(int totalFrameCount, Update updateFunc) {
        this.totalFrameCount = totalFrameCount;
        this.updateFunc = updateFunc;
        resetTo(-1);
    }

    /**
     * Check whether it's animating
     *
     * @return true if playing, false otherwise
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Get current playing fps
     *
     * @return fps
     */
    public double getFps() {
        return fps;
    }

    /**
     * Retrieve the total frame count
     *
     * @return total frame count
     */
    public int getTotalFrameCount() {
        return totalFrameCount;
    }

    /**
     * Pause and reset current frame to specified frame.<br>
     * Note that the <code>pauseCallbackOnce</code> will be cleared and will NOT be called when calling the method.
     *
     * @param frame the frame index to be set, starting at 0, if specified as -1, it will be set to 0 instead
     * @return <code>this</code>
     */
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

    /**
     * Set the callback function which will be called when the animation is paused.<br>
     * When the callback is called, it will be cleared from this helper instance
     *
     * @param pauseCallbackOnce the callback function
     * @return <code>this</code>
     */
    public FrameBasedAnimationHelper setPauseCallbackOnce(Runnable pauseCallbackOnce) {
        this.pauseCallbackOnce = pauseCallbackOnce;
        return this;
    }

    /**
     * Set the animation terminating frame
     *
     * @param endFrame frame index starting at 0
     * @return <code>this</code>
     */
    public FrameBasedAnimationHelper setEndFrame(int endFrame) {
        this.endFrame = endFrame;
        return this;
    }

    /**
     * Set fps of this object
     *
     * @param fps fps
     * @return <code>this</code>
     */
    public FrameBasedAnimationHelper setFps(double fps) {
        this.fps = fps;
        return this;
    }

    /**
     * Start to play with specified fps. If it's already started, the fps will be reset to the specified value.
     *
     * @param fps fps
     */
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

    /**
     * Pause the animation, and call <code>pauseCallbackOnce</code> if exists, then it will be cleared.
     *
     * @return <code>this</code>
     */
    public FrameBasedAnimationHelper pause() {
        playing = false;
        HZ.get().deregister(this);

        // run callback (once)
        var pauseCallbackLocal = this.pauseCallbackOnce;
        this.pauseCallbackOnce = null;
        if (pauseCallbackLocal != null) {
            ThreadUtils.get().runOnFX(pauseCallbackLocal);
        }
        return this;
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
