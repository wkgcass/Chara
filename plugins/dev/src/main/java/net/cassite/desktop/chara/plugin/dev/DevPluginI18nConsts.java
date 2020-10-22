// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.dev;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;

public class DevPluginI18nConsts {
    private DevPluginI18nConsts() {
    }

    public static final Words devPluginEnabled = new WordsBuilder
        ("Dev插件已启用")
        .setEn("Dev plugin is enabled")
        .build();
    public static final Words devPluginDisabled = new WordsBuilder
        ("Dev插件已禁用")
        .setEn("Dev plugin is disabled")
        .build();
}
