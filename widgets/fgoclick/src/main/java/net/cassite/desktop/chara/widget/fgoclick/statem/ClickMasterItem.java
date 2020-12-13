// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.statem;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.robot.ExecutableAction;
import net.cassite.desktop.chara.widget.fgoclick.robot.RunningContext;

public class ClickMasterItem implements ExecutableAction {
    @Override
    public void execute(RunningContext ctx) {
        Logger.info(toString());
        ctx.clickAt(ctx.anchor.getMasterItem(), ctx.sleepAndDone(0.5));
    }

    private static final Words str = new WordsBuilder
        ("点击御主技能按钮")
        .setEn("click master skill button")
        .build();

    @Override
    public String toString() {
        return str.get()[0];
    }
}
