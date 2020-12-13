// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.robot;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;

public class DummyClick implements ExecutableAction {
    @Override
    public void execute(RunningContext ctx) {
        Logger.info(toString());
        ctx.keepRunning(5, 0.3,
            cb -> ctx.clickAt(ctx.anchor.getDummyClick(), cb),
            ctx::done);
    }

    private static final Words str = new WordsBuilder
        ("随意点击")
        .setEn("dummy click")
        .build();

    @Override
    public String toString() {
        return str.get()[0];
    }
}
