// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.statem;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.fgo.NoblePhantasm;
import net.cassite.desktop.chara.widget.fgoclick.robot.ExecutableAction;
import net.cassite.desktop.chara.widget.fgoclick.robot.RunningContext;

public class ClickNoblePhantasm implements ExecutableAction {
    private final int servant;
    private final String description;

    public ClickNoblePhantasm(NoblePhantasm np) {
        this.servant = np.getServant().getIndex();
        this.description = "[" + np.getServant().getName() + "].[" + np.getName() + "]";
    }

    @Override
    public void execute(RunningContext ctx) {
        Logger.info(toString());
        ctx.clickNoblePhantasm(servant, ctx.sleepAndDone(0.5));
    }

    private static final Words str = new WordsBuilder
        ("点击从者{1}的宝具")
        .setEn("click noble phantasm of servant {1}")
        .build();

    @Override
    public String toString() {
        return str.get()[0].replace("{1}", "" + servant) + " ::: " + description;
    }
}
