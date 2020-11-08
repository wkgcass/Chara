// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Div;

public class AbstractPart {
    protected final Div root = new Div();
    private final Group parent;

    public AbstractPart(Group parent) {
        this.parent = parent;
        parent.getChildren().add(root);
    }

    public Div getRoot() {
        return root;
    }

    public void reposition(boolean above, AbstractPart part) {
        parent.getChildren().remove(root);
        int index = 0;
        boolean found = false;
        if (part != null) {
            for (var node : parent.getChildren()) {
                if (node == part.root) {
                    found = true;
                    break;
                }
                ++index;
            }
        }
        if (!found) {
            if (above) {
                parent.getChildren().add(root);
            } else {
                parent.getChildren().add(0, root);
            }
            return;
        }
        if (above) {
            index += 1;
        }
        parent.getChildren().add(index, root);
    }
}
