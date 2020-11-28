// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Static;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaConsts;

public class EyeLeft extends AbstractEye {
    private final Static eye;

    public EyeLeft(Group parent, ElithyaConsts elithyaConsts) {
        super(parent, new EyeConf()
            .setBoundMinX(elithyaConsts.eyeTrackBoundMinX)
            .setBoundMaxX(elithyaConsts.eyeTrackBoundMaxX)
            .setBoundMinY(elithyaConsts.eyeTrackBoundMinY)
            .setBoundMaxY(elithyaConsts.eyeTrackBoundMaxY)
            .setOriginalX(elithyaConsts.eyeLeftOriginalX)
            .setOriginalY(elithyaConsts.eyeLeftOriginalY)
            .setInitX(elithyaConsts.eyeLeftInitX)
            .setInitY(elithyaConsts.eyeLeftInitY)
            .setLeftX(elithyaConsts.eyeLeftLeftX)
            .setLeftY(elithyaConsts.eyeLeftLeftY)
            .setRightX(elithyaConsts.eyeLeftRightX)
            .setRightY(elithyaConsts.eyeLeftRightY)
            .setMinY(elithyaConsts.eyeLeftMinY)
            .setMaxY(elithyaConsts.eyeLeftMaxY)
            .setMiddleHoleMinX(elithyaConsts.eyeRightInitX)
            .setMiddleHoleMaxX(elithyaConsts.eyeLeftInitX)
        );

        eye = new Static("static/014_eye_left.PNG");
        eye.addTo(root);
    }

    @Override
    protected Static getEye() {
        return eye;
    }
}
