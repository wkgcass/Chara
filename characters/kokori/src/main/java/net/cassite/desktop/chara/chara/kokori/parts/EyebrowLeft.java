// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;

public class EyebrowLeft extends AbstractPart implements Eyebrow {
    public EyebrowLeft(Group parent) {
        super(parent);
        Static brow = new Static("static/002_eye_bow_left.PNG");
        brow.addTo(root);
    }
}
