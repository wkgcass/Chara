// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.coordinate;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.util.StageUtils;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;

public class FgoClickAbstractStage extends Stage {
    private final Stage transparentTemporaryUtilityStage;
    protected final FgoClickConsts consts;
    protected final Pane pane;

    public FgoClickAbstractStage(FgoClickConsts consts) {
        this.consts = consts;
        transparentTemporaryUtilityStage = StageUtils.createTransparentTemporaryUtilityStage();
        initOwner(transparentTemporaryUtilityStage);
        initStyle(StageStyle.TRANSPARENT);
        setResizable(false);
        setAlwaysOnTop(true);
        pane = new Pane();
        Scene scene = new Scene(pane);
        setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        pane.setBackground(new Background(new BackgroundFill(
            new Color(0x0c / 255d, 0x28 / 255d, 0x32 / 255d, 0.5),
            CornerRadii.EMPTY, Insets.EMPTY
        )));
        transparentTemporaryUtilityStage.show();

        setX(ConfigManager.get().getDoubleValue(FgoClickConsts.boundsX));
        setY(ConfigManager.get().getDoubleValue(FgoClickConsts.boundsY));
        setWidth(ConfigManager.get().getDoubleValue(FgoClickConsts.boundsWidth));
        setHeight(ConfigManager.get().getDoubleValue(FgoClickConsts.boundsHeight));
    }

    protected void addPoint(Point p, Words words) {
        addPoint(p, words.get()[0]);
    }

    protected void addPoint(Point p, String text) {
        addPoint(pane, p, text);
    }

    protected void addPoint(Pane pane, Point p, Words words) {
        addPoint(pane, p, words.get()[0]);
    }

    private double[] textBounds(String text) {
        Text foo = new Text(text);
        foo.setFont(new Font(consts.clickPointTextFontSize));
        double textWidth = foo.getLayoutBounds().getWidth();
        double textHeight = foo.getLayoutBounds().getHeight();
        return new double[]{textWidth, textHeight};
    }

    protected void addPoint(Pane pane, Point p, String text) {
        double x = p.x;
        double y = p.y;

        Label label = new Label(text);
        label.setFont(new Font(consts.clickPointTextFontSize));
        label.setTextFill(new Color(1, 1, 1, 1));

        Group textG = new Group(label);

        var inner = new Circle(consts.clickPointCircleInnerRadius);
        inner.setFill(new Color(1, 0, 0, 1));
        var outer = new Circle(consts.clickPointCircleOuterRadius);
        outer.setFill(new Color(1, 1, 1, 1));

        Group point = new Group(outer, inner);

        Group g = new Group(point, textG);
        pane.getChildren().add(g);

        point.setLayoutX(x);
        point.setLayoutY(y);

        // calculate text position
        double[] foo = textBounds(text);
        double textWidth = foo[0];
        double textHeight = foo[1];
        if (x - textWidth / 2 < 0) {
            textG.setLayoutX(0);
        } else if (x + textWidth / 2 > getWidth()) {
            textG.setLayoutX(getWidth() - textWidth);
        } else {
            textG.setLayoutX(x - textWidth / 2);
        }
        if (y - consts.clickPointCircleOuterRadius - consts.clickPointTextMargin - textHeight < 0) {
            textG.setLayoutY(y + consts.clickPointCircleOuterRadius + consts.clickPointTextMargin);
        } else {
            textG.setLayoutY(y - consts.clickPointCircleOuterRadius - consts.clickPointTextMargin - textHeight);
        }
    }

    protected void addRec(Pane pane, Rec rec, Words words) {
        addRec(pane, rec, words.get()[0]);
    }

    protected void addRec(Pane pane, Rec rec, String text) {
        Label label = new Label(text);
        label.setFont(new Font(consts.clickPointTextFontSize));
        label.setTextFill(new Color(1, 1, 1, 1));

        Group textG = new Group(label);

        double width = rec.bottomRight.x - rec.topLeft.x;
        double height = rec.bottomRight.y - rec.topLeft.y;
        Rectangle rectangle = new Rectangle(0, 0, width, height);
        rectangle.setStroke(new Color(1, 0, 0, 1));
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStrokeWidth(consts.strokeWidth);

        Group rect = new Group(rectangle);

        Group g = new Group(rect, textG);
        pane.getChildren().add(g);

        rect.setLayoutX(rec.topLeft.x);
        rect.setLayoutY(rec.topLeft.y);

        // calculate text position
        double[] foo = textBounds(text);
        double textWidth = foo[0];
        double textHeight = foo[1];

        double left = rec.topLeft.x + width / 2 - textWidth / 2;
        double right = rec.bottomRight.x + width / 2 + textWidth / 2;
        if (left < 0) {
            textG.setLayoutX(0);
        } else if (right > getWidth()) {
            textG.setLayoutX(getWidth() - textWidth);
        } else {
            textG.setLayoutX(left);
        }

        double top = rec.topLeft.y - textHeight - consts.clickPointTextMargin;
        if (top < 0) {
            textG.setLayoutY(rec.bottomRight.y + consts.clickPointTextMargin);
        } else {
            textG.setLayoutY(top);
        }
    }

    @Override
    public void hide() {
        super.hide();
        transparentTemporaryUtilityStage.hide();
    }

    protected void hideThis() {
        super.hide();
    }
}
