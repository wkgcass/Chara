// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import javafx.scene.image.Image;
import net.cassite.desktop.chara.model.Model;
import net.cassite.desktop.chara.util.Utils;

public class Global {
    /**
     * the model object
     */
    public static Model model;
    /**
     * name of the model
     */
    public static String modelName;
    /**
     * filepath to the model file
     */
    public static String modelFilePath;
    /**
     * image to be used as icon of the model
     */
    public static Image modelIcon;
    /**
     * the model version number
     */
    public static int modelVersion;
    /**
     * the chara program default icon
     */
    public static Image charaDefaultIcon;
    /**
     * whether global screen enabled
     */
    public static final boolean globalScreenEnabled = Utils.isWindows(); // only enable for windows

    private static Boolean r18Features = null;
    private static Boolean debugFeatures = null;

    private Global() {
    }

    /**
     * Whether should enable R18 features
     *
     * @return true to enable r18 features, false otherwise
     */
    public static boolean r18Features() {
        if (r18Features == null) {
            r18Features = false;
        }
        return r18Features;
    }

    /**
     * Enable or disable r18 features
     *
     * @param b true to enable, false to disable
     */
    public static void setR18Features(boolean b) {
        if (r18Features != null) {
            throw new IllegalStateException("r18Features is already set: " + r18Features);
        }
        r18Features = b;
    }

    /**
     * Whether should enable debug features
     *
     * @return true to enable debug features, false otherwise
     */
    public static boolean debugFeatures() {
        if (debugFeatures == null) {
            debugFeatures = false;
        }
        return debugFeatures;
    }

    /**
     * Enable or disable debug features
     *
     * @param b true to enable, false to disable
     */
    public static void setDebugFeatures(boolean b) {
        if (debugFeatures != null) {
            throw new IllegalStateException("debugFeatures is already set: " + debugFeatures);
        }
        debugFeatures = b;
    }
}
