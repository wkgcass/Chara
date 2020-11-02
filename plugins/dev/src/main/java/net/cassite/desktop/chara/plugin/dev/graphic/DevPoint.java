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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import net.cassite.desktop.chara.graphic.Div;
import net.cassite.desktop.chara.graphic.StageTransformer;
import net.cassite.desktop.chara.util.DragHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DevPoint {
    private static final int fontSize = 15;
    static final int pointOuterRadius = 6;
    private static final int pointInnerRadius = 5;
    private static final int textMargin = 5;

    public final StageTransformer primaryStage;
    public final int pointIndex;
    double x;
    double y;
    double realx;
    double realy;
    private final Label label;
    private final Pane textBackground;
    private final Group text;
    private final Group point;
    final Set<DevLine> linkedLines = new HashSet<>();

    public DevPoint(StageTransformer primaryStage, int pointIndex, double x, double y) {
        this.primaryStage = primaryStage;
        this.pointIndex = pointIndex;
        this.x = x;
        this.y = y;

        label = new Label();
        label.setFont(new Font(fontSize));
        label.setTextFill(new Color(0, 0, 0, 1));
        textBackground = new Pane();
        textBackground.setBackground(new Background(new BackgroundFill(
            new Color(1, 1, 1, 0.75),
            CornerRadii.EMPTY, Insets.EMPTY
        )));
        this.text = new Group(textBackground, label);

        var inner = new Circle(pointInnerRadius);
        inner.setFill(new Color(1, 0, 0, 1));
        var outer = new Circle(pointOuterRadius);
        outer.setFill(new Color(1, 1, 1, 1));
        point = new Group(outer, inner);

        // handle drag events
        var dragHandler = new DragHandler(this::setPointPosition, () -> new double[]{point.getLayoutX(), point.getLayoutY()});
        point.setOnMousePressed(dragHandler);
        point.setOnMouseDragged(dragHandler);
    }

    private void setPointPosition(double[] xy) {
        double x = xy[0];
        double y = xy[1];
        if (x < 0) {
            x = 0;
        }
        if (x > primaryStage.getStage().getWidth()) {
            x = primaryStage.getStage().getWidth();
        }
        if (y < 0) {
            y = 0;
        }
        if (y > primaryStage.getStage().getHeight()) {
            y = primaryStage.getStage().getHeight();
        }
        x = primaryStage.getImageXBySceneX(x);
        y = primaryStage.getImageYBySceneY(y);

        this.x = x;
        this.y = y;
        refreshPositions();
    }

    public void refreshPositions() {
        var str = String.format("p%d(%.2f, %.2f)", pointIndex, this.x, this.y);
        utils.setTextBounds(str, fontSize, label, textBackground);

        var x = primaryStage.getSceneXByImageX(this.x);
        var y = primaryStage.getSceneYByImageY(this.y);
        point.setLayoutX(x);
        point.setLayoutY(y);
        this.realx = x;
        this.realy = y;

        // calculate label position
        double textWidth = textBackground.getPrefWidth();
        double textHeight = textBackground.getPrefHeight();
        if (x - textWidth / 2 < 0) {
            text.setLayoutX(0);
        } else if (x + textWidth / 2 > primaryStage.getStage().getWidth()) {
            text.setLayoutX(primaryStage.getStage().getWidth() - textWidth);
        } else {
            text.setLayoutX(x - textWidth / 2);
        }
        if (y - pointOuterRadius - textMargin - textHeight < 0) {
            text.setLayoutY(y + pointOuterRadius + textMargin);
        } else {
            text.setLayoutY(y - pointOuterRadius - textMargin - textHeight);
        }

        // calculate lines positions
        linkedLines.forEach(DevLine::refreshPositions);
    }

    public void addTo(Div pointsDiv, Div pointLabelsDiv) {
        pointsDiv.getChildren().add(point);
        pointLabelsDiv.getChildren().add(text);
    }

    public void removeFrom(Div pointsDiv, Div pointLabelsDiv) {
        pointsDiv.getChildren().remove(point);
        pointLabelsDiv.getChildren().remove(text);
    }

    public double len(DevPoint that) {
        return Math.sqrt(
            (this.x - that.x) * (this.x - that.x)
                + (this.y - that.y) * (this.y - that.y)
        );
    }

    public Set<DevLine> getLinkedLines() {
        return Collections.unmodifiableSet(linkedLines);
    }

    public String toString() {
        return "p" + pointIndex + "(" + x + "," + y + ")";
    }
}
