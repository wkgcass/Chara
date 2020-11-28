// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Static;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaConsts;

public class EyeRight extends AbstractEye {
    private final Static eye;

    public EyeRight(Group parent, ElithyaConsts elithyaConsts) {
        super(parent, new EyeConf()
            .setBoundMinX(elithyaConsts.eyeTrackBoundMinX)
            .setBoundMaxX(elithyaConsts.eyeTrackBoundMaxX)
            .setBoundMinY(elithyaConsts.eyeTrackBoundMinY)
            .setBoundMaxY(elithyaConsts.eyeTrackBoundMaxY)
            .setOriginalX(elithyaConsts.eyeRightOriginalX)
            .setOriginalY(elithyaConsts.eyeRightOriginalY)
            .setInitX(elithyaConsts.eyeRightInitX)
            .setInitY(elithyaConsts.eyeRightInitY)
            .setLeftX(elithyaConsts.eyeRightLeftX)
            .setLeftY(elithyaConsts.eyeRightLeftY)
            .setRightX(elithyaConsts.eyeRightRightX)
            .setRightY(elithyaConsts.eyeRightRightY)
            .setMinY(elithyaConsts.eyeRightMinY)
            .setMaxY(elithyaConsts.eyeRightMaxY)
            .setMiddleHoleMinX(elithyaConsts.eyeRightInitX)
            .setMiddleHoleMaxX(elithyaConsts.eyeLeftInitX)
        );

        eye = new Static("static/015_eye_right.PNG");
        eye.addTo(root);
    }

    @Override
    protected Static getEye() {
        return eye;
    }
}
