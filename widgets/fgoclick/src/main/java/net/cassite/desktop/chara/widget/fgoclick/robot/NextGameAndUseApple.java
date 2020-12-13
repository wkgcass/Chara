// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.robot;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.Point;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickUtils;

public class NextGameAndUseApple implements ExecutableAction {
    @Override
    public void execute(RunningContext ctx) {
        Logger.info(toString());

        if (!ctx.settings.isLoop()) {
            Logger.warn("script file says do not loop, break the scripting loop");
            ctx.done(true);
            return;
        }

        Logger.info("check ap");
        Image image = ctx.getScreenCapture(ctx.anchor.getApCost());
        var reader = image.getPixelReader();
        int countRed = 0;
        int pointTotal = 0;
        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                ++pointTotal;
                Color c = reader.getColor(x, y);
                if (FgoClickUtils.sameColor(c, FgoClickConsts.lowAP, ctx.consts)) {
                    ++countRed;
                }
            }
        }
        double redRatio = countRed / (double) pointTotal;
        Logger.info("red ratio: " + redRatio);
        boolean isLowAP = redRatio > 0.1; // test results: 0.1910331384 or 0.0

        final boolean finalIsLowAP = isLowAP;
        if (isLowAP) {
            Logger.warn("AP is NOT ok, need to use apple");
        } else {
            Logger.info("AP is ok");
        }
        Logger.info("clicking to run next game");
        ctx.clickAt(ctx.anchor.getDirectNextGame(), () -> ctx.sleep(0.5, () -> {
            if (!finalIsLowAP) {
                ctx.sleep(0.5, ctx::done);
                return;
            }
            String useApple = ctx.settings.getUseApple();
            if (useApple == null || useApple.equals("no")) {
                throw new IllegalArgumentException("useApple cannot be null nor no for NextGameAndUseApple action");
            }
            switch (useApple) {
                case "stone":
                    Logger.warn("using stone to restore AP");
                    ctx.clickAt(ctx.anchor.getApStone(), () -> afterClickingApple(ctx));
                    break;
                case "golden":
                    Logger.warn("using golden apple");
                    ctx.clickAt(ctx.anchor.getAppleGolden(), () -> afterClickingApple(ctx));
                    break;
                case "silver":
                    Logger.warn("using silver apple");
                    ctx.clickAt(ctx.anchor.getAppleSilver(), () -> afterClickingApple(ctx));
                    break;
                case "copper":
                    Logger.warn("using copper apple");
                    ctx.drag(ctx.anchor.getAppleSilver(), new Point(ctx.anchor.getAppleSilver().x, ctx.anchor.getOrigin().y),
                        () -> ctx.sleep(0.5,
                            () -> ctx.clickAt(ctx.anchor.getAppleCopper(), () -> afterClickingApple(ctx))
                        ));
                    break;
                default:
                    throw new IllegalArgumentException("unknown apple type: " + useApple);
            }
        }));
    }

    private void afterClickingApple(RunningContext ctx) {
        ctx.sleep(0.5, () -> {
            ctx.saveScreenCapture("ap");
            Logger.info("click confirm to use apple");
            ctx.clickAt(ctx.anchor.getConfirmToUseApple(), ctx.sleepAndDone(0.5));
        });
    }

    private static final Words str = new WordsBuilder
        ("继续游戏并使用苹果")
        .setEn("next game and use apple")
        .build();

    @Override
    public String toString() {
        return str.get()[0];
    }
}
