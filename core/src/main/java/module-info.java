// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

module net.cassite.desktop.chara {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.swing;
    requires java.desktop;
    requires java.logging;
    requires jnativehook;
    requires vproxy;
    exports net.cassite.desktop.chara;
    exports net.cassite.desktop.chara.chara;
    exports net.cassite.desktop.chara.chat;
    exports net.cassite.desktop.chara.control;
    exports net.cassite.desktop.chara.graphic;
    exports net.cassite.desktop.chara.i18n;
    exports net.cassite.desktop.chara.manager;
    exports net.cassite.desktop.chara.model;
    exports net.cassite.desktop.chara.special;
    exports net.cassite.desktop.chara.util;
}
