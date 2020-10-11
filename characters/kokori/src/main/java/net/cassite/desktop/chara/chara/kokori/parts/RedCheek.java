// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Static;

public class RedCheek extends AbstractPart {
    private final Static cheek;

    private boolean visible = false;

    public RedCheek(Group parent) {
        super(parent);
        cheek = new Static("static/007_z05_cheek.PNG");
    }

    public boolean isVisible() {
        return visible;
    }

    public void show() {
        if (visible) {
            return;
        }
        visible = true;
        cheek.addTo(root);
    }

    public void hide() {
        if (!visible) {
            return;
        }
        visible = false;
        cheek.removeFrom(root);
    }
}
