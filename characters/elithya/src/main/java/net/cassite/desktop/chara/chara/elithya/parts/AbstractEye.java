// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Static;

public abstract class AbstractEye extends AbstractPart {
    private final EyeConf conf;

    public AbstractEye(Group parent, EyeConf conf) {
        super(parent);
        this.conf = conf;
    }

    public void init() {
        setPosition(conf.initX, conf.initY);
    }

    public void track(double x, double y) {
        double yBase;
        if (x < conf.initX) {
            double percentage = (conf.middleHoleMinX - x) / (conf.initX - conf.boundMinX);
            setX(conf.initX - percentage * (conf.initX - conf.leftX));
            yBase = (conf.leftY - conf.initY) * percentage + conf.initY;
        } else {
            double percentage = (x - conf.middleHoleMaxX) / (conf.boundMaxX - conf.initX);
            setX(conf.initX + percentage * (conf.rightX - conf.initX));
            yBase = (conf.rightY - conf.initY) * percentage + conf.initY;
        }
        trackY(y, yBase);
    }

    public void resetXAndTrackY(double y) {
        resetX();
        trackY(y, conf.initY);
    }

    private void trackY(double y, double yBase) {
        if (y < yBase) {
            setY(yBase - (yBase - y) / (yBase - conf.boundMinY) * (yBase - conf.minY));
        } else {
            setY(yBase + (y - yBase) / (conf.boundMaxY - yBase) * (conf.maxY - yBase));
        }
    }

    public void reset() {
        setPosition(conf.initX, conf.initY);
    }

    public void resetX() {
        setX(conf.initX);
    }

    private void setPosition(double x, double y) {
        getEye().setPosition(x - conf.originalX, y - conf.originalY);
    }

    private void setX(double x) {
        getEye().setPosition(x - conf.originalX, getEye().getDeltaY());
    }

    private void setY(double y) {
        getEye().setPosition(getEye().getDeltaX(), y - conf.originalY);
    }

    abstract protected Static getEye();

    public static final class EyeConf {
        private double boundMinX;
        private double boundMaxX;
        private double boundMinY;
        private double boundMaxY;
        private double minY;
        private double maxY;
        private double originalX;
        private double originalY;
        private double initX;
        private double initY;
        private double leftX;
        private double leftY;
        private double rightX;
        private double rightY;
        private double middleHoleMinX;
        private double middleHoleMaxX;

        public EyeConf setBoundMinX(double boundMinX) {
            this.boundMinX = boundMinX;
            return this;
        }

        public EyeConf setBoundMaxX(double boundMaxX) {
            this.boundMaxX = boundMaxX;
            return this;
        }

        public EyeConf setBoundMinY(double boundMinY) {
            this.boundMinY = boundMinY;
            return this;
        }

        public EyeConf setBoundMaxY(double boundMaxY) {
            this.boundMaxY = boundMaxY;
            return this;
        }

        public EyeConf setMinY(double minY) {
            this.minY = minY;
            return this;
        }

        public EyeConf setMaxY(double maxY) {
            this.maxY = maxY;
            return this;
        }

        public EyeConf setOriginalX(double originalX) {
            this.originalX = originalX;
            return this;
        }

        public EyeConf setOriginalY(double originalY) {
            this.originalY = originalY;
            return this;
        }

        public EyeConf setInitX(double initX) {
            this.initX = initX;
            return this;
        }

        public EyeConf setInitY(double initY) {
            this.initY = initY;
            return this;
        }

        public EyeConf setLeftX(double leftX) {
            this.leftX = leftX;
            return this;
        }

        public EyeConf setLeftY(double leftY) {
            this.leftY = leftY;
            return this;
        }

        public EyeConf setRightX(double rightX) {
            this.rightX = rightX;
            return this;
        }

        public EyeConf setRightY(double rightY) {
            this.rightY = rightY;
            return this;
        }

        public EyeConf setMiddleHoleMinX(double middleHoleMinX) {
            this.middleHoleMinX = middleHoleMinX;
            return this;
        }

        public EyeConf setMiddleHoleMaxX(double middleHoleMaxX) {
            this.middleHoleMaxX = middleHoleMaxX;
            return this;
        }
    }
}
