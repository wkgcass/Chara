// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin;

import java.util.zip.ZipFile;

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
     * Initiate the plugin.<br>
     * You should put irrecoverable operations into this function.
     * Recoverable operations should be put into <code>launch()</code> method.
     *
     * @param zipFile zip file of the plugin
     * @throws Exception any exception when initiating
     */
    void init(ZipFile zipFile) throws Exception;

    /**
     * Launch the plugin.<br>
     * You should put recoverable operations into this function,
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
}
