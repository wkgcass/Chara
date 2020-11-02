// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.dev;

import javafx.scene.layout.Pane;
import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.graphic.Alert;
import net.cassite.desktop.chara.graphic.Div;
import net.cassite.desktop.chara.graphic.StageTransformer;
import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.plugin.dev.graphic.*;
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
    private AppCallback appCallback;
    private final Map<Integer, DevPoint> points = new HashMap<>();
    private final Set<DevLine> lines = new HashSet<>();
    private final Set<DevArc> arcs = new HashSet<>();
    private final Div pointsDiv = new Div();
    private final Div arcsDiv = new Div();
    private final Div linesDiv = new Div();
    private final Div arcLabelsDiv = new Div();
    private final Div pointLabelsDiv = new Div();
    private final Div root = new Div();

    public DevPlugin() {
        root.getChildren().addAll(arcsDiv, linesDiv, pointsDiv, arcLabelsDiv, pointLabelsDiv);
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
        registrations.add(EventBus.watch(Events.AppCallbackReady, this::appCallbackReady));
        registrations.add(EventBus.watch(Events.PrimaryStageResized, this::resized));
        registrations.add(EventBus.watch(Events.MessageTaken, this::messageTaken));
        registrations.add(EventBus.watch(Events.MouseClickedImagePosition, e -> click(e[0], e[1])));

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

    private void appCallbackReady(AppCallback appCallback) {
        this.appCallback = appCallback;
        if (enabled) {
            appCallback.setDraggable(false);
        }
    }

    private void resized(Void v) {
        if (primaryStage == null) {
            return;
        }
        points.values().forEach(DevPoint::refreshPositions);
    }

    private void messageTaken(String msg) {
        if (!msg.startsWith("::dev:")) {
            return;
        }
        msg = msg.substring("::dev:".length());
        if (msg.startsWith("point:")) {
            msg = msg.substring("point:".length());
            handlePoint(msg);
        } else if (msg.startsWith("line:")) {
            msg = msg.substring("line:".length());
            handleLine(msg);
        }
    }

    private void handlePoint(String msg) {
        if (msg.equals("hide-all")) {
            Logger.info("remove all points");
            clearPoints();
            return;
        }

        String[] split = msg.split(":");
        if (split.length != 2) {
            Logger.error("invalid command, expecting ::dev:point:${index}:${action}");
            return;
        }
        String numStr = split[0];
        String action = split[1];

        Integer num = parseInt(numStr);
        if (num == null) {
            Logger.error("invalid command, " + numStr + " is not a valid integer");
            return;
        }
        var point = points.get(num);
        if (point == null) {
            Logger.error("point " + num + " not found");
            return;
        }
        if (action.equals("hide")) {
            Logger.info("remove point " + point);
            removePoint(point);
        } else {
            Logger.error("unknown action " + action + " for point" + num);
        }
    }

    private void handleLine(String msg) {
        if (msg.equals("hide-all")) {
            Logger.info("remove all lines");
            clearLines();
            return;
        }

        String[] split = msg.split(":");
        if (split.length != 3) {
            Logger.error("invalid command, expecting ::dev:line:${point-1-idx}:${point-2-idx}:${action}");
            return;
        }
        String num1Str = split[0];
        String num2Str = split[1];
        String action = split[2];

        Integer num1 = parseInt(split[0]);
        if (num1 == null) {
            Logger.error("invalid command, " + num1Str + " is not a valid integer");
            return;
        }
        Integer num2 = parseInt(split[1]);
        if (num2 == null) {
            Logger.error("invalid command, " + num2Str + " is not a valid integer");
            return;
        }
        var p1 = points.get(num1);
        var p2 = points.get(num2);
        if (p1 == null) {
            Logger.error("point " + num1 + " not found");
            return;
        }
        if (p2 == null) {
            Logger.error("point " + num2 + " not found");
            return;
        }
        if (p1 == p2) {
            Logger.error("point " + num1 + " and point " + num2 + " are the same point");
            return;
        }
        var lineOpt = lines.stream().filter(l -> (l.p1 == p1 && l.p2 == p2) || (l.p2 == p1 && l.p1 == p2)).findAny();
        if (action.equals("hide")) {
            if (lineOpt.isEmpty()) {
                Logger.error("line " + DevLine.toString(p1, p2) + " not found");
                return;
            }
            var line = lineOpt.get();
            Logger.info("remove line " + line);
            removeLine(line);
        } else if (action.equals("show")) {
            if (lineOpt.isPresent()) {
                Logger.error("line " + lineOpt.get() + " already exists");
                return;
            }
            var line = new DevLine(p1, p2);
            Logger.info("add line " + line);
            line.addTo(linesDiv);
            lines.add(line);

            // check whether need to add arc
            createArc(p1, line);
            createArc(p2, line);
        }
    }

    private void createArc(DevPoint p, DevLine line) {
        if (p.getLinkedLines().size() > 1) { // multiple lines connected to p
            for (DevLine l : p.getLinkedLines()) {
                if (l == line) {
                    continue;
                }
                DevArc arc = new DevArc(line, l);
                Logger.info("add arc " + arc);
                arc.addTo(arcsDiv, arcLabelsDiv);
                arcs.add(arc);
            }
        }
    }

    private void clearPoints() {
        points.values().forEach(p -> p.removeFrom(pointsDiv, pointLabelsDiv));
        points.clear();
        clearLines(); // no points to be linked, so clear lines
    }

    private void clearLines() {
        lines.forEach(l -> {
            l.removeFrom(linesDiv);
            l.unlinkPoints();
        });
        lines.clear();
        clearArcs(); // no lines to form arcs, so clear arcs
    }

    private void clearArcs() {
        arcs.forEach(a -> a.removeFrom(arcsDiv, arcLabelsDiv));
        arcs.clear();
    }

    private void removePoint(DevPoint p) {
        p.removeFrom(pointsDiv, pointLabelsDiv);
        points.remove(p.pointIndex);
        // should remove lines
        p.getLinkedLines().forEach(this::removeLine);
    }

    private void removeLine(DevLine l) {
        l.removeFrom(linesDiv);
        l.unlinkPoints();
        lines.remove(l);
        // should remove arcs
        l.getLinkedArcs().forEach(this::removeArc);
    }

    private void removeArc(DevArc a) {
        a.removeFrom(arcsDiv, arcLabelsDiv);
        arcs.remove(a);
    }

    private Integer parseInt(String n) {
        try {
            return Integer.parseInt(n);
        } catch (NumberFormatException e) {
            return null;
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
        var point = new DevPoint(primaryStage, i, x, y);
        point.addTo(pointsDiv, pointLabelsDiv);
        point.refreshPositions();
        points.put(i, point);
    }

    @Override
    public void clicked() {
        enabled = !enabled;
        if (enabled) {
            Alert.alert(DevPluginI18nConsts.devPluginEnabled.get()[0]);
            appCallback.setDraggable(false);
        } else {
            Alert.alert(DevPluginI18nConsts.devPluginDisabled.get()[0]);
            appCallback.setDraggable(true);
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
