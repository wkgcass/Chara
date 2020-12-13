// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.robot;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;

public class ClickDoNotUseApple implements ExecutableAction {
    @Override
    public void execute(RunningContext ctx) {
        Logger.info(toString());
        ctx.clickAt(ctx.anchor.getNoDirectNext(), ctx.sleepAndDone(10));
    }

    private static final Words str = new WordsBuilder
        ("不使用苹果")
        .setEn("do not use apple")
        .build();

    @Override
    public String toString() {
        return str.get()[0];
    }
}
