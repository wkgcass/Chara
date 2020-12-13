// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.robot;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickUtils;

public class ClickLastGameAndCancelApple implements ExecutableAction {
    @Override
    public void execute(RunningContext ctx) {
        Logger.info(toString());
        if (ctx.isStopped()) {
            Logger.warn("should break the loop because it's already stopped");
            ctx.done(true);
            return;
        }
        if (ctx.isSkipping()) {
            Logger.warn("should continue because it's skipping");
            ctx.done();
            return;
        }
        ctx.clickAt(ctx.anchor.getLastGame(), () -> ctx.sleep(0.5, () -> {
            var golden = FgoClickUtils.calcColor(ctx.getScreenCapture(ctx.anchor.getGoldenAppleIcon()),
                ctx.anchor.getGoldenAppleIcon().topLeft, ctx.anchor.getGoldenAppleIcon());
            var silver = FgoClickUtils.calcColor(ctx.getScreenCapture(ctx.anchor.getSilverAppleIcon()),
                ctx.anchor.getSilverAppleIcon().topLeft, ctx.anchor.getSilverAppleIcon());
            if (FgoClickUtils.sameColorWithLog(golden, FgoClickConsts.goldenAppleIcon, ctx.consts)
                && FgoClickUtils.sameColorWithLog(silver, FgoClickConsts.silverAppleIcon, ctx.consts)) {
                Logger.warn("ap still not enough, wait for 30s and try again");
                ctx.clickAt(ctx.anchor.getCancelApplePoint(), () -> ctx.sleep(30, () -> execute(ctx)));
            } else {
                Logger.info("ap is enough now");
                ctx.sleep(0.5, ctx::done);
            }
        }));
    }

    private static final Words str = new WordsBuilder
        ("点击'上次执行'直到ap足够")
        .setEn("Click last game until enough ap")
        .build();

    @Override
    public String toString() {
        return str.get()[0];
    }
}
