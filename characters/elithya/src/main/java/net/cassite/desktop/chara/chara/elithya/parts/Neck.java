// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;

public class Neck extends AbstractPart {
    public Neck(Group parent) {
        super(parent);

        Static neck = new Static("static/024_neck.PNG");
        neck.addTo(root);
    }
}
