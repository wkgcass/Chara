// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chat;

import java.util.function.Consumer;

public interface Chatbot {
    String name();

    void init(String config) throws Exception;

    void takeMessage(String msg, Consumer<String[]> cb);
}
