// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Div;
import net.cassite.desktop.chara.graphic.Static;

public abstract class AbstractEye extends AbstractPart implements Eye {
    protected final Div eyeGroup = new Div();
    protected final Static highlight;
    protected final Static eye;

    public AbstractEye(Group parent, Static highlight, Static eye) {
        super(parent);
        this.highlight = highlight;
        this.eye = eye;
        eye.addTo(eyeGroup);
    }

    @Override
    public void zoom(double ratio) {
        if (ratio <= 0.01) {
            ratio = 0.01;
        }
        eye.resize(ratio);
        highlight.resize(ratio);
    }
}
