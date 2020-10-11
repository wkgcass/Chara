// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.i18n;

import net.cassite.desktop.chara.util.Logger;

import java.util.Locale;
import java.util.Map;

public class Words extends WordsSelector {
    private static final Locale locale = Locale.getDefault();

    private final String[] zh;
    private final String[] en;

    public Words(String[] zh, String[] en) {
        this.en = en;
        this.zh = zh;
    }

    public static Words fromMap(Map<String, String[]> map) {
        var zh = map.get("ZH");
        var en = map.get("EN");
        if (zh == null) {
            zh = en;
            en = null;
        }
        if (zh == null) {
            return null;
        }
        return new Words(zh, en);
    }

    public String[] get() {
        assert Logger.debug("current locale = " + locale.getLanguage());
        String lang = locale.getLanguage();

        //noinspection SwitchStatementWithTooFewBranches
        switch (lang) {
            case "zh":
                return zh;
            default:
                if (en == null) {
                    return zh;
                }
                return en;
        }
    }

    @Override
    public Words select() {
        return this;
    }

    @Override
    public int count() {
        return 1;
    }
}
