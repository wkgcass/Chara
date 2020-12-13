// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.statem;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.Point;
import net.cassite.desktop.chara.widget.fgoclick.robot.ExecutableAction;
import net.cassite.desktop.chara.widget.fgoclick.robot.RunningContext;

public class SelectItem implements ExecutableAction {
    private final int total;
    private final int select;

    public SelectItem(int total, int select) {
        this.total = total;
        this.select = select;
    }

    @Override
    public void execute(RunningContext ctx) {
        Point p;
        if (total == 3) {
            if (select == 1) {
                p = ctx.anchor.getSelect1Of3();
            } else if (select == 2) {
                p = ctx.anchor.getSelect2Of3();
            } else if (select == 3) {
                p = ctx.anchor.getSelect3Of3();
            } else throw new IllegalArgumentException("unexpected selection select: " + select + " of total: " + total);
        } else throw new UnsupportedOperationException("unsupported selection total: " + total);

        Logger.info(toString());
        ctx.clickAt(p, ctx.sleepAndDone(0.5));
    }

    private static final Words str = new WordsBuilder
        ("点击对象")
        .setEn("select item")
        .build();

    @Override
    public String toString() {
        return str.get()[0] + " " + select + "/" + total;
    }
}
