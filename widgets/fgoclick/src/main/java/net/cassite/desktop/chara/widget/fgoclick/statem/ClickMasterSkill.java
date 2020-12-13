// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.statem;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.Point;
import net.cassite.desktop.chara.widget.fgoclick.robot.ExecutableAction;
import net.cassite.desktop.chara.widget.fgoclick.robot.RunningContext;
import net.cassite.desktop.chara.widget.fgoclick.fgo.MasterSkill;

public class ClickMasterSkill implements ExecutableAction {
    private final int skill;
    private final String description;

    public ClickMasterSkill(MasterSkill skill) {
        this.skill = skill.getIndex();
        this.description = "[" + skill.getName() + "]";
    }

    @Override
    public void execute(RunningContext ctx) {
        Point p;
        if (skill == 1) {
            p = ctx.anchor.getMasterSkill1();
        } else if (skill == 2) {
            p = ctx.anchor.getMasterSkill2();
        } else if (skill == 3) {
            p = ctx.anchor.getMasterSkill3();
        } else throw new IllegalArgumentException("unknown master skill: " + skill);

        Logger.info(toString());
        ctx.clickAt(p, ctx.sleepAndDone(0.5));
    }

    private static final Words str = new WordsBuilder
        ("点击御主技能")
        .setEn("click master skill")
        .build();

    @Override
    public String toString() {
        return str.get()[0] + " " + skill + " ::: " + description;
    }
}
