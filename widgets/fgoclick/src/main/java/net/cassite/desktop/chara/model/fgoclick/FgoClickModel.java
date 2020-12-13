// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.model.fgoclick;

import net.cassite.desktop.chara.chara.Chara;
import net.cassite.desktop.chara.i18n.I18nConsts;
import net.cassite.desktop.chara.model.Model;
import net.cassite.desktop.chara.model.ModelInitConfig;
import net.cassite.desktop.chara.util.ResourceHandler;
import net.cassite.desktop.chara.widget.fgoclick.FgoClick;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;

import java.util.Collections;
import java.util.List;

public class FgoClickModel implements Model {
    @Override
    public String name() {
        return "fgoclick";
    }

    @Override
    public int version() {
        return 1000000; // _THE_VERSION_
    }

    private static final Data data = new DataBuilder()
        .setAboutMessage("" +
            "author: wkgcass\n" +
            "code license: GPLv2 with classpath exception"
        )
        .setActiveInteractionSupported(false)
        .setDefaultAllowActiveInteraction(false)
        .setDefaultAlwaysOnTop(true)
        .setDefaultMessageEnabled(true)
        .setDefaultMouseIndicatorEnabled(false)
        .setDefaultShowIconOnTaskbar(false)
        .setMessageSupported(false)
        .setModelMenuItemText(I18nConsts.widgetMenu)
        .build();

    @Override
    public Data data() {
        return data;
    }

    @Override
    public Chara construct(ConstructParams constructParams) {
        return new FgoClick(constructParams.cb, constructParams.parent, constructParams.characterMenu, fgoClickConsts);
    }

    @Override
    public List<String> requiredImages() {
        return Collections.emptyList();
    }

    @Override
    public List<ResourceHandler> resourceHandlers() {
        return Collections.emptyList();
    }

    private FgoClickConsts fgoClickConsts;

    @Override
    public void init(ModelInitConfig modelInitConfig) {
        fgoClickConsts = new FgoClickConsts(modelInitConfig);
    }
}
