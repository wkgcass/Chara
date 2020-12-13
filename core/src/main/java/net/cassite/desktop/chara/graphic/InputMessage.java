// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

public class InputMessage {
    public final String message;
    public int colorHash;
    public boolean alwaysShow;

    public InputMessage(String message) {
        this.message = message;
    }

    public InputMessage setColor(int colorHash) {
        this.colorHash = colorHash;
        return this;
    }

    public InputMessage setAlwaysShow(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        return this;
    }
}
