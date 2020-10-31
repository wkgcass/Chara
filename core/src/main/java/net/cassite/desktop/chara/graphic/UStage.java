// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import net.cassite.desktop.chara.util.DragWindowHandler;
import net.cassite.desktop.chara.util.Utils;

import java.util.Objects;

public class UStage {
    public static final double TITLE_HEIGHT = 24;
    public static final double BORDER_W = 0.85;

    private static final double TITLE_TEXT_SIZE = 13;
    private static final double TEXT_Y_FIX = 0.5;
    private static final double STAGE_CORNER_RADIUS_PIXELS = 4;
    private static final double CONTROL_BUTTON_RADIUS = 6;
    private static final double CONTROL_BUTTON_RANGE_RADIUS = 10;
    private static final Color PANE_BACKGROUND_COLOR = new Color(0xf4 / 255d, 0xf4 / 255d, 0xf4 / 255d, 1);
    private static final Color FOCUSED_BORDER_COLOR = new Color(0xd0 / 255d, 0xd0 / 255d, 0xd0 / 255d, 1);
    private static final Color CLOSE_BUTTON_NORMAL_COLOR = new Color(0xed / 255d, 0x6b / 255d, 0x5e / 255d, 1);
    private static final Color CLOSE_BUTTON_MOUSE_DOWN_COLOR = new Color(0xb2 / 255d, 0x50 / 255d, 0x48 / 255d, 1);
    private static final Color CLOSE_SYMBOL_COLOR = new Color(0x46 / 255d, 0x08 / 255d, 0x04 / 255d, 1);
    private static final Color TITLE_TEXT_COLOR = new Color(0x40 / 255d, 0x40 / 255d, 0x40 / 255d, 1);
    private static final Color LOSE_FOCUS_TITLE_BACKGROUND_COLOR = new Color(0xf6 / 255d, 0xf6 / 255d, 0xf6 / 255d, 1);
    private static final Color LOSE_FOCUS_BORDER_COLOR = new Color(0xd7 / 255d, 0xd7 / 255d, 0xd7 / 255d, 1);

    private static final Background TITLE_BACKGROUND = new Background(new BackgroundFill(
        new LinearGradient(0, 0, 0, TITLE_HEIGHT, false, CycleMethod.NO_CYCLE,
            new Stop(0, new Color(0xf4 / 255d, 0xf4 / 255d, 0xf4 / 255d, 1)),
            new Stop(TITLE_HEIGHT, FOCUSED_BORDER_COLOR)),
        new CornerRadii(STAGE_CORNER_RADIUS_PIXELS, STAGE_CORNER_RADIUS_PIXELS, 0, 0, false),
        Insets.EMPTY
    ));
    private static final CornerRadii PANE_CORNER_RADII = new CornerRadii(
        0, 0, STAGE_CORNER_RADIUS_PIXELS, STAGE_CORNER_RADIUS_PIXELS, false);
    private static final CornerRadii TITLE_CORNER_RADII = new CornerRadii(
        STAGE_CORNER_RADIUS_PIXELS, STAGE_CORNER_RADIUS_PIXELS, 0, 0, false);
    private static final Background LOSE_FOCUS_TITLE_BACKGROUND = new Background(new BackgroundFill(
        LOSE_FOCUS_TITLE_BACKGROUND_COLOR,
        TITLE_CORNER_RADII,
        Insets.EMPTY
    ));
    private static final Border FOCUSED_TITLE_BORDER = new Border(new BorderStroke(
        FOCUSED_BORDER_COLOR,
        BorderStrokeStyle.SOLID,
        TITLE_CORNER_RADII,
        new BorderWidths(BORDER_W, BORDER_W, 0, BORDER_W)
    ));
    private static final Border LOSE_FOCUS_TITLE_BORDER = new Border(new BorderStroke(
        LOSE_FOCUS_BORDER_COLOR,
        BorderStrokeStyle.SOLID,
        TITLE_CORNER_RADII,
        new BorderWidths(BORDER_W, BORDER_W, 0, BORDER_W)
    ));
    private static final Border FOCUSED_PANE_BORDER = new Border(new BorderStroke(
        FOCUSED_BORDER_COLOR,
        BorderStrokeStyle.SOLID,
        PANE_CORNER_RADII,
        new BorderWidths(0, BORDER_W, BORDER_W, BORDER_W)
    ));
    private static final Border LOSE_FOCUS_PANE_BORDER = new Border(new BorderStroke(
        LOSE_FOCUS_BORDER_COLOR,
        BorderStrokeStyle.SOLID,
        PANE_CORNER_RADII,
        new BorderWidths(0, BORDER_W, BORDER_W, BORDER_W)
    ));

    private final Stage stage;
    private final Pane pane;
    private final Pane titlePane;
    private final Label title;
    private final Line separator;

    public UStage() {
        stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);

        pane = new Pane();
        pane.setLayoutX(0);
        pane.setLayoutY(TITLE_HEIGHT - BORDER_W);
        pane.setBackground(new Background(new BackgroundFill(
            PANE_BACKGROUND_COLOR,
            PANE_CORNER_RADII,
            Insets.EMPTY
        )));
        pane.setBorder(FOCUSED_PANE_BORDER);
        root.getChildren().add(pane);

        titlePane = new Pane();
        titlePane.setLayoutX(0);
        titlePane.setLayoutY(0);
        titlePane.setPrefHeight(TITLE_HEIGHT);
        titlePane.setBackground(TITLE_BACKGROUND);
        titlePane.setBorder(FOCUSED_TITLE_BORDER);
        {
            title = new Label();
            title.setFont(new Font(TITLE_TEXT_SIZE));
            title.setTextFill(TITLE_TEXT_COLOR);
            titlePane.getChildren().add(title);

            Pane controlButtonsPane = new Pane();
            controlButtonsPane.setBackground(Background.EMPTY);
            controlButtonsPane.setPrefWidth(CONTROL_BUTTON_RADIUS * 2);
            controlButtonsPane.setPrefHeight(CONTROL_BUTTON_RADIUS * 2);
            controlButtonsPane.setLayoutX(13 - CONTROL_BUTTON_RADIUS);
            controlButtonsPane.setLayoutY((TITLE_HEIGHT - CONTROL_BUTTON_RADIUS * 2) / 2);
            Group closeButtonGroup;
            Circle closeButton;
            Group closeSymbol;
            {
                closeButtonGroup = new Group();
                closeButtonGroup.setLayoutX(CONTROL_BUTTON_RADIUS);
                closeButtonGroup.setLayoutY(CONTROL_BUTTON_RADIUS);
                {
                    closeButton = new Circle();
                    closeButton.setRadius(CONTROL_BUTTON_RADIUS);
                    closeButton.setFill(CLOSE_BUTTON_NORMAL_COLOR);
                    closeButton.setMouseTransparent(true);
                    closeSymbol = new Group();
                    closeSymbol.setMouseTransparent(true);
                    {
                        final double fixX = 0.25;
                        final double fixY = 0.25;
                        final double width = 0.75;
                        Line line1 = new Line(-2.5 + fixX, -2.5 + fixY, 2.5 + fixX, 2.75 + fixY);
                        line1.setStroke(CLOSE_SYMBOL_COLOR);
                        line1.setStrokeWidth(width);
                        Line line2 = new Line(-2.5 + fixX, 2.5 + fixY, 2.5 + fixX, -2.5 + fixY);
                        line2.setStroke(CLOSE_SYMBOL_COLOR);
                        line2.setStrokeWidth(width);
                        closeSymbol.getChildren().addAll(line1, line2);
                    }

                    Circle closeButtonClickRange = new Circle();
                    closeButtonClickRange.setRadius(CONTROL_BUTTON_RANGE_RADIUS);
                    closeButtonClickRange.setFill(Color.TRANSPARENT);

                    closeButtonClickRange.setOnMousePressed(e -> closeButton.setFill(CLOSE_BUTTON_MOUSE_DOWN_COLOR));
                    closeButtonClickRange.setOnMouseClicked(e -> stage.fireEvent(new WindowEvent(null, WindowEvent.WINDOW_CLOSE_REQUEST)));
                    closeButtonGroup.getChildren().addAll(closeButtonClickRange, closeButton);
                }
                controlButtonsPane.getChildren().add(closeButtonGroup);
            }
            titlePane.getChildren().add(controlButtonsPane);
            boolean[] symbolIsShown = {false};
            controlButtonsPane.setOnMouseMoved(e -> {
                if (symbolIsShown[0]) return;
                symbolIsShown[0] = true;
                closeButtonGroup.getChildren().add(closeSymbol);
            });
            controlButtonsPane.setOnMouseExited(e -> {
                if (!symbolIsShown[0]) return;
                symbolIsShown[0] = false;
                closeButtonGroup.getChildren().remove(closeSymbol);
            });
        }
        var dragHandler = new DragWindowHandler(stage);
        titlePane.setOnMousePressed(dragHandler);
        titlePane.setOnMouseDragged(dragHandler);
        root.getChildren().add(titlePane);

        separator = new Line();
        separator.setStartX(0);
        separator.setStartY(TITLE_HEIGHT);
        separator.setEndY(TITLE_HEIGHT);
        separator.setStrokeWidth(BORDER_W);
        separator.setStroke(LOSE_FOCUS_BORDER_COLOR);

        stage.focusedProperty().addListener((ob, old, focused) -> {
            if (!Objects.equals(old, focused) && focused) {
                titlePane.setBackground(TITLE_BACKGROUND);
                titlePane.setBorder(FOCUSED_TITLE_BORDER);
                root.getChildren().remove(separator);
                pane.setBorder(FOCUSED_PANE_BORDER);
            } else {
                titlePane.setBackground(LOSE_FOCUS_TITLE_BACKGROUND);
                titlePane.setBorder(LOSE_FOCUS_TITLE_BORDER);
                root.getChildren().add(separator);
                pane.setBorder(LOSE_FOCUS_PANE_BORDER);
            }
        });
    }

    public void setStageX(double x) {
        stage.setX(x);
    }

    public double getStageX() {
        return stage.getX();
    }

    public void setStageY(double y) {
        stage.setY(y);
    }

    public double getStageY() {
        return stage.getY();
    }

    private double titleTextWidth;
    private double titleTextHeight;

    private void calculateTitlePosition(boolean calculateSize) {
        if (calculateSize) {
            Label t = new Label(title.getText());
            t.setFont(new Font(TITLE_TEXT_SIZE));
            new Scene(t);
            var img = t.snapshot(new SnapshotParameters(), null);
            titleTextWidth = img.getWidth();
            titleTextHeight = img.getHeight();
        }
        title.setLayoutX((stage.getWidth() - titleTextWidth) / 2);
        title.setLayoutY((TITLE_HEIGHT - titleTextHeight) / 2 + TEXT_Y_FIX);
    }

    public void setPaneWidth(double paneWidth) {
        this.pane.setPrefWidth(paneWidth);
        this.titlePane.setPrefWidth(paneWidth);
        this.separator.setEndX(paneWidth);
        this.stage.setWidth(paneWidth);
        calculateTitlePosition(false);
    }

    public double getPaneWidth() {
        return pane.getWidth();
    }

    public void setPaneHeight(double paneHeight) {
        this.pane.setPrefHeight(paneHeight);
        this.stage.setHeight(paneHeight + TITLE_HEIGHT);
        calculateTitlePosition(false);
    }

    public double getPaneHeight() {
        return pane.getHeight();
    }

    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }

    public void centerOnScreen() {
        stage.centerOnScreen();
    }

    public void setTitle(String title) {
        this.title.setText(title);
        calculateTitlePosition(true);
    }

    public Pane getRootPane() {
        return pane;
    }

    public void setOnCloseRequest(EventHandler<WindowEvent> handler) {
        stage.setOnCloseRequest(handler);
    }

    public void setIcon(Image charaDefaultIcon) {
        Utils.setIcon(stage, charaDefaultIcon);
    }
}