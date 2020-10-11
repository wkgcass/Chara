// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.kokori.Kokori;
import net.cassite.desktop.chara.graphic.Static;

public class EyeRight extends AbstractEye implements Eye {
    private boolean highlightVisible = true;

    public EyeRight(Group parent) {
        super(parent, new Static("static/011_eye_highlight_right.PNG"), new Static("static/012_eye_right.PNG"));
        Static white = new Static("static/013_eye_white_right.PNG");

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
        if (x < Kokori.CharaConsts.EYE_TRACK_X_MIN) {
            x = Kokori.CharaConsts.EYE_TRACK_X_MIN;
        }
        if (x > Kokori.CharaConsts.EYE_TRACK_X_MAX) {
            x = Kokori.CharaConsts.EYE_TRACK_X_MAX;
        }
        if (y < Kokori.CharaConsts.EYE_TRACK_Y_MIN) {
            y = Kokori.CharaConsts.EYE_TRACK_Y_MIN;
        }
        if (y > Kokori.CharaConsts.EYE_TRACK_Y_MAX) {
            y = Kokori.CharaConsts.EYE_TRACK_Y_MAX;
        }

        double deltaX;
        if (Kokori.CharaConsts.EYE_RIGHT_ORIGINAL_X <= x && x <= Kokori.CharaConsts.EYE_LEFT_ORIGINAL_X) {
            deltaX = 0;
        } else {
            if (x < Kokori.CharaConsts.EYE_RIGHT_ORIGINAL_X) {
                double total = Kokori.CharaConsts.EYE_RIGHT_ORIGINAL_X - Kokori.CharaConsts.EYE_TRACK_X_MIN;
                double ratio = (Kokori.CharaConsts.EYE_RIGHT_ORIGINAL_X - x) / total;
                deltaX = -(ratio * (Kokori.CharaConsts.EYE_RIGHT_ORIGINAL_X - Kokori.CharaConsts.EYE_RIGHT_X_MIN));
            } else {
                assert x > Kokori.CharaConsts.EYE_LEFT_ORIGINAL_X;
                double total = Kokori.CharaConsts.EYE_TRACK_X_MAX - Kokori.CharaConsts.EYE_LEFT_ORIGINAL_X;
                double ratio = (x - Kokori.CharaConsts.EYE_LEFT_ORIGINAL_X) / total;
                deltaX = ratio * (Kokori.CharaConsts.EYE_RIGHT_X_MAX - Kokori.CharaConsts.EYE_RIGHT_ORIGINAL_X);
            }
        }
        double deltaY;
        {
            if (y < Kokori.CharaConsts.EYE_RIGHT_ORIGINAL_Y) {
                double total = Kokori.CharaConsts.EYE_RIGHT_ORIGINAL_Y - Kokori.CharaConsts.EYE_TRACK_Y_MIN;
                double ratio = (Kokori.CharaConsts.EYE_RIGHT_ORIGINAL_Y - y) / total;
                deltaY = -(ratio * (Kokori.CharaConsts.EYE_RIGHT_ORIGINAL_Y - Kokori.CharaConsts.EYE_RIGHT_Y_MIN));
            } else {
                double total = Kokori.CharaConsts.EYE_TRACK_Y_MAX - Kokori.CharaConsts.EYE_RIGHT_ORIGINAL_Y;
                double ratio = (y - Kokori.CharaConsts.EYE_RIGHT_ORIGINAL_Y) / total;
                deltaY = ratio * (Kokori.CharaConsts.EYE_RIGHT_Y_MAX - Kokori.CharaConsts.EYE_RIGHT_ORIGINAL_Y);
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
}
