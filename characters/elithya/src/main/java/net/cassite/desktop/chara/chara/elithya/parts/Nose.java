// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;

public class Nose extends AbstractPart {
    public Nose(Group parent) {
        super(parent);

        Static nose = new Static("static/008_nose.PNG");
        nose.addTo(root);
    }
}
