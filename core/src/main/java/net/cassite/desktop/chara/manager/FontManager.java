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
    private static String monospaceFontFamily;

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
     * Check current default monospace font family. This method will not try to initated the default monospace font family, so you may get a nul lvalue.
     *
     * @return default monospace font family or null
     */
    public static String peekDefaultMonospaceFontFamily() {
        return monospaceFontFamily;
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
     * Set or replace the default monospace font family.
     *
     * @param monospaceFontFamily the monspace font family to set
     */
    public static void setDefaultMonospaceFontFamily(String monospaceFontFamily) {
        if (FontManager.monospaceFontFamily != null) {
            Logger.warn("replacing default monospace font family from " + FontManager.monospaceFontFamily + " to " + monospaceFontFamily);
        } else {
            Logger.info("monospace font family is set to " + monospaceFontFamily);
        }
        FontManager.monospaceFontFamily = monospaceFontFamily;
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

    /**
     * Get monospace font family. This method will initiate the monospace font family based on the current platform.
     *
     * @return the font family
     */
    public static String getMonospaceFontFamily() {
        if (monospaceFontFamily != null) {
            return monospaceFontFamily;
        }
        Logger.info("all available font families: " + Font.getFamilies());
        if (Utils.isMac()) {
            monospaceFontFamily = "Monaco";
        } else if (Utils.isWindows()) {
            monospaceFontFamily = "Consolas";
        } else {
            monospaceFontFamily = "WenQuanYi Zen Hei Mono";
        }
        Logger.info("monospace font family set to: " + monospaceFontFamily);
        return monospaceFontFamily;
    }
}
