// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.util;

import net.cassite.desktop.chara.util.Key;

public class Consts {
    private Consts() {
    }

    public static final Key<Integer> LOVE_POTION_COUNT = Key.of("love_potion_count", Integer.class);
    public static final Key<Boolean> LOVE_POTION_USED = Key.of("love_potion_used", Boolean.class);
    public static final Key<Integer> SEX_COUNT = Key.of("sex_count", Integer.class);
    public static final Key<Integer> SEX_TOTAL_TIME = Key.of("sex_total_time", Integer.class);
    public static final Key<Integer> ORGASM_COUNT = Key.of("orgasm_count", Integer.class);
    public static final Key<Integer> BAD_SEX_COUNT = Key.of("bad_sex_count", Integer.class);
    public static final Key<Integer> NORMAL_SEX_COUNT = Key.of("normal_sex_count", Integer.class);

    public static final int MAX_LOVE_POTION_COUNT = 10;
}
