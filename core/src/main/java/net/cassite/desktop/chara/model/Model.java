// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.model;

import javafx.scene.Group;
import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.chara.Chara;

import java.util.List;
import java.util.zip.ZipFile;

public interface Model {
    String name();

    Chara construct(AppCallback cb, Group parent);

    List<String> requiredImages();

    void init(ModelInitConfig conf);

    void customizeInit(ZipFile model) throws Exception;
}
