// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.i18n;

public class WordsBuilder {
    private final String[] zh;
    private String[] en;

    private String[] merge(String a, String... more) {
        String[] arr = new String[more.length + 1];
        arr[0] = a;
        System.arraycopy(more, 0, arr, 1, more.length);
        return arr;
    }

    public WordsBuilder(String zh, String... more) {
        this.zh = merge(zh, more);
    }

    public WordsBuilder setEn(String en, String... more) {
        this.en = merge(en, more);
        return this;
    }

    public Words build() {
        return new Words(zh, en);
    }
}
