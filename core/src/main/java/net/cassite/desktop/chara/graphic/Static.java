// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import net.cassite.desktop.chara.manager.ImageManager;
import net.cassite.desktop.chara.util.XImage;

public class Static {
    private final ImageView imageView = new ImageView();
    private final XImage img;
    private double resizeRatioX = 1;
    private double resizeRatioY = 1;
    private double deltaX = 0;
    private double deltaY = 0;

    public Static(String imageName) {
        img = ImageManager.load(imageName);
        assert img != null;

        imageView.setSmooth(true);
        imageView.setImage(img.image);
        resetPosition();
    }

    public void addTo(Group group) {
        group.getChildren().add(this.imageView);
    }

    public void removeFrom(Group group) {
        group.getChildren().remove(this.imageView);
    }

    public void setPosition(double x, double y) {
        this.deltaX = x;
        this.deltaY = y;
        resetPosition();
    }

    public void resize(double ratio) {
        this.resizeRatioX = ratio;
        this.resizeRatioY = ratio;
        resetPosition();
    }

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

    public double getResizeRatioX() {
        return resizeRatioX;
    }

    public double getResizeRatioY() {
        return resizeRatioY;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }
}
