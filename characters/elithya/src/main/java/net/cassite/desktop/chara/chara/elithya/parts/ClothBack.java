// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;

public class ClothBack extends AbstractPart {
    public ClothBack(Group parent) {
        super(parent);

        Static clothBack = new Static("static/033_cloth_back.PNG");
        clothBack.addTo(root);
    }
}
