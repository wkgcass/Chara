// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.manager;

import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.util.Consts;
import net.cassite.desktop.chara.util.Key;
import net.cassite.desktop.chara.util.Logger;
import vjson.JSON;
import vjson.util.ObjectBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * The config manager
 */
public class ConfigManager {
    private static ConfigManager instance = null;

    /**
     * The config instance
     */
    public class Config {
        private String modelFile;
        private Double stageX;
        private Double stageY;
        private Double characterRatio;
        private Boolean chatFeatureEnabled;
        private Boolean alwaysOnTop;
        private Boolean activeInteractionEnabled;
        private Double bondPoint;
        private Double desirePoint;
        private final Map<Key<Integer>, Integer> integerRegisters = new ConcurrentHashMap<>();
        private final Map<Key<Double>, Double> doubleRegisters = new ConcurrentHashMap<>();
        private final Map<Key<Boolean>, Boolean> booleanRegisters = new ConcurrentHashMap<>();

        private String chatbot;
        private long lastTimestamp;

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

        public Boolean getChatFeatureEnabled() {
            return chatFeatureEnabled;
        }

        public void setChatFeatureEnabled(boolean chatFeatureEnabled) {
            this.chatFeatureEnabled = chatFeatureEnabled;
            save();
        }

        public Boolean getAlwaysOnTop() {
            return alwaysOnTop;
        }

        public void setAlwaysOnTop(boolean alwaysOnTop) {
            this.alwaysOnTop = alwaysOnTop;
            save();
        }

        public Boolean getActiveInteractionEnabled() {
            return activeInteractionEnabled;
        }

        public void setActiveInteractionEnabled(boolean activeInteractionEnabled) {
            this.activeInteractionEnabled = activeInteractionEnabled;
            save();
        }

        public Double getBondPoint() {
            return bondPoint;
        }

        public void setBondPoint(Double bondPoint) {
            this.bondPoint = bondPoint;
            save();
        }

        public Double getDesirePoint() {
            return desirePoint;
        }

        public void setDesirePoint(Double desirePoint) {
            this.desirePoint = desirePoint;
            save();
        }

        public String getChatbot() {
            return chatbot;
        }

        public int getIntValue(Key<Integer> key) {
            Integer n = integerRegisters.get(key);
            if (n == null) {
                return 0;
            }
            return n;
        }

        public void setIntValue(Key<Integer> key, int v) {
            integerRegisters.put(key, v);
            save();
        }

        public synchronized int incIntValue(Key<Integer> key, int inc) {
            int n = getIntValue(key) + inc;
            setIntValue(key, n);
            return n;
        }

        public double getDoubleValue(Key<Double> key) {
            Double n = doubleRegisters.get(key);
            if (n == null) {
                return 0;
            }
            return n;
        }

        public void setDoubleValue(Key<Double> key, double v) {
            doubleRegisters.put(key, v);
            save();
        }

        public synchronized double incDoubleValue(Key<Double> key, double inc) {
            double n = getDoubleValue(key) + inc;
            setDoubleValue(key, n);
            return n;
        }

        public boolean getBoolValue(Key<Boolean> key) {
            Boolean b = booleanRegisters.get(key);
            if (b == null) {
                return false;
            }
            return b;
        }

        public void setBoolValue(Key<Boolean> key, boolean v) {
            booleanRegisters.put(key, v);
            save();
        }

        public long getLastTimestamp() {
            return lastTimestamp;
        }

        public void setLastTimestamp(long lastTimestamp) {
            this.lastTimestamp = lastTimestamp;
            save();
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
            if (obj.containsKey("chat_feature_enabled")) {
                var o = obj.get("chat_feature_enabled");
                if (o instanceof JSON.Bool) {
                    this.chatFeatureEnabled = ((JSON.Bool) o).booleanValue();
                }
            }
            if (obj.containsKey("always_on_top")) {
                var o = obj.get("always_on_top");
                if (o instanceof JSON.Bool) {
                    this.alwaysOnTop = ((JSON.Bool) o).booleanValue();
                }
            }
            if (obj.containsKey("active_interaction_enabled")) {
                var o = obj.get("active_interaction_enabled");
                if (o instanceof JSON.Bool) {
                    this.activeInteractionEnabled = ((JSON.Bool) o).booleanValue();
                }
            }
            if (obj.containsKey("bond_point")) {
                var o = obj.get("bond_point");
                if (o instanceof JSON.Double) {
                    this.bondPoint = ((JSON.Double) o).doubleValue();
                }
            }
            if (obj.containsKey("desire_point")) {
                var o = obj.get("desire_point");
                if (o instanceof JSON.Double) {
                    this.desirePoint = ((JSON.Double) o).doubleValue();
                }
            }
            if (obj.containsKey("chatbot")) {
                var o = obj.get("chatbot");
                if (o instanceof JSON.String) {
                    this.chatbot = ((JSON.String) o).toJavaObject();
                }
            }
            if (obj.containsKey("integerRegisters")) {
                var o = obj.get("integerRegisters");
                if (o instanceof JSON.Object) {
                    var oo = (JSON.Object) o;
                    for (var key : oo.keySet()) {
                        var v = oo.get(key);
                        if (v instanceof JSON.Integer) {
                            this.integerRegisters.put(Key.of(key, Integer.class), ((JSON.Integer) v).intValue());
                        }
                    }
                }
            }
            if (obj.containsKey("doubleRegisters")) {
                var o = obj.get("doubleRegisters");
                if (o instanceof JSON.Object) {
                    var oo = (JSON.Object) o;
                    for (var key : oo.keySet()) {
                        var v = oo.get(key);
                        if (v instanceof JSON.Double) {
                            this.doubleRegisters.put(Key.of(key, Double.class), ((JSON.Double) v).doubleValue());
                        }
                    }
                }
            }
            if (obj.containsKey("booleanRegisters")) {
                var o = obj.get("booleanRegisters");
                if (o instanceof JSON.Object) {
                    var oo = (JSON.Object) o;
                    for (var key : oo.keySet()) {
                        var v = oo.get(key);
                        if (v instanceof JSON.Bool) {
                            this.booleanRegisters.put(Key.of(key, Boolean.class), ((JSON.Bool) v).booleanValue());
                        }
                    }
                }
            }
            if (obj.containsKey("last_timestamp")) {
                var o = obj.get("last_timestamp");
                if (o instanceof JSON.Long) {
                    this.lastTimestamp = ((JSON.Long) o).longValue();
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
            if (chatFeatureEnabled != null) {
                ob.put("chat_feature_enabled", chatFeatureEnabled);
            }
            if (alwaysOnTop != null) {
                ob.put("always_on_top", alwaysOnTop);
            }
            if (activeInteractionEnabled != null) {
                ob.put("active_interaction_enabled", activeInteractionEnabled);
            }
            if (desirePoint != null) {
                ob.put("desire_point", desirePoint);
            }
            if (chatbot != null) {
                ob.put("chatbot", chatbot);
            }
            if (!integerRegisters.isEmpty()) {
                ob.putObject("integerRegisters", o -> {
                    for (var key : integerRegisters.keySet()) {
                        o.put(key.getName(), integerRegisters.get(key));
                    }
                });
            }
            if (!doubleRegisters.isEmpty()) {
                ob.putObject("doubleRegisters", o -> {
                    for (var key : doubleRegisters.keySet()) {
                        o.put(key.getName(), doubleRegisters.get(key));
                    }
                });
            }
            if (!booleanRegisters.isEmpty()) {
                ob.putObject("booleanRegisters", o -> {
                    for (var key : booleanRegisters.keySet()) {
                        o.put(key.getName(), booleanRegisters.get(key));
                    }
                });
            }
            ob.put("last_timestamp", lastTimestamp);
            return ob.build().pretty();
        }

        @Override
        public String toString() {
            return "Config{" +
                "modelFile='" + modelFile + '\'' +
                ", stageX=" + stageX +
                ", stageY=" + stageY +
                ", characterRatio=" + characterRatio +
                ", bondPoint=" + bondPoint +
                ", desirePoint=" + desirePoint +
                ", integerRegisters=" + integerRegisters +
                ", doubleRegisters=" + doubleRegisters +
                ", booleanRegisters=" + booleanRegisters +
                ", chatbot='" + chatbot + '\'' +
                ", lastTimestamp=" + lastTimestamp +
                '}';
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

        ThreadUtils.get().scheduleAtFixedRate(() -> {
            config.lastTimestamp = System.currentTimeMillis();
            save();
        }, 10, 10, TimeUnit.MINUTES);
    }

    /**
     * Get the <code>Config</code> instance
     *
     * @return <code>Config</code> instance
     */
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

    /**
     * Save config NOW<br>
     * The IO operation will take place on the current thread.<br>
     * Note that you do NOT have to call this method every time you modify a value,
     * the config object will save config in a gentle way when you called the setters of the config object.
     */
    public static void saveNow() {
        if (instance == null) {
            return;
        }
        try {
            instance.doSave();
        } catch (Exception e) {
            Logger.error("saving config failed", e);
        }
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
