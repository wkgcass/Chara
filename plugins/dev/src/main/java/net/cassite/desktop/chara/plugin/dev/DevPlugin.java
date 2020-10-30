// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.dev;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import net.cassite.desktop.chara.graphic.Alert;
import net.cassite.desktop.chara.graphic.Div;
import net.cassite.desktop.chara.graphic.StageTransformer;
import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.util.EventBus;
import net.cassite.desktop.chara.util.Events;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.ResourceHandler;

import java.util.*;

public class DevPlugin implements Plugin {
    private boolean enabled = false;
    @SuppressWarnings("rawtypes")
    private final Set<EventBus.WatchingRegistration> registrations = new HashSet<>();
    private StageTransformer primaryStage;
    private final Map<Integer, DevPoint> points = new HashMap<>();
    private final Div root = new Div();

    public DevPlugin() {
        root.setMouseTransparent(true);
    }

    @Override
    public String name() {
        return "dev";
    }

    @Override
    public int version() {
        return 1000000;
    }

    @Override
    public List<ResourceHandler> resourceHandlers() {
        return Collections.emptyList();
    }

    @Override
    public void launch() {
        registrations.add(EventBus.watch(Events.PrimaryStageReady, this::primaryStageReady));
        registrations.add(EventBus.watch(Events.PrimaryStageResized, this::resized));
        registrations.add(EventBus.watch(Events.MessageTaken, this::messageTaken));
        registrations.add(EventBus.watch(Events.MouseClickedImagePosition, e -> click(e[0], e[1])));

        if (primaryStage != null) {
            ((Group) primaryStage.getStage().getScene().getRoot()).getChildren().add(root);
        }

        enabled = false;
    }

    private void primaryStageReady(StageTransformer stage) {
        this.primaryStage = stage;
        var parent = (Pane) primaryStage.getStage().getScene().getRoot();
        parent.getChildren().add(root);

        if (enabled) {
            Alert.alert(DevPluginI18nConsts.devPluginEnabled.get()[0]);
        }
    }

    private void resized(Void v) {
        if (primaryStage == null) {
            return;
        }
        points.values().forEach(p -> p.calculatePosition(primaryStage));
    }

    private void messageTaken(String msg) {
        if (!msg.startsWith("::dev:")) {
            return;
        }
        msg = msg.substring("::dev:".length());
        if (msg.startsWith("point:")) {
            msg = msg.substring("point:".length());
            if (msg.equals("hide-all")) {
                points.values().forEach(p -> p.removeFrom(root));
                points.clear();
                return;
            }

            String[] split = msg.split(":");
            if (split.length != 2) {
                Logger.error("invalid command, expecting ::dev:point:${index}:${action}");
                return;
            }
            String numStr = split[0];
            String action = split[1];

            int num;
            try {
                num = Integer.parseInt(numStr);
            } catch (NumberFormatException e) {
                Logger.error("invalid command, " + numStr + " is not a valid integer");
                return;
            }
            var point = points.get(num);
            if (point == null) {
                return;
            }
            if (action.equals("hide")) {
                point.removeFrom(root);
                points.remove(num);
            } else {
                Logger.error("unknown action " + action + " for point" + num);
            }
        }
    }

    private void click(double x, double y) {
        if (!enabled) {
            return;
        }
        if (primaryStage == null) {
            return;
        }
        int i = 0;
        for (; ; ++i) {
            if (!points.containsKey(i)) {
                break;
            }
        }
        var point = new DevPoint(i, x, y);
        point.addTo(root);
        point.calculatePosition(primaryStage);
        points.put(i, point);
    }

    @Override
    public void clicked() {
        enabled = !enabled;
        if (enabled) {
            Alert.alert(DevPluginI18nConsts.devPluginEnabled.get()[0]);
        } else {
            Alert.alert(DevPluginI18nConsts.devPluginDisabled.get()[0]);
        }
    }

    @Override
    public void release() {
        for (var registration : registrations) {
            registration.cancel();
        }
        registrations.clear();
        if (primaryStage != null) {
            ((Pane) primaryStage.getStage().getScene().getRoot()).getChildren().remove(root);
        }
        enabled = false;
    }

    @Override
    public String about() {
        return "" +
            "author: wkgcass\n" +
            "code license: GPLv2 with classpath exception" +
            "";
    }
}
