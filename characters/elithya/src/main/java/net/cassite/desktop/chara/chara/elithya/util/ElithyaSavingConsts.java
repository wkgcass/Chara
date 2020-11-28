// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.util;

import net.cassite.desktop.chara.util.Key;

public class ElithyaSavingConsts {
    private ElithyaSavingConsts() {
    }

    public static final Key<Boolean> hideHat = Key.of("hide_hat", Boolean.class);
    public static final Key<Boolean> hideCloak = Key.of("hide_cloak", Boolean.class);
    public static final Key<Boolean> hideMoon = Key.of("hide_moon", Boolean.class);
    public static final Key<Integer> stockingsColor = Key.of("stockings_color", Integer.class); // 0|1:purple,2:black,3:none
    public static final Key<Integer> shoesColor = Key.of("shoes_color", Integer.class); // 0|1:purple,2:black
    public static final Key<Boolean> showWandLight = Key.of("show_wand_light", Boolean.class);
}
