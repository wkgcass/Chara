// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.model;

import net.cassite.desktop.chara.i18n.WordsSelector;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Rec;

import java.util.Map;

/**
 * The config for model initialization
 */
public class ModelInitConfig {
    /**
     * version number defined in model file
     */
    public int version;
    /**
     * a map containing words selector loaded from model <code>/words</code> directory
     */
    public Map<String, WordsSelector> interactionWordsSelectors;
    private Map<String, Integer> integerValuesMap;
    private Map<String, Double> doubleValuesMap;
    private Map<String, Rec> integerRectanglesMap;

    public ModelInitConfig() {
    }

    public void setIntegerValuesMap(Map<String, Integer> integerValuesMap) {
        this.integerValuesMap = integerValuesMap;
    }

    public void setDoubleValuesMap(Map<String, Double> doubleValuesMap) {
        this.doubleValuesMap = doubleValuesMap;
    }

    public void setIntegerRectanglesMap(Map<String, Rec> integerRectanglesMap) {
        this.integerRectanglesMap = integerRectanglesMap;
    }

    /**
     * Get an integer value from model <code>/values</code> directory
     *
     * @param key key
     * @return the loaded value
     */
    public int getInt(String key) {
        Integer n = integerValuesMap.get(key);
        if (n == null) {
            Logger.fatal("missing integer from model config: " + key);
            return 0;
        }
        return n;
    }

    /**
     * Get a double value from model <code>/values</code> directory
     *
     * @param key key
     * @return the loaded value
     */
    public double getDouble(String key) {
        Double n = doubleValuesMap.get(key);
        if (n == null) {
            Logger.fatal("missing double from model config: " + key);
            return 0D;
        }
        return n;
    }

    /**
     * Get an integer rectangle from model <code>/values</code> directory
     *
     * @param key key
     * @return the loaded value
     */
    public Rec getIntegerRectangle(String key) {
        Rec r = integerRectanglesMap.get(key);
        if (r == null) {
            Logger.fatal("missing integer rectangle from model config: " + key);
            return new Rec(0, 0, 0, 0);
        }
        return r;
    }

    /**
     * Get an integer rectangle from model <code>/values</code> directory and scale it
     *
     * @param key   key
     * @param scale scale ratio
     * @return the loaded and scaled value
     */
    public Rec getIntegerRectangle(String key, double scale) {
        Rec r = getIntegerRectangle(key);
        return new Rec(
            (int) (r.x1 * scale),
            (int) (r.y1 * scale),
            (int) (r.x2 * scale),
            (int) (r.y2 * scale));
    }
}
