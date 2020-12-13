// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.statem;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.robot.ExecutableAction;
import net.cassite.desktop.chara.widget.fgoclick.robot.RunningContext;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickUtils;

public class WaitForNextBattle implements ExecutableAction {
    private final int totalBattle;
    private final int battle;

    public WaitForNextBattle(int totalBattle, int battle) {
        this.totalBattle = totalBattle;
        this.battle = battle;
    }

    @Override
    public void execute(RunningContext ctx) {
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
        Logger.info(toString());
        if (battle == totalBattle) {
            ctx.sleep(32, () -> {
                var img = ctx.getScreenCapture(ctx.anchor.getAttack());
                var color = FgoClickUtils.calcColor(img, ctx.anchor.getAttack().topLeft, ctx.anchor.getAttack());
                if (FgoClickUtils.sameColorWithLog(color, FgoClickConsts.attackButton, ctx.consts)) {
                    Logger.warn("need more attacks");
                    moreAttacks(ctx);
                } else {
                    Logger.info("battle finishes");
                    ctx.done();
                }
            });
        } else {
            ctx.sleep(28, ctx::done);
        }
    }

    private final ClickAttack clickAttack = new ClickAttack();
    private final ClickAnyCard clickAnyCard = new ClickAnyCard();

    private void moreAttacks(RunningContext ctx) {
        Logger.info("need to click 'attack' button");
        clickAttack.execute(ctx, () -> {
            Logger.info("need to select an order card (1)");
            clickAnyCard.execute(ctx, () -> {
                Logger.info("need to select an order card (2)");
                clickAnyCard.execute(ctx, () -> {
                    Logger.info("need to select an order card (3)");
                    clickAnyCard.execute(ctx, () -> {
                        Logger.info("wait for battle to finish again");
                        execute(ctx);
                    });
                });
            });
        });
    }

    private static final Words str1 = new WordsBuilder
        ("等待下一面")
        .setEn("wait for next battle")
        .build();
    private static final Words str2 = new WordsBuilder
        ("等待游戏结束")
        .setEn("wait for battle to finish")
        .build();

    @Override
    public String toString() {
        if (battle == totalBattle) {
            return str2.get()[0];
        } else {
            return str1.get()[0] + " " + (battle + 1) + "/" + totalBattle;
        }
    }
}
