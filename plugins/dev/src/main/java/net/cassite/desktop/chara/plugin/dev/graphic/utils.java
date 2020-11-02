// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.dev.graphic;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

class utils {
    private utils() {
    }

    public static void setTextBounds(String str, double fontSize, Label label, Pane textBackground) {
        double textWidth;
        double textHeight;
        {
            Text text = new Text(str);
            text.setFont(new Font(fontSize));
            textWidth = text.getLayoutBounds().getWidth();
            textHeight = text.getLayoutBounds().getHeight();
        }
        label.setText(str);
        textBackground.setPrefWidth(textWidth);
        textBackground.setPrefHeight(textHeight);
    }
}
