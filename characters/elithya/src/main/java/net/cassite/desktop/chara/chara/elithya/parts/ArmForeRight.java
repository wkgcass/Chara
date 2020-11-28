// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;

public class ArmForeRight extends AbstractPart {
    public ArmForeRight(Group parent) {
        super(parent);

        Static armForeRight = new Static("static/036_arm_fore_right.PNG");
        armForeRight.addTo(root);
    }
}
