// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.scene.image.ImageView;
import net.cassite.desktop.chara.manager.ImageManager;
import net.cassite.desktop.chara.util.XImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Anima {
    public static final double DEFAULT_FPS = 45;

    private final XImage defaultImage;
    private final List<XImage> animationImages;
    private final ImageView imageView;
    private final FrameBasedAnimationHelper helper;

    public Anima(String defaultImage, String... animationImages) {
        this.defaultImage = ImageManager.load(defaultImage);

        List<XImage> images = new ArrayList<>(animationImages.length);
        for (String name : animationImages) {
            images.add(ImageManager.load(name));
        }
        this.animationImages = Collections.unmodifiableList(images);
        this.imageView = new ImageView();

        this.helper = new FrameBasedAnimationHelper(
            animationImages.length,
            this::update);

        init();
    }

    private void init() {
        this.imageView.setSmooth(true);
        this.imageView.setLayoutX(0);
        this.imageView.setLayoutY(0);
        resetTo(-1);
    }

    public Anima resetTo(int frame) {
        helper.resetTo(frame);
        if (frame == -1) {
            setImage(defaultImage);
        } else {
            setImage(animationImages.get(frame));
        }
        return this;
    }

    private void setImage(XImage image) {
        this.imageView.setImage(image.image);
        this.imageView.setFitWidth(image.getWidth());
        this.imageView.setFitHeight(image.getHeight());
        this.imageView.setX(image.x);
        this.imageView.setY(image.y);
    }

    public boolean isPlaying() {
        return helper.isPlaying();
    }

    public Anima setPauseCallbackOnce(Runnable pauseCallbackOnce) {
        helper.setPauseCallbackOnce(pauseCallbackOnce);
        return this;
    }

    public void play(double fps) {
        helper.play(fps);
    }

    public void play() {
        double fps = helper.getFps();
        if (fps == 0) {
            fps = DEFAULT_FPS;
        }
        helper.play(fps);
    }

    public void pause() {
        helper.pause();
    }

    private void update(int frames) {
        setImage(animationImages.get(frames));
    }

    public void addTo(Div div) {
        div.getChildren().add(this.imageView);
    }

    public void removeFrom(Div div) {
        div.getChildren().remove(this.imageView);
    }

    public Anima setEndFrame(int endFrame) {
        helper.setEndFrame(endFrame);
        return this;
    }
}
