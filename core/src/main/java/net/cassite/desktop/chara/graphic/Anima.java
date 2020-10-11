// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import net.cassite.desktop.chara.manager.ImageManager;
import net.cassite.desktop.chara.util.XImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Anima implements Updatable {
    public static final double DEFAULT_FPS = 45;

    private final XImage defaultImage;
    private final List<XImage> animationImages;

    private final ImageView imageView;

    private boolean playing;
    private double fps;
    private int currentFrame;
    private int endFrame;
    private long lastUpdateMillis;

    private Runnable pauseCallback;

    public Anima(String defaultImage, String... animationImages) {
        this.defaultImage = ImageManager.load(defaultImage);

        List<XImage> images = new ArrayList<>(animationImages.length);
        for (String name : animationImages) {
            images.add(ImageManager.load(name));
        }
        this.animationImages = Collections.unmodifiableList(images);

        this.imageView = new ImageView();

        init();
    }

    private void init() {
        this.imageView.setSmooth(true);
        this.imageView.setLayoutX(0);
        this.imageView.setLayoutY(0);
        reset();
    }

    public void reset() {
        pause();
        this.fps = DEFAULT_FPS;
        this.currentFrame = 0;
        this.endFrame = animationImages.size() - 1;
        this.lastUpdateMillis = -1;
        setImage(defaultImage);
    }

    private void setImage(XImage image) {
        this.imageView.setImage(image.image);
        this.imageView.setFitWidth(image.getWidth());
        this.imageView.setFitHeight(image.getHeight());
        this.imageView.setX(image.x);
        this.imageView.setY(image.y);
    }

    public boolean isPlaying() {
        return playing;
    }

    public void play(double fps, Runnable pauseCallback) {
        if (this.currentFrame == this.endFrame) {
            var endFrame = this.endFrame;
            reset();
            this.endFrame = endFrame;
        }
        this.fps = fps;
        this.playing = true;
        this.pauseCallback = pauseCallback;
        HZ.get().register(this);
    }

    public void play(double fps) {
        play(fps, null);
    }

    public void play() {
        play(this.fps);
    }

    public void pause() {
        playing = false;
        HZ.get().deregister(this);

        // run callback (once)
        var pauseCallbackLocal = this.pauseCallback;
        this.pauseCallback = null;
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
        setImage(animationImages.get(frames));

        if (currentFrame == endFrame) {
            pause();
        }
    }

    public void addTo(Div div) {
        div.getChildren().add(this.imageView);
    }

    public void removeFrom(Div div) {
        div.getChildren().remove(this.imageView);
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public void setEndFrame(int endFrame) {
        this.endFrame = endFrame;
    }
}
