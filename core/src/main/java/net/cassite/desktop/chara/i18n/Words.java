// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.i18n;

import net.cassite.desktop.chara.util.Consts;
import net.cassite.desktop.chara.util.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Map;

/**
 * An object containing words in multiple languages.
 */
public class Words extends WordsSelector {
    private static Locale locale = null;

    private static Locale locale() {
        if (locale == null) {
            File f = new File(System.getProperty("user.home") + "/" + Consts.CONFIG_BASE_DIR + "/" + Consts.LOCALE_FILE_NAME);
            if (f.isFile()) {
                try {
                    String s = Files.readString(f.toPath());
                    setLocale(s);
                } catch (IOException e) {
                    Logger.error("failed reading locale from " + f.getAbsolutePath());
                }
            } else {
                assert Logger.debug(f.getAbsolutePath() + " not exists");
            }
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    public static void setLocale(String text) {
        if (text != null) {
            text = text.trim();
        }
        File localeFile = new File(System.getProperty("user.home") + "/" + Consts.CONFIG_BASE_DIR + "/" + Consts.LOCALE_FILE_NAME);

        if (text == null) {
            locale = Locale.getDefault();
            if (localeFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                localeFile.delete();
            }
            assert Logger.debug("remove locale");
            return;
        }

        String[] langcountry;
        try {
            langcountry = text.split("-");
            locale = new Locale(langcountry[0], langcountry[1]);
        } catch (RuntimeException e) {
            Logger.error("failed setting locale by text " + text, e);
            return;
        }
        try {
            Files.write(localeFile.toPath(), text.getBytes());
            assert Logger.debug("locale set to " + text);
        } catch (IOException e) {
            Logger.error("failed persisting locale info " + text + " from " + localeFile.getAbsolutePath(), e);
        }
    }

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
        return locale().getLanguage() + "-" + locale().getCountry();
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
