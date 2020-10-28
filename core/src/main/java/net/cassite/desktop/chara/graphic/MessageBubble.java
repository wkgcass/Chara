// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.cassite.desktop.chara.manager.FontManager;
import net.cassite.desktop.chara.util.Consts;

public class MessageBubble {
    public final String message;
    private final Label text;
    private final Label background;
    private final Polygon tri;
    private final Group group;
    private final Consts.MsgBubbleColor color;

    private final double textWidth;
    private final double textHeight;

    private boolean pointToRight;

    public MessageBubble(String message, boolean pointToRight, Consts.MsgBubbleColor color) {
        // format message
        {
            StringBuilder sb = new StringBuilder();
            String[] arr = message.split("\n");
            boolean isFirst = true;
            for (String s : arr) {
                if (s.isBlank()) {
                    continue;
                }
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append("\n");
                }
                sb.append(s);
            }
            message = sb.toString();
        }
        this.message = message;
        this.pointToRight = pointToRight;
        this.color = color;

        {
            Text foo = new Text(message);
            // the calculation of text height is not correct, set the line spacing to fix the issue
            // it's not the ultimate fix but it looks better
            foo.setLineSpacing(FontManager.getLineSpacingFix());
            foo.setFont(Font.font(FontManager.getFontFamily(), Consts.MSG_FONT_SIZE));
            double w = foo.getLayoutBounds().getWidth();
            if (w > Consts.MSG_BUBBLE_MAX_WIDTH - Consts.MSG_BUBBLE_PADDING_HORIZONTAL * 2) {
                w = Consts.MSG_BUBBLE_MAX_WIDTH - Consts.MSG_BUBBLE_PADDING_HORIZONTAL * 2;
                foo.setWrappingWidth(w);
            }
            double h = foo.getLayoutBounds().getHeight();
            this.textWidth = w;
            this.textHeight = h;
        }
        text = new Label(message);
        text.setFont(Font.font(FontManager.getFontFamily(), Consts.MSG_FONT_SIZE));
        text.setTextFill(color.text);
        text.setWrapText(true);
        text.setPrefWidth(textWidth);
        background = new Label();
        background.setStyle("-fx-background-color: " + color.backgroundRGB + "; -fx-background-radius: 10px;");
        tri = new Polygon();

        group = new Group();
        group.setLayoutX(0);
        group.getChildren().addAll(background, tri, text);

        calculatePosition();
    }

    public double getHeight() {
        return textHeight + Consts.MSG_BUBBLE_PADDING_VERTICAL * 2;
    }

    public double getWidth() {
        return textWidth + Consts.MSG_BUBBLE_PADDING_HORIZONTAL * 2 + Consts.MSG_ARROW_WIDTH;
    }

    public void setY(double y) {
        group.setLayoutY(y);
    }

    public double getY() {
        return group.getLayoutY();
    }

    public void setPointToRight(boolean pointToRight) {
        if (this.pointToRight == pointToRight) {
            return;
        }
        this.pointToRight = pointToRight;
        calculatePosition();
    }

    public void calculatePosition() {
        text.setLayoutY(Consts.MSG_BUBBLE_PADDING_VERTICAL);
        if (pointToRight) {
            text.setLayoutX(Consts.MSG_STAGE_WIDTH - Consts.MSG_ARROW_WIDTH - Consts.MSG_BUBBLE_PADDING_HORIZONTAL - textWidth);
        } else {
            text.setLayoutX(Consts.MSG_ARROW_WIDTH + Consts.MSG_BUBBLE_PADDING_HORIZONTAL);
        }

        background.setPrefWidth(textWidth + Consts.MSG_BUBBLE_PADDING_HORIZONTAL * 2);
        double backgroundHeight = textHeight + Consts.MSG_BUBBLE_PADDING_VERTICAL * 2;
        background.setPrefHeight(backgroundHeight);
        background.setLayoutY(0);
        if (pointToRight) {
            background.setLayoutX(Consts.MSG_STAGE_WIDTH - Consts.MSG_ARROW_WIDTH - Consts.MSG_BUBBLE_PADDING_HORIZONTAL - textWidth - Consts.MSG_BUBBLE_PADDING_HORIZONTAL);
        } else {
            background.setLayoutX(Consts.MSG_ARROW_WIDTH);
        }

        tri.getPoints().clear();
        if (pointToRight) {
            tri.getPoints().addAll(
                0D, 0D,
                0D, Consts.MSG_ARROW_HEIGHT,
                (double) Consts.MSG_ARROW_WIDTH, Consts.MSG_ARROW_HEIGHT / 2D
            );
            tri.setLayoutX(Consts.MSG_STAGE_WIDTH - Consts.MSG_ARROW_WIDTH);
        } else {
            tri.getPoints().addAll(
                0D, Consts.MSG_ARROW_HEIGHT / 2D,
                (double) Consts.MSG_ARROW_WIDTH, 0D,
                (double) Consts.MSG_ARROW_WIDTH, Consts.MSG_ARROW_HEIGHT
            );
            tri.setLayoutX(0);
        }
        tri.setLayoutY((backgroundHeight - Consts.MSG_ARROW_HEIGHT) / 2);
        tri.setFill(color.background);
    }

    public void addTo(Pane pane) {
        pane.getChildren().add(group);
    }

    public void removeFrom(Pane pane) {
        pane.getChildren().remove(group);
    }

    public void setOnMouseClick(Runnable r) {
        text.setOnMouseClicked(e -> {
            r.run();
            e.consume();
        });
        background.setOnMouseClicked(e -> {
            r.run();
            e.consume();
        });
        tri.setOnMouseClicked(e -> {
            r.run();
            e.consume();
        });
    }

    public void hide() {
        group.setOpacity(0);
    }

    double posY; // used by message stage to record Y position before animation
    long createTime;
}
