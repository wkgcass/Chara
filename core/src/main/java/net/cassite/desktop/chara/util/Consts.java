// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import javafx.scene.paint.Color;

public class Consts {
    private Consts() {
    }

    public static final String VERSION = "1.0.0";
    public static final int VERSION_NUM;
    public static final String JNATIVEHOOK_VERSION = "2.1.0"; // _THE_JNATIVE_HOOK_VERSION_

    static {
        {
            var split = VERSION.split("\\.");
            VERSION_NUM = Integer.parseInt(split[0]) * 1_000_000 + Integer.parseInt(split[1]) * 1_000 + Integer.parseInt(split[2]);
        }
    }

    public static final String CACHE_DIR_NAME = ".chara/cache";
    public static final String CONFIG_BASE_DIR = ".chara/config";
    public static final String PLUGIN_BASE_DIR = ".chara/plugin";

    public static final int BAR_WIDTH = 255;
    public static final int BOND_BAR_MARGIN_TOP = 5;
    public static final int BOND_BAR_INNER_HEIGHT = 8;
    public static final int BOND_BAR_BORDER_WIDTH = 0;
    @SuppressWarnings("PointlessArithmeticExpression")
    public static final int BOND_BAR_HEIGHT = BOND_BAR_INNER_HEIGHT + BOND_BAR_BORDER_WIDTH * 2;
    public static final int BOND_BAR_MARGIN_BOTTOM = 2;

    public static final int DESIRE_BAR_MARGIN_TOP = 2;
    public static final int DESIRE_BAR_INNER_HEIGHT = 8;
    public static final int DESIRE_BAR_BORDER_WIDTH = 0;
    @SuppressWarnings("PointlessArithmeticExpression")
    public static final int DESIRE_BAR_HEIGHT = DESIRE_BAR_INNER_HEIGHT + DESIRE_BAR_BORDER_WIDTH * 2;
    public static final int DESIRE_BAR_MARGIN_BOTTOM = 5;

    public static final int CHARA_TOTAL_ABSOLUTE_MARGIN_TOP =
        BOND_BAR_MARGIN_TOP + BOND_BAR_HEIGHT + BOND_BAR_MARGIN_BOTTOM +
            DESIRE_BAR_MARGIN_TOP + DESIRE_BAR_HEIGHT + DESIRE_BAR_MARGIN_BOTTOM;

    public static final int MSG_STAGE_WIDTH = 400;
    public static final int MSG_BUBBLE_MAX_WIDTH = 392;
    public static final int MSG_BUBBLE_PADDING_HORIZONTAL = 12;
    public static final int MSG_BUBBLE_PADDING_VERTICAL = 8;
    public static final int MSG_BUBBLE_MARGIN_VERTICAL = 10;
    public static final int MSG_ARROW_WIDTH = MSG_STAGE_WIDTH - MSG_BUBBLE_MAX_WIDTH;
    public static final double MSG_ARROW_HEIGHT = 2 * MSG_ARROW_WIDTH * Math.tan(Math.PI / 6);
    public static final int MSG_FONT_SIZE = 16;
    public static final int MAX_MSG_COUNT = 10;

    public static final int INPUT_WIDTH = 255;
    public static final int INPUT_HEIGHT = 25;
    public static final int INPUT_FONT_SIZE = 15;
    public static final int INPUT_MARGIN_BOTTOM = 40; // the input will be inside the image
    public static final int INPUT_SHOW_Y_DELTA = 40;

    public static class MsgBubbleColor {
        public final Color background;
        public final String backgroundRGB;
        public final Color text;

        public MsgBubbleColor(Color background, String backgroundRGB, Color text) {
            this.background = background;
            this.backgroundRGB = backgroundRGB;
            this.text = text;
        }
    }

    public static final MsgBubbleColor[] MSG_BUBBLE_COLORS = new MsgBubbleColor[]{
        new MsgBubbleColor(
            new Color(0xf7 / 255D, 0xd4 / 255D, 0xe2 / 255D, 1),
            "#f7d4e2",
            new Color(0x1d / 255D, 0x1d / 255D, 0x1d / 255D, 1)
        ),
        new MsgBubbleColor(
            new Color(0x54 / 255D, 0xb9 / 255D, 0xf9 / 255D, 1),
            "#54b9f9",
            new Color(1, 1, 1, 1)
        ),
        new MsgBubbleColor(
            new Color(0xbe / 255D, 0xdf / 255D, 0xc5 / 255D, 1),
            "#bedfc5",
            new Color(0x37 / 255D, 0x7d / 255D, 0x21 / 255D, 1)
        )
    };
}
