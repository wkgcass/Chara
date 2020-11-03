// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

public class UStageConfig {
    public boolean noBorder = false;
    public boolean resizable = false;

    public UStageConfig setNoBorder(boolean noBorder) {
        this.noBorder = noBorder;
        return this;
    }

    public UStageConfig setResizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }
}
