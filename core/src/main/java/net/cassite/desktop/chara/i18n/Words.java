// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.i18n;

import net.cassite.desktop.chara.util.Logger;

import java.util.Locale;
import java.util.Map;

/**
 * An object containing words in multiple languages.
 */
public class Words extends WordsSelector {
    private static final Locale locale = Locale.getDefault();

    private final String[] sc;
    private final String[] en;

    /**
     * Construct the object with different languages
     *
     * @param sc chinese, not-null, may be used as the default language when all other languages are not set
     * @param en english, nullable
     */
    public Words(String[] sc, String[] en) {
        this.en = en;
        this.sc = sc;
    }

    /**
     * Construct the object from a map
     *
     * @param map a map in the form of <pre>{"CS" -$gt; [...], "EN" -$gt; [...]}</pre>
     * @return a constructed <code>Words</code> object
     */
    public static Words fromMap(Map<String, String[]> map) {
        var cs = map.get("CS");
        var en = map.get("EN");
        if (cs == null) {
            cs = en;
            en = null;
        }
        if (cs == null) {
            return null;
        }
        return new Words(cs, en);
    }

    /**
     * Retrieve the most corresponding language version of words
     *
     * @return the retrieved string array
     */
    public String[] get() {
        String locale = getLocale();
        assert Logger.debug("current locale = " + locale);

        //noinspection SwitchStatementWithTooFewBranches
        switch (locale) {
            case "zh-CN":
                return sc;
            default:
                if (en == null) {
                    return sc;
                }
                return en;
        }
    }

    /**
     * Get language-region, e.g. zh-CN, en-US, etc.
     *
     * @return {@link Locale#getLanguage()}-{@link Locale#getCountry()}
     */
    public static String getLocale() {
        return locale.getLanguage() + "-" + locale.getCountry();
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
