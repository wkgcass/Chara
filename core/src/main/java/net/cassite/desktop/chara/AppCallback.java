// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import net.cassite.desktop.chara.i18n.Words;

public interface AppCallback {
    void setCharaPoints(CharaPoints points);

    void showMessage(Words words);

    default void showMessage(String... msg) {
        showMessage(new Words(msg, null));
    }

    void clearAllMessages();

    void activeInteraction(Runnable cb);

    void clickNothing(double x, double y);

    void moveWindow(double deltaX, double deltaY);

    void setDraggable(boolean draggable);

    void setGlobalScreen(boolean globalScreen);

    void setAlwaysShowBar(boolean alwaysShowBar);
}
