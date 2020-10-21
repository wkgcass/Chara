// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.i18n;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Random selector of <code>Words</code>
 */
public class WordsSelector {
    private static final ThreadLocalRandom rand = ThreadLocalRandom.current();
    private final Words[] words;
    private final LinkedList<Integer> lastSelected = new LinkedList<>();

    /**
     * Constructor
     *
     * @param words the words may be selected
     */
    public WordsSelector(Words... words) {
        this.words = words;
    }

    /**
     * Select a <code>Words</code> object
     *
     * @return the selected <code>Words</code> object
     */
    public synchronized Words select() {
        int idx = rand.nextInt(words.length);
        while (lastSelected.contains(idx)) {
            ++idx;
        }
        if (idx >= words.length) {
            idx = 0;
        }
        while (lastSelected.contains(idx)) {
            ++idx;
        }
        lastSelected.add(idx);
        if (lastSelected.size() >= words.length) {
            for (int i = 0; i < 10 && !lastSelected.isEmpty(); ++i) {
                lastSelected.pollFirst();
            }
        }
        return words[idx];
    }

    /**
     * How many <code>Words</code> objects are contained in this selector
     *
     * @return the count result
     */
    public int count() {
        return words.length;
    }
}
