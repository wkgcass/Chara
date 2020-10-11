// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import net.cassite.desktop.chara.i18n.Words;

public class AppCallbackDelegate implements AppCallback {
    private final AppCallback target;

    public AppCallbackDelegate(AppCallback target) {
        this.target = target;
    }

    @Override
    public void setBondPoint(double current, double previous) {
        target.setBondPoint(current, previous);
    }

    @Override
    public void showMessage(Words words) {
        target.showMessage(words);
    }

    @Override
    public void clickNothing(double x, double y) {
        target.clickNothing(x, y);
    }
}
