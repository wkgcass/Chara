// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.manager;

import javafx.scene.text.Font;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Utils;

/**
 * The font manager
 */
public class FontManager {
    private FontManager() {
    }

    private static String fontFamily;

    /**
     * Get font family based on the current platform
     *
     * @return the font family corresponding to current platform, or empty string
     */
    public static String getFontFamily() {
        if (fontFamily != null) {
            return fontFamily;
        }
        Logger.info("all available font families: " + Font.getFamilies());
        if (Utils.isMac()) {
            fontFamily = "PingFang SC";
        } else if (Utils.isWindows()) {
            fontFamily = "Microsoft YaHei";
        } else {
            fontFamily = "";
        }
        Logger.info("font family set to: " + fontFamily);
        return fontFamily;
    }
}
