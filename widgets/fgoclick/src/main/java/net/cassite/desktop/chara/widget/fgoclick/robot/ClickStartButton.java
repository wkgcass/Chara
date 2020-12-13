// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.robot;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;

public class ClickStartButton implements ExecutableAction {
    @Override
    public void execute(RunningContext ctx) {
        Logger.info(toString());
        // wait for servants to show, this may take a lot of time
        ctx.sleep(7, () ->
            ctx.clickAt(ctx.anchor.getStart(), ctx::done));
    }

    private static final Words str = new WordsBuilder
        ("点击开始按钮")
        .setEn("Click start button")
        .build();

    @Override
    public String toString() {
        return str.get()[0];
    }
}
