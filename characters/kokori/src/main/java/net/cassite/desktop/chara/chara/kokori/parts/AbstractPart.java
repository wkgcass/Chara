// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Div;

public class AbstractPart {
    protected final Div root = new Div();

    public AbstractPart(Group parent) {
        parent.getChildren().add(root);
    }

    public Div getRoot() {
        return root;
    }
}
