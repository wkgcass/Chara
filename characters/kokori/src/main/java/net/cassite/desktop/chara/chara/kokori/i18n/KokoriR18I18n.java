// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.i18n;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;

public class KokoriR18I18n {
    private KokoriR18I18n() {
    }

    public static final Words wantHMenuItem = new WordsBuilder
        ("想要H")
        .setEn("Want H")
        .build();
    public static final Words useLovePotionMenuItem = new WordsBuilder
        ("使用媚药")
        .setEn("Use love potion")
        .build();

    public static final Words r18ExpressionManual = new WordsBuilder
        ("" +
            "  Y: 病娇\n" +
            "  U: 高潮")
        .setEn("" +
            "  Y: yandere\n" +
            "  U: orgasm")
        .build();
}
