// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.control;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.cassite.desktop.chara.util.DragHandler;
import net.cassite.desktop.chara.util.DragWindowHandler;
import net.cassite.desktop.chara.util.StageUtils;

public class BoundsSelector extends Stage {
    private static final double RESIZE_WIDTH = 20;
    private final Stage transparentTemporaryUtilityStage;

    public BoundsSelector() {
        transparentTemporaryUtilityStage = StageUtils.createTransparentTemporaryUtilityStage();

        initOwner(transparentTemporaryUtilityStage);
        initStyle(StageStyle.TRANSPARENT);
        setResizable(false);
        setAlwaysOnTop(true);
        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        pane.setBackground(new Background(new BackgroundFill(
            new Color(0x0c / 255d, 0x28 / 255d, 0x32 / 255d, 0.5),
            CornerRadii.EMPTY, Insets.EMPTY
        )));

        Pane centralDragWindowPane;
        {
            var dragWindowHandler = new DragWindowHandler(this);
            centralDragWindowPane = new Pane();
            centralDragWindowPane.setOpacity(0);
            centralDragWindowPane.setOnMousePressed(dragWindowHandler);
            centralDragWindowPane.setOnMouseDragged(dragWindowHandler);
            centralDragWindowPane.setCursor(Cursor.CROSSHAIR);
            centralDragWindowPane.setLayoutX(RESIZE_WIDTH);
            centralDragWindowPane.setLayoutY(RESIZE_WIDTH);
            pane.getChildren().add(centralDragWindowPane);
        }
        Pane rightResizePane;
        {
            var dragHandler = new DragHandler(
                xy -> setWidth(xy[0]),
                () -> new double[]{getWidth(), 0}
            );
            rightResizePane = new Pane();
            rightResizePane.setOpacity(0);
            rightResizePane.setOnMousePressed(dragHandler);
            rightResizePane.setOnMouseDragged(dragHandler);
            rightResizePane.setCursor(Cursor.H_RESIZE);
            rightResizePane.setLayoutY(RESIZE_WIDTH);
            rightResizePane.setPrefWidth(RESIZE_WIDTH);
            pane.getChildren().add(rightResizePane);
        }
        Pane bottomResizePane;
        {
            var dragHandler = new DragHandler(
                xy -> setHeight(xy[1]),
                () -> new double[]{0, getHeight()}
            );
            bottomResizePane = new Pane();
            bottomResizePane.setOpacity(0);
            bottomResizePane.setOnMousePressed(dragHandler);
            bottomResizePane.setOnMouseDragged(dragHandler);
            bottomResizePane.setCursor(Cursor.V_RESIZE);
            bottomResizePane.setLayoutX(RESIZE_WIDTH);
            bottomResizePane.setPrefHeight(RESIZE_WIDTH);
            pane.getChildren().add(bottomResizePane);
        }
        Pane bottomRightResizePane;
        {
            var dragHandler = new DragHandler(
                xy -> {
                    setWidth(xy[0]);
                    setHeight(xy[1]);
                },
                () -> new double[]{getWidth(), getHeight()}
            );
            bottomRightResizePane = new Pane();
            bottomRightResizePane.setOpacity(0);
            bottomRightResizePane.setOnMousePressed(dragHandler);
            bottomRightResizePane.setOnMouseDragged(dragHandler);
            bottomRightResizePane.setCursor(Cursor.NW_RESIZE);
            bottomRightResizePane.setPrefWidth(RESIZE_WIDTH);
            bottomRightResizePane.setPrefHeight(RESIZE_WIDTH);
            pane.getChildren().add(bottomRightResizePane);
        }
        pane.setOnMouseExited(e -> scene.setCursor(Cursor.DEFAULT));

        widthProperty().addListener((ob, old, now) -> {
            double n = now.doubleValue();
            pane.setPrefWidth(n);
            centralDragWindowPane.setPrefWidth(n - RESIZE_WIDTH * 2);
            rightResizePane.setLayoutX(n - RESIZE_WIDTH);
            bottomResizePane.setPrefWidth(n - RESIZE_WIDTH * 2);
            bottomRightResizePane.setLayoutX(n - RESIZE_WIDTH);
        });
        heightProperty().addListener((ob, old, now) -> {
            double n = now.doubleValue();
            pane.setPrefHeight(n);
            centralDragWindowPane.setPrefHeight(n - RESIZE_WIDTH * 2);
            rightResizePane.setPrefHeight(n - RESIZE_WIDTH * 2);
            bottomResizePane.setLayoutY(n - RESIZE_WIDTH);
            bottomRightResizePane.setLayoutY(n - RESIZE_WIDTH);
        });

        transparentTemporaryUtilityStage.show();
    }

    @Override
    public void hide() {
        super.hide();
        transparentTemporaryUtilityStage.hide();
    }
}
