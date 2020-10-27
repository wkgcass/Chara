// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.model;

import javafx.scene.Group;
import javafx.scene.control.Menu;
import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.chara.Chara;
import net.cassite.desktop.chara.i18n.I18nConsts;
import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.util.ResourceHandler;

import java.util.List;

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
     * Get constant data of the model<br>
     * You MUST NOT change any value on the returned object between any two calls.<br>
     * You should store the returned object in a field and reuse the object everytime this method is called.<br>
     * Use {@link DataBuilder} to build the returned object.<br>
     * This method will NOT be called before {@link #init(ModelInitConfig)}.
     *
     * @return constant data of the model.
     */
    Data data();

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
     * Get resource handlers of the model<br>
     * The list will be used on the startup loading bar.<br>
     * These handlers will be executed after {@link #requiredImages()} are loaded.
     *
     * @return the resource handlers list
     */
    List<ResourceHandler> resourceHandlers();

    /**
     * Initiate the model.
     *
     * @param conf config for initialization
     */
    void init(ModelInitConfig conf);

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

    class Data {
        /**
         * the character impl supports messages (in other words, the chat feature).
         */
        public final boolean messageSupported;
        /**
         * the character impl may perform active interaction<br>
         * active interaction means that the character may perform attractive things without user interaction.
         */
        public final boolean activeInteractionSupported;

        /**
         * controls whether the chat feature is enabled by default when the model is launched for the first time.<br>
         * note: if {@link #messageSupported} is set to false, this field will be ignored.
         */
        public final boolean defaultMessageEnabled;
        /**
         * controls whether the primary stage will be always on top by default when the model is launched for the first time
         */
        public final boolean defaultAlwaysOnTop;
        /**
         * controls whether the mouse indicator is enabled by default when the model is launched for the first time
         */
        public final boolean defaultMouseIndicatorEnabled;
        /**
         * controls whether active interaction is allowed by default when the model is launched for the first time.<br>
         * note: if {@link #activeInteractionSupported} is set to false, this field will be ignored
         */
        public final boolean defaultAllowActiveInteraction;
        /**
         * set the menu item text managed by the model<br>
         * you may set it to
         * {@link net.cassite.desktop.chara.i18n.I18nConsts#characterMenu}
         * or
         * {@link net.cassite.desktop.chara.i18n.I18nConsts#widgetMenu}
         */
        public final Words modelMenuItemText;
        public final boolean defaultShowIconOnTaskbar;

        private Data(boolean messageSupported,
                     boolean activeInteractionSupported,
                     boolean defaultMessageEnabled,
                     boolean defaultAlwaysOnTop,
                     boolean defaultMouseIndicatorEnabled,
                     boolean defaultAllowActiveInteraction,
                     Words modelMenuItemText,
                     boolean defaultShowIconOnTaskbar) {
            this.messageSupported = messageSupported;
            this.activeInteractionSupported = activeInteractionSupported;
            this.defaultMessageEnabled = defaultMessageEnabled;
            this.defaultAlwaysOnTop = defaultAlwaysOnTop;
            this.defaultMouseIndicatorEnabled = defaultMouseIndicatorEnabled;
            this.defaultAllowActiveInteraction = defaultAllowActiveInteraction;
            this.modelMenuItemText = modelMenuItemText;
            this.defaultShowIconOnTaskbar = defaultShowIconOnTaskbar;
        }
    }

    class DataBuilder {
        private boolean messageSupported = false;
        private boolean activeInteractionSupported = false;

        private boolean defaultMessageEnabled = true;
        private boolean defaultAlwaysOnTop = true;
        private boolean defaultMouseIndicatorEnabled = true;
        private boolean defaultAllowActiveInteraction = true;
        private Words modelMenuItemText = I18nConsts.modelMenu;
        private boolean defaultShowIconOnTaskbar = true;

        public Data build() {
            return new Data(
                messageSupported,
                activeInteractionSupported,
                defaultMessageEnabled,
                defaultAlwaysOnTop,
                defaultMouseIndicatorEnabled,
                defaultAllowActiveInteraction,
                modelMenuItemText,
                defaultShowIconOnTaskbar
            );
        }

        /**
         * @param messageSupported {@link Data#messageSupported}
         * @return this
         */
        public DataBuilder setMessageSupported(boolean messageSupported) {
            this.messageSupported = messageSupported;
            return this;
        }

        /**
         * @param activeInteractionSupported {@link Data#activeInteractionSupported}
         * @return this
         */
        public DataBuilder setActiveInteractionSupported(boolean activeInteractionSupported) {
            this.activeInteractionSupported = activeInteractionSupported;
            return this;
        }

        /**
         * @param defaultMessageEnabled {@link Data#defaultMessageEnabled}
         * @return this
         */
        public DataBuilder setDefaultMessageEnabled(boolean defaultMessageEnabled) {
            this.defaultMessageEnabled = defaultMessageEnabled;
            return this;
        }

        /**
         * @param defaultAlwaysOnTop {@link Data#defaultAlwaysOnTop}
         * @return this
         */
        public DataBuilder setDefaultAlwaysOnTop(boolean defaultAlwaysOnTop) {
            this.defaultAlwaysOnTop = defaultAlwaysOnTop;
            return this;
        }

        /**
         * @param defaultMouseIndicatorEnabled {@link Data#defaultMouseIndicatorEnabled}
         * @return this
         */
        public DataBuilder setDefaultMouseIndicatorEnabled(boolean defaultMouseIndicatorEnabled) {
            this.defaultMouseIndicatorEnabled = defaultMouseIndicatorEnabled;
            return this;
        }

        /**
         * @param defaultAllowActiveInteraction {@link Data#defaultAllowActiveInteraction}
         * @return this
         */
        public DataBuilder setDefaultAllowActiveInteraction(boolean defaultAllowActiveInteraction) {
            this.defaultAllowActiveInteraction = defaultAllowActiveInteraction;
            return this;
        }

        /**
         * @param modelMenuItemText {@link Data#modelMenuItemText}
         * @return this
         */
        public DataBuilder setModelMenuItemText(Words modelMenuItemText) {
            this.modelMenuItemText = modelMenuItemText;
            return this;
        }

        /**
         * @param defaultShowIconOnTaskbar {@link Data#defaultShowIconOnTaskbar}
         * @return this
         */
        public DataBuilder setDefaultShowIconOnTaskbar(boolean defaultShowIconOnTaskbar) {
            this.defaultShowIconOnTaskbar = defaultShowIconOnTaskbar;
            return this;
        }
    }

}
