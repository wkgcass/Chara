// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.manager;

import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.util.Consts;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Utils;
import vjson.JSON;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PluginManager {
    private static final PluginManager instance = new PluginManager();
    private final Map<String, Plugin> plugins = new HashMap<>();

    private PluginManager() {
    }

    public static PluginManager get() {
        return instance;
    }

    public void load() {
        File pluginFileDir = new File(System.getProperty("user.home") + "/" + Consts.PLUGIN_BASE_DIR);
        if (!pluginFileDir.isDirectory()) {
            Logger.warn(pluginFileDir + " is not a directory, plugins will not be loaded");
            return;
        }
        File[] files = pluginFileDir.listFiles(f -> f.getName().endsWith(".plugin"));
        if (files == null) {
            Logger.warn("failed to list plugin files in dir " + pluginFileDir);
            return;
        }
        if (files.length == 0) {
            assert Logger.debug("no plugin files found");
            return;
        }
        for (File f : files) {
            try {
                load(f);
            } catch (Exception e) {
                Logger.fatal("loading plugin " + f.getAbsolutePath() + " failed, please remove the plugin", e);
                return;
            }
        }
    }

    private void load(File file) throws Exception {
        ZipFile zipFile = new ZipFile(file);
        ZipEntry pluginJsonEntry = zipFile.getEntry("plugin.json");
        if (pluginJsonEntry == null) {
            throw new Exception("invalid plugin: plugin.json not found");
        }
        InputStream inputStream = zipFile.getInputStream(pluginJsonEntry);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        JSON.Object pluginJson = (JSON.Object) JSON.parse(sb.toString());
        String name = pluginJson.getString("name");
        int minVer = pluginJson.getInt("compatibleMinCodeVersion");
        int maxVer = pluginJson.getInt("compatibleMaxCodeVersion");
        String pluginClass = pluginJson.getString("pluginClass");
        if (plugins.containsKey(name)) {
            throw new Exception("plugin " + name + " is already loaded");
        }
        if (minVer > Consts.VERSION_NUM) {
            Logger.fatal("the plugin requires higher code version: " + Utils.verNum2Str(minVer));
            return;
        }
        if (maxVer != -1 && maxVer < Consts.VERSION_NUM) {
            Logger.fatal("the plugin requires lower code version: " + Utils.verNum2Str(maxVer));
            return;
        }

        Class<?> cls = Utils.loadClassFromZipFile(zipFile, "plugin-" + name, pluginClass);
        Plugin plugin = (Plugin) cls.getConstructor().newInstance();
        plugin.launch();

        plugins.put(name, plugin);

        br.close();
        zipFile.close();

        Logger.info("plugin " + name + " loaded");
    }

    public void release() {
        for (var p : plugins.values()) {
            p.release();
        }
        plugins.clear();
    }
}
