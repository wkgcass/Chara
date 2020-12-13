// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.control;

import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.cassite.desktop.chara.util.StageUtils;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;

public class SleepIndicatorStage extends Stage {
    private final double stageDeltaX;
    private final double stageDeltaY;

    private final Arc arc;

    public SleepIndicatorStage(FgoClickConsts consts, double stageDeltaX, double stageDeltaY, double width, double height) {
        this.stageDeltaX = stageDeltaX;
        this.stageDeltaY = stageDeltaY;

        initOwner(StageUtils.primaryStage);
        initStyle(StageStyle.TRANSPARENT);
        setWidth(width);
        setHeight(height);

        Pane root = new Pane();
        root.setBackground(Background.EMPTY);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

        arc = new Arc();
        arc.setFill(Color.TRANSPARENT);
        arc.setStroke(new Color(0.5, 0.75, 0.1, 1));
        arc.setType(ArcType.OPEN);
        arc.setRadiusX(consts.sleepIndicatorRadius - consts.sleepIndicatorBounds / 2d);
        arc.setRadiusY(consts.sleepIndicatorRadius - consts.sleepIndicatorBounds / 2d);
        arc.setStrokeWidth(consts.sleepIndicatorBounds);
        arc.setStartAngle(90);
        arc.setLength(0);
        arc.setCenterX(-stageDeltaX);
        arc.setCenterY(-stageDeltaY);

        root.getChildren().add(arc);
    }

    public void setDegree(double degree) {
        arc.setStartAngle(90 - degree);
        arc.setLength(degree);
    }

    public void setPosition(double x, double y) {
        setX(x + stageDeltaX);
        setY(y + stageDeltaY);
    }
}
