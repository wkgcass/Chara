// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.css;

import net.cassite.desktop.chara.util.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * JavaFX CSS option<br>
 * It's a helper class when you find it hard to use {@link Class#getResourceAsStream(String)} in a modular environment.<br>
 * This helper class will generate css and dump it into a temporary file, then you may let the JavaFX nodes to load the css.<br>
 * <pre>
 * node.getStylesheets().add(
 *   new XxxCss(...).toURLString()
 * )
 * </pre>
 */
public abstract class Css {
    abstract protected String name();

    abstract protected String text();

    /**
     * Dump the CSS content to a temporary file, and retrieve its URL
     *
     * @return the retrieved URL string
     */
    public String toURLString() {
        String name = name();
        String text = text();
        File tmp;
        try {
            tmp = File.createTempFile(name, ".css");
        } catch (IOException e) {
            Logger.fatal("creating temporary css file for " + name + " failed", e);
            return "";
        }
        tmp.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(text.getBytes());
        } catch (IOException e) {
            Logger.fatal("release css content to " + tmp.getAbsolutePath() + " failed");
            return "";
        }

        try {
            return tmp.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            Logger.shouldNotReachHere(e);
            return "";
        }
    }
}
