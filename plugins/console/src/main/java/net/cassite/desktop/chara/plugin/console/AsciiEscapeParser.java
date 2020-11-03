// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.console;

import javafx.scene.paint.Color;
import vproxybase.util.Logger;

import java.util.LinkedList;
import java.util.List;

public class AsciiEscapeParser {
    private static final Color NORMAL = new Color(0xb1 / 255d, 0xcb / 255d, 0xcd / 255d, 1);
    private static final Color INFO = new Color(0x8d / 255d, 0xc4 / 255d, 0x86 / 255d, 1);
    private static final Color WARN = new Color(0xaf / 255d, 0x89 / 255d, 0x2d / 255d, 1);
    private static final Color ERROR = new Color(0xcb / 255d, 0x42 / 255d, 0x39 / 255d, 1);
    private static final Color DEBUG = new Color(0x44 / 255d, 0x8b / 255d, 0xcc / 255d, 1);

    private int state = 0;
    // 0: reading normal texts
    // 1: reading escape (when met \u001b)
    private final StringBuilder escapeBuf = new StringBuilder();
    private final StringBuilder textBuf = new StringBuilder();
    private Color color = NORMAL;

    public List<TextInfo> parse(String text) {
        List<TextInfo> ret = new LinkedList<>();
        char[] chars = text.toCharArray();
        for (char c : chars) {
            if (c == '\n' || c == '\r') {
                if (color != NORMAL) {
                    finish(ret);
                }
                reset();
            }
            if (state == 0) {
                if (c == '\u001b') {
                    finish(ret);
                    escapeBuf.append(c);
                    state = 1;
                } else {
                    textBuf.append(c);
                }
            } else {
                assert state == 1;
                if (c == 'm') {
                    escapeBuf.append(c);
                    state = 0;
                    handleEscape();
                } else {
                    escapeBuf.append(c);
                }
            }
        }
        finish(ret);
        return ret;
    }

    private void reset() {
        color = NORMAL;
        escapeBuf.delete(0, escapeBuf.length());
        state = 0;
    }

    private void finish(List<TextInfo> ret) {
        String s = textBuf.toString();
        if (s.isEmpty()) {
            return;
        }
        ret.add(new TextInfo(s, color));
        textBuf.delete(0, textBuf.length());
    }

    private void handleEscape() {
        String esc = escapeBuf.toString();
        escapeBuf.delete(0, escapeBuf.length());
        switch (esc) {
            case Logger.DEBUG_COLOR:
                color = DEBUG;
                break;
            case Logger.INFO_COLOR:
                color = INFO;
                break;
            case Logger.WARN_COLOR:
                color = WARN;
                break;
            case Logger.ERROR_COLOR:
                color = ERROR;
                break;
            case Logger.RESET_COLOR:
                color = NORMAL;
                break;
        }
    }
}
