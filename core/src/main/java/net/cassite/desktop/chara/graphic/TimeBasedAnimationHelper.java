// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.application.Platform;

/**
 * A helper for time based animation.<br>
 * You may set the animation duration. The callback function will tell you the current percentage.
 */
public class TimeBasedAnimationHelper implements Updatable {
    public interface Update {
        void update(double percentage);
    }

    private int duration;
    private final Update updateFunc;
    private final int skip;
    private boolean playing;
    private long startTimestamp;
    private Runnable finishCallback;

    /**
     * Constructor
     *
     * @param duration   duration in millis
     * @param updateFunc callback function
     */
    public TimeBasedAnimationHelper(int duration, Update updateFunc) {
        this(duration, 0, updateFunc);
    }

    /**
     * Constructor
     *
     * @param duration   duration in millis
     * @param skip       how many events alerted by {@link HZ} can be skipped
     * @param updateFunc callback function
     */
    public TimeBasedAnimationHelper(int duration, int skip, Update updateFunc) {
        this.duration = duration;
        this.updateFunc = updateFunc;
        this.skip = skip;
        reset();
    }

    /**
     * Set the finish callback, which will be called when animation finishes
     *
     * @param finishCallback the callback function
     * @return <code>this</code>
     */
    public TimeBasedAnimationHelper setFinishCallback(Runnable finishCallback) {
        this.finishCallback = finishCallback;
        return this;
    }

    /**
     * Reset the duration
     *
     * @param duration duration in millis
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    private void reset() {
        playing = false;
        startTimestamp = -1;
        HZ.get().deregister(this);
    }

    /**
     * Begin to animate
     */
    public void play() {
        playing = true;
        startTimestamp = -1;
        HZ.get().register(this);
    }

    private int skipped = 0;

    @Override
    public void update(long current) {
        if (!playing) {
            return;
        }
        if (startTimestamp == -1) {
            startTimestamp = current;
        }
        long time = current - startTimestamp;
        double percentage;
        if (time > duration) {
            percentage = 1;
            reset();
        } else {
            percentage = (double) time / duration;
        }
        if (skipped >= skip || !playing) {
            updateFunc.update(percentage);
            skipped = 0;
            if (!playing) {
                if (finishCallback != null) {
                    Platform.runLater(() -> finishCallback.run());
                }
            }
        } else {
            skipped += 1;
        }
    }

    /**
     * Check whether it's animating
     *
     * @return true if is playing, false otherwise
     */
    public boolean isPlaying() {
        return playing;
    }
}
