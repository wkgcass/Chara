// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.statem;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.fgo.OrderCardType;
import net.cassite.desktop.chara.widget.fgoclick.robot.ExecutableAction;
import net.cassite.desktop.chara.widget.fgoclick.robot.RunningContext;

public class ClickAnyCard implements ExecutableAction {
    public ClickAnyCard() {
    }

    @Override
    public void execute(RunningContext ctx) {
        execute(ctx, ctx::done);
    }

    public void execute(RunningContext ctx, Runnable cb) {
        int countBusterCards = 0;
        int countArtsCards = 0;
        for (int i = 1; i <= 5; ++i) {
            if (!ctx.orderCardSelected(i)) {
                if (ctx.getTypeOfOrderCard(i) == OrderCardType.buster) {
                    ++countBusterCards;
                } else if (ctx.getTypeOfOrderCard(i) == OrderCardType.arts) {
                    ++countArtsCards;
                }
            }
        }
        Logger.info(toString());
        Runnable clickOrderCardCallback = () -> ctx.sleep(0.5, cb);
        if (ctx.selectedOrderCardsCount() == 0) {
            if (countBusterCards >= 3) {
                selectBusterCard(ctx, clickOrderCardCallback);
            } else if (countBusterCards == 2) {
                if (countArtsCards >= 1) {
                    selectArtsCard(ctx, clickOrderCardCallback);
                } else {
                    selectQuickCard(ctx, clickOrderCardCallback);
                }
            } else if (countBusterCards == 1) {
                if (countArtsCards >= 2) {
                    selectArtsCard(ctx, clickOrderCardCallback);
                } else {
                    selectQuickCard(ctx, clickOrderCardCallback);
                }
            } else {
                if (countArtsCards >= 3) {
                    selectArtsCard(ctx, clickOrderCardCallback);
                } else {
                    selectQuickCard(ctx, clickOrderCardCallback);
                }
            }
        } else if (ctx.selectedOrderCardsCount() == 1) {
            if (countBusterCards >= 2) {
                selectBusterCard(ctx, clickOrderCardCallback);
            } else if (countBusterCards == 1) {
                if (countArtsCards >= 1) {
                    selectArtsCard(ctx, clickOrderCardCallback);
                } else {
                    selectQuickCard(ctx, clickOrderCardCallback);
                }
            } else {
                if (countArtsCards >= 2) {
                    selectArtsCard(ctx, clickOrderCardCallback);
                } else {
                    selectQuickCard(ctx, clickOrderCardCallback);
                }
            }
        } else {
            if (countBusterCards >= 1) {
                selectBusterCard(ctx, clickOrderCardCallback);
            } else if (countArtsCards >= 1) {
                selectArtsCard(ctx, clickOrderCardCallback);
            } else {
                selectQuickCard(ctx, clickOrderCardCallback);
            }
        }
    }

    private void selectBusterCard(RunningContext ctx, Runnable cb) {
        for (int i = 1; i <= 5; ++i) {
            if (!ctx.orderCardSelected(i)) {
                if (ctx.getTypeOfOrderCard(i) == OrderCardType.buster) {
                    Logger.info("clicking buster card " + i);
                    ctx.clickOrderCard(i, cb);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("cannot find buster card to click");
    }

    private void selectArtsCard(RunningContext ctx, Runnable cb) {
        for (int i = 1; i <= 5; ++i) {
            if (!ctx.orderCardSelected(i)) {
                if (ctx.getTypeOfOrderCard(i) == OrderCardType.arts) {
                    Logger.info("clicking arts card " + i);
                    ctx.clickOrderCard(i, cb);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("cannot find arts card to click");
    }

    private void selectQuickCard(RunningContext ctx, Runnable cb) {
        for (int i = 1; i <= 5; ++i) {
            if (!ctx.orderCardSelected(i)) {
                if (ctx.getTypeOfOrderCard(i) == OrderCardType.quick) {
                    Logger.info("clicking quick card " + i);
                    ctx.clickOrderCard(i, cb);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("cannot find quick card to click");
    }

    private static final Words str = new WordsBuilder
        ("点击任意一张指令卡")
        .setEn("click any order card")
        .build();

    @Override
    public String toString() {
        return str.get()[0];
    }
}
