// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.statem;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.robot.ExecutableAction;
import net.cassite.desktop.chara.widget.fgoclick.robot.RunningContext;
import net.cassite.desktop.chara.widget.fgoclick.fgo.Skill;

public class WaitForSkill implements ExecutableAction {
    private static final int DEFAULT_SKILL_TIME = 4;
    private final int time;
    private final String description;

    public WaitForSkill(Skill skill) {
        this.time = skill.getAction().getTime() == 0 ? DEFAULT_SKILL_TIME : skill.getAction().getTime();
        this.description = "[" + skill.getServant().getName() + "].[" + skill.getName() + "]";
    }

    @Override
    public void execute(RunningContext ctx) {
        Logger.info(toString());
        ctx.sleep(time * 1.1, ctx::done);
    }

    private static final Words str = new WordsBuilder
        ("等待技能")
        .setEn("wait for skill")
        .build();

    @Override
    public String toString() {
        return str.get()[0] + " " + time + "s ::: " + description;
    }
}
