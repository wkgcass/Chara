// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.i18n;

public class WordsBuilder {
    private final String[] zh;
    private String[] en;

    public WordsBuilder(String... zh) {
        this.zh = zh;
    }

    public WordsBuilder setEn(String... en) {
        this.en = en;
        return this;
    }

    public Words build() {
        return new Words(zh, en);
    }
}
