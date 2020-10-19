// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.i18n;

public class I18nConsts {
    private I18nConsts() {
    }

    public static final Words SELECT_EXISTING_MODEL_CONFIG = new WordsBuilder
        ("选择已有的模型配置")
        .setEn("Select existing model config")
        .build();
    public static final Words OK_BTN = new WordsBuilder
        ("确定")
        .setEn("OK")
        .build();
    public static final Words CHOOSE_FILE_BTN = new WordsBuilder
        ("选择文件...")
        .setEn("Choose File...")
        .build();
    public static final Words CHOOSE_MODEL_FILE = new WordsBuilder
        ("选择模型文件")
        .setEn("Choose model file")
        .build();
    public static final Words LOADING = new WordsBuilder
        ("加载中...")
        .setEn("Loading...")
        .build();

    public static final Words enableChatFeatureItem = new WordsBuilder
        ("启用对话功能")
        .setEn("Enable chat feature")
        .build();
    public static final Words alwaysOnTopItem = new WordsBuilder
        ("窗口放置在最前")
        .setEn("Window always on top")
        .build();
    public static final Words activeInteractionItem = new WordsBuilder
        ("允许主动互动")
        .setEn("Allow active interaction")
        .build();
    public static final Words snapshotItem = new WordsBuilder
        ("截图")
        .setEn("Snapshot")
        .build();
    public static final Words characterMenu = new WordsBuilder
        ("角色")
        .setEn("Character")
        .build();
    public static final Words systemMenu = new WordsBuilder
        ("系统")
        .setEn("System")
        .build();
    public static final Words showVersionsItem = new WordsBuilder
        ("显示版本号")
        .setEn("Show versions")
        .build();
    public static final Words exitMenuItem = new WordsBuilder
        ("退出")
        .setEn("Exit")
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
    public static final Words snapshotSavedInClipboard = new WordsBuilder
        ("截图已保存至剪贴板中")
        .setEn("Snapshot saved into clipboard")
        .build();
    public static final Words activeInteractionEnabled = new WordsBuilder
        ("主动互动已开启")
        .setEn("Active interaction enabled")
        .build();
    public static final Words activeInteractionDisabled = new WordsBuilder
        ("主动互动已禁用")
        .setEn("Active interaction disabled")
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
