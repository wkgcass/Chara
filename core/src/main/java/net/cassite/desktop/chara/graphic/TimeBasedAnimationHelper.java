// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.application.Platform;

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

    public TimeBasedAnimationHelper(int duration, Update updateFunc) {
        this(duration, 0, updateFunc);
    }

    public TimeBasedAnimationHelper(int duration, int skip, Update updateFunc) {
        this.duration = duration;
        this.updateFunc = updateFunc;
        this.skip = skip;
        reset();
    }

    public TimeBasedAnimationHelper setFinishCallback(Runnable finishCallback) {
        this.finishCallback = finishCallback;
        return this;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    private void reset() {
        playing = false;
        startTimestamp = -1;
        HZ.get().deregister(this);
    }

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

    public boolean isPlaying() {
        return playing;
    }
}
