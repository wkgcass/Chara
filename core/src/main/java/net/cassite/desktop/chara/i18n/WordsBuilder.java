// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.i18n;

/**
 * A builder for constructing {@link Words} object.
 */
public class WordsBuilder {
    private final String[] cs;
    private String[] en;

    private String[] merge(String a, String... more) {
        String[] arr = new String[more.length + 1];
        arr[0] = a;
        System.arraycopy(more, 0, arr, 1, more.length);
        return arr;
    }

    /**
     * Construct the builder with chinese language words.<br>
     * Or may be used as the default language when all other languages are not set.
     *
     * @param cs   chinese, may be used as the default language when all other languages are not set
     * @param more more chinese strings
     */
    public WordsBuilder(String cs, String... more) {
        this.cs = merge(cs, more);
    }

    /**
     * Construct the builder with english language words
     *
     * @param en   english
     * @param more more english strings
     * @return <code>this</code>
     */
    public WordsBuilder setEn(String en, String... more) {
        this.en = merge(en, more);
        return this;
    }

    /**
     * Build the <code>Words</code> object
     *
     * @return <code>Words</code> object
     */
    public Words build() {
        return new Words(cs, en);
    }
}
