// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.graphic.AnimaList;
import net.cassite.desktop.chara.graphic.Div;
import net.cassite.desktop.chara.util.Utils;

public class HairSide extends AbstractPart {
    private final AnimaList hairSideLeft;
    private final AnimaList hairSideRight;

    private final Div leftGroup;
    private final Div rightGroup;

    public HairSide(Group parent) {
        super(parent);

        leftGroup = new Div();
        rightGroup = new Div();

        hairSideLeft = new AnimaList(this::swapLeft,
            new Anima("animation/hair_side_left/hair_side_left_000.png",
                Utils.buildSeqNames("animation/hair_side_left/hair_side_left_", 0, 30, ".png")),
            new Anima("animation/hair_side_left/hair_side_left_030.png",
                Utils.buildSeqNames("animation/hair_side_left/hair_side_left_", 30, 40, ".png"))
                .setFps(Anima.DEFAULT_FPS / 2)
        );
        hairSideRight = new AnimaList(this::swapRight,
            new Anima("animation/hair_side_right/hair_side_right_000.png",
                Utils.buildSeqNames("animation/hair_side_right/hair_side_right_", 0, 30, ".png")),
            new Anima("animation/hair_side_right/hair_side_right_030.png",
                Utils.buildSeqNames("animation/hair_side_right/hair_side_right_", 30, 40, ".png"))
                .setFps(Anima.DEFAULT_FPS / 2)
        );

        root.getChildren().add(rightGroup);
        root.getChildren().add(leftGroup);
    }

    private void swapLeft(Anima a, Anima b) {
        if (a != null) {
            a.removeFrom(leftGroup);
        }
        b.addTo(leftGroup);
    }

    private void swapRight(Anima a, Anima b) {
        if (a != null) {
            a.removeFrom(rightGroup);
        }
        b.addTo(rightGroup);
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
