// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.css;

public class MenuItemFontFamily extends Css {
    private final String fontFamily;

    public MenuItemFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    @Override
    protected String name() {
        return "menu-item-font-family";
    }

    @Override
    protected String text() {
        return "" +
            ".menu-item {\n" +
            "    -fx-font-family: \"" + fontFamily + "\";\n" +
            "}" +
            "";
    }
}
