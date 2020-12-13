// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.control;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.cassite.desktop.chara.manager.FontManager;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;

public class OneButton extends Group {
    public OneButton(FgoClickConsts consts, String text, ButtonColor color, Runnable clicked) {
        var corner = new CornerRadii(consts.cornerRadius);
        Background normalBackground = new Background(new BackgroundFill(
            color.normal,
            corner,
            Insets.EMPTY
        ));
        Background hoverBackground = new Background(new BackgroundFill(
            color.hover,
            corner,
            Insets.EMPTY
        ));
        Background activeBackground = new Background(new BackgroundFill(
            color.active,
            corner,
            Insets.EMPTY
        ));

        Pane central = new Pane();
        central.setBorder(Border.EMPTY);
        central.setBackground(normalBackground);
        central.setPrefWidth(consts.width - consts.margin * 2);
        central.setPrefHeight(consts.height - consts.margin * 2);
        central.setLayoutX(consts.margin);
        central.setLayoutY(consts.margin);
        central.setOnMouseMoved(e -> central.setBackground(hoverBackground));
        central.setOnMouseExited(e -> central.setBackground(normalBackground));
        central.setOnMousePressed(e -> central.setBackground(activeBackground));
        central.setOnMouseReleased(e -> central.setBackground(normalBackground));
        central.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                clicked.run();
            }
        });

        var borderCorner = new CornerRadii(consts.cornerRadius + consts.margin);
        Border border = new Border(new BorderStroke(
            color.normal,
            BorderStrokeStyle.SOLID,
            borderCorner,
            new BorderWidths(consts.borderWidth)
        ));

        Pane outer = new Pane();
        outer.setBorder(border);
        outer.setBackground(Background.EMPTY);
        outer.setPrefWidth(consts.width);
        outer.setPrefHeight(consts.height);
        outer.setLayoutX(0);
        outer.setLayoutY(0);
        outer.setMouseTransparent(true);

        Text foo = new Text(text);
        foo.setFont(Font.font(FontManager.getFontFamily(), consts.buttonFontSize));
        double w = foo.getLayoutBounds().getWidth();
        double h = foo.getLayoutBounds().getHeight();
        Label label = new Label(text);
        label.setFont(Font.font(FontManager.getFontFamily(), consts.buttonFontSize));
        label.setTextFill(new Color(1, 1, 1, 1));
        label.setLayoutX((consts.width - w) / 2);
        label.setLayoutY((consts.height - h) / 2);
        label.setMouseTransparent(true);

        getChildren().addAll(outer, central, label);
    }
}
