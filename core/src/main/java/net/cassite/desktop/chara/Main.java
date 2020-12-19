// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.cassite.desktop.chara.control.GlobalMouse;
import net.cassite.desktop.chara.graphic.UStage;
import net.cassite.desktop.chara.i18n.I18nConsts;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.manager.ModelManager;
import net.cassite.desktop.chara.manager.PluginManager;
import net.cassite.desktop.chara.util.*;
import vproxybase.dns.Resolver;
import vproxybase.util.Callback;
import vproxybase.util.Tuple3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class Main extends Application {
    @Override
    public void start(Stage javafxPrimaryStage) {
        preWork(() -> ThreadUtils.get().runOnFX(() -> {
            // get config
            Boolean showIconOnTaskbar = ConfigManager.get().getShowIconOnTaskbar();
            if (Utils.isWindows()) {
                if (showIconOnTaskbar == null) {
                    showIconOnTaskbar = Global.model.data().defaultShowIconOnTaskbar;
                    ConfigManager.get().setShowIconOnTaskbar(showIconOnTaskbar);
                }
            } else {
                showIconOnTaskbar = true;
                ConfigManager.get().setShowIconOnTaskbar(true);
            }
            Stage primaryStage;
            if (!showIconOnTaskbar) {
                Logger.info("hide icon on taskbar");
                primaryStage = new Stage();
                StageUtils.primaryStage = primaryStage;
                StageUtils.primaryTemporaryStage = javafxPrimaryStage;
                StageUtils.configureTransparentTemporaryUtilityStage(javafxPrimaryStage);
                primaryStage.initOwner(javafxPrimaryStage);
            } else {
                primaryStage = javafxPrimaryStage;
                StageUtils.primaryStage = primaryStage;
            }

            // construct root element
            Pane rootPane = new Pane();
            rootPane.setBackground(Background.EMPTY);
            Pane rootScalePane = new Pane();
            rootScalePane.setBackground(Background.EMPTY);
            rootPane.getChildren().add(rootScalePane);

            // scale
            Scale scale = new Scale();
            rootScalePane.getTransforms().add(scale);

            // put it into stage
            Scene scene = new Scene(rootPane);
            scene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(scene);

            // init
            App app = new App(primaryStage, scene, rootPane, rootScalePane, scale);
            app.init();

            // stage config
            setName(primaryStage, rootPane);
            Utils.setIcon(primaryStage, Global.modelIcon);
            if (StageUtils.primaryTemporaryStage != null) {
                StageUtils.primaryTemporaryStage.show();
            }
            primaryStage.show();

            // enable implicit exit
            Platform.setImplicitExit(true);

            app.ready();
        }));
    }

    private void setName(Stage primaryStage, Pane rootPane) {
        primaryStage.setTitle(Global.model.name());
        var os = System.getProperty("os.name");
        if (os != null && os.startsWith("Mac")) {
            MenuBar menuBar = new MenuBar();
            menuBar.setUseSystemMenuBar(true);

            Menu menu = new Menu(Global.model.name());
            MenuItem openLauncher = new MenuItem(I18nConsts.OPEN_LAUNCHER.get()[0]);
            openLauncher.setOnAction(e -> {
                String[] cmd = new String[]{"/usr/bin/open", "/Applications/chara.app"};
                try {
                    Runtime.getRuntime().exec(cmd);
                } catch (IOException ex) {
                    Logger.error("failed executing cmd " + Arrays.toString(cmd), ex);
                }
            });
            menu.getItems().add(openLauncher);

            menuBar.getMenus().add(menu);

            BorderPane bp = new BorderPane(menuBar);
            bp.setMaxWidth(1);
            bp.setMaxHeight(1);
            bp.setLayoutX(-2);
            bp.setLayoutY(-2);
            rootPane.getChildren().add(bp);
        }
    }

    private void preWork(Runnable cb) {
        launchDnsResolver();
        registerNativeHook();
        loadDefaultIcon();
        loadPlugins(() ->
            chooseModel(() ->
                chooseModelFile(() ->
                    loadModel(() ->
                        loadCommonItemsFromModel(() ->
                            loadImagesAndResources(Global.model.requiredImages(), Global.model.resourceHandlers(), cb)
                        )
                    )
                )
            )
        );
    }

    private void launchDnsResolver() {
        Resolver.getDefault();
    }

    private void registerNativeHook() {
        // disable native mouse logger
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalMouse.class.getPackageName());
        LogManager.getLogManager().reset();
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        // enable global events
        GlobalMouse.enable();
    }

    private void loadDefaultIcon() {
        InputStream inputStream = Main.class.getResourceAsStream("/chara.png");
        if (inputStream == null) {
            Logger.warn("/chara.png for icon not found");
            return;
        }
        Global.charaDefaultIcon = new Image(inputStream);
    }

    private void loadPlugins(Runnable cb) {
        PluginManager.get().load(cb);
    }

    private void chooseModel(Runnable cb) {
        ThreadUtils.get().submit(() -> {
            // get config files
            File configFileDir = new File(System.getProperty("user.home") + "/" + Consts.CONFIG_BASE_DIR);
            List<String> configs = null;
            if (configFileDir.exists()) {
                if (!configFileDir.isDirectory()) {
                    Logger.fatal("config file path " + configFileDir + " exists but is not a directory");
                    return;
                }
                File[] files = configFileDir.listFiles(f -> f.isFile() && f.getName().endsWith(".json"));
                if (files == null) {
                    Logger.fatal("getting files in config directory failed");
                    return;
                }
                if (files.length != 0) {
                    configs = new ArrayList<>(files.length);
                    for (var f : files) {
                        var s = f.getName();
                        configs.add(s.substring(0, s.length() - ".json".length()));
                    }
                }
            }

            if (configs == null || configs.isEmpty()) {
                assert Logger.debug("no config file, do not show config selection page");
                cb.run();
                return;
            }

            assert Logger.debug("config files found, show config selection page");
            final var finalConfigs = configs;
            ThreadUtils.get().runOnFX(() -> {
                UStage chooseModelConfigStage = new UStage();
                chooseModelConfigStage.setPaneWidth(256);
                chooseModelConfigStage.setPaneHeight(325);
                chooseModelConfigStage.centerOnScreen();
                chooseModelConfigStage.setTitle(I18nConsts.SELECT_EXISTING_MODEL_CONFIG.get()[0]);
                chooseModelConfigStage.setIcon(Global.charaDefaultIcon);

                Pane root = chooseModelConfigStage.getRootPane();

                ListView<String> listView = new ListView<>();
                listView.setPrefWidth(236);
                listView.setPrefHeight(230);
                listView.setLayoutX(10);
                listView.setLayoutY(10);
                var listData = FXCollections.observableList(finalConfigs);
                listView.setItems(listData);
                root.getChildren().add(listView);

                Button okBtn = new Button(I18nConsts.OK_BTN.get()[0]);
                okBtn.setPrefWidth(236);
                okBtn.setPrefHeight(30);
                okBtn.setLayoutX(10);
                okBtn.setLayoutY(250);
                okBtn.setDisable(true);
                root.getChildren().add(okBtn);

                Button chooseFileBtn = new Button(I18nConsts.CHOOSE_FILE_BTN.get()[0]);
                chooseFileBtn.setPrefWidth(236);
                chooseFileBtn.setPrefHeight(30);
                chooseFileBtn.setLayoutX(10);
                chooseFileBtn.setLayoutY(285);
                root.getChildren().add(chooseFileBtn);

                listView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<String>) c -> okBtn.setDisable(false));
                okBtn.setOnMouseClicked(e -> {
                    Global.modelName = listView.getSelectionModel().getSelectedItem();
                    assert Logger.debug("using config for model " + Global.modelName);
                    Platform.setImplicitExit(false);
                    chooseModelConfigStage.hide();
                    cb.run();
                });
                chooseFileBtn.setOnMouseClicked(e -> {
                    // do not select model, let the next step choose a file
                    assert Logger.debug("clicked Other button, choose model file later");
                    Platform.setImplicitExit(false);
                    chooseModelConfigStage.hide();
                    cb.run();
                });

                chooseModelConfigStage.show();
                Platform.setImplicitExit(true);
                chooseModelConfigStage.setOnCloseRequest(e -> {
                    Platform.setImplicitExit(false);
                    Logger.fatal("program exits while choosing model");
                });
            });
        });
    }

    private void chooseModelFile(Runnable cb) {
        ThreadUtils.get().runOnFX(() -> {
            // should run from UI thread

            if (Global.modelName != null) {
                String modelFile = ConfigManager.get().getModelFile();
                if (modelFile != null) {
                    File f = new File(modelFile);
                    if (f.isFile()) {
                        Logger.info("model file config exists: " + f);
                        Global.modelFilePath = f.getAbsolutePath();
                        cb.run();
                        return;
                    }
                }
                ConfigManager.get().setModelFile(null);
                if (modelFile != null) {
                    new Alert(Alert.AlertType.INFORMATION,
                        "the old model file is not valid anymore, please choose a new one")
                        .showAndWait();
                }
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(I18nConsts.CHOOSE_MODEL_FILE.get()[0]);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "model file",
                "*.model", "*.MODEL"));
            File file = fileChooser.showOpenDialog(null);
            if (file == null) {
                Logger.fatal("no model file selected");
                return;
            }
            Global.modelFilePath = file.getAbsolutePath();

            cb.run();
        });
    }

    private void loadModel(Runnable cb) {
        ThreadUtils.get().submit(() -> {
            // should run from thread pool

            Global.model = ModelManager.load(Global.modelFilePath);
            if (Global.model == null) {
                // will shutdown
                return;
            }
            if (Global.modelName != null) {
                if (!Global.modelName.equals(Global.model.name())) {
                    Logger.fatal("possible difference between config file and model file, remove the config and try again");
                    return;
                }
            }
            Global.modelName = Global.model.name();
            ConfigManager.get().setModelFile(Global.modelFilePath);
            cb.run();
        });
    }

    private void loadCommonItemsFromModel(Runnable cb) {
        ThreadUtils.get().submit(() -> {
            // should run from thread pool

            // icon
            {
                var iconInputStream = ModelManager.getEntryFromModel("icon.png");
                if (iconInputStream == null) {
                    Logger.warn("missing icon in model");
                    cb.run();
                    return;
                }
                Global.modelIcon = new Image(iconInputStream);
                try {
                    iconInputStream.close();
                } catch (IOException ignore) {
                }
            }

            cb.run();
        });
    }

    private void loadImagesAndResources(List<String> requiredImages, List<ResourceHandler> resourceHandlers, Runnable cb) {
        double allResourceRatioSum = 0;
        for (var h : resourceHandlers) {
            allResourceRatioSum += h.progressRatio;
        }
        allResourceRatioSum += requiredImages.size();
        final var finalAllResourceRatioSum = allResourceRatioSum;

        List<ResourceHandler> imageAndResourceHandlers = new ArrayList<>(requiredImages.size() + resourceHandlers.size());
        for (var imageStr : requiredImages) {
            imageAndResourceHandlers.add(new ImageResourceHandler(imageStr));
        }
        imageAndResourceHandlers.addAll(resourceHandlers);

        ThreadUtils.get().runOnFX(() -> {
            // should run from UI thread

            var loadingTup = StageUtils.createLoadingBarStage();
            loadingTup._1.show();
            Platform.setImplicitExit(true);

            loadResources(loadingTup, imageAndResourceHandlers, finalAllResourceRatioSum, () ->
                ThreadUtils.get().runOnFX(() -> {
                    Platform.setImplicitExit(false);
                    loadingTup._1.hide();
                    cb.run();
                }));
        });
    }

    private void loadResources(Tuple3<UStage, ProgressBar, Label> loadingTuple, List<ResourceHandler> resourceHandlers,
                               double allResourceRatioSum, Runnable cb) {
        ThreadUtils.get().runOnFX(() ->
            recursiveLoadResources(
                resourceHandlers.iterator(), allResourceRatioSum,
                loadingTuple, 0,
                cb));
    }

    private void recursiveLoadResources(Iterator<ResourceHandler> iterator, double allResourceRatioSum,
                                        Tuple3<UStage, ProgressBar, Label> loadingTuple, double lastProcess,
                                        Runnable cb) {
        if (!iterator.hasNext()) {
            loadingTuple._2.setProgress(1);
            loadingTuple._3.setText("");
            cb.run();
            return;
        }
        if (ThreadUtils.get().isShutdown()) {
            return;
        }
        var handler = iterator.next();
        loadingTuple._3.setText(handler.entrySuffix);

        ThreadUtils.get().submit(() -> {
            String entry = Global.model.name() + "/" + handler.entrySuffix;
            InputStream inputStream = ModelManager.getEntryFromModel(entry);
            if (inputStream == null) {
                Logger.fatal("cannot find entry " + entry + " in model file");
                return;
            }
            Logger.info("loading " + entry + " for model " + Global.model.name());
            try {
                handler.handler.accept(inputStream, new Callback<>() {
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
                            double now = lastProcess + handler.progressRatio / allResourceRatioSum;
                            loadingTuple._2.setProgress(now);
                            recursiveLoadResources(iterator, allResourceRatioSum,
                                loadingTuple, now,
                                cb);
                        });
                    }

                    @Override
                    protected void onFailed(Exception e) {
                        Logger.fatal("failed handling " + entry, e);
                    }
                });
            } catch (Exception e) {
                Logger.fatal("failed handling " + entry, e);
            }
        });
    }
}
