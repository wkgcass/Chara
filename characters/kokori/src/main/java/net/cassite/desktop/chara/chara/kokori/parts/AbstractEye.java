// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import net.cassite.desktop.chara.graphic.Static;
import net.cassite.desktop.chara.graphic.TimeBasedAnimationHelper;

public abstract class AbstractEye extends AbstractPart implements Eye {
    private final ColorAdjust colorAdjust;
    protected final Group eyeGroup = new Group();
    protected final Static highlight;
    protected final Static eye;

    public AbstractEye(Group parent, Static highlight, Static eye) {
        super(parent);
        this.highlight = highlight;
        this.eye = eye;
        eye.addTo(eyeGroup);
        this.colorAdjust = new ColorAdjust();
        eyeGroup.setEffect(colorAdjust);
    }

    private final TimeBasedAnimationHelper pupilColorAnimation = new TimeBasedAnimationHelper(
        9000, this::updatePupilColor
    ).setFinishCallback(this::play);

    private boolean keepPlaying = false;

    private void play() {
        if (!keepPlaying) {
            return;
        }
        pupilColorAnimation.play();
    }

    @Override
    public void beginAnimatingPupilColor() {
        keepPlaying = true;
        play();
    }

    @Override
    public void stopAnimatingPupilColor() {
        keepPlaying = false;
    }

    @Override
    public void resetPupilColor() {
        keepPlaying = false;
        colorAdjust.setHue(0);
    }

    private static final double MIN = -0.63;
    private static final double MAX = 0.33;
    private static final double FIRST = MAX / (MAX - MIN) / 2;
    private static final double SECOND = 0.5;
    private static final double THIRD = 1 - FIRST - SECOND;

    private void updatePupilColor(double percentage) {
        if (!keepPlaying) {
            return;
        }
        if (percentage < FIRST) {
            colorAdjust.setHue(percentage / FIRST * MAX);
        } else if (percentage < FIRST + SECOND) {
            colorAdjust.setHue(MAX + (percentage - FIRST) / SECOND * (MIN - MAX));
        } else {
            colorAdjust.setHue(MIN + (percentage - FIRST - SECOND) / THIRD * (-MIN));
        }
    }

    @Override
    public void zoom(double ratio) {
        if (ratio <= 0.01) {
            ratio = 0.01;
        }
        eye.resize(ratio);
        highlight.resize(ratio);
    }
}
