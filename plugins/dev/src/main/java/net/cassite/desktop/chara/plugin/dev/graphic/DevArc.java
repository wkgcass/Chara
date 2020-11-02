// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.dev.graphic;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import net.cassite.desktop.chara.graphic.Div;

public class DevArc {
    private static final int fontSize = 15;
    private static final int arcRadius = 40;
    private static final int arcTextRadius = 55;
    private static final int arcOuterWidth = 4;
    private static final int arcInnerWidth = 2;

    public final DevPoint o;
    public final DevPoint a;
    public final DevPoint b;
    private final Label label;
    private final Pane textBackground;
    private final Group text;
    private final Arc arcInner;
    private final Arc arcOuter;
    private final Group arc;

    public DevArc(DevLine l1, DevLine l2) {
        o = (l1.p1 == l2.p1) ? l1.p1 : l1.p2;
        if (l2.p1 != o && l2.p2 != o) {
            throw new IllegalArgumentException();
        }
        a = (l1.p1 == o) ? l1.p2 : l1.p1;
        b = (l2.p1 == o) ? l2.p2 : l2.p1;

        {
            arcInner = new Arc();
            arcInner.setCenterX(0);
            arcInner.setCenterY(0);
            arcInner.setRadiusX(arcRadius);
            arcInner.setRadiusY(arcRadius);
            arcInner.setStrokeWidth(arcInnerWidth);
            arcInner.setStroke(new Color(1, 0, 0, 1));
            arcInner.setFill(Color.TRANSPARENT);
            arcInner.setType(ArcType.OPEN);
        }
        {
            arcOuter = new Arc();
            arcOuter.setCenterX(0);
            arcOuter.setCenterY(0);
            arcOuter.setRadiusX(arcRadius);
            arcOuter.setRadiusY(arcRadius);
            arcOuter.setStrokeWidth(arcOuterWidth);
            arcOuter.setStroke(new Color(1, 1, 1, 1));
            arcOuter.setFill(Color.TRANSPARENT);
            arcOuter.setType(ArcType.OPEN);
        }
        arc = new Group(arcOuter, arcInner);

        label = new Label();
        label.setFont(new Font(fontSize));
        label.setTextFill(new Color(0, 0, 0, 1));
        textBackground = new Pane();
        textBackground.setBackground(new Background(new BackgroundFill(
            new Color(1, 1, 1, 0.75),
            CornerRadii.EMPTY, Insets.EMPTY
        )));
        this.text = new Group(textBackground, label);

        refreshPositions();

        l1.linkedArcs.add(this);
        l2.linkedArcs.add(this);
    }

    public void refreshPositions() {
        // calculate angle
        double lo = a.len(b);
        double la = o.len(a);
        double lb = o.len(b);
        double cosO = (la * la + lb * lb - lo * lo) / 2 / la / lb;
        double degreeO = Math.acos(cosO) / Math.PI * 180;
        var str = String.format("%.2f", degreeO);
        utils.setTextBounds(str, fontSize, label, textBackground);

        final var x = o.realx;
        final var y = o.realy;
        arc.setLayoutX(x);
        arc.setLayoutY(y);
        arcInner.setLength(degreeO);
        arcOuter.setLength(degreeO);

        // calculate starting angle
        // should calculate both lines and compare them
        double degreeA = calculateDegreeOfXAndOToPoint(a);
        double degreeB = calculateDegreeOfXAndOToPoint(b);
        if (degreeA < degreeB) {
            var foo = degreeB;
            degreeB = degreeA;
            degreeA = foo;
        }
        double startDegree;
        if (degreeA - degreeB > 180) {
            startDegree = degreeA;
        } else {
            startDegree = degreeB;
        }
        arcInner.setStartAngle(startDegree);
        arcOuter.setStartAngle(startDegree);

        // calculate label position
        // get middle degree
        double middleDegree;
        if (degreeA - degreeB > 180) {
            middleDegree = 180 + (degreeA + degreeB) / 2;
        } else {
            middleDegree = (degreeA + degreeB) / 2;
        }
        double middleArc = middleDegree / 180 * Math.PI;
        double centerX = x;
        double centerY = y;
        if (middleDegree == 0) {
            centerX += arcTextRadius;
            centerY -= 0;
        } else if (middleDegree == 90) {
            centerX += 0;
            centerY -= arcTextRadius;
        } else if (middleDegree == 180) {
            centerX -= arcTextRadius;
            centerY += 0;
        } else if (middleDegree == -90) {
            centerX -= 0;
            centerY += arcTextRadius;
        } else {
            centerX += arcTextRadius * Math.cos(middleArc);
            centerY -= arcTextRadius * Math.sin(middleArc);
        }

        double textWidth = textBackground.getPrefWidth();
        double textHeight = textBackground.getPrefHeight();
        if (centerX - textWidth / 2 < 0) {
            text.setLayoutX(0);
        } else if (centerX + textWidth / 2 > o.primaryStage.getStage().getWidth()) {
            text.setLayoutX(o.primaryStage.getStage().getWidth() - textWidth);
        } else {
            text.setLayoutX(centerX - textWidth / 2);
        }
        if (centerY - textHeight / 2 < 0) {
            text.setLayoutY(0);
        } else if (centerY + textHeight / 2 > o.primaryStage.getStage().getHeight()) {
            text.setLayoutY(o.primaryStage.getStage().getHeight() - textHeight);
        } else {
            text.setLayoutY(centerY - textHeight / 2);
        }
    }

    private double calculateDegreeOfXAndOToPoint(DevPoint p) {
        double x = p.x - o.x;
        double y = o.y - p.y;
        if (x == 0) {
            if (y > 0) {
                return 90;
            } else {
                return -90;
            }
        } else if (x > 0 && y > 0) {
            return Math.atan(y / x) / Math.PI * 180;
        } else if (x > 0 && y < 0) {
            return -Math.atan(-y / x) / Math.PI * 180;
        } else if (x < 0 && y > 0) {
            return 180 - Math.atan(y / -x) / Math.PI * 180;
        } else {
            assert x < 0 && y < 0;
            return -180 + Math.atan(-y / -x) / Math.PI * 180;
        }
    }

    public void addTo(Div arcsDiv, Div arcLabelsDiv) {
        arcsDiv.getChildren().add(arc);
        arcLabelsDiv.getChildren().add(text);
    }

    public void removeFrom(Div arcsDiv, Div arcLabelsDiv) {
        arcsDiv.getChildren().remove(arc);
        arcLabelsDiv.getChildren().remove(text);
    }

    @Override
    public String toString() {
        return "(" + a + "-->" + o + "<--" + b + ")";
    }
}
