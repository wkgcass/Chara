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
     * whether should enable R18 features
     */
    public static final boolean r18features;
    /**
     * whether should enable debug features
     */
    public static final boolean debugFeatures;
    /**
     * whether global screen enabled
     */
    public static final boolean globalScreenEnabled = Utils.isWindows(); // only enable for windows

    static {
        String r18 = System.getProperty("r18Features");
        if (r18 == null) {
            r18 = System.getenv("r18Features");
        }
        r18features = "true".equals(r18);

        String debug = System.getProperty("ebugFeatures"); // -DebugFeatures=...
        if (debug == null) {
            debug = System.getenv("debugFeatures");
        }
        debugFeatures = "true".equals(debug);
    }

    private Global() {
    }
}
