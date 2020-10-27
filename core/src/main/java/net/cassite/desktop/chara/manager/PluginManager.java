// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.manager;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.util.*;
import vjson.JSON;
import vproxybase.util.Callback;
import vproxybase.util.Tuple;
import vproxybase.util.Tuple3;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PluginManager {
    private static final PluginManager instance = new PluginManager();
    private final Map<String, Plugin> plugins = new LinkedHashMap<>();

    private PluginManager() {
    }

    public static PluginManager get() {
        return instance;
    }

    public Collection<Plugin> getPlugins() {
        return plugins.values();
    }

    public void load(Runnable cb) {
        File pluginFileDir = new File(System.getProperty("user.home") + "/" + Consts.PLUGIN_BASE_DIR);
        if (!pluginFileDir.isDirectory()) {
            Logger.warn(pluginFileDir + " is not a directory, plugins will not be loaded");
            cb.run();
            return;
        }
        File[] files = pluginFileDir.listFiles(f -> f.getName().endsWith(".plugin"));
        if (files == null) {
            Logger.warn("failed to list plugin files in dir " + pluginFileDir);
            cb.run();
            return;
        }
        if (files.length == 0) {
            assert Logger.debug("no plugin files found");
            cb.run();
            return;
        }
        List<Tuple<Plugin, ZipFile>> allPlugins = new LinkedList<>();
        for (File f : files) {
            try {
                var tup = preLoad(f);
                if (tup == null) {
                    return; // error should already been reported, program will be terminated
                }
                allPlugins.add(tup);
            } catch (Exception e) {
                Logger.fatal("loading plugin " + f.getAbsolutePath() + " failed, please remove the plugin", e);
                return;
            }
        }
        postLoad(allPlugins, () -> {
            if (ThreadUtils.get().isShutdown()) {
                return;
            }
            // launch all plugins
            for (var tup : allPlugins) {
                tup.left.launch();
                Logger.info("plugin " + tup.left.name() + " loaded");
                try {
                    tup.right.close();
                } catch (IOException ignore) {
                }
            }
            // callback
            cb.run();
        });
    }

    private Tuple<Plugin, ZipFile> preLoad(File file) throws Exception {
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
        inputStream.close();

        JSON.Object pluginJson = (JSON.Object) JSON.parse(sb.toString());
        String name = pluginJson.getString("name");
        int version = pluginJson.getInt("version");
        int minVer = pluginJson.getInt("compatibleMinCodeVersion");
        int maxVer = pluginJson.getInt("compatibleMaxCodeVersion");
        String pluginClass = pluginJson.getString("pluginClass");
        if (plugins.containsKey(name)) {
            throw new Exception("plugin " + name + " is already loaded");
        }
        if (minVer > Consts.VERSION_NUM) {
            Logger.fatal("the plugin requires higher code version: " + Utils.verNum2Str(minVer));
            return null;
        }
        if (maxVer != -1 && maxVer < Consts.VERSION_NUM) {
            Logger.fatal("the plugin requires lower code version: " + Utils.verNum2Str(maxVer));
            return null;
        }

        Class<?> cls = Utils.loadClassFromZipFile(zipFile, "plugin-" + name, pluginClass);
        Plugin plugin = (Plugin) cls.getConstructor().newInstance();

        // check plugin name and version
        if (!name.equals(plugin.name())) {
            Logger.fatal("name in the plugin config " + name +
                " is not the same as plugin.name() in code " + plugin.name());
            return null;
        }
        if (version != plugin.version()) {
            Logger.fatal("version in the plugin config " + Utils.verNum2Str(version) +
                " is not the same as plugin.version() in code " + Utils.verNum2Str(plugin.version()));
            return null;
        }

        // register
        plugins.put(name, plugin);

        assert Logger.debug("plugin " + name + " pre-loaded");
        return new Tuple<>(plugin, zipFile);
    }

    private void postLoad(List<Tuple<Plugin, ZipFile>> allPlugins, Runnable cb) {
        List<Tuple3<Plugin, ZipFile, ResourceHandler>> resourceHandlers = new LinkedList<>();
        double total = 0;
        for (var tup : allPlugins) {
            var resLs = tup.left.resourceHandlers();
            for (var res : resLs) {
                resourceHandlers.add(new Tuple3<>(tup.left, tup.right, res));
                total += res.progressRatio;
            }
        }
        final var finalTotal = total;

        var loadingTuple = StageUtils.createLoadingBarStage();
        loadingTuple._1.show();
        Platform.setImplicitExit(true);

        ThreadUtils.get().runOnFX(() ->
            recursiveLoad(resourceHandlers.iterator(), finalTotal,
                loadingTuple, 0, () -> {
                    Platform.setImplicitExit(false);
                    loadingTuple._1.hide();
                    cb.run();
                }));
    }

    private void recursiveLoad(Iterator<Tuple3<Plugin, ZipFile, ResourceHandler>> iterator, double total,
                               Tuple3<Stage, ProgressBar, Label> loadingTuple, double lastProgress, Runnable cb) {
        var bar = loadingTuple._2;
        var label = loadingTuple._3;
        if (!iterator.hasNext()) {
            bar.setProgress(1);
            label.setText("");
            cb.run();
            return;
        }
        if (ThreadUtils.get().isShutdown()) {
            return;
        }
        var handle = iterator.next();
        label.setText(handle._3.entrySuffix);

        ThreadUtils.get().submit(() -> {
            String entry = handle._1.name() + "/" + handle._3.entrySuffix;
            InputStream inputStream = Utils.getEntryFromZipFile(handle._2, entry);
            if (inputStream == null) {
                Logger.fatal("cannot find entry " + entry + " in plugin file " + handle._1.name());
                return;
            }
            Logger.info("loading " + entry + " for plugin " + handle._1.name());
            try {
                handle._3.handler.accept(inputStream, new Callback<>() {
                    @Override
                    protected void onSucceeded(Void aVoid) {
                        try {
                            inputStream.close();
                        } catch (IOException ignore) {
                        }
                        // wait 1 millisecond to make the cpu lower
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ignore) {
                        }
                        if (ThreadUtils.get().isShutdown()) {
                            return;
                        }

                        // increase progressbar and recurse
                        Platform.runLater(() -> {
                            double now = lastProgress + handle._3.progressRatio / total;
                            loadingTuple._2.setProgress(now);
                            recursiveLoad(iterator, total,
                                loadingTuple, now,
                                cb);
                        });
                    }

                    @Override
                    protected void onFailed(Exception e) {
                        Logger.fatal("failed handling " + entry + " for plugin " + handle._1.name(), e);
                    }
                });
            } catch (Exception e) {
                Logger.fatal("failed handling " + entry + " for plugin " + handle._1.name(), e);
            }
        });
    }

    public void release() {
        for (var p : plugins.values()) {
            p.release();
        }
        plugins.clear();
    }
}
