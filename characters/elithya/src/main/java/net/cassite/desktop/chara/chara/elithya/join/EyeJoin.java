// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.join;

import net.cassite.desktop.chara.chara.elithya.parts.EyeLeft;
import net.cassite.desktop.chara.chara.elithya.parts.EyeRight;
import net.cassite.desktop.chara.chara.elithya.parts.HeadWithEyeBlink;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaConsts;

public class EyeJoin {
    private final HeadWithEyeBlink headWithEyeBlink;
    private final EyeLeft left;
    private final EyeRight right;
    private final ElithyaConsts elithyaConsts;

    public EyeJoin(HeadWithEyeBlink headWithEyeBlink, EyeLeft left, EyeRight right, ElithyaConsts elithyaConsts) {
        this.headWithEyeBlink = headWithEyeBlink;
        this.left = left;
        this.right = right;
        this.elithyaConsts = elithyaConsts;

        left.init();
        right.init();
    }

    private boolean isTracking = false;

    public void track(double x, double y) {
        if (x < elithyaConsts.eyeTrackBoundMinX || x > elithyaConsts.eyeTrackBoundMaxX
            || y < elithyaConsts.eyeTrackBoundMinY || y > elithyaConsts.eyeTrackBoundMaxY) {
            reset();
            return;
        }
        // need to track
        if (!isTracking) {
            blink();
        }
        isTracking = true;
        if (elithyaConsts.eyeRightInitX < x && x < elithyaConsts.eyeLeftInitX) {
            left.resetXAndTrackY(y);
            right.resetXAndTrackY(y);
            return;
        }
        left.track(x, y);
        right.track(x, y);
    }

    public void reset() {
        if (!isTracking) {
            return;
        }
        isTracking = false;
        left.reset();
        right.reset();
        headWithEyeBlink.blink();
    }

    public void blink() {
        headWithEyeBlink.blink();
    }
}
