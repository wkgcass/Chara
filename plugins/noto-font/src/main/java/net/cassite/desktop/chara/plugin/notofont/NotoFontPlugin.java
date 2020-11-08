// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.notofont;

import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.ResourceHandler;

import java.util.Collections;
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
    public List<ResourceHandler> resourceHandlers() {
        return Collections.emptyList();
    }

    @Override
    public void launch() {
        Logger.info(name() + " launch()");
    }

    @Override
    public void clicked() {
        Logger.info(name() + " clicked()");
    }

    @Override
    public void release() {
        Logger.info(name() + " release()");
    }

    @Override
    public String about() {
        return "" +
            "author: $AUTHOR\n" +
            "code license: GPLv2 with classpath exception" +
            "";
    }
}
