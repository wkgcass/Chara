// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.model;

import net.cassite.desktop.chara.i18n.WordsSelector;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Rec;

import java.util.Map;

public class ModelInitConfig {
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

    public int getInt(String key) {
        Integer n = integerValuesMap.get(key);
        if (n == null) {
            Logger.fatal("missing integer from model config: " + key);
            return 0;
        }
        return n;
    }

    public double getDouble(String key) {
        Double n = doubleValuesMap.get(key);
        if (n == null) {
            Logger.fatal("missing double from model config: " + key);
            return 0D;
        }
        return n;
    }

    public Rec getIntegerRectangle(String key) {
        Rec r = integerRectanglesMap.get(key);
        if (r == null) {
            Logger.fatal("missing integer rectangle from model config: " + key);
            return new Rec(0, 0, 0, 0);
        }
        return r;
    }
}
