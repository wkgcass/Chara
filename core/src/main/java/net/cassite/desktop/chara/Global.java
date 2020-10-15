// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import javafx.scene.image.Image;
import net.cassite.desktop.chara.model.Model;

public class Global {
    public static Model model;
    public static String modelName;
    public static String modelFilePath;
    public static Image modelIcon;
    public static int modelVersion;

    public static final boolean r18features = System.getProperty("r18Features", "false").equals("true");
    public static final boolean debugFeatures = System.getProperty("ebugFeatures", "false").equals("true"); // -DebugFeatures=...

    private Global() {
    }
}
