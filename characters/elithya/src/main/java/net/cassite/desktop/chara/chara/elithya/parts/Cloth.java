// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.util.Utils;

public class Cloth extends AbstractPart {
    private final Anima cloth1;
    private final Anima cloth2;
    private final Anima cloth3;

    private final Anima clothReverse1;
    private final Anima clothReverse2;
    private final Anima clothReverse3;

    public Cloth(Group parent) {
        super(parent);

        cloth1 = new Anima("animation/022_cloth_breast/cloth_breast_000.png",
            ElithyaUtils.joinSeqs(
                Utils.buildSeqNames("animation/022_cloth_breast/cloth_breast_", 0, 45, ".png"),
                Utils.buildSeqNames("animation/022_cloth_breast/cloth_breast_", 60, 90, ".png")
            ))
            .setFps(120);
        cloth1.addTo(root);
        cloth2 = new Anima("animation/022_cloth_breast/cloth_breast_015.png",
            Utils.buildSeqNames("animation/022_cloth_breast/cloth_breast_", 14, 45, ".png"));
        cloth2.setFps(80);
        cloth3 = new Anima("animation/022_cloth_breast/cloth_breast_045.png",
            Utils.buildSeqNames("animation/022_cloth_breast/cloth_breast_", 44, 60, ".png"));
        cloth3.setFps(30);

        clothReverse1 = new Anima("animation/022_cloth_breast/cloth_breast_059.png",
            ElithyaUtils.joinSeqs(
                Utils.buildSeqNamesReverse("animation/022_cloth_breast/cloth_breast_", 59, 15, ".png"),
                Utils.buildSeqNamesReverse("animation/022_cloth_breast/cloth_breast_", 89, 60, ".png")
            ));
        clothReverse1.setFps(120);
        clothReverse2 = new Anima("animation/022_cloth_breast/cloth_breast_044.png",
            Utils.buildSeqNamesReverse("animation/022_cloth_breast/cloth_breast_", 44, 15, ".png"));
        clothReverse2.setFps(80);
        clothReverse3 = new Anima("animation/022_cloth_breast/cloth_breast_014.png",
            Utils.buildSeqNamesReverse("animation/022_cloth_breast/cloth_breast_", 14, 0, ".png"));
        clothReverse3.setFps(30);
    }

    private boolean isPlaying = false;

    @SuppressWarnings("DuplicatedCode")
    private void animate(Anima begin, Anima slow, Anima finish) {
        if (isPlaying) {
            return;
        }
        isPlaying = true;

        cloth3.removeFrom(root);
        cloth2.removeFrom(root);
        cloth1.removeFrom(root);

        clothReverse3.removeFrom(root);
        clothReverse2.removeFrom(root);
        clothReverse1.removeFrom(root);

        ElithyaUtils.smoothAnimation(root, begin, slow, finish, () -> isPlaying = false);
    }

    public void playInitialDown() {
        animate(cloth1, cloth2, cloth3);
    }

    public void playInitialUp() {
        animate(clothReverse1, clothReverse2, clothReverse3);
    }
}
