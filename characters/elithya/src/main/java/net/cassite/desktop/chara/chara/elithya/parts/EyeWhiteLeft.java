// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;

public class EyeWhiteLeft extends AbstractPart {
    public EyeWhiteLeft(Group parent) {
        super(parent);

        Static white = new Static("static/016_eye_white_left.PNG");
        white.addTo(root);
    }
}
