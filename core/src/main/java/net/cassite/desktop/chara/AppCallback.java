// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;

public interface AppCallback {
    void setBondPoint(double current, double previous);

    void showMessage(Words words);

    default void showMessage(String... msg) {
        showMessage(new WordsBuilder(msg).build());
    }

    void clickNothing(double x, double y);
}
