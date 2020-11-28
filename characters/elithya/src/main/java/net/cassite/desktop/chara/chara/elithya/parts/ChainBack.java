// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.join.LanternJoin;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaConsts;
import net.cassite.desktop.chara.util.Utils;

public class ChainBack extends AbstractChainRotate {
    private final Anima chainBack1;
    private final Anima chainBack2;
    private final Anima chainBack3;

    private final Anima chainBackReverse1;
    private final Anima chainBackReverse2;
    private final Anima chainBackReverse3;

    public ChainBack(Group parent, ElithyaConsts elithyaConsts) {
        super(parent, elithyaConsts);

        chainBack1 = new Anima("animation/045_chain_back/chain_back_000.png",
            ElithyaUtils.joinSeqs(
                Utils.buildSeqNames("animation/045_chain_back/chain_back_", 0, 30, ".png"),
                Utils.buildSeqNames("animation/045_chain_back/chain_back_", 40, 60, ".png")
            ))
            .setFps(LanternJoin.beginFps);
        chainBack1.addTo(root);
        chainBack2 = new Anima("animation/045_chain_back/chain_back_010.png",
            Utils.buildSeqNames("animation/045_chain_back/chain_back_", 10, 30, ".png"));
        chainBack2.setFps(LanternJoin.slowFps);
        chainBack3 = new Anima("animation/045_chain_back/chain_back_030.png",
            Utils.buildSeqNames("animation/045_chain_back/chain_back_", 30, 40, ".png"));
        chainBack3.setFps(LanternJoin.finishFps);

        chainBackReverse1 = new Anima("animation/045_chain_back/chain_back_039.png",
            ElithyaUtils.joinSeqs(
                Utils.buildSeqNamesReverse("animation/045_chain_back/chain_back_", 39, 10, ".png"),
                Utils.buildSeqNamesReverse("animation/045_chain_back/chain_back_", 59, 40, ".png")
            ));
        chainBackReverse1.setFps(LanternJoin.beginFps);
        chainBackReverse2 = new Anima("animation/045_chain_back/chain_back_029.png",
            Utils.buildSeqNamesReverse("animation/045_chain_back/chain_back_", 29, 10, ".png"));
        chainBackReverse2.setFps(LanternJoin.slowFps);
        chainBackReverse3 = new Anima("animation/045_chain_back/chain_back_009.png",
            Utils.buildSeqNamesReverse("animation/045_chain_back/chain_back_", 9, 0, ".png"));
        chainBackReverse3.setFps(LanternJoin.finishFps);
    }

    private boolean isPlaying = false;

    @SuppressWarnings("DuplicatedCode")
    private void animate(Anima begin, Anima slow, Anima finish, Runnable cb) {
        chainBack3.removeFrom(root);
        chainBack2.removeFrom(root);
        chainBack1.removeFrom(root);

        chainBackReverse3.removeFrom(root);
        chainBackReverse2.removeFrom(root);
        chainBackReverse1.removeFrom(root);

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
        animate(chainBack1, chainBack2, chainBack3, callbacks[0]);
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
        animate(chainBackReverse1, chainBackReverse2, chainBackReverse3, callbacks[0]);
        // swing left
        swingDirection = 1;
        animateRotate(callbacks[1]);
    }
}
