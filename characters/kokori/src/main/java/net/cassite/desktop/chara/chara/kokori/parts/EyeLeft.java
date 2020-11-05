// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Static;
import net.cassite.desktop.chara.model.kokori.KokoriConsts;

public class EyeLeft extends AbstractEye implements Eye {
    private final KokoriConsts kokoriConsts;
    private boolean highlightVisible = true;

    public EyeLeft(KokoriConsts kokoriConsts, Group parent) {
        super(parent, new Static("static/014_eye_high_light_left.PNG"), new Static("static/015_eye_left.PNG"));
        this.kokoriConsts = kokoriConsts;
        Static white = new Static("static/016_eye_white_left.PNG");

        white.addTo(root);
        root.getChildren().add(eyeGroup);
        highlight.addTo(root);
    }

    @Override
    public boolean hasHighlight() {
        return highlightVisible;
    }

    @Override
    public void addHighlight() {
        if (highlightVisible) {
            return;
        }
        highlightVisible = true;
        highlight.addTo(root);
    }

    @Override
    public void removeHighlight() {
        if (!highlightVisible) {
            return;
        }
        highlightVisible = false;
        highlight.removeFrom(root);
    }

    @Override
    public void track(double x, double y) {
        if (x < kokoriConsts.eyeTrackXMin) {
            x = kokoriConsts.eyeTrackXMin;
        }
        if (x > kokoriConsts.eyeTrackXMax) {
            x = kokoriConsts.eyeTrackXMax;
        }
        if (y < kokoriConsts.eyeTrackYMin) {
            y = kokoriConsts.eyeTrackYMin;
        }
        if (y > kokoriConsts.eyeTrackYMax) {
            y = kokoriConsts.eyeTrackYMax;
        }

        double deltaX;
        if (kokoriConsts.eyeRightOriginalX <= x && x <= kokoriConsts.eyeLeftOriginalX) {
            deltaX = 0;
        } else {
            if (x < kokoriConsts.eyeRightOriginalX) {
                double total = kokoriConsts.eyeRightOriginalX - kokoriConsts.eyeTrackXMin;
                double ratio = (kokoriConsts.eyeRightOriginalX - x) / total;
                deltaX = -(ratio * (kokoriConsts.eyeLeftOriginalX - kokoriConsts.eyeLeftXMin));
            } else {
                assert x > kokoriConsts.eyeLeftOriginalX;
                double total = kokoriConsts.eyeTrackXMax - kokoriConsts.eyeLeftOriginalX;
                double ratio = (x - kokoriConsts.eyeLeftOriginalX) / total;
                deltaX = ratio * (kokoriConsts.eyeLeftXMax - kokoriConsts.eyeLeftOriginalX);
            }
        }
        double deltaY;
        {
            if (y < kokoriConsts.eyeLeftOriginalY) {
                double total = kokoriConsts.eyeLeftOriginalY - kokoriConsts.eyeTrackYMin;
                double ratio = (kokoriConsts.eyeLeftOriginalY - y) / total;
                deltaY = -(ratio * (kokoriConsts.eyeLeftOriginalY - kokoriConsts.eyeLeftYMin));
            } else {
                double total = kokoriConsts.eyeTrackYMax - kokoriConsts.eyeLeftOriginalY;
                double ratio = (y - kokoriConsts.eyeLeftOriginalY) / total;
                deltaY = ratio * (kokoriConsts.eyeLeftYMax - kokoriConsts.eyeLeftOriginalY);
            }
        }

        eye.setPosition(deltaX, deltaY);
        highlight.setPosition(deltaX, deltaY);
    }

    @Override
    public void restorePosition() {
        eye.setPosition(0, 0);
        highlight.setPosition(0, 0);
    }

    @Override
    public void move(double relativeX, double relativeY) {
        eye.setPosition(relativeX - kokoriConsts.eyeLeftOriginalX, relativeY - kokoriConsts.eyeLeftOriginalY);
        highlight.setPosition(relativeX - kokoriConsts.eyeLeftOriginalX, relativeY - kokoriConsts.eyeLeftOriginalY);
    }
}
