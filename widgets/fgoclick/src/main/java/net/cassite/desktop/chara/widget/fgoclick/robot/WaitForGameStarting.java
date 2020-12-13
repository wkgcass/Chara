// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.robot;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;

public class WaitForGameStarting implements ExecutableAction {
    @Override
    public void execute(RunningContext ctx) {
        Logger.info(toString());
        ctx.sleep(15, ctx::done);
    }

    private static final Words str = new WordsBuilder
        ("等待游戏开始")
        .setEn("wait for game to start")
        .build();

    @Override
    public String toString() {
        return str.get()[0];
    }
}
