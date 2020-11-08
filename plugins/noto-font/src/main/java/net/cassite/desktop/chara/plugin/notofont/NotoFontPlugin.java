// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.notofont;

import javafx.scene.text.Font;
import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.manager.FontManager;
import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.ResourceHandler;

import java.util.LinkedList;
import java.util.List;

public class NotoFontPlugin implements Plugin {
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
    public double priority() {
        return GENERAL_FONT_PRIORITY - 1; // load later than general font because it may not contain monospace font
    }

    @Override
    public List<ResourceHandler> resourceHandlers() {
        List<ResourceHandler> ret = new LinkedList<>();
        Words words = new WordsBuilder
            ("cs")
            .setEn("en")
            .build();
        String lang = words.get()[0];
        //noinspection SwitchStatementWithTooFewBranches
        switch (lang) {
            case "cs":
                ret.add(new ResourceHandler("font/NotoSansSC-Regular.otf", (inputStream, cb) -> {
                    FontManager.registerFont(inputStream);
                    cb.succeeded(null);
                }));
                break;
            default:
                Logger.error("The plugin does not contain your language: " + lang);
        }
        return ret;
    }

    @Override
    public void launch() {
        Logger.info("current font families list: " + Font.getFamilies());
        FontManager.setDefaultFontFamily("Noto Sans SC Regular");
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
