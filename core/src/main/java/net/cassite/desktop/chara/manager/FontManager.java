// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.manager;

import javafx.scene.text.Font;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Utils;

import java.io.InputStream;

/**
 * The font manager
 */
public class FontManager {
    private FontManager() {
    }

    private static String fontFamily;

    /**
     * Register a new font
     *
     * @param inputStream input stream of the font file
     * @throws IllegalArgumentException input stream is invalid
     */
    public static void registerFont(InputStream inputStream) throws IllegalArgumentException {
        var font = Font.loadFont(inputStream, 1);
        if (font == null) {
            throw new IllegalArgumentException("loading font failed");
        }
    }

    /**
     * Check current default font family. This method will not try to initiated the default font family, so you may get a null value.
     *
     * @return default font family or null
     */
    public static String peekDefaultFontFamily() {
        return fontFamily;
    }

    /**
     * Set or replace the default font family.
     *
     * @param fontFamily the font family to set
     */
    public static void setDefaultFontFamily(String fontFamily) {
        if (FontManager.fontFamily != null) {
            Logger.warn("replacing default font family from " + FontManager.fontFamily + " to " + fontFamily);
        } else {
            Logger.info("font family is set to " + fontFamily);
        }
        FontManager.fontFamily = fontFamily;
    }

    /**
     * Get font family. This method will initiate the font family based on the current platform.
     *
     * @return the font family
     */
    public static String getFontFamily() {
        if (fontFamily != null) {
            return fontFamily;
        }
        Logger.info("all available font families: " + Font.getFamilies());
        if (Utils.isMac()) {
            fontFamily = "PingFang SC";
        } else if (Utils.isWindows()) {
            fontFamily = "YouYuan";
        } else {
            fontFamily = "WenQuanYi Micro Hei";
        }
        Logger.info("font family set to: " + fontFamily);
        return fontFamily;
    }
}
