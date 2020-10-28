// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.wqyfont;

import javafx.scene.text.Font;
import net.cassite.desktop.chara.manager.FontManager;
import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.ResourceHandler;

import java.util.Collections;
import java.util.List;

public class WqyFontPlugin implements Plugin {
    @Override
    public String name() {
        return "wqy-font";
    }

    @Override
    public int version() {
        return 1000000;
    }

    @Override
    public List<ResourceHandler> resourceHandlers() {
        return Collections.singletonList(
            new ResourceHandler("font/wqy-microhei.ttc", (inputStream, cb) -> {
                FontManager.registerFont(inputStream);
                FontManager.setDefaultFontFamily("WenQuanYi Micro Hei", 1.5);
                Logger.info("current font families list: " + Font.getFamilies());
                cb.succeeded(null);
            })
        );
    }

    @Override
    public void launch() {
        // do nothing
    }

    @Override
    public void clicked() {
        // do nothing
    }

    @Override
    public void release() {
        // do nothing
    }
}
