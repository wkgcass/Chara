// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Static;

public class LegRight extends AbstractPart {
    public LegRight(Group parent) {
        super(parent);

        Static shoeFront = new Static("static/032_shoe_right_front.PNG");
        Static footLeft = new Static("static/033_foot_right.PNG");
        Static legLeft = new Static("static/034_leg_right.PNG");
        Static shoeBack = new Static("static/035_shoe_right_back.PNG");

        shoeBack.addTo(root);
        legLeft.addTo(root);
        footLeft.addTo(root);
        shoeFront.addTo(root);
    }
}
