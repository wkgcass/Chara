// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.vpwsagent;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.chara.Chara;
import net.cassite.desktop.chara.graphic.Alert;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.model.Model;
import net.cassite.desktop.chara.model.vpwsagent.AgentConsts;
import net.cassite.desktop.chara.model.vpwsagent.Resources;
import net.cassite.desktop.chara.util.Logger;
import vproxyx.WebSocksProxyAgent;
import vproxyx.util.Browser;
import vproxyx.websocks.ConfigLoader;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public class Agent implements Chara {
    private final AppCallback appCallback;
    private final AgentConsts consts;
    private final Data data;

    private final Group iconGroup = new Group();
    private final ImageView idleView;
    private final ImageView launchingView;
    private final ImageView runningView;

    private final MenuItem stopMenuItem;
    private final MenuItem editMenuItem;
    private final MenuItem discardCurrentConfigMenuItem;

    private WebSocksProxyAgent agent;

    public Agent(Model.ConstructParams params, Resources resources, AgentConsts consts) {
        this.appCallback = params.cb;
        this.consts = consts;
        this.data = new DataBuilder()
            .setImageWidth(consts.boundsRadius * 2)
            .setImageHeight(consts.boundsRadius * 2)
            .setMinWidth(consts.boundsRadius * 2)
            .setInitialWidth(consts.boundsRadius * 2)
            .setMinX(0)
            .setMaxX(consts.boundsRadius * 2)
            .setTopMiddleX(consts.boundsRadius)
            .setBottomMiddleX(consts.boundsRadius)
            .setMessageOffsetX(consts.boundsRadius + 10)
            .setMessageAtMinY(consts.boundsRadius - 10)
            .setMinY(0)
            .setMaxY(consts.boundsRadius * 2)
            .build();

        Group parent = params.parent;
        Menu menu = params.characterMenu;

        idleView = new ImageView(resources.idleImage);
        idleView.setSmooth(true);
        idleView.setFitWidth(consts.iconSide);
        idleView.setFitHeight(consts.iconSide);

        launchingView = new ImageView(resources.launchingImage);
        launchingView.setSmooth(true);
        launchingView.setFitWidth(consts.iconSide);
        launchingView.setFitHeight(consts.iconSide);

        runningView = new ImageView(resources.runningImage);
        runningView.setSmooth(true);
        runningView.setFitWidth(consts.iconSide);
        runningView.setFitHeight(consts.iconSide);

        Circle bounds = new Circle(consts.boundsRadius);
        bounds.setCenterX(consts.boundsRadius);
        bounds.setCenterY(consts.boundsRadius);
        bounds.setStrokeWidth(0);
        bounds.setFill(consts.shadowColor);
        parent.getChildren().add(bounds);

        Circle background = new Circle(consts.backgroundRadius);
        background.setCenterX(consts.boundsRadius);
        background.setCenterY(consts.boundsRadius);
        background.setStrokeWidth(0);
        background.setFill(consts.backgroundColor);
        parent.getChildren().add(background);

        iconGroup.setLayoutX(consts.iconX);
        iconGroup.setLayoutY(consts.iconY);
        parent.getChildren().add(iconGroup);

        stopMenuItem = new MenuItem(AgentConsts.stopMenuItem.get()[0]);
        stopMenuItem.setOnAction(e -> stop());
        menu.getItems().add(stopMenuItem);
        editMenuItem = new MenuItem(AgentConsts.editMenuItem.get()[0]);
        editMenuItem.setOnAction(e -> createOrEdit());
        menu.getItems().add(editMenuItem);
        var importMenuItem = new MenuItem(AgentConsts.importMenuItem.get()[0]);
        importMenuItem.setOnAction(e -> importConfig());
        menu.getItems().add(importMenuItem);
        discardCurrentConfigMenuItem = new MenuItem(AgentConsts.discardCurrentConfigMenuItem.get()[0]);
        discardCurrentConfigMenuItem.setOnAction(e -> discard());
        menu.getItems().add(discardCurrentConfigMenuItem);
    }

    private String getConfigFilePath() {
        return ConfigManager.get().getStringValue(AgentConsts.configFilePath);
    }

    private void setConfigFilePath(String path) {
        ConfigManager.get().setStringValue(AgentConsts.configFilePath, path);
        if (path.isEmpty()) {
            editMenuItem.setText(AgentConsts.newMenuItem.get()[0]);
            discardCurrentConfigMenuItem.setDisable(true);
        } else {
            editMenuItem.setText(AgentConsts.editMenuItem.get()[0]);
            discardCurrentConfigMenuItem.setDisable(false);
        }
        if (agent != null) {
            Alert.alert(AgentConsts.configApplyAfterRestarted.get()[0]);
        }
    }

    @Override
    public void ready(ReadyParams params) {
        appCallback.setAlwaysHideBar(true);
        iconGroup.getChildren().add(idleView);

        stopMenuItem.setDisable(true);
        if (getConfigFilePath().isEmpty()) {
            editMenuItem.setText(AgentConsts.newMenuItem.get()[0]);
            discardCurrentConfigMenuItem.setDisable(true);
        }
    }

    private boolean isLaunching = false;

    private void launch() {
        if (agent != null) {
            return;
        }
        if (isLaunching) {
            return;
        }

        // check file
        String configFilePath = getConfigFilePath();
        if (configFilePath.isEmpty()) {
            // try to find the default config file
            String defaultConfigFilePath = vproxybase.util.Utils.homefile("vpws-agent.conf");
            File f = new File(defaultConfigFilePath);
            if (f.isFile()) {
                var alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                alert.setTitle(AgentConsts.doYouWantToUseDefaultConfigFile.get()[0]);
                alert.setHeaderText(AgentConsts.doYouWantToUseDefaultConfigFile.get()[0]);
                alert.setContentText(AgentConsts.confirmUseDefaultConfigFileContent.get()[0]);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    setConfigFilePath(defaultConfigFilePath);
                    configFilePath = defaultConfigFilePath;
                }
            } else {
                // still not found
                Alert.alert(AgentConsts.noConfigToLoad.get()[0]);
                return;
            }
        }

        isLaunching = true;
        Logger.info("launching ...");
        iconGroup.getChildren().clear();
        iconGroup.getChildren().add(launchingView);

        final String finalConfigFilePath = configFilePath;
        new Thread(() -> {
            try {
                ConfigLoader loader = new ConfigLoader();
                try {
                    loader.load(finalConfigFilePath);
                } catch (Exception e) {
                    Logger.warn("config file is invalid, will be cleared", e);
                    setConfigFilePath("");
                    throw e;
                }

                WebSocksProxyAgent agent = new WebSocksProxyAgent();
                agent.setConfigLoader(loader);
                agent.launch();

                this.agent = agent;
            } catch (Exception e) {
                Logger.error("launching failed", e);
            } finally {
                if (agent == null) {
                    Logger.warn("launching failed");
                    Platform.runLater(() -> {
                        isLaunching = false;
                        iconGroup.getChildren().clear();
                        iconGroup.getChildren().add(idleView);
                    });
                } else {
                    Logger.info("launched");
                    Platform.runLater(() -> {
                        isLaunching = false;
                        stopMenuItem.setDisable(false);
                        iconGroup.getChildren().clear();
                        iconGroup.getChildren().add(runningView);
                    });
                }
            }
        }).start();
    }

    private boolean stopping = false;

    private void stop() {
        if (agent == null) {
            return;
        }
        if (stopping) {
            return;
        }
        stopping = true;
        Logger.info("stopping ...");
        new Thread(() -> {
            agent.stop();
            Platform.runLater(() -> {
                agent = null;
                stopMenuItem.setDisable(true);
                iconGroup.getChildren().clear();
                iconGroup.getChildren().add(idleView);
                stopping = false;
            });
        }).start();
    }

    private void createOrEdit() {
        String filePath = getConfigFilePath();
        if (filePath.isEmpty()) {
            launchBrowserForEditing(false);
        } else {
            ConfigLoader loader = new ConfigLoader();
            try {
                loader.load(filePath);
            } catch (Exception e) {
                Logger.error("loading config failed", e);
            }
            String json = loader.toJson().stringify();
            Logger.info("current config: " + json);
            String base64 = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(AgentConsts.BASE64PREFIX + base64);
            clipboard.setContent(content);
            launchBrowserForEditing(true);
        }
    }

    private void launchBrowserForEditing(boolean useClipboard) {
        Browser.open("https://vproxy-tools.github.io/vpwsui/index.html?useClipboard=" + useClipboard);
    }

    private void importConfig() {
        if (getConfigFilePath().isEmpty()) {
            doImport();
        } else {
            var alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle(AgentConsts.areYouSureToImport.get()[0]);
            alert.setHeaderText(AgentConsts.areYouSureToImport.get()[0]);
            alert.setContentText(AgentConsts.confirmImportContent.get()[0]);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                doImport();
            }
        }
    }

    private void doImport() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(AgentConsts.configFileChooserTitle.get()[0]);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
            "vpws-agent config file",
            "*.conf", "*.CONF"));
        File file = chooser.showOpenDialog(null);
        if (file == null) {
            return;
        }
        if (!file.isFile()) {
            Logger.error(file.getAbsolutePath() + " is not a file");
            return;
        }
        ConfigLoader configLoader = new ConfigLoader();
        try {
            configLoader.load(file.getAbsolutePath());
        } catch (Exception e) {
            Logger.error("loading " + file.getAbsolutePath() + " failed", e);
            return;
        }
        // loading succeeded
        setConfigFilePath(file.getAbsolutePath());
    }

    private void discard() {
        if (getConfigFilePath().isEmpty()) {
            return;
        }
        var alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle(AgentConsts.areYouSureToDiscard.get()[0]);
        alert.setHeaderText(AgentConsts.areYouSureToDiscard.get()[0]);
        alert.setContentText(AgentConsts.confirmDiscardContent.get()[0]);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            setConfigFilePath("");
        }
    }

    @Override
    public void mouseMove(double x, double y) {
        // ignore
    }

    @Override
    public void mouseLeave() {
        // ignore
    }

    @Override
    public void dragged() {
        // ignore
    }

    @Override
    public void click(double x, double y) {
        if ((x - consts.boundsRadius) * (x - consts.boundsRadius)
            + (y - consts.boundsRadius) * (y - consts.boundsRadius)
            < consts.boundsRadius * consts.boundsRadius) {
            // inside circle
            launch();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // ignore
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ignore
    }

    @Override
    public Data data() {
        return data;
    }

    @Override
    public int shutdown(Runnable cb) {
        return 0;
    }

    @Override
    public void release() {
        if (agent != null) {
            agent.stop();
            agent = null;
        }
    }

    @Override
    public void takeMessage(String msg) {
        // ignore
    }

    @Override
    public boolean getDebugInfo(ClipboardContent content) {
        content.putString(agent == null ? "null" : agent.toString());
        return true;
    }

    @Override
    public void takeDebugMessage(Clipboard clipboard) {
        // ignore
    }
}
