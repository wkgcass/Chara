// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.cassite.desktop.chara.control.NativeMouseListenerUtils;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.manager.ImageManager;
import net.cassite.desktop.chara.manager.ModelManager;
import net.cassite.desktop.chara.util.Consts;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Utils;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        preWork(() -> Platform.runLater(() -> {
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
            setIcon(primaryStage);
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
            menuBar.getMenus().add(new Menu(Global.model.name()));
            BorderPane bp = new BorderPane(menuBar);
            bp.setMaxWidth(1);
            bp.setMaxHeight(1);
            bp.setLayoutX(-2);
            bp.setLayoutY(-2);
            rootPane.getChildren().add(bp);
        }
    }

    private void setIcon(Stage primaryStage) {
        primaryStage.getIcons().add(Global.modelIcon);
        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                var image = SwingFXUtils.fromFXImage(Global.modelIcon, null);
                taskbar.setIconImage(image);
            }
        }
    }

    private void preWork(Runnable cb) {
        registerNativeHook();
        chooseModel(() ->
            chooseModelFile(() ->
                loadModel(() ->
                    loadCommonItemsFromModel(() ->
                        loadImages(Global.model.requiredImages(), cb)
                    )
                )
            )
        );
    }

    private void registerNativeHook() {
        // disable native mouse logger
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackageName());
        LogManager.getLogManager().reset();
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
        // enable native mouse
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeMouseMotionListener(NativeMouseListenerUtils.get());
        } catch (NativeHookException e) {
            Logger.error("register native hook failed\n" +
                "it's necessary for detecting the mouse movements", e);
        }
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
            Platform.runLater(() -> {
                Stage chooseModelConfigStage = new Stage();
                chooseModelConfigStage.initStyle(StageStyle.UTILITY);
                chooseModelConfigStage.setWidth(256);
                chooseModelConfigStage.setHeight(290);
                chooseModelConfigStage.setResizable(false);
                Utils.fixStageSize(chooseModelConfigStage, StageStyle.UTILITY);
                chooseModelConfigStage.centerOnScreen();
                chooseModelConfigStage.setTitle("Select existing model config");

                Pane root = new Pane();
                Scene scene = new Scene(root);
                chooseModelConfigStage.setScene(scene);

                ListView<String> listView = new ListView<>();
                listView.setPrefWidth(236);
                listView.setPrefHeight(230);
                listView.setLayoutX(10);
                listView.setLayoutY(10);
                var listData = FXCollections.observableList(finalConfigs);
                listView.setItems(listData);
                root.getChildren().add(listView);

                Button okBtn = new Button("OK");
                okBtn.setPrefWidth(236);
                okBtn.setPrefHeight(20);
                okBtn.setLayoutX(10);
                okBtn.setLayoutY(250);
                okBtn.setDisable(true);
                root.getChildren().add(okBtn);

                listView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<String>) c -> okBtn.setDisable(false));
                okBtn.setOnMouseClicked(e -> {
                    Global.modelName = listView.getSelectionModel().getSelectedItem();
                    assert Logger.debug("using config for model " + Global.modelName);
                    Platform.setImplicitExit(false);
                    chooseModelConfigStage.hide();
                    cb.run();
                });

                chooseModelConfigStage.show();
                chooseModelConfigStage.setOnCloseRequest(e -> {
                    if (Global.modelName == null) {
                        Logger.fatal("model config not selected");
                    }
                });
            });
        });
    }

    private void chooseModelFile(Runnable cb) {
        Platform.runLater(() -> {
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
            fileChooser.setTitle("Choose model file");
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
                    Logger.fatal("missing icon in model");
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

    private void loadImages(List<String> requiredImageList, Runnable cb) {
        Platform.runLater(() -> {
            // should run from UI thread

            Stage loadingStage = new Stage();
            loadingStage.initStyle(StageStyle.UTILITY);
            loadingStage.setWidth(600);
            loadingStage.setHeight(80);
            loadingStage.setResizable(false);
            loadingStage.setTitle("Loading...");
            Utils.fixStageSize(loadingStage, StageStyle.UTILITY);
            loadingStage.centerOnScreen();

            Pane pane = new Pane();
            Scene scene = new Scene(pane);
            loadingStage.setScene(scene);

            Label label = new Label();
            label.setLayoutX(10);
            label.setLayoutY(10);
            label.setPrefHeight(20);
            label.setText("Loading ...");
            pane.getChildren().add(label);

            ProgressBar progressBar = new ProgressBar();
            progressBar.setLayoutX(10);
            progressBar.setLayoutY(40);
            progressBar.setPrefWidth(580);
            progressBar.setPrefHeight(20);
            progressBar.setProgress(0);
            pane.getChildren().add(progressBar);

            loadingStage.show();

            recursiveLoadImages(requiredImageList.iterator(), requiredImageList.size(), 0, label, progressBar, loadingStage, cb);
        });
    }

    private void recursiveLoadImages(Iterator<String> iterator, int total, int currentIndex,
                                     Label label, ProgressBar progressBar,
                                     Stage loadingStage, Runnable runAfterLoading) {
        if (!iterator.hasNext()) {
            progressBar.setProgress(1);
            label.setText("");
            Platform.setImplicitExit(false);
            loadingStage.hide();

            runAfterLoading.run();
            return;
        }
        if (ThreadUtils.get().isShutdown()) {
            return;
        }

        progressBar.setProgress(((double) currentIndex) / total);
        String name = iterator.next();
        label.setText(name);

        ThreadUtils.get().submit(() -> {
            ImageManager.load(name);
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignore) {
            }
            if (ThreadUtils.get().isShutdown()) {
                return;
            }
            Platform.runLater(() ->
                recursiveLoadImages(iterator, total, currentIndex + 1,
                    label, progressBar,
                    loadingStage, runAfterLoading));
        });
    }
}
