// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.graphic.Static;
import net.cassite.desktop.chara.graphic.TimeBasedAnimationHelper;
import net.cassite.desktop.chara.model.kokori.KokoriConsts;
import net.cassite.desktop.chara.util.Utils;

public class Mouth extends AbstractPart {
    private final KokoriConsts kokoriConsts;

    private final Anima animaHappyToDefault;
    private final Anima animaDefaultToHappy;
    private final Anima animaDefaultToSad;
    private final Anima animaSadToDefault;
    private final Anima animaHappyToSad;
    private final Anima animaSadToHappy;
    private final Static mouthOpen;
    private final Static mouthLine;
    private int state = 0; // 0: default 1: happy 2: sad 3: open

    private final Rotate rotate;

    public Mouth(KokoriConsts kokoriConsts, Group parent) {
        super(parent);
        this.kokoriConsts = kokoriConsts;

        animaHappyToDefault = new Anima("animation/mouth/mouth_000.png",
            Utils.buildSeqNames("animation/mouth/mouth_", 0, 12, ".png"));
        animaDefaultToHappy = new Anima("animation/mouth/mouth_011.png",
            Utils.buildSeqNamesReverse("animation/mouth/mouth_", 11, 0, ".png"));
        animaDefaultToSad = new Anima("animation/mouth/mouth_011.png",
            Utils.buildSeqNames("animation/mouth/mouth_", 11, 20, ".png"));
        animaSadToDefault = new Anima("animation/mouth/mouth_019.png",
            Utils.buildSeqNamesReverse("animation/mouth/mouth_", 19, 11, ".png"));
        animaHappyToSad = new Anima("animation/mouth/mouth_000.png",
            Utils.buildSeqNames("animation/mouth/mouth_", 0, 20, ".png"));
        animaSadToHappy = new Anima("animation/mouth/mouth_019.png",
            Utils.buildSeqNamesReverse("animation/mouth/mouth_", 19, 0, ".png"));
        mouthOpen = new Static("static/005_z05_mouth_open.PNG");
        mouthOpen.resizeY(kokoriConsts.mouth_mouthOpen_yMinRatio);
        mouthLine = new Static("static/005_mouth_line.PNG");

        animaDefaultToHappy.addTo(root);

        rotate = new Rotate(0, kokoriConsts.mouth_rotate_x, kokoriConsts.mouth_rotate_y);
        root.getTransforms().add(rotate);
    }

    private void removeAll() {
        animaHappyToDefault.removeFrom(root);
        animaDefaultToHappy.removeFrom(root);
        animaDefaultToSad.removeFrom(root);
        animaSadToDefault.removeFrom(root);
        animaHappyToSad.removeFrom(root);
        animaSadToHappy.removeFrom(root);
        mouthOpen.removeFrom(root);
        mouthLine.removeFrom(root);
    }

    public void toDefault() {
        resetRotate(); // always reset rotate when called
        if (state == 0) {
            return;
        }
        if (state == 1) {
            removeAll();
            animaHappyToDefault.addTo(root);
            animaHappyToDefault.play(60);
        } else if (state == 2) {
            removeAll();
            animaSadToDefault.addTo(root);
            animaSadToDefault.play(60);
        } else {
            assert state == 3;
            mouthOpenAnimateToDefault(() -> {
                removeAll();
                mouthLine.addTo(root);
            });
        }
        state = 0;
    }

    public void toHappy() {
        if (state == 1) {
            return;
        }
        if (state == 0) {
            removeAll();
            animaDefaultToHappy.addTo(root);
            animaDefaultToHappy.play(60);
        } else if (state == 2) {
            removeAll();
            animaSadToHappy.addTo(root);
            animaSadToHappy.play(60);
        } else {
            assert state == 3;
            mouthOpenAnimateToDefault(() -> {
                state = 0;
                toHappy();
            });
        }
        resetRotate();
        state = 1;
    }

    public void toSad() {
        if (state == 2) {
            return;
        }
        if (state == 0) {
            removeAll();
            animaDefaultToSad.addTo(root);
            animaDefaultToSad.play(60);
        } else if (state == 1) {
            removeAll();
            animaHappyToSad.addTo(root);
            animaHappyToSad.play(60);
        } else {
            assert state == 3;
            mouthOpenAnimateToDefault(() -> {
                state = 0;
                toSad();
            });
        }
        resetRotate();
        state = 2;
    }

    public void toOpen() {
        if (state == 3) {
            return;
        }
        if (state == 0) {
            removeAll();
            mouthOpen.addTo(root);
            mouthDefaultToAnimateOpen(() -> {
            });
        } else if (state == 1) {
            removeAll();
            animaHappyToDefault.addTo(root);
            animaHappyToDefault.setPauseCallbackOnce(() -> {
                mouthOpen.addTo(root);
                mouthDefaultToAnimateOpen(() -> {
                });
            }).play(60);
        } else {
            assert state == 2;
            removeAll();
            animaSadToDefault.addTo(root);
            animaSadToDefault.setPauseCallbackOnce(() -> {
                mouthOpen.addTo(root);
                mouthDefaultToAnimateOpen(() -> {
                });
            }).play(60);
        }
        resetRotate();
        state = 3;
    }

    private final TimeBasedAnimationHelper mouthOpenAnimationHelper =
        new TimeBasedAnimationHelper(120, this::updateOpenAnimation);
    private double beginOpenMouthRatio = 0;
    private double targetOpenMouthRatio = 0;

    private void mouthOpenAnimateToDefault(Runnable cb) {
        beginOpenMouthRatio = mouthOpen.getResizeRatioY();
        targetOpenMouthRatio = kokoriConsts.mouth_mouthOpen_yMinRatio;
        mouthOpenAnimationHelper.setFinishCallback(cb).play();
    }

    private void mouthDefaultToAnimateOpen(Runnable cb) {
        beginOpenMouthRatio = mouthOpen.getResizeRatioY();
        targetOpenMouthRatio = kokoriConsts.mouth_mouthOpen_yMaxRatio;
        mouthOpenAnimationHelper.setFinishCallback(cb).play();
    }

    private void updateOpenAnimation(double percentage) {
        mouthOpen.resizeY(
            (targetOpenMouthRatio - beginOpenMouthRatio) * percentage
                + beginOpenMouthRatio
        );
    }

    private void resetRotate() {
        rotate.setAngle(0);
    }

    public void tiltToRight() {
        rotate.setAngle(kokoriConsts.mouth_tiltToRight_angle);
    }

    public void tiltToLeft() {
        rotate.setAngle(kokoriConsts.mouth_tiltToLeft_angle);
    }
}
