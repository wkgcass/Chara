// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Div;

/**
 * Part of a character, e.g. hand, arm, leg, etc...
 */
public class AbstractPart {
    /**
     * The root group to add things into
     */
    protected final Div root = new Div();
    private final Group parent;

    /**
     * Construct the part object with the parent group
     *
     * @param parent parent group
     */
    public AbstractPart(Group parent) {
        this.parent = parent;
        parent.getChildren().add(root);
    }

    /**
     * Get the root group of this part
     *
     * @return root group of this part
     */
    public Div getRoot() {
        return root;
    }

    /**
     * Reposition this part, in other words, move this part above or below another part
     *
     * @param above true to move above the specified part, false below
     * @param part  maybe null to move to the front or to the back, otherwise move to neighbor of the specified part
     */
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
