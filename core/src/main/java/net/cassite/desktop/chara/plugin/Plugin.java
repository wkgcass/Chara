// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin;

public interface Plugin {
    /**
     * Get name of the plugin
     *
     * @return name of the plugin
     */
    String name();

    /**
     * Get version of the plugin
     *
     * @return version of the plugin
     */
    int version();

    /**
     * Launch the plugin
     *
     * @throws Exception any exception when launching
     */
    void launch() throws Exception;

    /**
     * The plugin menu item is clicked
     */
    void clicked();

    /**
     * Release any resource the plugin is holding and shutdown the plugin
     */
    void release();
}
