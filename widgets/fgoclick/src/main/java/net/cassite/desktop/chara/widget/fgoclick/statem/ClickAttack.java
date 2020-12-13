// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.statem;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.robot.ExecutableAction;
import net.cassite.desktop.chara.widget.fgoclick.robot.RunningContext;

public class ClickAttack implements ExecutableAction {
    @Override
    public void execute(RunningContext ctx) {
        Logger.info(toString());
        execute(ctx, ctx::done);
    }

    void execute(RunningContext ctx, Runnable cb) {
        ctx.clickAt(ctx.anchor.getAttack(), () -> ctx.sleep(3, cb));
    }

    private static final Words str = new WordsBuilder
        ("点击Attack按钮")
        .setEn("click 'Attack' button")
        .build();

    @Override
    public String toString() {
        return str.get()[0];
    }
}
