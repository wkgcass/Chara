// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.model;

import javafx.scene.Group;
import javafx.scene.control.Menu;
import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.chara.Chara;

import java.util.List;
import java.util.zip.ZipFile;

public interface Model {
    /**
     * Name of the model/character
     *
     * @return name
     */
    String name();

    /**
     * Version of the model/character
     *
     * @return version
     */
    int version();

    /**
     * Construct the character object
     *
     * @param params parameters for construction
     * @return character object
     * @see Chara
     */
    Chara construct(ConstructParams params);

    /**
     * Retrieve the image list to be loaded<br>
     * The list will be used on the startup loading bar.
     *
     * @return the image list
     */
    List<String> requiredImages();

    /**
     * Initiate the model.
     *
     * @param conf config for initialization
     */
    void init(ModelInitConfig conf);

    /**
     * Customized initialization process.
     *
     * @param model the model zip file
     * @throws Exception any exception occurred
     */
    void customizeInit(ZipFile model) throws Exception;

    class ConstructParams {
        /**
         * app callback
         */
        public final AppCallback cb;
        /**
         * the JavaFX parent group.<br>
         * all visual resources should be added to this group.
         */
        public final Group parent;
        /**
         * menu for the character.
         */
        public final Menu characterMenu;

        public ConstructParams(AppCallback cb, Group parent, Menu characterMenu) {
            this.cb = cb;
            this.parent = parent;
            this.characterMenu = characterMenu;
        }
    }
}
