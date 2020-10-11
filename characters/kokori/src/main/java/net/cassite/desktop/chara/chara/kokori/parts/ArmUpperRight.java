// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Static;

public class ArmUpperRight extends AbstractPart{
    public ArmUpperRight(Group parent) {
        super(parent);

        Static upper = new Static("static/037_arm_upper_right.PNG");
        upper.addTo(root);
    }
}
