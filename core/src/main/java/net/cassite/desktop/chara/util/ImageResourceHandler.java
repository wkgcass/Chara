// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import net.cassite.desktop.chara.manager.ImageManager;

public class ImageResourceHandler extends ResourceHandler {
    public ImageResourceHandler(String entrySuffix) {
        super(entrySuffix, (is, cb) -> {
            ImageManager.load(entrySuffix);
            cb.succeeded(null);
        });
    }
}
