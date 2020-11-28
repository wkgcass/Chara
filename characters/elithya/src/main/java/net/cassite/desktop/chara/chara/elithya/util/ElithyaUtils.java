// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.util;

import net.cassite.desktop.chara.graphic.Anima;
import net.cassite.desktop.chara.graphic.Div;
import net.cassite.desktop.chara.util.Utils;

public class ElithyaUtils {
    private ElithyaUtils() {
    }

    public static String[] joinSeqs(String[]... seqs) {
        int len = 0;
        for (String[] strings : seqs) {
            len += strings.length;
        }
        String[] ret = new String[len];
        int retIdx = 0;
        for (String[] seq : seqs) {
            for (String s : seq) {
                ret[retIdx++] = s;
            }
        }
        return ret;
    }

    public static Runnable[] synchronize(int count, Runnable cb) {
        int[] cnt = new int[]{0};
        Runnable[] ret = new Runnable[count];
        for (int i = 0; i < count; ++i) {
            ret[i] = () -> {
                ++cnt[0];
                if (cnt[0] == count) {
                    cb.run();
                }
            };
        }
        return ret;
    }

    public static void smoothAnimation(Div root, Anima begin, Anima slow, Anima finish, Runnable cb) {
        begin.addTo(root);
        begin.setPauseCallbackOnce(() -> {
            begin.removeFrom(root);
            begin.resetTo(-1);
            slow.setPauseCallbackOnce(() -> {
                slow.removeFrom(root);
                slow.resetTo(-1);
                finish.setPauseCallbackOnce(() -> {
                    finish.removeFrom(root);
                    finish.resetTo(-1);
                    begin.addTo(root);
                    cb.run();
                }).play();
                finish.addTo(root);
            }).play();
            slow.addTo(root);
        }).play();
    }

    private static String n2s(int n) {
        if (n < 10) {
            return "00" + n;
        } else if (n < 100) {
            return "0" + n;
        } else {
            return "" + n;
        }
    }

    public static LoopingAnima buildLoopingAnima(String prefix, int step, String suffix) {
        return new LoopingAnima(
            new Anima(prefix + "000" + suffix,
                Utils.buildSeqNames(prefix, 0, step * 3, suffix)),
            new Anima(prefix + n2s(4 * step) + suffix,
                joinSeqs(
                    Utils.buildSeqNames(prefix, 4 * step, 6 * step, suffix),
                    Utils.buildSeqNames(prefix, step, 3 * step, suffix)
                )),
            new Anima(prefix + n2s(3 * step) + suffix,
                Utils.buildSeqNames(prefix, 3 * step, 4 * step, suffix))
        );
    }
}
