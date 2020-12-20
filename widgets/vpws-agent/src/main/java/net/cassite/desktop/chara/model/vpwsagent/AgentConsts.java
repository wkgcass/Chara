// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.model.vpwsagent;

import javafx.scene.paint.Color;
import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.model.ModelInitConfig;
import net.cassite.desktop.chara.util.Key;

public class AgentConsts {
    public static final String BASE64PREFIX = "VPWS-AGENT-UI-JSON-CONFIG:BASE64:";

    public static Key<String> configFilePath = Key.of("config_file_path", String.class);

    public static final Words proxyMenuItem = new WordsBuilder
        ("代理")
        .setEn("Proxy")
        .build();
    public static final Words stopMenuItem = new WordsBuilder
        ("停止")
        .setEn("Stop")
        .build();
    public static final Words editMenuItem = new WordsBuilder
        ("编辑配置")
        .setEn("Edit config")
        .build();
    public static final Words newMenuItem = new WordsBuilder
        ("新建配置")
        .setEn("New config")
        .build();
    public static final Words importMenuItem = new WordsBuilder
        ("导入配置文件")
        .setEn("Import config file")
        .build();
    public static final Words discardCurrentConfigMenuItem = new WordsBuilder
        ("舍弃当前配置")
        .setEn("Discard current config")
        .build();
    public static final Words areYouSureToImport = new WordsBuilder
        ("确定继续导入吗？")
        .setEn("Are you sure you want to import?")
        .build();
    public static final Words confirmImportContent = new WordsBuilder
        ("当前已加载配置文件，导入成功后原配置文件将被舍弃（原文件不会被覆盖）。")
        .setEn("The configuration file is already loaded. " +
            "The current config will be discarded after the new config is successfully imported. " +
            "(The original file will not be overwritten.)")
        .build();
    public static final Words configFileChooserTitle = new WordsBuilder
        ("选择配置文件")
        .setEn("Choose a configuration file")
        .build();
    public static final Words areYouSureToDiscard = new WordsBuilder
        ("确定舍弃配置吗？")
        .setEn("Are you sure you want to discard?")
        .build();
    public static final Words confirmDiscardContent = new WordsBuilder
        ("即将舍弃当前配置（原文件不会被删除）。")
        .setEn("Current config will be discarded. (The original file will not be deleted.)")
        .build();
    public static final Words doYouWantToUseDefaultConfigFile = new WordsBuilder
        ("是否使用默认配置文件？")
        .setEn("Do you want to use the default config file?")
        .build();
    public static final Words confirmUseDefaultConfigFileContent = new WordsBuilder
        ("没有找到配置，但是在默认文件路径上存在配置文件，是否使用该配置？")
        .setEn("Configuration not found, but found a file on the default config file path, do you want to use this config file?")
        .build();
    public static final Words noConfigToLoad = new WordsBuilder
        ("没有找到配置，请导入配置或者新建配置文件")
        .setEn("Configuration not found, please import a config file or create a new one")
        .build();
    public static final Words configApplyAfterRestarted = new WordsBuilder
        ("重新启动代理后配置生效")
        .setEn("The config will apply after restarting the proxy")
        .build();

    public final int boundsRadius = 160 / 2;
    public final int backgroundRadius = 156 / 2;
    public final int iconSide = 120;
    public final int iconX = 28;
    public final int iconY = 24;
    public final Color backgroundColor = new Color(255 / 255d, 254 / 255d, 245 / 255d, 1);
    public final Color shadowColor = new Color(235 / 255d, 235 / 255d, 235 / 255d, 1);

    public AgentConsts(ModelInitConfig config) {
        // TODO
    }
}
