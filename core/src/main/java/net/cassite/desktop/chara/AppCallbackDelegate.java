// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import net.cassite.desktop.chara.i18n.Words;

/**
 * The delegate for {@link AppCallback} interface
 */
public class AppCallbackDelegate implements AppCallback {
    private final AppCallback target;

    /**
     * Construct the delegate with the target
     *
     * @param target target
     */
    public AppCallbackDelegate(AppCallback target) {
        this.target = target;
    }

    @Override
    public void setCharaPoints(CharaPoints points) {
        target.setCharaPoints(points);
    }

    @Override
    public void showMessage(Words words) {
        target.showMessage(words);
    }

    @Override
    public void showMessage(String... msg) {
        target.showMessage(msg);
    }

    @Override
    public void clearAllMessages() {
        target.clearAllMessages();
    }

    @Override
    public void activeInteraction(Runnable cb) {
        target.activeInteraction(cb);
    }

    @Override
    public void clickNothing(double x, double y) {
        target.clickNothing(x, y);
    }

    @Override
    public void moveWindow(double anchorX, double deltaX, double anchorY, double deltaY) {
        target.moveWindow(anchorX, deltaX, anchorY, deltaY);
    }

    @Override
    public double[] getWindowPosition() {
        return target.getWindowPosition();
    }

    @Override
    public void setDraggable(boolean draggable) {
        target.setDraggable(draggable);
    }

    @Override
    public void setGlobalScreen(boolean globalScreen) {
        target.setGlobalScreen(globalScreen);
    }

    @Override
    public void setAlwaysShowBar(boolean alwaysShowBar) {
        target.setAlwaysShowBar(alwaysShowBar);
    }

    @Override
    public void setAlwaysHideBar(boolean alwaysHideBar) {
        target.setAlwaysHideBar(alwaysHideBar);
    }
}
