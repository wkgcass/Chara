// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;

public class HandRight extends AbstractPart {
    public HandRight(Group parent) {
        super(parent);

        Static hand = new Static("static/035_hand_right.PNG");
        hand.addTo(root);
    }
}
