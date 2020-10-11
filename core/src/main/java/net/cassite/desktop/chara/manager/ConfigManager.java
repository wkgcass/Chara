// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.manager;

import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.util.Consts;
import net.cassite.desktop.chara.util.Logger;
import vjson.JSON;
import vjson.util.ObjectBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ConfigManager {
    private static ConfigManager instance = null;

    public class Config {
        private String modelFile;
        private Double stageX;
        private Double stageY;
        private Double characterRatio;
        private Double bondPoint;

        private String chatbot;

        public String getModelFile() {
            return modelFile;
        }

        public void setModelFile(String modelFile) {
            this.modelFile = modelFile;
            save();
        }

        public Double getStageX() {
            return stageX;
        }

        public synchronized void setStageX(Double stageX) {
            this.stageX = stageX;
            save();
        }

        public Double getStageY() {
            return stageY;
        }

        public synchronized void setStageY(Double stageY) {
            this.stageY = stageY;
            save();
        }

        public Double getCharacterRatio() {
            return characterRatio;
        }

        public synchronized void setCharacterRatio(Double characterRatio) {
            this.characterRatio = characterRatio;
            save();
        }

        public Double getBondPoint() {
            return bondPoint;
        }

        public void setBondPoint(Double bondPoint) {
            this.bondPoint = bondPoint;
            save();
        }

        public String getChatbot() {
            return chatbot;
        }

        private void from(JSON.Object obj) {
            if (obj.containsKey("model_file")) {
                var o = obj.get("model_file");
                if (o instanceof JSON.String) {
                    this.modelFile = ((JSON.String) o).toJavaObject();
                }
            }
            if (obj.containsKey("stage_x")) {
                var o = obj.get("stage_x");
                if (o instanceof JSON.Double) {
                    this.stageX = ((JSON.Double) o).doubleValue();
                }
            }
            if (obj.containsKey("stage_y")) {
                var o = obj.get("stage_y");
                if (o instanceof JSON.Double) {
                    this.stageY = ((JSON.Double) o).doubleValue();
                }
            }
            if (obj.containsKey("chara_ratio")) {
                var o = obj.get("chara_ratio");
                if (o instanceof JSON.Double) {
                    this.characterRatio = ((JSON.Double) o).doubleValue();
                }
            }
            if (obj.containsKey("bond_point")) {
                var o = obj.get("bond_point");
                if (o instanceof JSON.Double) {
                    this.bondPoint = ((JSON.Double) o).doubleValue();
                }
            }
            if (obj.containsKey("chatbot")) {
                var o = obj.get("chatbot");
                if (o instanceof JSON.String) {
                    this.chatbot = ((JSON.String) o).toJavaObject();
                }
            }
        }

        public String serialize() {
            ObjectBuilder ob = new ObjectBuilder();
            if (modelFile != null) {
                ob.put("model_file", modelFile);
            }
            if (stageX != null) {
                ob.put("stage_x", stageX);
            }
            if (stageY != null) {
                ob.put("stage_y", stageY);
            }
            if (characterRatio != null) {
                ob.put("chara_ratio", characterRatio);
            }
            if (bondPoint != null) {
                ob.put("bond_point", bondPoint);
            }
            if (chatbot != null) {
                ob.put("chatbot", chatbot);
            }
            return ob.build().pretty();
        }
    }

    private final Config config;

    private ConfigManager() {
        Objects.requireNonNull(Global.modelName, "Global.modelName");

        config = new Config();
        final String configFilePath = System.getProperty("user.home")
            + "/" + Consts.CONFIG_BASE_DIR
            + "/" + Global.modelName + ".json";
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            assert Logger.debug("config file not exists");
            return;
        }
        if (!configFile.isFile()) {
            Logger.error("config file exists but is not file: " + configFile);
            return;
        }
        String content;
        try {
            content = Files.readString(configFile.toPath());
        } catch (IOException e) {
            Logger.fatal("reading config file " + configFile + " failed", e);
            return;
        }
        //noinspection rawtypes
        JSON.Instance inst;
        try {
            inst = JSON.parse(content);
        } catch (Exception e) {
            return;
        }
        if (!(inst instanceof JSON.Object)) {
            Logger.error("config file is not json object: " + content);
            return;
        }
        JSON.Object o = (JSON.Object) inst;
        config.from(o);
    }

    public static Config get() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance.config;
    }

    private volatile boolean pending = false;

    private void save() {
        if (pending) {
            return;
        }
        pending = true;
        ThreadUtils.get().schedule(() -> {
            pending = false;
            try {
                doSave();
            } catch (Exception e) {
                Logger.error("saving config failed", e);
            }
        }, 1, TimeUnit.SECONDS);
    }

    private void doSave() throws Exception {
        Objects.requireNonNull(Global.modelName, "Global.modelName");

        String content = config.serialize();
        final String configFilePath = System.getProperty("user.home")
            + "/" + Consts.CONFIG_BASE_DIR
            + "/" + Global.modelName + ".json";
        File configFile = new File(configFilePath);
        if (configFile.exists()) {
            if (!configFile.isFile()) {
                throw new Exception(configFile + " exists but is not a file");
            }
        } else {
            File path = configFile.getParentFile();
            if (path.exists()) {
                if (!path.isFile()) {
                    throw new Exception(path + " exists but is not a directory");
                }
            } else {
                if (!path.mkdirs()) {
                    throw new Exception("mkdirs failed: " + path);
                }
            }
            if (!configFile.createNewFile()) {
                throw new Exception("create new file failed: " + configFile);
            }
        }
        Files.write(configFile.toPath(), content.getBytes());
        assert Logger.debug("config file saved successfully");
    }
}
