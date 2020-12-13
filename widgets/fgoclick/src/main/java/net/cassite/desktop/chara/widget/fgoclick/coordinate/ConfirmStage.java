// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.coordinate;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Utils;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickUtils;

import static net.cassite.desktop.chara.widget.fgoclick.util.FgoClickI18nConsts.*;

public class ConfirmStage extends FgoClickAbstractStage {
    private final CalculatedAnchor anchor;
    private final Robot robot;
    private final Pane[] panes;
    private final Pane battlePane;
    private final Pane attackPane;
    private final Pane supportPane;
    private final Pane menuPane;

    public ConfirmStage(FgoClickConsts consts, CalculatedAnchor anchor, Robot robot) {
        super(consts);
        this.anchor = anchor;
        this.robot = robot;

        addPoint(anchor.getOrigin(), originPoint);
        addPoint(anchor.getMaxXY(), maxXYPoint);
        addPoint(anchor.getTitle(), titlePoint);

        battlePane = new Pane();
        battlePane.setBackground(Background.EMPTY);
        addPoint(battlePane, anchor.getSkill11(), skillPoint.get()[0] + "1.1");
        addPoint(battlePane, anchor.getSkill12(), skillPoint.get()[0] + "1.2");
        addPoint(battlePane, anchor.getSkill13(), skillPoint.get()[0] + "1.3");
        addPoint(battlePane, anchor.getSkill21(), skillPoint.get()[0] + "2.1");
        addPoint(battlePane, anchor.getSkill22(), skillPoint.get()[0] + "2.2");
        addPoint(battlePane, anchor.getSkill23(), skillPoint.get()[0] + "2.3");
        addPoint(battlePane, anchor.getSkill31(), skillPoint.get()[0] + "3.1");
        addPoint(battlePane, anchor.getSkill32(), skillPoint.get()[0] + "3.2");
        addPoint(battlePane, anchor.getSkill33(), skillPoint.get()[0] + "3.3");
        addPoint(battlePane, anchor.getMasterItem(), masterItemPoint);
        addPoint(battlePane, anchor.getMasterSkill1(), masterSkillPoint.get()[0] + "1");
        addPoint(battlePane, anchor.getMasterSkill2(), masterSkillPoint.get()[0] + "2");
        addPoint(battlePane, anchor.getMasterSkill3(), masterSkillPoint.get()[0] + "3");
        addRec(battlePane, anchor.getAttack(), attackPoint);
        setAttackLabels();
        addPoint(battlePane, anchor.getSelect1Of3(), selectPoint.get()[0] + "1/3");
        addPoint(battlePane, anchor.getSelect2Of3(), selectPoint.get()[0] + "2/3");
        addPoint(battlePane, anchor.getSelect3Of3(), selectPoint.get()[0] + "3/3");
        addPoint(battlePane, anchor.getConfirm(), confirmPoint);

        attackPane = new Pane();
        attackPane.setBackground(Background.EMPTY);
        addRec(attackPane, anchor.getOrderCard1(), orderCardPoint.get()[0] + "1");
        addRec(attackPane, anchor.getOrderCard2(), orderCardPoint.get()[0] + "2");
        addRec(attackPane, anchor.getOrderCard3(), orderCardPoint.get()[0] + "3");
        addRec(attackPane, anchor.getOrderCard4(), orderCardPoint.get()[0] + "4");
        addRec(attackPane, anchor.getOrderCard5(), orderCardPoint.get()[0] + "5");
        addPoint(attackPane, anchor.getNoblePhantasm1(), noblePhantasmPoint.get()[0] + "1");
        addPoint(attackPane, anchor.getNoblePhantasm2(), noblePhantasmPoint.get()[0] + "2");
        addPoint(attackPane, anchor.getNoblePhantasm3(), noblePhantasmPoint.get()[0] + "3");

        Pane finish = new Pane();
        finish.setBackground(Background.EMPTY);
        addPoint(finish, anchor.getDummyClick(), dummyClickPoint);
        addPoint(finish, anchor.getNext(), nextPoint);
        addRec(finish, anchor.getApCost(), apCostRec);
        addPoint(finish, anchor.getNoDirectNext(), noDirectNextPoint);
        addPoint(finish, anchor.getDirectNextGame(), directNextGamePoint);
        addPoint(finish, anchor.getApStone(), apStonePoint);
        addPoint(finish, anchor.getAppleGolden(), appleGoldenPoint);
        addPoint(finish, anchor.getAppleSilver(), appleSilverPoint);
        addPoint(finish, anchor.getAppleCopper(), appleCopperPoint);
        addPoint(finish, anchor.getConfirmToUseApple(), confirmToUseApplePoint);

        supportPane = new Pane();
        supportPane.setBackground(Background.EMPTY);
        addPoint(supportPane, anchor.getCasterSupport(), casterSupportPoint);
        addPoint(supportPane, anchor.getRefreshList(), refreshListPoint);
        addRec(supportPane, anchor.getSupport1Skill1(), supportSkillPoint.get()[0] + "1.1");
        addRec(supportPane, anchor.getSupport1Skill2(), supportSkillPoint.get()[0] + "1.2");
        addRec(supportPane, anchor.getSupport1Skill3(), supportSkillPoint.get()[0] + "1.3");
        addRec(supportPane, anchor.getSupport2Skill1(), supportSkillPoint.get()[0] + "2.1");
        addRec(supportPane, anchor.getSupport2Skill2(), supportSkillPoint.get()[0] + "2.2");
        addRec(supportPane, anchor.getSupport2Skill3(), supportSkillPoint.get()[0] + "2.3");
        addPoint(supportPane, anchor.getChooseSupport1(), chooseSupportPoint.get()[0] + "1");
        addPoint(supportPane, anchor.getChooseSupport2(), chooseSupportPoint.get()[0] + "2");
        addPoint(supportPane, anchor.getConfirmRefreshingSupport(), confirmRefreshingSupportPoint);

        menuPane = new Pane();
        menuPane.setBackground(Background.EMPTY);
        addPoint(menuPane, anchor.getLastGame(), lastGamePoint);
        addRec(menuPane, anchor.getGoldenAppleIcon(), goldenAppleIconPoint);
        addRec(menuPane, anchor.getSilverAppleIcon(), silverAppleIconPoint);
        addPoint(menuPane, anchor.getCancelApplePoint(), cancelApplePoint);

        addPoint(menuPane, anchor.getStart(), startPoint);

        pane.getChildren().add(battlePane);
        panes = new Pane[]{
            battlePane,
            attackPane,
            finish,
            supportPane,
            menuPane,
        };

        pane.setOnMouseClicked(e -> this.switchPane());
    }

    private int index = 0;

    private void switchPane() {
        Pane last;
        Pane next;
        int i = ++index;
        if (i >= panes.length) {
            index = 0;
            last = panes[panes.length - 1];
            next = panes[0];
        } else {
            last = panes[i - 1];
            next = panes[i];
        }
        pane.getChildren().remove(last);
        if (next == battlePane) {
            hideThis();
            Utils.delayNoRecord(100, () -> {
                setAttackLabels();
                show();
                pane.getChildren().add(next);
            });
        } else if (next == attackPane) {
            hideThis();
            Utils.delayNoRecord(100, () -> {
                setOrderCardLabels();
                show();
                pane.getChildren().add(next);
            });
        } else if (next == supportPane) {
            hideThis();
            Utils.delayNoRecord(100, () -> {
                setSupportSkillLabels();
                show();
                pane.getChildren().add(next);
            });
        } else if (next == menuPane) {
            hideThis();
            Utils.delayNoRecord(100, () -> {
                setAppleIconLabels();
                show();
                pane.getChildren().add(next);
            });
        } else {
            pane.getChildren().add(next);
        }
    }

    private Group attackButtonColor = null;

    private void setAttackLabels() {
        if (attackButtonColor != null) {
            battlePane.getChildren().remove(attackButtonColor);
        }
        Image img = robot.getScreenCapture(null,
            anchor.getStage().x + anchor.getAttack().topLeft.x,
            anchor.getStage().y + anchor.getAttack().topLeft.y,
            anchor.getAttack().bottomRight.x - anchor.getAttack().topLeft.x + 1,
            anchor.getAttack().bottomRight.y - anchor.getAttack().topLeft.y + 1);
        FgoClickUtils.debugImage(img, "attack");
        Point origin = anchor.getAttack().topLeft;
        Logger.info("attack button");
        attackButtonColor = makePoint(img, origin, anchor.getAttack());

        battlePane.getChildren().add(attackButtonColor);
    }

    private final Group[] orderCardColors = new Group[5];

    private void setOrderCardLabels() {
        for (var g : orderCardColors) {
            if (g != null) {
                attackPane.getChildren().remove(g);
            }
        }
        Image img = robot.getScreenCapture(null,
            anchor.getStage().x + anchor.getOrderCard1().topLeft.x,
            anchor.getStage().y + anchor.getOrderCard1().topLeft.y,
            anchor.getOrderCard5().bottomRight.x - anchor.getOrderCard1().topLeft.x + 1, // +1 to avoid out of bounds
            anchor.getOrderCard5().bottomRight.y - anchor.getOrderCard1().topLeft.y + 1);
        FgoClickUtils.debugImage(img, "order card");
        Point origin = anchor.getOrderCard1().topLeft;
        Logger.info("point order card 1");
        orderCardColors[0] = makePoint(img, origin, anchor.getOrderCard1());
        Logger.info("point order card 2");
        orderCardColors[1] = makePoint(img, origin, anchor.getOrderCard2());
        Logger.info("point order card 3");
        orderCardColors[2] = makePoint(img, origin, anchor.getOrderCard3());
        Logger.info("point order card 4");
        orderCardColors[3] = makePoint(img, origin, anchor.getOrderCard4());
        Logger.info("point order card 5");
        orderCardColors[4] = makePoint(img, origin, anchor.getOrderCard5());

        for (var g : orderCardColors) {
            attackPane.getChildren().add(g);
        }
    }

    private final Group[] skillColors = new Group[6];

    private void setSupportSkillLabels() {
        for (var g : skillColors) {
            if (g != null) {
                supportPane.getChildren().remove(g);
            }
        }
        Image img = FgoClickUtils.getSupportSkillImage(robot, anchor);
        Point origin = anchor.getSupport1Skill1().topLeft;
        Logger.info("point support skill 1 1");
        skillColors[0] = makePoint(img, origin, anchor.getSupport1Skill1());
        Logger.info("point support skill 1 2");
        skillColors[1] = makePoint(img, origin, anchor.getSupport1Skill2());
        Logger.info("point support skill 1 3");
        skillColors[2] = makePoint(img, origin, anchor.getSupport1Skill3());
        Logger.info("point support skill 2 1");
        skillColors[3] = makePoint(img, origin, anchor.getSupport2Skill1());
        Logger.info("point support skill 2 2");
        skillColors[4] = makePoint(img, origin, anchor.getSupport2Skill2());
        Logger.info("point support skill 2 3");
        skillColors[5] = makePoint(img, origin, anchor.getSupport2Skill3());

        for (var g : skillColors) {
            supportPane.getChildren().add(g);
        }
    }

    private final Group[] appleIconColors = new Group[2];

    private void setAppleIconLabels() {
        for (var g : appleIconColors) {
            if (g != null) {
                menuPane.getChildren().remove(g);
            }
        }
        Image img = robot.getScreenCapture(null,
            anchor.getStage().x + anchor.getGoldenAppleIcon().topLeft.x,
            anchor.getStage().y + anchor.getGoldenAppleIcon().topLeft.y,
            anchor.getSilverAppleIcon().bottomRight.x - anchor.getGoldenAppleIcon().topLeft.x,
            anchor.getSilverAppleIcon().bottomRight.y - anchor.getGoldenAppleIcon().topLeft.y);
        FgoClickUtils.debugImage(img, "apple icon");
        Point origin = anchor.getGoldenAppleIcon().topLeft;
        Logger.info("point golden apple icon");
        appleIconColors[0] = makePoint(img, origin, anchor.getGoldenAppleIcon());
        Logger.info("point silver apple icon");
        appleIconColors[1] = makePoint(img, origin, anchor.getSilverAppleIcon());

        for (var g : appleIconColors) {
            menuPane.getChildren().add(g);
        }
    }

    private Group makePoint(Image img, Point origin, Rec rec) {
        double w = (int) (rec.bottomRight.x - rec.topLeft.x);
        double h = (int) (rec.bottomRight.y - rec.topLeft.y);
        Color color = FgoClickUtils.calcColorToShow(img, origin, rec, consts);

        var inner = new Circle(consts.clickPointCircleInnerRadius);
        inner.setFill(color);
        var outer = new Circle(consts.clickPointCircleOuterRadius);
        outer.setFill(new Color(1, 1, 1, 1));

        Group point = new Group(outer, inner);

        point.setLayoutX(rec.topLeft.x + w / 2);
        point.setLayoutY(rec.topLeft.y + h / 2);

        return point;
    }
}
