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

public class TwoButtons extends Group {
    private final Label label1;
    private final Label label2;
    private final double marginMiddle;
    private final double buttonCentralWidth;
    private final FgoClickConsts consts;

    public TwoButtons(FgoClickConsts consts,
                      String text1, ButtonColor color1, Runnable clicked1,
                      String text2, ButtonColor color2, Runnable clicked2) {
        this.consts = consts;
        marginMiddle = consts.cornerRadius - consts.margin + 0.1;
        buttonCentralWidth = consts.width / 2d - consts.margin - marginMiddle / 2;
        {
            var corner1Central = new CornerRadii(consts.cornerRadius, 0, 0, consts.cornerRadius, false);
            Background normalBackground1 = new Background(new BackgroundFill(
                color1.normal,
                corner1Central,
                Insets.EMPTY
            ));
            Background hoverBackground1 = new Background(new BackgroundFill(
                color1.hover,
                corner1Central,
                Insets.EMPTY
            ));
            Background activeBackground1 = new Background(new BackgroundFill(
                color1.active,
                corner1Central,
                Insets.EMPTY
            ));

            var corner1Border = new CornerRadii(
                consts.cornerRadius + consts.margin,
                0, 0,
                consts.cornerRadius + consts.margin,
                false);
            //noinspection SuspiciousNameCombination
            Border border1 = new Border(new BorderStroke(
                color1.normal,
                BorderStrokeStyle.SOLID,
                corner1Border,
                new BorderWidths(consts.borderWidth, 0, consts.borderWidth, consts.borderWidth)
            ));

            Pane central1 = new Pane();
            central1.setBorder(Border.EMPTY);
            central1.setBackground(normalBackground1);
            central1.setPrefWidth(buttonCentralWidth);
            central1.setPrefHeight(consts.height - consts.margin * 2);
            central1.setLayoutX(consts.margin);
            central1.setLayoutY(consts.margin);
            central1.setOnMouseMoved(e -> central1.setBackground(hoverBackground1));
            central1.setOnMouseExited(e -> central1.setBackground(normalBackground1));
            central1.setOnMousePressed(e -> central1.setBackground(activeBackground1));
            central1.setOnMouseReleased(e -> central1.setBackground(normalBackground1));
            central1.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    clicked1.run();
                }
            });

            Pane outer1 = new Pane();
            outer1.setBorder(border1);
            outer1.setBackground(Background.EMPTY);
            outer1.setPrefWidth(consts.width / 2d);
            outer1.setPrefHeight(consts.height);
            outer1.setLayoutX(0);
            outer1.setLayoutY(0);
            outer1.setMouseTransparent(true);

            label1 = new Label();
            setButton1Text(text1);

            getChildren().addAll(outer1, central1, label1);
        }

        {
            var corner2Central = new CornerRadii(0, consts.cornerRadius, consts.cornerRadius, 0, false);
            Background normalBackground2 = new Background(new BackgroundFill(
                color2.normal,
                corner2Central,
                Insets.EMPTY
            ));
            Background hoverBackground2 = new Background(new BackgroundFill(
                color2.hover,
                corner2Central,
                Insets.EMPTY
            ));
            Background activeBackground2 = new Background(new BackgroundFill(
                color2.active,
                corner2Central,
                Insets.EMPTY
            ));

            var corner2Border = new CornerRadii(0,
                consts.cornerRadius + consts.margin,
                consts.cornerRadius + consts.margin,
                0,
                false);
            //noinspection SuspiciousNameCombination
            Border border2 = new Border(new BorderStroke(
                color2.normal,
                BorderStrokeStyle.SOLID,
                corner2Border,
                new BorderWidths(consts.borderWidth, consts.borderWidth, consts.borderWidth, 0)
            ));

            Pane central2 = new Pane();
            central2.setBorder(Border.EMPTY);
            central2.setBackground(normalBackground2);
            central2.setPrefWidth(buttonCentralWidth);
            central2.setPrefHeight(consts.height - consts.margin * 2);
            central2.setLayoutX(consts.width / 2d + marginMiddle / 2);
            central2.setLayoutY(consts.margin);
            central2.setOnMouseMoved(e -> central2.setBackground(hoverBackground2));
            central2.setOnMouseExited(e -> central2.setBackground(normalBackground2));
            central2.setOnMousePressed(e -> central2.setBackground(activeBackground2));
            central2.setOnMouseReleased(e -> central2.setBackground(normalBackground2));
            central2.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    clicked2.run();
                }
            });

            Pane outer2 = new Pane();
            outer2.setBorder(border2);
            outer2.setBackground(Background.EMPTY);
            outer2.setPrefWidth(consts.width / 2d);
            outer2.setPrefHeight(consts.height);
            outer2.setLayoutX(consts.width / 2d);
            outer2.setLayoutY(0);
            outer2.setMouseTransparent(true);

            label2 = new Label();
            setButton2Text(text2);

            getChildren().addAll(outer2, central2, label2);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public void setButton1Text(String text1) {
        Text foo1 = new Text(text1);
        foo1.setFont(Font.font(FontManager.getFontFamily(), consts.buttonFontSize));
        double w1 = foo1.getLayoutBounds().getWidth();
        double h1 = foo1.getLayoutBounds().getHeight();
        label1.setText(text1);
        label1.setFont(Font.font(FontManager.getFontFamily(), consts.buttonFontSize));
        label1.setTextFill(new Color(1, 1, 1, 1));
        label1.setPrefWidth(w1);
        label1.setPrefHeight(h1);
        //                (the central width - textw) / 2 + margin left
        label1.setLayoutX((buttonCentralWidth - w1) / 2 + consts.margin);
        label1.setLayoutY((consts.height - h1) / 2);
        label1.setMouseTransparent(true);
    }

    @SuppressWarnings("DuplicatedCode")
    public void setButton2Text(String text2) {
        Text foo2 = new Text(text2);
        foo2.setFont(Font.font(FontManager.getFontFamily(), consts.buttonFontSize));
        double w2 = foo2.getLayoutBounds().getWidth();
        double h2 = foo2.getLayoutBounds().getHeight();
        label2.setText(text2);
        label2.setFont(Font.font(FontManager.getFontFamily(), consts.buttonFontSize));
        label2.setTextFill(new Color(1, 1, 1, 1));
        label2.setPrefWidth(w2);
        label2.setPrefHeight(h2);
        //              (the central width - textw) / 2 + margin left       + (margin middle) / 2
        label2.setLayoutX((buttonCentralWidth - w2) / 2 + consts.width / 2d + marginMiddle / 2);
        label2.setLayoutY((consts.height - h2) / 2);
        label2.setMouseTransparent(true);
    }
}
