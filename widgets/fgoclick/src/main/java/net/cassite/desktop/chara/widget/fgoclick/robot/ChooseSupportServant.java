// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.robot;

import javafx.scene.paint.Color;
import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickI18nConsts;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickUtils;

public class ChooseSupportServant implements ExecutableAction {
    @Override
    public void execute(RunningContext ctx) {
        Logger.info(toString());
        loopUntilSelected(ctx);
    }

    private void loopUntilSelected(RunningContext ctx) {
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

        // wait for possible slow network
        ctx.sleep(4, () -> {
            Logger.info("clicking caster");
            ctx.clickAt(ctx.anchor.getCasterSupport(), () -> ctx.sleep(1, () -> {
                Logger.info("checking servants");
                Color[] colors = ctx.getSupportSkillColors();

                String support = ctx.settings.getSupport();
                if (support.isBlank()) {
                    chooseAnySupport(ctx, colors);
                } else if (support.equals(FgoClickI18nConsts.ScathachSkathi.get()[0])) {
                    chooseScathachSkathi(ctx, colors);
                } else if (support.equals(FgoClickI18nConsts.ZhugeKongming.get()[0])) {
                    chooseZhugeKongming(ctx, colors);
                } else throw new IllegalArgumentException("unknown support servant: " + support);
            }));
        });
    }

    private void chooseAnySupport(RunningContext ctx, Color[] colors) {
        Logger.info("choosing any support");

        if (!FgoClickUtils.sameColorWithLog(colors[0], FgoClickConsts.NoSkill1, ctx.consts)
            || !FgoClickUtils.sameColorWithLog(colors[1], FgoClickConsts.NoSkill2, ctx.consts)
            || !FgoClickUtils.sameColorWithLog(colors[2], FgoClickConsts.NoSkill3, ctx.consts)) {
            Logger.info("first servant is not empty");
            ctx.clickAt(ctx.anchor.getChooseSupport1(), ctx::done);
        } else if (!FgoClickUtils.sameColorWithLog(colors[3], FgoClickConsts.NoSkill1, ctx.consts)
            || !FgoClickUtils.sameColorWithLog(colors[4], FgoClickConsts.NoSkill2, ctx.consts)
            || !FgoClickUtils.sameColorWithLog(colors[5], FgoClickConsts.NoSkill3, ctx.consts)) {
            Logger.info("second servant is not empty");
            ctx.clickAt(ctx.anchor.getChooseSupport2(), ctx::done);
        } else {
            Logger.info("both first and second servants are empty");
            refresh(ctx);
        }
    }

    private void choose(RunningContext ctx, Color skill1, Color skill2, Color skill3, Color[] colors) {
        if (FgoClickUtils.sameColorWithLog(colors[0], skill1, ctx.consts)
            && FgoClickUtils.sameColorWithLog(colors[1], skill2, ctx.consts)
            && FgoClickUtils.sameColorWithLog(colors[2], skill3, ctx.consts)) {
            Logger.info("first servant selected");
            ctx.clickAt(ctx.anchor.getChooseSupport1(), ctx::done);
        } else if (FgoClickUtils.sameColorWithLog(colors[3], skill1, ctx.consts)
            && FgoClickUtils.sameColorWithLog(colors[4], skill2, ctx.consts)
            && FgoClickUtils.sameColorWithLog(colors[5], skill3, ctx.consts)) {
            Logger.info("second servant selected");
            ctx.clickAt(ctx.anchor.getChooseSupport2(), ctx::done);
        } else {
            Logger.info("both first and second servants are not expected");
            refresh(ctx);
        }
    }

    private void chooseScathachSkathi(RunningContext ctx, Color[] colors) {
        Logger.info("choosing ScathachSkathi");
        choose(ctx, FgoClickConsts.ScathachSkathiSkill1, FgoClickConsts.ScathachSkathiSkill2, FgoClickConsts.ScathachSkathiSkill3, colors);
    }

    private void chooseZhugeKongming(RunningContext ctx, Color[] colors) {
        Logger.info("choosing ZhugeKongming");
        choose(ctx, FgoClickConsts.ZhugeKongmingSkill1, FgoClickConsts.ZhugeKongmingSkill2, FgoClickConsts.ZhugeKongmingSkill3, colors);
    }

    private void refresh(RunningContext ctx) {
        Logger.info("refreshing support list");
        ctx.clickAt(ctx.anchor.getRefreshList(), () -> ctx.sleep(0.5, () ->
            ctx.clickAt(ctx.anchor.getConfirmRefreshingSupport(), () -> ctx.sleep(4, () -> {
                Logger.info("try to select again");
                loopUntilSelected(ctx);
            }))));
    }

    private static final Words str = new WordsBuilder
        ("选择助战从者")
        .setEn("choose support servant")
        .build();

    @Override
    public String toString() {
        return str.get()[0];
    }
}
