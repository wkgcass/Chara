// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;

public class NeckBreast extends AbstractPart {
    public NeckBreast(Group parent) {
        super(parent);

        Static neckBreast = new Static("static/025_neck_breast.PNG");
        neckBreast.addTo(root);
    }
}
