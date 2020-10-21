// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import javafx.scene.image.Image;
import net.cassite.desktop.chara.model.Model;

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
     * whether should enable R18 features
     */
    public static final boolean r18features = System.getProperty("r18Features", "false").equals("true");
    /**
     * whether should enable debug features
     */
    public static final boolean debugFeatures = System.getProperty("ebugFeatures", "false").equals("true"); // -DebugFeatures=...

    private Global() {
    }
}
