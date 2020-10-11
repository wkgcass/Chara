// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.special;

import net.cassite.desktop.chara.Global;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModelFileNotFoundException extends Exception {
    public ModelFileNotFoundException() {
        super(Global.modelFilePath);
    }

    public static void moveModelFile() {
        String modelFile = Global.modelFilePath;
        if (modelFile == null) {
            return;
        }
        File f = new File(modelFile);
        if (!f.exists()) {
            return;
        }
        Path newName = findNewName(f);
        try {
            Files.move(f.toPath(), newName);
        } catch (IOException ignore) {
        }
    }

    private static Path findNewName(File f) {
        String base = f.getParent() + "/" + f.getName() + ".removed.";
        int n = 0;
        while (true) {
            String tryName = base + n;
            File x = new File(tryName);
            if (!x.exists()) {
                return Path.of(tryName);
            }
        }
    }
}
