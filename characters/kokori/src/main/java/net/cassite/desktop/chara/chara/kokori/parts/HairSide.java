// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.graphic.Div;
import net.cassite.desktop.chara.util.Utils;

public class HairSide extends AbstractPart {
    private final Anima hairSideLeft;
    private final Anima hairSideRight;

    private final Div leftGroup;
    private final Div rightGroup;

    public HairSide(Group parent) {
        super(parent);
        hairSideLeft = new Anima("animation/hair_side_left/hair_side_left_000.png",
            Utils.buildSeqNames("animation/hair_side_left/hair_side_left_", 0, 40, ".png"));
        hairSideRight = new Anima("animation/hair_side_right/hair_side_right_000.png",
            Utils.buildSeqNames("animation/hair_side_right/hair_side_right_", 0, 40, ".png"));

        leftGroup = new Div();
        hairSideLeft.addTo(leftGroup);
        rightGroup = new Div();
        hairSideRight.addTo(rightGroup);

        root.getChildren().add(rightGroup);
        root.getChildren().add(leftGroup);
    }

    public void swing() {
        hairSideLeft.play();
        hairSideRight.play();
    }

    public Div getLeftGroup() {
        return leftGroup;
    }

    public Div getRightGroup() {
        return rightGroup;
    }
}
