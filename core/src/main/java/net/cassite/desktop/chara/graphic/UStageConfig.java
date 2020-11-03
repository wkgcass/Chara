// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

/**
 * UStage config
 */
public class UStageConfig {
    /**
     * stage no border, default false, has border
     */
    public boolean noBorder = false;
    /**
     * stage is resizable, default false, not resizable
     */
    public boolean resizable = false;

    /**
     * @param noBorder {@link #noBorder}
     * @return <code>this</code>
     */
    public UStageConfig setNoBorder(boolean noBorder) {
        this.noBorder = noBorder;
        return this;
    }

    /**
     * @param resizable {@link #resizable}
     * @return <code>this</code>
     */
    public UStageConfig setResizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }
}
