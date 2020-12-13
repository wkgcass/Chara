// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.statem;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.robot.ExecutableAction;
import net.cassite.desktop.chara.widget.fgoclick.robot.RunningContext;
import net.cassite.desktop.chara.widget.fgoclick.fgo.NoblePhantasm;

public class WaitForNoblePhantasm implements ExecutableAction {
    private static final int DEFAULT_NP_TIME = 40;
    private final int time;
    private final String description;

    public WaitForNoblePhantasm(NoblePhantasm np) {
        this.time = np.getAction().getTime() == 0 ? DEFAULT_NP_TIME : np.getAction().getTime();
        this.description = "[" + np.getServant().getName() + "].[" + np.getName() + "]";
    }

    @Override
    public void execute(RunningContext ctx) {
        Logger.info(toString());
        ctx.sleep(time * 1.1 + 5, ctx::done);
    }

    private static final Words str = new WordsBuilder
        ("等待宝具")
        .setEn("wait for noble phantasm")
        .build();

    @Override
    public String toString() {
        return str.get()[0] + " " + time + "s ::: " + description;
    }
}
