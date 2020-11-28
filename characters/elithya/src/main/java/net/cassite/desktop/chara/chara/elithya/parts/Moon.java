// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;
import net.cassite.desktop.chara.graphic.TimeBasedAnimationHelper;

public class Moon extends AbstractPart {
    private final Static moon;
    private final GaussianBlur blur;

    public Moon(Group parent) {
        super(parent);

        moon = new Static("static/046_moon.PNG");
        blur = new GaussianBlur(0);
        root.setEffect(blur);
    }

    private boolean isShown = false;
    private final TimeBasedAnimationHelper blurAnimation = new TimeBasedAnimationHelper(500, this::updateBlur);
    private double blurRadiusBegin;
    private double blurRadiusEnd;
    private double opacityBegin;
    private double opacityEnd;

    public boolean isShown() {
        return isShown;
    }

    public void show() {
        if (isShown) {
            return;
        }
        isShown = true;
        if (blurAnimation.isPlaying()) {
            blurAnimation.setFinishCallback(null);
        } else {
            moon.addTo(root);
        }

        blurRadiusEnd = 0;
        opacityEnd = 1;
        playBlur();
    }

    public void hide() {
        if (!isShown) {
            blur.setRadius(100);
            root.setOpacity(0);
            return;
        }
        isShown = false;

        blurRadiusEnd = 100;
        opacityEnd = 0;
        blurAnimation.setFinishCallbackOnce(() -> moon.removeFrom(root));
        playBlur();
    }

    private void playBlur() {
        blurRadiusBegin = blur.getRadius();
        opacityBegin = root.getOpacity();
        blurAnimation.play();
    }

    private void updateBlur(double percentage) {
        blur.setRadius((blurRadiusEnd - blurRadiusBegin) * percentage + blurRadiusBegin);
        root.setOpacity((opacityEnd - opacityBegin) * percentage + opacityBegin);
    }
}
