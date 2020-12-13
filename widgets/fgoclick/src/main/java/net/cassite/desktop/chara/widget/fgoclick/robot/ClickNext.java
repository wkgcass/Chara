// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.robot;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;

public class ClickNext implements ExecutableAction {
    @Override
    public void execute(RunningContext ctx) {
        Logger.info(toString());
        // save screen capture for trophy
        ctx.saveScreenCapture("trophy");
        // click next
        ctx.keepRunning(3, 0.5,
            cb -> ctx.clickAt(ctx.anchor.getNext(), cb),
            ctx::done);
    }

    private static final Words str = new WordsBuilder
        ("点击'下一步'")
        .setEn("click 'next'")
        .build();

    @Override
    public String toString() {
        return str.get()[0];
    }
}
