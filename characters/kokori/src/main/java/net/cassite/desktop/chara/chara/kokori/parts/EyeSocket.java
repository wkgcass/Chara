// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import net.cassite.desktop.chara.graphic.Div;

public interface EyeSocket {
    Div getRoot();

    void close(Runnable cb);

    void open();
}
