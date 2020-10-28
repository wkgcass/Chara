// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.tianxingchatbot;

import net.cassite.desktop.chara.chat.tianxing.TianxingChatbot;
import net.cassite.desktop.chara.graphic.Alert;
import net.cassite.desktop.chara.i18n.tianxing.I18n;
import net.cassite.desktop.chara.manager.ChatbotManager;
import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.util.ResourceHandler;

import java.util.Collections;
import java.util.List;

public class TianxingChatbotPlugin implements Plugin {
    @Override
    public String name() {
        return "tianxing-chatbot";
    }

    @Override
    public int version() {
        return 1000000;
    }

    @Override
    public List<ResourceHandler> resourceHandlers() {
        return Collections.emptyList();
    }

    @Override
    public void launch() {
        ChatbotManager.register(new TianxingChatbot());
    }

    @Override
    public void clicked() {
        TianxingChatbot.switchType();
        alertApiType();
    }

    private void alertApiType() {
        if (TianxingChatbot.apiType == TianxingChatbot.APIType.tuling) {
            Alert.alert(I18n.usingTianxingTuling.get()[0]);
        } else if (TianxingChatbot.apiType == TianxingChatbot.APIType.robot) {
            Alert.alert(I18n.usingTianxingRobot.get()[0]);
        }
    }

    @Override
    public void release() {
        // do nothing
    }
}
