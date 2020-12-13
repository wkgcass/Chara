// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import net.cassite.desktop.chara.util.DragHandler;
import net.cassite.desktop.chara.util.DragWindowHandler;
import net.cassite.desktop.chara.util.Utils;

import java.util.Objects;

/**
 * The stage unifies appearance across platforms, and provides accurate pane width/height
 */
public class UStage {
    /**
     * height of the title
     */
    public static final double TITLE_HEIGHT = 24;
    /**
     * the border width if border exists
     */
    public static final double NORMAL_BORDER_W = 0.85;

    private static final double RESIZE_ACTION_LENGTH = 15;
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
        new BorderWidths(NORMAL_BORDER_W, NORMAL_BORDER_W, 0, NORMAL_BORDER_W)
    ));
    private final Border LOSE_FOCUS_TITLE_BORDER = new Border(new BorderStroke(
        LOSE_FOCUS_BORDER_COLOR,
        BorderStrokeStyle.SOLID,
        TITLE_CORNER_RADII,
        new BorderWidths(NORMAL_BORDER_W, NORMAL_BORDER_W, 0, NORMAL_BORDER_W)
    ));

    private final double BORDER_W;
    private final Border FOCUSED_PANE_BORDER;
    private final Border LOSE_FOCUS_PANE_BORDER;

    private final Stage stage;
    private final Group mainWindow = new Group();
    private final Pane backgroundPane;
    private final Pane pane;
    private final Pane titlePane;
    private final Label title;
    private final Line separator;
    private final Rectangle resizeBottomRight;

    /**
     * construct the stage with default parameters
     */
    public UStage() {
        this(new UStageConfig());
    }

    /**
     * construct the stage with configuration
     *
     * @param config config
     */
    public UStage(UStageConfig config) {
        stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(config.resizable);
        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);

        // initiate constants
        if (config.noBorder) {
            BORDER_W = 0;
        } else {
            BORDER_W = NORMAL_BORDER_W;
        }
        FOCUSED_PANE_BORDER = new Border(new BorderStroke(
            FOCUSED_BORDER_COLOR,
            BorderStrokeStyle.SOLID,
            PANE_CORNER_RADII,
            new BorderWidths(0, BORDER_W, BORDER_W, BORDER_W)
        ));
        LOSE_FOCUS_PANE_BORDER = new Border(new BorderStroke(
            LOSE_FOCUS_BORDER_COLOR,
            BorderStrokeStyle.SOLID,
            PANE_CORNER_RADII,
            new BorderWidths(0, BORDER_W, BORDER_W, BORDER_W)
        ));

        backgroundPane = new Pane();
        backgroundPane.setLayoutX(0);
        backgroundPane.setLayoutY(TITLE_HEIGHT);
        backgroundPane.setBackground(new Background(new BackgroundFill(
            PANE_BACKGROUND_COLOR,
            PANE_CORNER_RADII,
            Insets.EMPTY
        )));
        backgroundPane.setBorder(FOCUSED_PANE_BORDER);
        mainWindow.getChildren().add(backgroundPane);

        pane = new Pane();
        pane.setBorder(Border.EMPTY);
        pane.setBackground(Background.EMPTY);
        pane.setLayoutX(0);
        pane.setLayoutY(0);
        {
            pane.layoutBoundsProperty().addListener((ob, old, now) -> {
                Rectangle recMain = new Rectangle();
                recMain.setStrokeWidth(0);
                recMain.setX(BORDER_W);
                recMain.setY(0);
                recMain.setWidth(now.getWidth() - BORDER_W * 2);
                recMain.setHeight(now.getHeight() - STAGE_CORNER_RADIUS_PIXELS);

                Rectangle recBot = new Rectangle();
                recBot.setStrokeWidth(0);
                recBot.setX(STAGE_CORNER_RADIUS_PIXELS);
                recBot.setY(now.getHeight() - STAGE_CORNER_RADIUS_PIXELS);
                recBot.setWidth(now.getWidth() - STAGE_CORNER_RADIUS_PIXELS * 2);
                recBot.setHeight(STAGE_CORNER_RADIUS_PIXELS - BORDER_W);

                Arc arcLeft = new Arc();
                arcLeft.setStrokeWidth(0);
                arcLeft.setCenterX(BORDER_W + STAGE_CORNER_RADIUS_PIXELS);
                arcLeft.setCenterY(now.getHeight() - BORDER_W - STAGE_CORNER_RADIUS_PIXELS);
                arcLeft.setStartAngle(180);
                arcLeft.setLength(90);
                arcLeft.setRadiusX(STAGE_CORNER_RADIUS_PIXELS);
                arcLeft.setRadiusY(STAGE_CORNER_RADIUS_PIXELS);
                arcLeft.setType(ArcType.ROUND);

                Arc arcRight = new Arc();
                arcRight.setStrokeWidth(0);
                arcRight.setCenterX(now.getWidth() - BORDER_W - STAGE_CORNER_RADIUS_PIXELS);
                arcRight.setCenterY(now.getHeight() - BORDER_W - STAGE_CORNER_RADIUS_PIXELS);
                arcRight.setStartAngle(270);
                arcRight.setLength(90);
                arcRight.setRadiusX(STAGE_CORNER_RADIUS_PIXELS);
                arcRight.setRadiusY(STAGE_CORNER_RADIUS_PIXELS);
                arcRight.setType(ArcType.ROUND);

                Group group = new Group(recMain, recBot, arcLeft, arcRight);
                pane.setClip(group);
            });
        }
        backgroundPane.getChildren().add(pane);

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
                    closeButtonClickRange.setOnMouseClicked(e -> {
                        stage.fireEvent(new WindowEvent(null, WindowEvent.WINDOW_CLOSE_REQUEST));
                        closeButton.setFill(CLOSE_BUTTON_NORMAL_COLOR);
                    });
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
        mainWindow.getChildren().add(titlePane);

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
                mainWindow.getChildren().remove(separator);
                backgroundPane.setBorder(FOCUSED_PANE_BORDER);
            } else {
                titlePane.setBackground(LOSE_FOCUS_TITLE_BACKGROUND);
                titlePane.setBorder(LOSE_FOCUS_TITLE_BORDER);
                mainWindow.getChildren().add(separator);
                backgroundPane.setBorder(LOSE_FOCUS_PANE_BORDER);
            }
        });

        // handle stage resizing
        resizeBottomRight = new Rectangle();
        resizeBottomRight.setWidth(RESIZE_ACTION_LENGTH);
        resizeBottomRight.setHeight(RESIZE_ACTION_LENGTH);
        resizeBottomRight.setFill(Color.TRANSPARENT);
        resizeBottomRight.setStrokeWidth(0);
        resizeBottomRight.setStroke(Color.TRANSPARENT);
        resizeBottomRight.setMouseTransparent(false);
        if (config.resizable) {
            resizeBottomRight.setOnMouseMoved(e -> resizeBottomRight.setCursor(Cursor.NW_RESIZE));
            resizeBottomRight.setOnMouseExited(e -> resizeBottomRight.setCursor(Cursor.DEFAULT));
            var dragResizeHandler = new DragHandler(
                xy -> {
                    setPaneWidth(xy[0]);
                    setPaneHeight(xy[1]);
                },
                () -> new double[]{backgroundPane.getWidth(), backgroundPane.getHeight()}
            );
            resizeBottomRight.setOnMousePressed(dragResizeHandler);
            resizeBottomRight.setOnMouseDragged(dragResizeHandler);

            mainWindow.getChildren().add(resizeBottomRight);
        }

        root.getChildren().add(mainWindow);
    }

    /**
     * Set screen x position of the stage (i.e. title top left x position of screen)
     *
     * @param x screen x
     */
    public void setStageX(double x) {
        stage.setX(x);
    }

    /**
     * Get screen x position of the stage (i.e. title top left x position of screen)
     *
     * @return screen x
     */
    public double getStageX() {
        return stage.getX();
    }

    /**
     * Set screen y position of the stage (i.e. title top left y position of screen)
     *
     * @param y screen y
     */
    public void setStageY(double y) {
        stage.setY(y);
    }

    /**
     * Get screen y position of the stage (i.e. title top left y position of screen)
     *
     * @return screen y
     */
    public double getStageY() {
        return stage.getY();
    }

    private double titleTextWidth;
    private double titleTextHeight;

    private void calculateTitlePosition(boolean calculateSize) {
        if (calculateSize) {
            Text text = new Text(title.getText());
            text.setFont(new Font(TITLE_TEXT_SIZE));
            titleTextWidth = text.getLayoutBounds().getWidth();
            titleTextHeight = text.getLayoutBounds().getHeight();
        }
        title.setLayoutX((stage.getWidth() - titleTextWidth) / 2);
        title.setLayoutY((TITLE_HEIGHT - titleTextHeight) / 2 + TEXT_Y_FIX);
    }

    /**
     * Set width of the root pane, including left border and right border
     *
     * @param paneWidth width
     */
    public void setPaneWidth(double paneWidth) {
        this.backgroundPane.setPrefWidth(paneWidth);
        this.pane.setPrefWidth(paneWidth);
        this.titlePane.setPrefWidth(paneWidth);
        this.separator.setEndX(paneWidth);
        this.stage.setWidth(paneWidth);
        this.resizeBottomRight.setX(paneWidth - RESIZE_ACTION_LENGTH);
        calculateTitlePosition(false);
    }

    /**
     * Get width of the root pane, including left border and right border
     *
     * @return pane width
     */
    public double getPaneWidth() {
        return pane.getWidth();
    }

    /**
     * Set height of the root pane, not including the title, including the bottom border
     *
     * @param paneHeight height
     */
    public void setPaneHeight(double paneHeight) {
        this.backgroundPane.setPrefHeight(paneHeight);
        this.pane.setPrefHeight(paneHeight);
        this.stage.setHeight(paneHeight + TITLE_HEIGHT);
        this.resizeBottomRight.setY(paneHeight + TITLE_HEIGHT - RESIZE_ACTION_LENGTH);
        calculateTitlePosition(false);
    }

    /**
     * Get height of the root pane, not including the title, including the bottom border
     *
     * @return pane height
     */
    public double getPaneHeight() {
        return pane.getHeight();
    }

    /**
     * Get the property of pane width
     *
     * @return pane width property
     * @see #setPaneWidth(double)
     */
    public ReadOnlyDoubleProperty paneWidth() {
        return pane.widthProperty();
    }

    /**
     * Get the property of pane height
     *
     * @return pane height property
     * @see #setPaneHeight(double)
     */
    public ReadOnlyDoubleProperty paneHeight() {
        return pane.heightProperty();
    }

    /**
     * The stage is showing
     *
     * @return true if showing, false otherwise
     */
    public boolean isShowing() {
        return stage.isShowing();
    }

    /**
     * Show the stage
     */
    public void show() {
        stage.show();
    }

    /**
     * Hide the stage
     */
    public void hide() {
        stage.hide();
    }

    /**
     * Move the stage to the center of screen
     */
    public void centerOnScreen() {
        stage.centerOnScreen();
    }

    // ----------- THE FOLLOWING CODE IS THE SAME AS {@link StageTransformer#getScreen()} -----------
    // ----------- MAKE SURE ANY CHANGE TO THIS CODE ALSO APPLY THERE -----------
    private Screen lastRetrievedScreen;

    /**
     * Get current screen of this stage
     *
     * @return screen object
     */
    @SuppressWarnings("DuplicatedCode")
    public Screen getScreen() {
        Screen s = Utils.getScreen(stage.getX() + stage.getWidth() / 2, stage.getY() + stage.getHeight() / 2);
        if (s == null) {
            if (lastRetrievedScreen == null) {
                return Screen.getPrimary();
            }
            return lastRetrievedScreen;
        }
        lastRetrievedScreen = s;
        return s;
    }
    // ----------- THE UPPER CODE IS THE SAME AS {@link StageTransformer#getScreen()} -----------
    // ----------- MAKE SURE ANY CHANGE TO THIS CODE ALSO APPLY THERE -----------

    /**
     * Set title of the stage
     *
     * @param title title
     */
    public void setTitle(String title) {
        this.title.setText(title);
        this.stage.setTitle(title);
        calculateTitlePosition(true);
    }

    /**
     * Get root pane.<br>
     * All nodes should be put into this pane.
     *
     * @return the root pane
     */
    public Pane getRootPane() {
        return pane;
    }

    /**
     * Request focus
     */
    public void requestFocus() {
        stage.requestFocus();
    }

    /**
     * Check whether this stage is always on top
     *
     * @return true if always on top, false otherwise
     */
    public boolean isAlwaysOnTop() {
        return stage.isAlwaysOnTop();
    }

    /**
     * Set the stage always on top or not
     *
     * @param alwaysOnTop true if always on top, false otherwise
     */
    public void setAlwaysOnTop(boolean alwaysOnTop) {
        stage.setAlwaysOnTop(alwaysOnTop);
    }

    /**
     * Set the handler on close request
     *
     * @param handler the handler
     */
    public void setOnCloseRequest(EventHandler<WindowEvent> handler) {
        stage.setOnCloseRequest(handler);
    }

    /**
     * Set icon of the stage
     *
     * @param icon icon
     */
    public void setIcon(Image icon) {
        Utils.setIcon(stage, icon);
    }
}
