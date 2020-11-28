// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;

public class ArmUpperRight extends AbstractPart {
    public ArmUpperRight(Group parent) {
        super(parent);

        Static armUpperRight = new Static("static/038_arm_upper_right.PNG");
        armUpperRight.addTo(root);
    }
}
