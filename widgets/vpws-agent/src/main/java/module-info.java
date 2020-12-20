// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

module vpwsagent {
    requires javafx.graphics;
    requires javafx.controls;
    requires net.cassite.desktop.chara;
    requires vproxy;
    requires javafx.swing;
    exports net.cassite.desktop.chara.model.vpwsagent;
    exports run.vpwsagent;
}
