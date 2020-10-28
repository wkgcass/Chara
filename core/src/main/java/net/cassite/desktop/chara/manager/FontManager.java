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
    private static double lineSpacingFix;

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
     * @param fontFamily     the font family to set
     * @param lineSpacingFix used to fix the text line height calculation
     */
    public static void setDefaultFontFamily(String fontFamily, double lineSpacingFix) {
        if (FontManager.fontFamily != null) {
            Logger.warn("replacing default font family from " + FontManager.fontFamily + " to " + fontFamily + ", fix = " + lineSpacingFix);
        } else {
            Logger.info("font family is set to " + fontFamily + ", fix = " + lineSpacingFix);
        }
        FontManager.fontFamily = fontFamily;
        FontManager.lineSpacingFix = lineSpacingFix;
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
            lineSpacingFix = 1.8;
        } else if (Utils.isWindows()) {
            fontFamily = "YouYuan";
            lineSpacingFix = 2;
        } else {
            fontFamily = "WenQuanYi Micro Hei";
            lineSpacingFix = 1.5;
        }
        Logger.info("font family set to: " + fontFamily);
        return fontFamily;
    }

    public static double getLineSpacingFix() {
        getFontFamily(); // ensure it's set
        return lineSpacingFix;
    }
}
