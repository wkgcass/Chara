// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.notofont;

import javafx.scene.text.Font;
import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.manager.FontManager;
import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.ResourceHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class NotoFontPlugin implements Plugin {
    private static final String LOCALE;
    private static final FontInfo FONT_INFO;

    static {
        var map = new HashMap<String, FontInfo>();
        // PUT NEW FONT CONFIGURATIONS HERE
        {
            map.put("zh-CN", new FontInfo("NotoSansSC-Regular.otf", "Noto Sans SC Regular"));
        }

        String locale = Words.getLocale();
        LOCALE = locale;
        FONT_INFO = map.get(locale);
    }

    private static class FontInfo {
        final String entry;
        final String family;
        final String monoEntry;
        final String monoFamily;

        private FontInfo(String entry, String family) {
            this(entry, family, null, null);
        }

        private FontInfo(String entry, String family, String monoEntry, String monoFamily) {
            this.entry = entry;
            this.family = family;
            this.monoEntry = monoEntry;
            this.monoFamily = monoFamily;
        }
    }

    public NotoFontPlugin() {
    }

    @Override
    public String name() {
        return "noto-font";
    }

    @Override
    public int version() {
        return 1000000; // _THE_VERSION_
    }

    @Override
    public int priority() {
        // load later than general font because it contains more logic and more likely to be used by user
        return GENERAL_FONT_PRIORITY - GENERAL_PRIORITY_STEP;
    }

    @Override
    public List<ResourceHandler> resourceHandlers() {
        List<ResourceHandler> ret = new LinkedList<>();
        if (FONT_INFO == null) {
            Logger.error("The plugin does not contain your locale: " + LOCALE);
        } else {
            ret.add(new ResourceHandler("font/" + FONT_INFO.entry, (inputStream, cb) -> {
                FontManager.registerFont(inputStream);
                cb.succeeded(null);
            }));
            if (FONT_INFO.monoFamily != null) {
                ret.add(new ResourceHandler("font/" + FONT_INFO.monoEntry, ((inputStream, cb) -> {
                    FontManager.registerFont(inputStream);
                    cb.succeeded(null);
                })));
            }
        }
        return ret;
    }

    @Override
    public void launch() {
        if (FONT_INFO != null) {
            Logger.info("current font families list: " + Font.getFamilies());
            FontManager.setDefaultFontFamily(FONT_INFO.family);
            if (FONT_INFO.monoFamily != null) {
                FontManager.setDefaultMonospaceFontFamily(FONT_INFO.monoFamily);
            }
        }
    }

    @Override
    public void clicked() {
        // do nothing
    }

    @Override
    public void release() {
        // do nothing
    }

    @Override
    public String about() {
        return "" +
            "author: wkgcass\n" +
            "code license: GPLv2 with classpath exception\n" +
            "font: noto\n" +
            "font license: Open Font License" +
            "";
    }
}
