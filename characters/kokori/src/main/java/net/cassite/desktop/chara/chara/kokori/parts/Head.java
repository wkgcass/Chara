// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;

public class Head extends AbstractPart {
    public Head(Group parent) {
        super(parent);

        Static head = new Static("static/008_head.PNG");
        Static earRight = new Static("static/009_ear_right.PNG");
        Static earLeft = new Static("static/010_ear_left.PNG");

        earLeft.addTo(root);
        earRight.addTo(root);
        head.addTo(root);
    }
}
