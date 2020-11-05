// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.i18n;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;

public class KokoriI18n {
    private KokoriI18n() {
    }

    public static final Words aboutNameMenuItem = new WordsBuilder
        ("关于名字")
        .setEn("About name")
        .build();
    public static final Words thingsSheLikesMenuItem = new WordsBuilder
        ("喜欢的东西")
        .setEn("Things she likes")
        .build();
    public static final Words thingsSheHatesMenuItem = new WordsBuilder
        ("讨厌的东西")
        .setEn("Things she hates")
        .build();
    public static final Words bondStoryMenuItem = new WordsBuilder
        ("羁绊故事")
        .setEn("Bond story")
        .build();
    public static final Words expressionMenuItem = new WordsBuilder
        ("表情")
        .setEn("Expression")
        .build();
    public static final Words proposeMenuItem = new WordsBuilder
        ("表白")
        .setEn("Profess")
        .build();

    public static final Words bondPointTooLowWarning = new WordsBuilder
        ("当前羁绊值过低，继续互动可能引发灾难性后果")
        .setEn("The current bond point is too low, continuing to interact may cause catastrophic consequences")
        .build();

    public static final Words expressionManual = new WordsBuilder
        ("" +
            "使用Ctrl+Shift+Alt+下列按键：\n" +
            "  H: 开心\n" +
            "  S: 害羞\n" +
            "  A: 伤心\n" +
            "  D: 厌恶\n" +
            "  R: 重置\n" +
            "  P: 惊讶")
        .setEn("" +
            "Use Ctrl+Shift+Alt+ the following keys:\n" +
            "  H: happy\n" +
            "  S: shy\n" +
            "  A: sad\n" +
            "  D: disgust\n" +
            "  R: Reset\n" +
            "  P: surprised")
        .build();
}
