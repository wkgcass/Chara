// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import net.cassite.desktop.chara.CharaPoints;
import net.cassite.desktop.chara.css.ProgressBarCss;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.util.Consts;

public class Bars {
    public final Group barGroup;
    private final ProgressBar bondBar;
    private final ProgressBar desireBar;

    public Bars(Group barGroup) {
        this.barGroup = barGroup;
        this.barGroup.setLayoutY(Consts.BOND_BAR_MARGIN_TOP);
        this.barGroup.setOpacity(0);

        this.bondBar = new ProgressBar(0);
        this.bondBar.setPrefWidth(Consts.BAR_WIDTH);
        this.bondBar.setPrefHeight(Consts.BOND_BAR_HEIGHT);
        //noinspection SuspiciousNameCombination
        this.bondBar.getStylesheets().add(new ProgressBarCss(
            Consts.BOND_BAR_INNER_HEIGHT,
            Consts.BOND_BAR_BORDER_WIDTH, Consts.BOND_BAR_BORDER_WIDTH,
            Consts.BOND_BAR_BORDER_WIDTH, Consts.BOND_BAR_BORDER_WIDTH
        ).toURLString());
        this.barGroup.getChildren().add(bondBar);

        this.desireBar = new ProgressBar(0);
        this.desireBar.setLayoutY(Consts.BOND_BAR_HEIGHT + Consts.BOND_BAR_MARGIN_BOTTOM + Consts.DESIRE_BAR_MARGIN_TOP);
        this.desireBar.setPrefWidth(Consts.BAR_WIDTH);
        this.desireBar.setPrefHeight(Consts.DESIRE_BAR_HEIGHT);
        //noinspection SuspiciousNameCombination
        this.desireBar.getStylesheets().add(new ProgressBarCss(
            Consts.DESIRE_BAR_INNER_HEIGHT,
            Consts.DESIRE_BAR_BORDER_WIDTH, Consts.DESIRE_BAR_BORDER_WIDTH,
            Consts.DESIRE_BAR_BORDER_WIDTH, Consts.DESIRE_BAR_BORDER_WIDTH
        ).toURLString());
        this.barGroup.getChildren().add(desireBar);
    }

    private final TimeBasedAnimationHelper barAnimationHelper = new TimeBasedAnimationHelper(
        250, this::animateBar);

    private double barOpacityBegin = 0;
    private double barOpacityTarget = 0;
    private double bondBarProgressBegin = 0;
    private double bondBarProgressTarget = 0;
    private double desireBarProgressBegin = 0;
    private double desireBarProgressTarget = 0;
    private double finalBarOpacity = 0;
    private boolean animatingShowBar = false;
    private boolean alwaysShowBar = false;

    public void setAlwaysShowBar(boolean alwaysShowBar) {
        this.alwaysShowBar = alwaysShowBar;
        if (alwaysShowBar) {
            doShowBar();
        } else {
            doHideBar();
        }
    }

    public void doShowBar() {
        animatingShowBar = true;
        finalBarOpacity = 1;
        if (barAnimationHelper.isPlaying()) {
            if (barOpacityTarget != 1) {
                // is animating to hide
                barOpacityTarget = 1;
            }
            // else {
            //   is animating to show
            //   so do nothing, it will keep showing later
            // }
        } else {
            if (barGroup.getOpacity() == 1) {
                return; // already showing, do nothing
            }
            barOpacityBegin = barGroup.getOpacity();
            barOpacityTarget = 1;
            bondBarProgressBegin = bondBar.getProgress();
            bondBarProgressTarget = bondBar.getProgress();
            desireBarProgressBegin = desireBar.getProgress();
            desireBarProgressTarget = desireBar.getProgress();
            barAnimationHelper.setFinishCallback(() -> {
                animatingShowBar = false;
                barAnimationHelper.setFinishCallback(null);
            }).play();
        }
    }

    public void doHideBar() {
        if (alwaysShowBar) {
            return;
        }
        var animatingShowBarLocal = animatingShowBar;
        animatingShowBar = false;
        finalBarOpacity = 0;
        if (barAnimationHelper.isPlaying()) {
            if (barOpacityTarget != 1 || animatingShowBarLocal) {
                // is animating to hide
                // change the target
                barOpacityTarget = 0;
            }
            // else {
            //   is animating to show
            //   so do nothing, it will hide later
            // }
        } else {
            if (barGroup.getOpacity() == 0) {
                return; // already hidden, do nothing
            }
            barOpacityBegin = barGroup.getOpacity();
            barOpacityTarget = 0;
            bondBarProgressBegin = bondBar.getProgress();
            bondBarProgressTarget = bondBar.getProgress();
            desireBarProgressBegin = desireBar.getProgress();
            desireBarProgressTarget = desireBar.getProgress();
            barAnimationHelper.play();
        }
    }

    private double fixCharaPoint(double d) {
        if (d < 0) {
            return 0;
        }
        if (d > 1) {
            return 1;
        }
        return d;
    }

    public void setCharaPoints(CharaPoints points) {
        double bondCurrent = fixCharaPoint(points.bondCurrent);
        double bondPrevious = fixCharaPoint(points.bondPrevious);
        double desireCurrent = fixCharaPoint(points.desireCurrent);
        double desirePrevious = fixCharaPoint(points.desirePrevious);

        ConfigManager.get().setBondPoint(bondCurrent);
        ConfigManager.get().setDesirePoint(desireCurrent);

        if (bondCurrent >= bondPrevious) {
            bondBar.setStyle("-fx-accent: " + points.bondIncrColor + ";");
        } else {
            bondBar.setStyle("-fx-accent: " + points.bondDecrColor + ";");
        }
        if (desireCurrent >= desirePrevious) {
            desireBar.setStyle("-fx-accent: " + points.desireIncrColor + ";");
        } else {
            desireBar.setStyle("-fx-accent: " + points.desireDecrColor + ";");
        }
        // reduce animation flashes when opacity changes rapidly
        barOpacityBegin = barGroup.getOpacity() + 0.01;
        barOpacityBegin = barOpacityBegin > 1 ? 1 : barOpacityBegin;
        barOpacityTarget = 1;
        bondBarProgressBegin = bondPrevious;
        bondBarProgressTarget = bondPrevious;
        desireBarProgressBegin = desirePrevious;
        desireBarProgressTarget = desirePrevious;
        barAnimationHelper.setDuration(barOpacityBegin == 1 ? 0 : 250);

        barAnimationHelper
            .setFinishCallback(() -> {
                barOpacityBegin = 1;
                barOpacityTarget = 1;
                bondBarProgressBegin = bondPrevious;
                bondBarProgressTarget = bondCurrent;
                desireBarProgressBegin = desirePrevious;
                desireBarProgressTarget = desireCurrent;
                barAnimationHelper.setDuration(1000);
                barAnimationHelper.setFinishCallback(() -> {
                    barOpacityBegin = 1;
                    barOpacityTarget = finalBarOpacity;
                    bondBarProgressBegin = bondCurrent;
                    bondBarProgressTarget = bondCurrent;
                    desireBarProgressBegin = desireCurrent;
                    desireBarProgressTarget = desireCurrent;
                    barAnimationHelper.setDuration(250);
                    barAnimationHelper.setFinishCallback(null);
                    barAnimationHelper.play();
                });
                barAnimationHelper.play();
            });
        barAnimationHelper.play();
    }

    private void animateBar(double percentage) {
        double opacity = (barOpacityTarget - barOpacityBegin) * percentage + barOpacityBegin;
        barGroup.setOpacity(opacity);
        double bondProgress = (bondBarProgressTarget - bondBarProgressBegin) * percentage + bondBarProgressBegin;
        bondBar.setProgress(bondProgress);
        double desireProgress = (desireBarProgressTarget - desireBarProgressBegin) * percentage + desireBarProgressBegin;
        desireBar.setProgress(desireProgress);
    }
}
