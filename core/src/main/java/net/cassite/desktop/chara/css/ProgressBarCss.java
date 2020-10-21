// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.css;

/**
 * CSS for the progress bar
 */
public class ProgressBarCss extends Css {
    private final int innerHeight;
    private final int borderTop;
    private final int borderRight;
    private final int borderBottom;
    private final int borderLeft;

    /**
     * Construct the progress bar css
     *
     * @param innerHeight  the current progress bar (inner bar) height
     * @param borderTop    border width top
     * @param borderRight  border width right
     * @param borderBottom border width bottom
     * @param borderLeft   border width left
     */
    public ProgressBarCss(int innerHeight, int borderTop, int borderRight, int borderBottom, int borderLeft) {
        this.innerHeight = innerHeight;
        this.borderTop = borderTop;
        this.borderRight = borderRight;
        this.borderBottom = borderBottom;
        this.borderLeft = borderLeft;
    }

    @Override
    protected String name() {
        return "progress-bar";
    }

    @Override
    protected String text() {
        return "" +
            ".progress-bar > .bar {\n" +
            "  -fx-padding: " + innerHeight + "px;\n" +
            "  -fx-background-insets: " + borderTop + "px " + borderRight + "px " + borderBottom + "px " + borderLeft + "px" + ";\n" +
            "}\n" +
            "";
    }
}
