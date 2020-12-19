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
import net.cassite.desktop.chara.util.Utils;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;

public class SleepIndicatorStage extends Stage {
    private final double stageDeltaX;
    private final double stageDeltaY;
    private final FgoClickConsts consts;

    private final Arc arc;

    public SleepIndicatorStage(FgoClickConsts consts, double stageDeltaX, double stageDeltaY, double width, double height) {
        this.stageDeltaX = stageDeltaX;
        this.stageDeltaY = stageDeltaY;
        this.consts = consts;

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
        arc.setStroke(FgoClickConsts.sleepIndicatorDanger);
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

    private Color getColor(Color src, Color dst, double p) {
        float[] hsvSrc = new float[3];
        java.awt.Color.RGBtoHSB((int) (src.getRed() * 255), (int) (src.getGreen() * 255), (int) (src.getBlue() * 255), hsvSrc);
        float[] hsvDst = new float[3];
        java.awt.Color.RGBtoHSB((int) (dst.getRed() * 255), (int) (dst.getGreen() * 255), (int) (dst.getBlue() * 255), hsvDst);
        double h = (hsvDst[0] - hsvSrc[0]) * p + hsvSrc[0];
        double s = (hsvDst[1] - hsvSrc[1]) * p + hsvSrc[1];
        double v = (hsvDst[2] - hsvSrc[2]) * p + hsvSrc[2];
        double o = (dst.getOpacity() - src.getOpacity()) * p + src.getOpacity();
        return Color.hsb(h * 360, s, v, o);
    }

    public void setDegree(double degree) {
        if (degree > consts.sleepIndicatorFineDegree) {
            arc.setStroke(FgoClickConsts.sleepIndicatorFine);
        } else if (degree > consts.sleepIndicatorWarnUpperDegree) {
            double p = (consts.sleepIndicatorFineDegree - degree) / (consts.sleepIndicatorFineDegree - consts.sleepIndicatorWarnUpperDegree);
            arc.setStroke(getColor(
                FgoClickConsts.sleepIndicatorFine,
                FgoClickConsts.sleepIndicatorWarn,
                p
            ));
        } else if (degree > consts.sleepIndicatorWarnLowerDegree) {
            arc.setStroke(FgoClickConsts.sleepIndicatorWarn);
        } else if (degree > consts.sleepIndicatorDangerDegree) {
            double p = (consts.sleepIndicatorWarnLowerDegree - degree) / (consts.sleepIndicatorWarnLowerDegree - consts.sleepIndicatorDangerDegree);
            arc.setStroke(getColor(
                FgoClickConsts.sleepIndicatorWarn,
                FgoClickConsts.sleepIndicatorDanger,
                p
            ));
        } else {
            arc.setStroke(FgoClickConsts.sleepIndicatorDanger);
        }
        arc.setStartAngle(90 - degree);
        arc.setLength(degree);
    }

    public void setPosition(double x, double y) {
        x = x + stageDeltaX;
        y = y + stageDeltaY;
        setX(x);
        setY(y);
        if (!Utils.isWindows()) { // only windows support y < 0 stages
            if (y < 0) {
                super.hide();
            } else {
                if (needToShow) {
                    show();
                }
            }
        }
    }

    private boolean needToShow = false;

    public void doShow() {
        needToShow = true;
        show();
    }

    @Override
    public void hide() {
        needToShow = false;
        super.hide();
    }
}
