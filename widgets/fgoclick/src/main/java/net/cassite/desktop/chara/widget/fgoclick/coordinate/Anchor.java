// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.coordinate;

import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;

public class Anchor {
    public Point title;
    public Point skill11;
    public Point skill12;
    public Point skill13;
    public Point skill22;
    public Point skill33;
    public Point masterItem;
    public Point masterSkill3;
    public Point masterSkill1;
    public Point attack;
    public Point trophy;
    public Point back;

    public Anchor() {
    }

    public Anchor(ConfigManager.Config config) {
        title = new Point(
            config.getDoubleValue(FgoClickConsts.titleX),
            config.getDoubleValue(FgoClickConsts.titleY)
        );
        skill11 = new Point(
            config.getDoubleValue(FgoClickConsts.skill11X),
            config.getDoubleValue(FgoClickConsts.skill11Y)
        );
        skill12 = new Point(
            config.getDoubleValue(FgoClickConsts.skill12X),
            config.getDoubleValue(FgoClickConsts.skill12Y)
        );
        skill13 = new Point(
            config.getDoubleValue(FgoClickConsts.skill13X),
            config.getDoubleValue(FgoClickConsts.skill13Y)
        );
        skill22 = new Point(
            config.getDoubleValue(FgoClickConsts.skill22X),
            config.getDoubleValue(FgoClickConsts.skill22Y)
        );
        skill33 = new Point(
            config.getDoubleValue(FgoClickConsts.skill33X),
            config.getDoubleValue(FgoClickConsts.skill33Y)
        );
        masterItem = new Point(
            config.getDoubleValue(FgoClickConsts.masterItemX),
            config.getDoubleValue(FgoClickConsts.masterItemY)
        );
        masterSkill3 = new Point(
            config.getDoubleValue(FgoClickConsts.masterSkill3X),
            config.getDoubleValue(FgoClickConsts.masterSkill3Y)
        );
        masterSkill1 = new Point(
            config.getDoubleValue(FgoClickConsts.masterSkill1X),
            config.getDoubleValue(FgoClickConsts.masterSkill1Y)
        );
        attack = new Point(
            config.getDoubleValue(FgoClickConsts.attackX),
            config.getDoubleValue(FgoClickConsts.attackY)
        );
        trophy = new Point(
            config.getDoubleValue(FgoClickConsts.trophyX),
            config.getDoubleValue(FgoClickConsts.trophyY)
        );
        back = new Point(
            config.getDoubleValue(FgoClickConsts.backX),
            config.getDoubleValue(FgoClickConsts.backY)
        );
    }

    public void save() {
        ConfigManager.get().setDoubleValue(FgoClickConsts.titleX, title.x);
        ConfigManager.get().setDoubleValue(FgoClickConsts.titleY, title.y);
        ConfigManager.get().setDoubleValue(FgoClickConsts.skill11X, skill11.x);
        ConfigManager.get().setDoubleValue(FgoClickConsts.skill11Y, skill11.y);
        ConfigManager.get().setDoubleValue(FgoClickConsts.skill12X, skill12.x);
        ConfigManager.get().setDoubleValue(FgoClickConsts.skill12Y, skill12.y);
        ConfigManager.get().setDoubleValue(FgoClickConsts.skill13X, skill13.x);
        ConfigManager.get().setDoubleValue(FgoClickConsts.skill13Y, skill13.y);
        ConfigManager.get().setDoubleValue(FgoClickConsts.skill22X, skill22.x);
        ConfigManager.get().setDoubleValue(FgoClickConsts.skill22Y, skill22.y);
        ConfigManager.get().setDoubleValue(FgoClickConsts.skill33X, skill33.x);
        ConfigManager.get().setDoubleValue(FgoClickConsts.skill33Y, skill33.y);
        ConfigManager.get().setDoubleValue(FgoClickConsts.masterItemX, masterItem.x);
        ConfigManager.get().setDoubleValue(FgoClickConsts.masterItemY, masterItem.y);
        ConfigManager.get().setDoubleValue(FgoClickConsts.masterSkill3X, masterSkill3.x);
        ConfigManager.get().setDoubleValue(FgoClickConsts.masterSkill3Y, masterSkill3.y);
        ConfigManager.get().setDoubleValue(FgoClickConsts.masterSkill1X, masterSkill1.x);
        ConfigManager.get().setDoubleValue(FgoClickConsts.masterSkill1Y, masterSkill1.y);
        ConfigManager.get().setDoubleValue(FgoClickConsts.attackX, attack.x);
        ConfigManager.get().setDoubleValue(FgoClickConsts.attackY, attack.y);
        ConfigManager.get().setDoubleValue(FgoClickConsts.trophyX, trophy.x);
        ConfigManager.get().setDoubleValue(FgoClickConsts.trophyY, trophy.y);
        ConfigManager.get().setDoubleValue(FgoClickConsts.backX, back.x);
        ConfigManager.get().setDoubleValue(FgoClickConsts.backY, back.y);
    }

    private static final double sampleWidth = 1334;
    private static final double sampleHeight = 750;
    private static final double sampleSkillDistanceX = 100;
    private static final double sampleServantSkillsDistanceX = 136;
    private static final double sampleDeltaYBetweenSkillAndMasterSkill = 268;
    private static final double sampleDeltaYBetweenMasterSkillAndMasterSkill = 308;
    private static final double sampleDeltaYBetweenTrophyAndBack = 685;
    private static final double sampleDeltaYBetweenSkillAndTrophy = 576;
    private static final double sampleSkill12X = 170;
    private static final double sampleSkill12Y = 607;
    private static final double sampleTrophyX = 1013;
    private static final double sampleMasterSkillY = 327;
    private static final double sampleBackY = 712;

    private static final double attackLeftX = 1115;
    private static final double attackTopY = 594;
    private static final double attackRightX = 1252;
    private static final double attackBottomY = 656;
    private static final double orderCard1LeftX = 95;
    private static final double orderCard1RightX = 189;
    private static final double orderCard2LeftX = 361;
    private static final double orderCard2RightX = 456;
    private static final double orderCard3LeftX = 615;
    private static final double orderCard3RightX = 727;
    private static final double orderCard4LeftX = 885;
    private static final double orderCard4RightX = 991;
    private static final double orderCard5LeftX = 1152;
    private static final double orderCard5RightX = 1264;
    private static final double orderCardTopY = 560;
    private static final double orderCardBottomY = 616;
    private static final double noblePhantasm1X = 428;
    private static final double noblePhantasm2X = 676;
    private static final double noblePhantasm3X = 913;
    private static final double noblePhantasmY = 222;
    private static final double select1Of3X = 341;
    private static final double select3Of3X = 666;
    private static final double select2Of3X = 992;
    private static final double selectOf3Y = 449;
    private static final double confirmX = 889;
    private static final double confirmY = 441;

    private static final double dummyClickX = 340;
    private static final double dummyClickY = 712;
    private static final double nextX = 1152;
    private static final double nextY = 704;
    private static final double apCostTopLeftX = 817;
    private static final double apCostTopLeftY = 391;
    private static final double apCostBottomRightX = 862;
    private static final double apCostBottomRightY = 422;
    private static final double noDirectNextX = 461;
    private static final double noDirectNextY = 590;
    private static final double directNextGameX = 875;
    private static final double directNextGameY = 592;
    private static final double selectAppleX = 667;
    private static final double apStoneY = 183;
    private static final double goldenAppleY = 339;
    private static final double silverAppleY = 485;
    private static final double copperAppleY = 517;
    private static final double confirmToUseAppleX = 871;
    private static final double confirmToUseAppleY = 590;
    private static final double casterSupportX = 446;
    private static final double refreshListX = 889;
    private static final double supportPageButtonY = 135;
    private static final double SUPPORT_SKILL_INC = 5;
    private static final double support1SkillTopY = 325 + SUPPORT_SKILL_INC;
    private static final double support1SkillBottomY = 372 - SUPPORT_SKILL_INC;
    private static final double support2SkillTopY = 533 + SUPPORT_SKILL_INC;
    private static final double support2SkillBottomY = 581 - SUPPORT_SKILL_INC;
    private static final double supportSkill1LeftX = 874 + SUPPORT_SKILL_INC;
    private static final double supportSkill1RightX = 924 - SUPPORT_SKILL_INC;
    private static final double supportSkill2LeftX = 955 + SUPPORT_SKILL_INC;
    private static final double supportSkill2RightX = 1004 - SUPPORT_SKILL_INC;
    private static final double supportSkill3LeftX = 1036 + SUPPORT_SKILL_INC;
    private static final double supportSkill3RightX = 1084 - SUPPORT_SKILL_INC;
    private static final double chooseSupportX = 636;
    private static final double chooseSupport1Y = 293;
    private static final double chooseSupport2Y = 519;
    private static final double confirmRefreshingSupportX = 774;
    private static final double confirmRefreshingSupportY = 586;

    private static final double lastGameX = 962;
    private static final double lastGameY = 250;
    private static final double cancelAppleX = 630;
    private static final double cancelAppleY = 648;
    private static final double appleIconLeftX = 349;
    private static final double appleIconRightX = 428;
    private static final double goldenAppleIconTopY = 300;
    private static final double goldenAppleIconBottomY = 377;
    private static final double silverAppleIconTopY = 458;
    private static final double silverAppleIconBottomY = 532;

    private static final double startX = 1239;
    private static final double startY = 705;

    public CalculatedAnchor calculate() {
        CalculatedAnchor ret = new CalculatedAnchor();
        ret.setStage(new Point(ConfigManager.get().getDoubleValue(FgoClickConsts.boundsX), ConfigManager.get().getDoubleValue(FgoClickConsts.boundsY)));
        ret.setTitle(title);
        double skillY = (skill11.y + skill12.y + skill13.y + skill22.y + skill33.y) / 5;
        double skillDistance = ((skill12.x - skill11.x) + (skill13.x - skill12.x)) / 2;
        if (skill11.x > skill12.x)
            throw new IllegalArgumentException("skill_1_1.x > skill_1_2.x: " + skill11.x + " > " + skill12.x);
        if (skill12.x > skill13.x)
            throw new IllegalArgumentException("skill_1_2.x > skill_1_3.x: " + skill12.x + " > " + skill13.x);
        if (skill13.x > skill22.x)
            throw new IllegalArgumentException("skill_1_3.x > skill_2_2.x: " + skill13.x + " > " + skill22.x);
        if (skill22.x > skill33.x)
            throw new IllegalArgumentException("skill_2_2.x > skill_3_3.x: " + skill22.x + " > " + skill33.x);
        double calcSkill11X = skill12.x - skillDistance;
        double calcSkill13X = skill12.x + skillDistance;
        double calcSkill21X = skill22.x - skillDistance;
        double calcSkill23X = skill22.x + skillDistance;
        double calcSkill31X = skill33.x - 2 * skillDistance;
        double calcSkill32X = skill33.x - skillDistance;
        if (calcSkill13X > calcSkill21X)
            throw new IllegalArgumentException("skill_1_3.x > skill_2_1.x: " + calcSkill13X + " > " + calcSkill21X);
        if (calcSkill23X > calcSkill31X)
            throw new IllegalArgumentException("skill_2_3.x > skill_3_1.x: " + calcSkill23X + " > " + calcSkill31X);
        ret.setSkill11(new Point(calcSkill11X, skillY));
        ret.setSkill12(new Point(skill12.x, skillY));
        ret.setSkill13(new Point(calcSkill13X, skillY));
        ret.setSkill21(new Point(calcSkill21X, skillY));
        ret.setSkill22(new Point(skill22.x, skillY));
        ret.setSkill23(new Point(calcSkill23X, skillY));
        ret.setSkill31(new Point(calcSkill31X, skillY));
        ret.setSkill32(new Point(calcSkill32X, skillY));
        ret.setSkill33(new Point(skill33.x, skillY));
        double masterSkill1X = masterSkill1.x;
        double masterSkill3X = masterSkill3.x;
        if (masterSkill1X > masterSkill3X)
            throw new IllegalArgumentException("master_skill_1.x > master_skill_3.x: " + masterSkill1X + " > " + masterSkill3X);
        double masterSkillDistance = (masterSkill3X - masterSkill1X) / 2;
        if (Math.abs(masterSkillDistance - skillDistance) > 10)
            throw new IllegalArgumentException("master_skill_distance differs too much from skill_distance: " + masterSkillDistance + ", " + skillDistance);
        double masterSkill2X = masterSkill1X + masterSkillDistance;
        double masterSkillY = (masterSkill1.y + masterSkill3.y + masterItem.y) / 3;
        ret.setMasterItem(new Point(masterItem.x, masterSkillY));
        ret.setMasterSkill1(new Point(masterSkill1X, masterSkillY));
        ret.setMasterSkill2(new Point(masterSkill2X, masterSkillY));
        ret.setMasterSkill3(new Point(masterSkill3X, masterSkillY));

        if (masterItem.y > skillY)
            throw new IllegalArgumentException("master_item.y > skill.y: " + masterItem.y + " > " + skillY);
        if (trophy.y > skillY)
            throw new IllegalArgumentException("trophy.y > skill.y: " + trophy.y + " > " + skillY);
        if (trophy.y > back.y)
            throw new IllegalArgumentException("trophy.y > back.y: " + trophy.y + " > " + back.y);
        if (trophy.y > masterSkillY)
            throw new IllegalArgumentException("trophy.y > master_skill.y: " + trophy.y + " > " + masterSkillY);

        // the following coordinates must be calculated with proportion
        double pX1 = skillDistance / sampleSkillDistanceX;
        double pX2 = ((calcSkill21X - calcSkill13X + calcSkill31X - calcSkill23X) / 2) / sampleServantSkillsDistanceX;
        double pY1 = (skillY - masterItem.y) / sampleDeltaYBetweenSkillAndMasterSkill;
        double pY2 = (skillY - trophy.y) / sampleDeltaYBetweenSkillAndTrophy;
        double pY3 = (back.y - trophy.y) / sampleDeltaYBetweenTrophyAndBack;
        double pY4 = (masterSkillY - trophy.y) / sampleDeltaYBetweenMasterSkillAndMasterSkill;

        if (Math.abs(pX1 - pX2) > 0.1)
            throw new IllegalArgumentException("proportions x differ too much: skill_distance, servant_skill_distance: " + pX1 + ", " + pX2);

        if (Math.abs(pY1 - pY2) > 0.1)
            throw new IllegalArgumentException("proportions y differ too much: skill_to_master_skill, skill_to_trophy: " + pY1 + ", " + pY2);
        if (Math.abs(pY1 - pY3) > 0.1)
            throw new IllegalArgumentException("proportions y differ too much: skill_to_master_skill, back_to_trophy: " + pY1 + ", " + pY3);
        if (Math.abs(pY1 - pY4) > 0.1)
            throw new IllegalArgumentException("proportions y differ too much: skill_to_master_skill, master_skill_to_trophy: " + pY1 + ", " + pY4);
        //
        if (Math.abs(pY2 - pY3) > 0.1)
            throw new IllegalArgumentException("proportions y differ too much: skill_to_trophy, back_to_trophy: " + pY2 + ", " + pY3);
        if (Math.abs(pY2 - pY4) > 0.1)
            throw new IllegalArgumentException("proportions y differ too much: skill_to_trophy, master_skill_to_trophy: " + pY2 + ", " + pY4);
        //
        if (Math.abs(pY3 - pY4) > 0.1)
            throw new IllegalArgumentException("proportions y differ too much: back_to_trophy, master_skill_to_trophy: " + pY3 + ", " + pY4);

        double px = (pX1 + pX2) / 2;
        double py = (pY1 + pY2 + pY3 + pY4) / 4;

        double originX1 = skill12.x - sampleSkill12X * px;
        double originX2 = trophy.x - sampleTrophyX * px;
        //
        double originY1 = skillY - sampleSkill12Y * py;
        double originY2 = masterSkillY - sampleMasterSkillY * py;
        double originY3 = back.y - sampleBackY * py;

        double originX = (originX1 + originX2) / 2;
        double originY = (originY1 + originY2 + originY3) / 3;
        if (originX < 0)
            throw new IllegalArgumentException("calculated origin x < 0: " + originX);
        if (originY < 0)
            throw new IllegalArgumentException("calculated origin y < 0: " + originY);
        ret.setOrigin(new Point(originX, originY));
        ret.setMaxXY(new Point(originX + sampleWidth * px, originY + sampleHeight * py));

        ret.setAttack(new Rec(
            new Point(originX + attackLeftX * px, originY + attackTopY * py),
            new Point(originX + attackRightX * px, originY + attackBottomY * py)
        ));
        ret.setOrderCard1(new Rec(
            new Point(originX + orderCard1LeftX * px, originY + orderCardTopY * py),
            new Point(originX + orderCard1RightX * px, originY + orderCardBottomY * py)
        ));
        ret.setOrderCard2(new Rec(
            new Point(originX + orderCard2LeftX * px, originY + orderCardTopY * py),
            new Point(originX + orderCard2RightX * px, originY + orderCardBottomY * py)
        ));
        ret.setOrderCard3(new Rec(
            new Point(originX + orderCard3LeftX * px, originY + orderCardTopY * py),
            new Point(originX + orderCard3RightX * px, originY + orderCardBottomY * py)
        ));
        ret.setOrderCard4(new Rec(
            new Point(originX + orderCard4LeftX * px, originY + orderCardTopY * py),
            new Point(originX + orderCard4RightX * px, originY + orderCardBottomY * py)
        ));
        ret.setOrderCard5(new Rec(
            new Point(originX + orderCard5LeftX * px, originY + orderCardTopY * py),
            new Point(originX + orderCard5RightX * px, originY + orderCardBottomY * py)
        ));

        ret.setNoblePhantasm1(new Point(originX + noblePhantasm1X * px, originY + noblePhantasmY * py));
        ret.setNoblePhantasm2(new Point(originX + noblePhantasm2X * px, originY + noblePhantasmY * py));
        ret.setNoblePhantasm3(new Point(originX + noblePhantasm3X * px, originY + noblePhantasmY * py));

        ret.setSelect1Of3(new Point(originX + select1Of3X * px, originY + selectOf3Y * py));
        ret.setSelect2Of3(new Point(originX + select2Of3X * px, originY + selectOf3Y * py));
        ret.setSelect3Of3(new Point(originX + select3Of3X * px, originY + selectOf3Y * py));

        ret.setConfirm(new Point(originX + confirmX * px, originY + confirmY * py));

        ret.setDummyClick(new Point(originX + dummyClickX * px, originY + dummyClickY * py));
        ret.setNext(new Point(originX + nextX * px, originY + nextY * py));
        ret.setApCost(new Rec(
            new Point(originX + apCostTopLeftX * px, originY + apCostTopLeftY * py),
            new Point(originX + apCostBottomRightX * px, originY + apCostBottomRightY * py)
        ));
        ret.setNoDirectNext(new Point(originX + noDirectNextX * px, originY + noDirectNextY * py));
        ret.setDirectNextGame(new Point(originX + directNextGameX * px, originY + directNextGameY * py));
        ret.setApStone(new Point(originX + selectAppleX * px, originY + apStoneY * py));
        ret.setAppleGolden(new Point(originX + selectAppleX * px, originY + goldenAppleY * py));
        ret.setAppleSilver(new Point(originX + selectAppleX * px, originY + silverAppleY * py));
        ret.setAppleCopper(new Point(originX + selectAppleX * px, originY + copperAppleY * py));
        ret.setConfirmToUseApple(new Point(originX + confirmToUseAppleX * px, originY + confirmToUseAppleY * py));
        ret.setCasterSupport(new Point(originX + casterSupportX * px, originY + supportPageButtonY * py));
        ret.setRefreshList(new Point(originX + refreshListX * px, originY + supportPageButtonY * py));
        ret.setSupport1Skill1(new Rec(
            new Point(originX + supportSkill1LeftX * px, originY + support1SkillTopY * py),
            new Point(originX + supportSkill1RightX * px, originY + support1SkillBottomY * py)
        ));
        ret.setSupport1Skill2(new Rec(
            new Point(originX + supportSkill2LeftX * px, originY + support1SkillTopY * py),
            new Point(originX + supportSkill2RightX * px, originY + support1SkillBottomY * py)
        ));
        ret.setSupport1Skill3(new Rec(
            new Point(originX + supportSkill3LeftX * px, originY + support1SkillTopY * py),
            new Point(originX + supportSkill3RightX * px, originY + support1SkillBottomY * py)
        ));
        ret.setSupport2Skill1(new Rec(
            new Point(originX + supportSkill1LeftX * px, originY + support2SkillTopY * py),
            new Point(originX + supportSkill1RightX * px, originY + support2SkillBottomY * py)
        ));
        ret.setSupport2Skill2(new Rec(
            new Point(originX + supportSkill2LeftX * px, originY + support2SkillTopY * py),
            new Point(originX + supportSkill2RightX * px, originY + support2SkillBottomY * py)
        ));
        ret.setSupport2Skill3(new Rec(
            new Point(originX + supportSkill3LeftX * px, originY + support2SkillTopY * py),
            new Point(originX + supportSkill3RightX * px, originY + support2SkillBottomY * py)
        ));
        ret.setChooseSupport1(new Point(originX + chooseSupportX * px, originY + chooseSupport1Y * py));
        ret.setChooseSupport2(new Point(originX + chooseSupportX * px, originY + chooseSupport2Y * py));
        ret.setConfirmRefreshingSupport(new Point(originX + confirmRefreshingSupportX * px, originY + confirmRefreshingSupportY * py));

        ret.setLastGame(new Point(originX + lastGameX * px, originY + lastGameY * py));
        ret.setGoldenAppleIcon(new Rec(
            new Point(originX + appleIconLeftX * px, originY + goldenAppleIconTopY * py),
            new Point(originX + appleIconRightX * px, originY + goldenAppleIconBottomY * py)
        ));
        ret.setSilverAppleIcon(new Rec(
            new Point(originX + appleIconLeftX * px, originY + silverAppleIconTopY * py),
            new Point(originX + appleIconRightX * px, originY + silverAppleIconBottomY * py)
        ));
        ret.setCancelApplePoint(new Point(originX + cancelAppleX * px, originY + cancelAppleY * py));

        ret.setStart(new Point(originX + startX * px, originY + startY * py));

        return ret;
    }
}
