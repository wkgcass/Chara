// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Static;

public class EyebrowRight extends AbstractPart implements Eyebrow {
    public EyebrowRight(Group parent) {
        super(parent);
        Static brow = new Static("static/001_eye_bow_right.PNG");
        brow.addTo(root);
    }
}
