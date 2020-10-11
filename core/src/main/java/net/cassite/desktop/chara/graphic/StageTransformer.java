// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.stage.Screen;
import javafx.stage.Stage;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.util.Logger;

public class StageTransformer {
    private final Stage stage;
    private final double originalWidth;
    private final double originalHeight;
    private final double cutLeft;
    private final double cutRight;
    private final double cutTop;
    private final double cutBottom;

    private double scaleRatio = 1;

    public StageTransformer(Stage stage,
                            double originalWidth,
                            double originalHeight,
                            double cutLeft,
                            double cutRight,
                            double cutTop,
                            double cutBottom) {
        this.stage = stage;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        this.cutLeft = cutLeft;
        this.cutRight = cutRight;
        this.cutTop = cutTop;
        this.cutBottom = cutBottom;

        stage.setX(0);
        stage.setY(0);
        stage.setWidth(originalWidth - cutLeft - cutRight);
        stage.setHeight(originalHeight - cutBottom - cutTop);
    }

    public Stage getStage() {
        return stage;
    }

    private void debugStage(String reason) {
        assert Logger.debug("StageTransformer::" + reason + "::[" + stage.getWidth() + "," + stage.getHeight() + "]+(" + stage.getX() + "," + stage.getY() + ")");
    }

    private void moveAbsolute(double deltaX, double deltaY) {
        stage.setX(stage.getX() + deltaX);
        stage.setY(stage.getY() + deltaY);
    }

    public void scale(double ratio) {
        scaleAt(cutLeft, cutTop, ratio);
    }

    public void scaleAt(double originalOldScaleAtX, double originalOldScaleAtY, double ratio) {
        assert Logger.debug("scaleAt(" + originalOldScaleAtX + "," + originalOldScaleAtY + "," + ratio + ")");

        double w = (originalWidth - cutLeft - cutRight) * ratio;
        double h = (originalHeight - cutBottom - cutTop) * ratio;
        stage.setWidth(w);
        stage.setHeight(h);

        double actualOldScaleAtX = (originalOldScaleAtX - cutLeft) * this.scaleRatio;
        double actualOldScaleAtY = (originalOldScaleAtY - cutTop) * this.scaleRatio;
        double actualNewScaleAtX = (originalOldScaleAtX - cutLeft) * ratio;
        double actualNewScaleAtY = (originalOldScaleAtY - cutTop) * ratio;
        moveAbsolute(actualOldScaleAtX - actualNewScaleAtX,
            actualOldScaleAtY - actualNewScaleAtY);

        this.scaleRatio = ratio;

        debugStage("scaleAt");
    }

    public void centerOnScreen() {
        assert Logger.debug("centerOnScreen()");

        double centerX = stage.getX() + stage.getWidth() / 2;
        double centerY = stage.getY() + stage.getHeight() / 2;
        Screen screen = getScreen();
        var bounds = screen.getBounds();
        double screenCenterX = bounds.getWidth() / 2;
        double screenCenterY = bounds.getHeight() / 2;

        moveAbsolute(screenCenterX - centerX, screenCenterY - centerY);

        debugStage("centerOnScreen");
    }

    public double getScaleRatio() {
        return scaleRatio;
    }

    public double getAbsoluteX() {
        return stage.getX();
    }

    public double getAbsoluteY() {
        return stage.getY();
    }

    public void setAbsoluteX(double x) {
        stage.setX(x);
    }

    public void setAbsoluteY(double y) {
        stage.setY(y);
    }

    public void saveConfig() {
        ConfigManager.get().setStageX(stage.getX());
        ConfigManager.get().setStageY(stage.getY());
        ConfigManager.get().setCharacterRatio(scaleRatio);
    }

    public Screen getScreen() {
        Screen screen;
        var ls = Screen.getScreensForRectangle(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
        if (ls.isEmpty()) {
            assert Logger.debug("getScreenForRectangle return empty");
            screen = Screen.getPrimary();
        } else {
            assert Logger.debug("getScreenForRectangle return " + ls.size());
            screen = ls.get(0);
        }
        return screen;
    }

    public double getCutLeft() {
        return cutLeft;
    }

    public double getCutRight() {
        return cutRight;
    }

    public double getCutTop() {
        return cutTop;
    }

    public double getCutBottom() {
        return cutBottom;
    }

    public double calculateX(double x) {
        x -= getCutLeft();
        x *= getScaleRatio();
        return x;
    }

    public double calculateY(double y) {
        y -= getCutTop();
        y *= getScaleRatio();
        return y;
    }
}
