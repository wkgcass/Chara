// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.wqyfont;

import javafx.scene.text.Font;
import net.cassite.desktop.chara.manager.FontManager;
import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.ResourceHandler;

import java.util.Arrays;
import java.util.List;

public class WqyFontPlugin implements Plugin {
    @Override
    public String name() {
        return "wqy-font";
    }

    @Override
    public int version() {
        return 1000000; // _THE_VERSION_
    }

    @Override
    public List<ResourceHandler> resourceHandlers() {
        return Arrays.asList(
            new ResourceHandler("font/wqy-microhei.ttc", (inputStream, cb) -> {
                FontManager.registerFont(inputStream);
                FontManager.setDefaultFontFamily("WenQuanYi Micro Hei");
                cb.succeeded(null);
            }),
            new ResourceHandler("font/wqy-microhei-mono.ttf", (inputStream, cb) -> {
                FontManager.registerFont(inputStream);
                FontManager.setDefaultMonospaceFontFamily("WenQuanYi Micro Hei Mono");
                cb.succeeded(null);
            })
        );
    }

    @Override
    public void launch() {
        // do nothing
        Logger.info("current font families list: " + Font.getFamilies());
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
            "font: wqy-microhei\n" +
            "font license: Apache2 and GPLv3";
    }
}
