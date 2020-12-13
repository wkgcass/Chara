// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.coordinate;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Line;
import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Utils;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickI18nConsts;

import java.util.function.Consumer;

public class AnchorSelector extends FgoClickAbstractStage {
    private final Robot robot;
    private final Consumer<Anchor> callback;
    private final AppCallback appCallback;
    private final Line lineH = new Line();
    private final Line lineV = new Line();

    public AnchorSelector(Robot robot, AppCallback appCallback, FgoClickConsts consts, Consumer<Anchor> callback) {
        super(consts);
        this.robot = robot;
        this.appCallback = appCallback;
        this.callback = callback;

        lineH.setStroke(new Color(1, 0, 0, 1));
        lineH.setStrokeWidth(1.5);
        lineV.setStroke(new Color(1, 0, 0, 1));
        lineV.setStrokeWidth(1.5);
        pane.getChildren().addAll(lineH, lineV);

        pane.setOnMouseClicked(this::click);
        pane.setOnMouseMoved(e -> {
            lineH.setStartX(-1);
            lineH.setStartY(e.getY());
            lineH.setEndX(getWidth() + 1);
            lineH.setEndY(e.getY());

            lineV.setStartX(e.getX());
            lineV.setStartY(-1);
            lineV.setEndX(e.getX());
            lineV.setEndY(getHeight() + 1);
        });

        appCallback.clearAllMessages();
        appCallback.showMessage(FgoClickI18nConsts.clickTitleBarHelpMessage, true);
    }

    private int step = 0;
    private final Anchor anchor = new Anchor();

    private void click(MouseEvent e) {
        var p = new Point(e.getX(), e.getY());
        switch (step) {
            case 0:
                anchor.title = p;
                addPoint(p, FgoClickI18nConsts.titlePoint.get()[0]);
                appCallback.clearAllMessages();
                appCallback.showMessage(FgoClickI18nConsts.clickServant1Skill1HelpMessage, true);
                step = 1;
                break;
            case 1:
                anchor.skill11 = p;
                addPoint(p, FgoClickI18nConsts.skillPoint.get()[0] + "1.1");
                appCallback.clearAllMessages();
                appCallback.showMessage(FgoClickI18nConsts.clickServant1Skill2HelpMessage, true);
                step = 2;
                break;
            case 2:
                anchor.skill12 = p;
                addPoint(p, FgoClickI18nConsts.skillPoint.get()[0] + "1.2");
                appCallback.clearAllMessages();
                appCallback.showMessage(FgoClickI18nConsts.clickServant1Skill3HelpMessage, true);
                step = 3;
                break;
            case 3:
                anchor.skill13 = p;
                addPoint(p, FgoClickI18nConsts.skillPoint.get()[0] + "1.3");
                appCallback.clearAllMessages();
                appCallback.showMessage(FgoClickI18nConsts.clickServant2Skill2HelpMessage, true);
                step = 4;
                break;
            case 4:
                anchor.skill22 = p;
                addPoint(p, FgoClickI18nConsts.skillPoint.get()[0] + "2.2");
                appCallback.clearAllMessages();
                appCallback.showMessage(FgoClickI18nConsts.clickServant3Skill3HelpMessage, true);
                step = 5;
                break;
            case 5:
                anchor.skill33 = p;
                addPoint(p, FgoClickI18nConsts.skillPoint.get()[0] + "3.3");
                appCallback.clearAllMessages();
                appCallback.showMessage(FgoClickI18nConsts.clickMasterItemHelpMessage, true);
                step = 6;
                break;
            case 6:
                anchor.masterItem = p;
                addPoint(p, FgoClickI18nConsts.masterItemPoint.get()[0]);
                robotClick(p, () -> {
                    appCallback.clearAllMessages();
                    appCallback.showMessage(FgoClickI18nConsts.clickMasterSkill3HelpMessage, true);
                    step = 7;
                });
                break;
            case 7:
                anchor.masterSkill3 = p;
                addPoint(p, FgoClickI18nConsts.masterSkillPoint.get()[0] + "3");
                appCallback.clearAllMessages();
                appCallback.showMessage(FgoClickI18nConsts.clickMasterSkill1HelpMessage, true);
                step = 8;
                break;
            case 8:
                anchor.masterSkill1 = p;
                addPoint(p, FgoClickI18nConsts.masterSkillPoint.get()[0] + "1");
                appCallback.clearAllMessages();
                appCallback.showMessage(FgoClickI18nConsts.clickTrophyHelpMessage, true);
                step = 9;
                break;
            case 9:
                anchor.trophy = p;
                addPoint(p, FgoClickI18nConsts.trophyPoint.get()[0]);
                appCallback.clearAllMessages();
                appCallback.showMessage(FgoClickI18nConsts.clickAttackHelpMessage, true);
                step = 10;
                break;
            case 10:
                anchor.attack = p;
                addPoint(p, FgoClickI18nConsts.attackPoint.get()[0]);
                robotClick(p, () -> {
                    appCallback.clearAllMessages();
                    appCallback.showMessage(FgoClickI18nConsts.clickBackHelpMessage, true);
                    step = 11;
                });
                break;
            case 11:
                anchor.back = p;
                addPoint(p, FgoClickI18nConsts.backPoint.get()[0]);
                robotClick(p, () -> {
                    appCallback.clearAllMessages();
                    step = 12;
                    callback.accept(anchor);
                });
                break;
            case 100:
                // do nothing
                break;
            default:
                Logger.error("should not reach here with step=" + step);
        }
    }

    private void robotClick(Point p, Runnable cb) {
        hideThis();
        step = 100;
        Utils.delayNoRecord(100, () -> {
            // move to title and click
            robot.mouseMove(getX() + anchor.title.x, getY() + anchor.title.y);
            robot.mouseClick(MouseButton.PRIMARY);
            Utils.delayNoRecord(100, () -> {
                // move to 'attack' and click
                robot.mouseMove(getX() + p.x, getY() + p.y);
                robot.mouseClick(MouseButton.PRIMARY);
                Utils.delayNoRecord(100, () -> {
                    show();
                    cb.run();
                });
            });
        });
    }
}
