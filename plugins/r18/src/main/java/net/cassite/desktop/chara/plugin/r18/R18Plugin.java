// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.r18;

import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.ResourceHandler;

import java.util.Collections;
import java.util.List;

public class R18Plugin implements Plugin {
    public R18Plugin() {
    }

    @Override
    public String name() {
        return "r18";
    }

    @Override
    public int version() {
        return 1000000;
    }

    @Override
    public List<ResourceHandler> resourceHandlers() {
        return Collections.emptyList();
    }

    @Override
    public void launch() {
        try {
            Global.setR18Features(true);
            Logger.info("r18 features enabled");
        } catch (Exception e) {
            Logger.error("failed enabling r18 features", e);
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
            "code license: GPLv2 with classpath exception" +
            "";
    }
}
