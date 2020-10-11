// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.graphic.Static;
import net.cassite.desktop.chara.util.Utils;

public class HandRight extends AbstractPart {
    private final Static arrow;
    private boolean arrowVisible = false;
    private final Anima rune;
    private boolean runeVisible = false;

    public HandRight(Group parent) {
        super(parent);

        Static hand = new Static("static/021_hand_right.PNG");
        hand.addTo(root);

        arrow = new Static("static/019_arrow_in_hand.PNG");
        rune = new Anima("animation/rune/rune_000.png",
            Utils.buildSeqNames("animation/rune/rune_", 0, 40, ".png"));
    }

    public void showArrow() {
        if (arrowVisible) {
            return;
        }
        arrowVisible = true;
        arrow.addTo(root);
    }

    public void showRune() {
        if (runeVisible) {
            return;
        }
        runeVisible = true;
        rune.addTo(root);
    }

    public void hideArrow() {
        if (!arrowVisible) {
            return;
        }
        arrowVisible = false;
        arrow.removeFrom(root);
    }

    public void hideRune() {
        if (!runeVisible) {
            return;
        }
        runeVisible = false;
        rune.removeFrom(root);
    }

    public boolean arrowIsVisible() {
        return arrowVisible;
    }

    public boolean runeIsVisible() {
        return runeVisible;
    }

    public void runeFlow() {
        rune.play();
    }
}
