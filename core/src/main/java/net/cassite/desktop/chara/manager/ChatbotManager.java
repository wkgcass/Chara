// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.manager;

import net.cassite.desktop.chara.chat.Chatbot;
import net.cassite.desktop.chara.chat.TianxingTuling;
import net.cassite.desktop.chara.util.Logger;

public class ChatbotManager {
    private static volatile Chatbot selected = null;

    private static final Chatbot[] impls = new Chatbot[]{
        new TianxingTuling(),
    };

    public static Chatbot get() {
        if (selected != null) {
            return selected;
        }
        synchronized (ChatbotManager.class) {
            if (selected != null) {
                return selected;
            }
            String chatbotConf = ConfigManager.get().getChatbot();
            if (chatbotConf == null) {
                assert Logger.debug("no chatbot provided");
                return null;
            }
            String name = chatbotConf.split(":")[0];
            String args = "";
            if (chatbotConf.length() > name.length()) {
                args = chatbotConf.substring(name.length() + 1);
            }
            Chatbot chatbot = null;
            for (var impl : impls) {
                if (impl.name().equals(name)) {
                    chatbot = impl;
                    break;
                }
            }
            if (chatbot == null) {
                Logger.fatal("no chatbot implementation called " + name);
                return null;
            }
            try {
                chatbot.init(args);
            } catch (Exception e) {
                Logger.fatal("initiating chatbot failed", e);
                return null;
            }
            selected = chatbot;
            assert Logger.debug("using chatbot " + selected.name());
        }
        return selected;
    }
}
