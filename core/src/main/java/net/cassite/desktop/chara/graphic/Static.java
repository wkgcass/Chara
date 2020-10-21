// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.scene.image.ImageView;
import net.cassite.desktop.chara.manager.ImageManager;
import net.cassite.desktop.chara.util.XImage;

/**
 * Encapsulation for an image and provides transformation and movement for it.
 */
public class Static {
    private final ImageView imageView = new ImageView();
    private final XImage img;
    private double resizeRatioX = 1;
    private double resizeRatioY = 1;
    private double deltaX = 0;
    private double deltaY = 0;

    /**
     * Constructor
     *
     * @param imageName the image entry name in the model of the default shown image.
     *                  the name will be passed to {@link ImageManager#load(String)},
     *                  so the name will be prepended with the model name.
     */
    public Static(String imageName) {
        img = ImageManager.load(imageName);
        assert img != null;

        imageView.setSmooth(true);
        imageView.setImage(img.image);
        resetPosition();
    }

    /**
     * Add this instance to JavaFX
     *
     * @param div a <code>Group</code> object
     */
    public void addTo(Div div) {
        div.getChildren().add(this.imageView);
    }

    /**
     * Add this instance to JavaFX
     *
     * @param div a <code>Group</code> object
     */
    public void removeFrom(Div div) {
        div.getChildren().remove(this.imageView);
    }

    /**
     * Set the delta position relative to the original one
     *
     * @param x delta x
     * @param y delta y
     */
    public void setPosition(double x, double y) {
        this.deltaX = x;
        this.deltaY = y;
        resetPosition();
    }

    /**
     * Set the resize ratio. The resize transformation will be applied at the center of the image
     *
     * @param ratio resize ratio
     */
    public void resize(double ratio) {
        this.resizeRatioX = ratio;
        this.resizeRatioY = ratio;
        resetPosition();
    }

    /**
     * Set the resize ratio on Y direction. The resize transformation will be applied at the center of the image.
     *
     * @param ratio y resize ratio
     */
    public void resizeY(double ratio) {
        this.resizeRatioY = ratio;
        resetPosition();
    }

    private void resetPosition() {
        var w = img.getWidth();
        var h = img.getHeight();
        imageView.setX(img.x + deltaX + (w - w * resizeRatioX) / 2);
        imageView.setY(img.y + deltaY + (h - h * resizeRatioY) / 2);
        imageView.setFitWidth(w * resizeRatioX);
        imageView.setFitHeight(h * resizeRatioY);
    }

    /**
     * Get resize ratio on X direction
     *
     * @return resizeRatioX
     */
    public double getResizeRatioX() {
        return resizeRatioX;
    }

    /**
     * Get resize ratio on Y direction
     *
     * @return resizeRatioY
     */
    public double getResizeRatioY() {
        return resizeRatioY;
    }

    /**
     * Get moved length on X direction
     *
     * @return deltaX
     */
    public double getDeltaX() {
        return deltaX;
    }

    /**
     * Get moved length on Y direction
     *
     * @return deltaY
     */
    public double getDeltaY() {
        return deltaY;
    }
}
