// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.control;

import javafx.scene.control.CheckMenuItem;
import net.cassite.desktop.chara.graphic.Alert;
import net.cassite.desktop.chara.i18n.I18nConsts;
import net.cassite.desktop.chara.i18n.Words;

public class LocaleCheckMenuItem extends CheckMenuItem {
    public final String description;
    public final String language;
    public final String region;

    public LocaleCheckMenuItem(String description, String language, String region) {
        this.description = description;
        this.language = language;
        this.region = region;
        setText(description + "/" + language + "-" + region);
        check();
    }

    public void check() {
        setSelected(Words.getLocale().equals(language + "-" + region));
    }

    public void set() {
        Words.setLocale(language + "-" + region);
        check();
        Alert.alert(I18nConsts.someComponentsResetAfterReboot.get()[0]);
    }
}
