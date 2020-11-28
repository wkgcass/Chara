// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.chat;

import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.chara.elithya.Elithya;
import net.cassite.desktop.chara.chat.Chatbot;
import net.cassite.desktop.chara.i18n.I18nConsts;
import net.cassite.desktop.chara.manager.ChatbotManager;

public class ElithyaChatbot {
    private final Elithya elithya;
    private final Chatbot chatbot;
    private final AppCallback appCallback;

    public ElithyaChatbot(Elithya elithya, AppCallback appCallback) {
        this.elithya = elithya;
        this.appCallback = appCallback;
        this.chatbot = ChatbotManager.get();
        if (chatbot != null) {
            chatbot.registerMessageCallback(appCallback::showMessage);
        }
    }

    public void takeMessage(String msg) {
        if (Global.debugFeatures() && msg.startsWith("::")) {
            debugMessage(msg.substring("::".length()));
            return;
        }

        if (chatbot == null) {
            appCallback.showMessage(I18nConsts.chatbotNotConfigured);
            return;
        }

        chatbot.takeMessage(msg);
    }

    private void debugMessage(String msg) {
        if (msg.startsWith("set:")) {
            msg = msg.substring("set:".length());
            if (msg.startsWith("arm_fore_right:")) {
                msg = msg.substring("arm_fore_right:".length());
                if (msg.startsWith("rotate:")) {
                    msg = msg.substring("rotate:".length());
                    double n;
                    try {
                        n = Double.parseDouble(msg);
                    } catch (NumberFormatException e) {
                        appCallback.showMessage("not a number");
                        return;
                    }
                    elithya.armRight.foreRotate(n);
                    return;
                }
            } else if (msg.startsWith("hand_right:")) {
                msg = msg.substring("hand_right:".length());
                if (msg.startsWith("rotate:")) {
                    msg = msg.substring("rotate:".length());
                    double n;
                    try {
                        n = Double.parseDouble(msg);
                    } catch (NumberFormatException e) {
                        appCallback.showMessage("not a number");
                        return;
                    }
                    elithya.armRight.handRotate(n);
                    return;
                }
            }
        }
        // TODO
        appCallback.showMessage("unknown command");
    }
}
