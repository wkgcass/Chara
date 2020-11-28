// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.join.LanternJoin;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaConsts;
import net.cassite.desktop.chara.util.Utils;

public class ChainFront extends AbstractChainRotate {
    private final Anima chainFront1;
    private final Anima chainFront2;
    private final Anima chainFront3;

    private final Anima chainFrontReverse1;
    private final Anima chainFrontReverse2;
    private final Anima chainFrontReverse3;

    public ChainFront(Group parent, ElithyaConsts elithyaConsts) {
        super(parent, elithyaConsts);

        chainFront1 = new Anima("animation/042_chain_front/chain_front_000.png",
            ElithyaUtils.joinSeqs(
                Utils.buildSeqNames("animation/042_chain_front/chain_front_", 0, 30, ".png"),
                Utils.buildSeqNames("animation/042_chain_front/chain_front_", 40, 60, ".png")
            ))
            .setFps(LanternJoin.beginFps);
        chainFront1.addTo(root);
        chainFront2 = new Anima("animation/042_chain_front/chain_front_010.png",
            Utils.buildSeqNames("animation/042_chain_front/chain_front_", 10, 30, ".png"));
        chainFront2.setFps(LanternJoin.slowFps);
        chainFront3 = new Anima("animation/042_chain_front/chain_front_030.png",
            Utils.buildSeqNames("animation/042_chain_front/chain_front_", 30, 40, ".png"));
        chainFront3.setFps(LanternJoin.finishFps);

        chainFrontReverse1 = new Anima("animation/042_chain_front/chain_front_039.png",
            ElithyaUtils.joinSeqs(
                Utils.buildSeqNamesReverse("animation/042_chain_front/chain_front_", 39, 10, ".png"),
                Utils.buildSeqNamesReverse("animation/042_chain_front/chain_front_", 59, 40, ".png")
            ));
        chainFrontReverse1.setFps(LanternJoin.beginFps);
        chainFrontReverse2 = new Anima("animation/042_chain_front/chain_front_029.png",
            Utils.buildSeqNamesReverse("animation/042_chain_front/chain_front_", 29, 10, ".png"));
        chainFrontReverse2.setFps(LanternJoin.slowFps);
        chainFrontReverse3 = new Anima("animation/042_chain_front/chain_front_009.png",
            Utils.buildSeqNamesReverse("animation/042_chain_front/chain_front_", 9, 0, ".png"));
        chainFrontReverse3.setFps(LanternJoin.finishFps);
    }

    private boolean isPlaying = false;

    @SuppressWarnings("DuplicatedCode")
    private void animate(Anima begin, Anima slow, Anima finish, Runnable cb) {
        chainFront3.removeFrom(root);
        chainFront2.removeFrom(root);
        chainFront1.removeFrom(root);

        chainFrontReverse3.removeFrom(root);
        chainFrontReverse2.removeFrom(root);
        chainFrontReverse1.removeFrom(root);

        ElithyaUtils.smoothAnimation(root, begin, slow, finish, cb);
    }

    @SuppressWarnings("DuplicatedCode")
    public void swingRight(Runnable cb) {
        if (isPlaying) {
            return;
        }
        isPlaying = true;

        Runnable[] callbacks = ElithyaUtils.synchronize(2, () -> {
            isPlaying = false;
            cb.run();
        });
        // bend left
        animate(chainFront1, chainFront2, chainFront3, callbacks[0]);
        // swing right
        swingDirection = -1;
        animateRotate(callbacks[1]);
    }

    @SuppressWarnings("DuplicatedCode")
    public void swingLeft(Runnable cb) {
        if (isPlaying) {
            return;
        }
        isPlaying = true;

        Runnable[] callbacks = ElithyaUtils.synchronize(2, () -> {
            isPlaying = false;
            cb.run();
        });
        // bend right
        animate(chainFrontReverse1, chainFrontReverse2, chainFrontReverse3, callbacks[0]);
        // swing left
        swingDirection = 1;
        animateRotate(callbacks[1]);
    }
}
