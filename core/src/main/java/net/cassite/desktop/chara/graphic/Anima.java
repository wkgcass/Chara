// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.scene.image.ImageView;
import net.cassite.desktop.chara.manager.ImageManager;
import net.cassite.desktop.chara.util.XImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulation for animation<br>
 * An <code>Anima</code> instance holds a sequence of images, and you may set them to play at desired fps, or pause at any time.
 */
public class Anima {
    public static final double DEFAULT_FPS = 45;

    private final XImage defaultImage;
    private final List<XImage> animationImages;
    private final ImageView imageView;
    private final FrameBasedAnimationHelper helper;

    /**
     * Constructor
     *
     * @param defaultImage    the image entry name in the model of the default shown image.
     *                        the name will be passed to {@link ImageManager#load(String)},
     *                        so the name will be prepended with the model name.
     * @param animationImages the image entry names in the model of the images to animate.
     *                        the names will be passed to {@link ImageManager#load(String)},
     *                        so the names will be prepended with the model name.
     * @see ImageManager#load(String)
     */
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

    /**
     * Pause and reset to the specified frame. Image will change to the specified frame.<br>
     * Note that the <code>PauseCallbackOnce</code> callback will be removed and will NOT be called when calling this method.
     *
     * @param frame frame index starting from 0, use -1 for default image
     * @return <code>this</code>
     */
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

    /**
     * Check whether it's animating
     *
     * @return true if it's playing, false otherwise
     */
    public boolean isPlaying() {
        return helper.isPlaying();
    }

    /**
     * Set callback function which will be called when it's paused or animates to the final frame.<br>
     * The callback will maximally be called only once.
     *
     * @param pauseCallbackOnce the callback
     * @return <code>this</code>
     */
    public Anima setPauseCallbackOnce(Runnable pauseCallbackOnce) {
        helper.setPauseCallbackOnce(pauseCallbackOnce);
        return this;
    }

    /**
     * Begin to animate. If it's already playing, the fps will be updated.
     *
     * @param fps fps
     */
    public void play(double fps) {
        helper.play(fps);
    }

    /**
     * Same as calling {@link #play(double)}. If it's the first time playing, the fps will be set to {@link #DEFAULT_FPS}
     */
    public void play() {
        double fps = helper.getFps();
        if (fps == 0) {
            fps = DEFAULT_FPS;
        }
        helper.play(fps);
    }

    /**
     * Pause the animation, and the <code>pauseCallbackOnce</code> will be called if exists, then it will be removed.
     *
     * @return <code>this</code>
     */
    public Anima pause() {
        helper.pause();
        return this;
    }

    private void update(int frames) {
        setImage(animationImages.get(frames));
    }

    /**
     * Add the anima instance to JavaFX
     *
     * @param div a <code>Group</code> object
     */
    public void addTo(Div div) {
        div.getChildren().add(this.imageView);
    }

    /**
     * Remove the anima instance from JavaFX
     *
     * @param div a <code>Group</code> object
     */
    public void removeFrom(Div div) {
        div.getChildren().remove(this.imageView);
    }

    /**
     * Set the animation terminating frame
     *
     * @param endFrame frame index starting at 0
     * @return <code>this</code>
     */
    public Anima setEndFrame(int endFrame) {
        helper.setEndFrame(endFrame);
        return this;
    }

    /**
     * Set fps for the animation
     *
     * @param fps fps
     * @return <code>this</code>
     */
    public Anima setFps(double fps) {
        helper.setFps(fps);
        return this;
    }
}
