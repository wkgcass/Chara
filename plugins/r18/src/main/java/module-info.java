// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

@SuppressWarnings("JavaModuleNaming")
module net.cassite.desktop.chara.plugin.r18 {
    requires javafx.graphics;
    requires javafx.controls;
    requires vproxy;
    requires net.cassite.desktop.chara;
    exports net.cassite.desktop.chara.plugin.r18;
    exports run.plugin.r18;
}
