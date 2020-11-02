// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.dev.graphic;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.cassite.desktop.chara.graphic.Div;

import java.util.LinkedList;
import java.util.List;

public class DevLine {
    private static final int lineOuterWidth = 4;
    private static final int lineInnerWidth = 2;

    public final DevPoint p1;
    public final DevPoint p2;
    final Line outer;
    final Line inner;
    private final Group group = new Group();
    final List<DevArc> linkedArcs = new LinkedList<>();

    public DevLine(DevPoint p1, DevPoint p2) {
        this.p1 = p1;
        this.p2 = p2;

        outer = new Line(p1.realx, p1.realy, p2.realx, p2.realy);
        outer.setStrokeWidth(lineOuterWidth);
        outer.setStroke(new Color(1, 1, 1, 1));
        inner = new Line(p1.realx, p1.realy, p2.realx, p2.realy);
        inner.setStrokeWidth(lineInnerWidth);
        inner.setStroke(new Color(1, 0, 0, 1));

        group.getChildren().addAll(outer, inner);

        this.p1.linkedLines.add(this);
        this.p2.linkedLines.add(this);
    }

    public void addTo(Div div) {
        div.getChildren().add(group);
    }

    public void removeFrom(Div div) {
        div.getChildren().remove(group);
    }

    public void unlinkPoints() {
        p1.linkedLines.remove(this);
        p2.linkedLines.remove(this);
    }

    public void refreshPositions() {
        refresh(outer);
        refresh(inner);

        linkedArcs.forEach(DevArc::refreshPositions);
    }

    private void refresh(Line line) {
        if (line.getStartX() != p1.realx) {
            line.setStartX(p1.realx);
        }
        if (line.getStartY() != p1.realy) {
            line.setStartY(p1.realy);
        }
        if (line.getEndX() != p2.realx) {
            line.setEndX(p2.realx);
        }
        if (line.getEndY() != p2.realy) {
            line.setEndY(p2.realy);
        }
    }

    public List<DevArc> getLinkedArcs() {
        return linkedArcs;
    }

    @Override
    public String toString() {
        return toString(p1, p2);
    }

    public static String toString(DevPoint p1, DevPoint p2) {
        return "(" + p1 + "--" + p2 + ")";
    }
}
