// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.statem;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.Point;
import net.cassite.desktop.chara.widget.fgoclick.robot.ExecutableAction;
import net.cassite.desktop.chara.widget.fgoclick.robot.RunningContext;
import net.cassite.desktop.chara.widget.fgoclick.fgo.Skill;

public class ClickSkill implements ExecutableAction {
    private final int servant;
    private final int skill;
    private final String description;

    public ClickSkill(Skill skill) {
        this.servant = skill.getServant().getIndex();
        this.skill = skill.getIndex();
        this.description = "[" + skill.getServant().getName() + "].[" + skill.getName() + "]";
    }

    @Override
    public void execute(RunningContext ctx) {
        Point p;
        if (servant == 1) {
            if (skill == 1) {
                p = ctx.anchor.getSkill11();
            } else if (skill == 2) {
                p = ctx.anchor.getSkill12();
            } else if (skill == 3) {
                p = ctx.anchor.getSkill13();
            } else throw new IllegalArgumentException("unknown skill: " + skill);
        } else if (servant == 2) {
            if (skill == 1) {
                p = ctx.anchor.getSkill21();
            } else if (skill == 2) {
                p = ctx.anchor.getSkill22();
            } else if (skill == 3) {
                p = ctx.anchor.getSkill23();
            } else throw new IllegalArgumentException("unknown skill: " + skill);
        } else if (servant == 3) {
            if (skill == 1) {
                p = ctx.anchor.getSkill31();
            } else if (skill == 2) {
                p = ctx.anchor.getSkill32();
            } else if (skill == 3) {
                p = ctx.anchor.getSkill33();
            } else throw new IllegalArgumentException("unknown skill: " + skill);
        } else throw new IllegalArgumentException("unknown servant: " + servant);

        Logger.info(toString());
        ctx.clickAt(p, ctx.sleepAndDone(0.5));
    }

    private static final Words str = new WordsBuilder
        ("点击从者{1}的技能{2}")
        .setEn("click skill {2} of servant {1}")
        .build();

    @Override
    public String toString() {
        return str.get()[0].replace("{1}", "" + servant).replace("{2}", "" + skill) + " ::: " + description;
    }
}
