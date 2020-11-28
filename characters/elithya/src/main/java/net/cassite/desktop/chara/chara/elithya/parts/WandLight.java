// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;
import net.cassite.desktop.chara.graphic.TimeBasedAnimationHelper;

public class WandLight extends AbstractPart {
    private final Static light;

    public WandLight(Group parent) {
        super(parent);
        light = new Static("static/035_001_hand_right_wand_light.PNG");
        root.setOpacity(0);
    }

    private boolean isShown = false;
    private double beginOpacity;
    private double targetOpacity;
    private final TimeBasedAnimationHelper helper = new TimeBasedAnimationHelper(500, this::updateOpacity);

    private void playOpacity() {
        beginOpacity = root.getOpacity();
        helper.play();
    }

    private void updateOpacity(double percentage) {
        root.setOpacity((targetOpacity - beginOpacity) * percentage + beginOpacity);
    }

    public boolean isShown() {
        return isShown;
    }

    public void show() {
        if (isShown) {
            return;
        }
        isShown = true;
        if (helper.isPlaying()) {
            helper.setFinishCallback(null);
        } else {
            light.addTo(root);
        }
        targetOpacity = 1;
        playOpacity();
    }

    public void hide() {
        if (!isShown) {
            return;
        }
        isShown = false;
        helper.setFinishCallbackOnce(() -> light.removeFrom(root));
        targetOpacity = 0;
        playOpacity();
    }
}
