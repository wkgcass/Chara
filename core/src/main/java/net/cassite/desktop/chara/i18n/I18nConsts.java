// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.i18n;

public class I18nConsts {
    private I18nConsts() {
    }

    public static final Words showMessageHelper = new WordsBuilder
        ("使用组合键Ctrl+Shift+Win/Cmd+下列快捷按键:\n" +
            "+H  显示帮助信息\n" +
            "+C  开启或关闭对话功能\n" +
            "+T  窗口是否放置在最前")
        .setEn("Use combination of Ctrl+Shift+Win/Cmd+ following hot keys:\n" +
            "+H  show help message\n" +
            "+C  enable or disable chat feature\n" +
            "+T  enable or disable window always on top")
        .build();
    public static final Words messageEnabled = new WordsBuilder
        ("对话功能已启用")
        .setEn("Chat feature enabled")
        .build();
    public static final Words messageDisabled = new WordsBuilder
        ("对话功能已禁用")
        .setEn("Chat feature disabled")
        .build();
    public static final Words alwaysOnTopDisabled = new WordsBuilder
        ("取消放置在最前")
        .setEn("Always on top disabled")
        .build();
    public static final Words alwaysOnTopEnabled = new WordsBuilder
        ("放置在最前")
        .setEn("Always on top enabled")
        .build();
    public static final Words bondPointTooLowWarning = new WordsBuilder
        ("当前羁绊值过低，继续互动可能引发灾难性后果")
        .setEn("The current bond point is too low, continuing to interact may cause catastrophic consequences")
        .build();
    public static final Words chatbotNotConfigured = new WordsBuilder
        ("聊天机器人尚未配置")
        .setEn("Chatbot is not configured yet")
        .build();

    public static Words modelFileNotFound = new WordsBuilder
        ("模型文件丢失")
        .setEn("Model file not found")
        .build();
}
