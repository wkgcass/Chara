// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.dev;

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
import javafx.scene.text.Text;
import net.cassite.desktop.chara.graphic.Div;
import net.cassite.desktop.chara.graphic.StageTransformer;

public class DevPoint {
    private static final int fontSize = 15;
    private static final int pointOuterRadius = 6;
    private static final int pointInnerRadius = 5;
    private static final int textMargin = 5;

    public final int pointIndex;
    public final double x;
    public final double y;
    public final double textWidth;
    public final double textHeight;
    public final Group text;
    public final Group point;
    public final Group group;

    public DevPoint(int pointIndex, double x, double y) {
        this.pointIndex = pointIndex;
        this.x = x;
        this.y = y;

        var str = String.format("p%d(%.2f, %.2f)", pointIndex, x, y);
        Text text = new Text(str);
        text.setFont(new Font(fontSize));
        textWidth = text.getLayoutBounds().getWidth();
        textHeight = text.getLayoutBounds().getHeight();
        var label = new Label(str);
        label.setFont(new Font(fontSize));
        label.setTextFill(new Color(0, 0, 0, 1));
        var textBackground = new Pane();
        textBackground.setPrefWidth(textWidth);
        textBackground.setPrefHeight(textHeight);
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

        group = new Group(this.text, point);
    }

    public void calculatePosition(StageTransformer primaryStage) {
        var x = primaryStage.getSceneXByImageX(this.x);
        var y = primaryStage.getSceneYByImageY(this.y);
        point.setLayoutX(x);
        point.setLayoutY(y);

        // calculate label position
        if (x - textWidth / 2 < 0) {
            text.setLayoutX(0);
        } else if (x + textWidth / 2 > primaryStage.getStage().getWidth()) {
            text.setLayoutX(primaryStage.getStage().getWidth() - textWidth);
        } else {
            text.setLayoutX(x - textWidth / 2);
        }
        if (y + pointOuterRadius + textMargin + textHeight > primaryStage.getStage().getHeight()) {
            text.setLayoutY(y + pointOuterRadius + textMargin);
        } else {
            text.setLayoutY(y - pointOuterRadius - textMargin - textHeight);
        }
    }

    public void addTo(Div div) {
        div.getChildren().add(group);
    }

    public void removeFrom(Div div) {
        div.getChildren().remove(group);
    }
}
