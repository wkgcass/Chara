// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin;

import net.cassite.desktop.chara.util.ResourceHandler;

import java.util.List;

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
     * Get priority of the plugin.<br>
     * Greater the priority is, earlier the plugin is launched.<br>
     * Also, greater the priority is, later the plugin is released.<br>
     * Default priority is 0, most plugins may ignore this method.<br>
     * Note: the priorities won't effect the plugins constructing process.
     *
     * @return priority of the plugin
     */
    default double priority() {
        return 0;
    }

    /**
     * Get resource handlers of the plugin<br>
     * The list will be used on the startup loading bar.
     *
     * @return the resource handlers list
     */
    List<ResourceHandler> resourceHandlers();

    /**
     * Launch the plugin.<br>
     * You should put initiating operations into this function,
     * and these operations should be reverted in <code>release</code> function.
     */
    void launch();

    /**
     * The plugin menu item is clicked
     */
    void clicked();

    /**
     * Revert operation in <code>launch()</code>.<br>
     */
    void release();

    /**
     * The string to show when clicking "About Chara" menu item.
     *
     * @return the "about" string of this plugin
     */
    String about();
}
