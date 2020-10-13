// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.chat;

import javafx.application.Platform;
import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.chat.Chatbot;
import net.cassite.desktop.chara.chara.kokori.Kokori;
import net.cassite.desktop.chara.chara.kokori.personality.KokoriPersonality;
import net.cassite.desktop.chara.chara.kokori.personality.KokoriWords;
import net.cassite.desktop.chara.graphic.Alert;
import net.cassite.desktop.chara.i18n.I18nConsts;
import net.cassite.desktop.chara.manager.ChatbotManager;
import net.cassite.desktop.chara.util.Logger;

public class KokoriChatBot {
    private final Kokori kokori;
    private final KokoriPersonality personality;
    private final AppCallback appCallback;

    public KokoriChatBot(Kokori kokori, KokoriPersonality personality, AppCallback appCallback) {
        this.kokori = kokori;
        this.personality = personality;
        this.appCallback = appCallback;
    }

    public void takeMessage(String msg) {
        ThreadUtils.get().submit(() -> doTakeMessage(msg));
    }

    private void doTakeMessage(String msg) {
        // check special commands
        if (msg.startsWith("::")) {
            runCommand(msg.substring("::".length()));
            return;
        }

        // check pre-built questions
        if (handleMessage(msg)) {
            return;
        }

        // use a chatbot
        Chatbot chatbot = ChatbotManager.get();
        if (chatbot == null) {
            Alert.alert(I18nConsts.chatbotNotConfigured.get()[0]);
            return;
        }

        appCallback.showMessage("...");
        String[] resp = chatbot.takeMessage(msg);
        if (resp == null || resp.length == 0) {
            assert Logger.debug("response of " + msg + " is empty");
            return;
        }
        appCallback.showMessage(resp);
    }

    private void runCommand(String cmd0) {
        Platform.runLater(() -> {
            String cmd = cmd0;

            if (cmd.startsWith("repeat:")) {
                appCallback.showMessage(cmd.substring("repeat:".length()));
                return;
            } else if (cmd.startsWith("get:")) {
                cmd = cmd.substring("get:".length());
                if (cmd.equals("bond_point")) {
                    appCallback.showMessage(String.format("%.3f", personality.getBondPoint()));
                    return;
                }
            } else if (cmd.startsWith("set:")) {
                cmd = cmd.substring("set:".length());
                if (cmd.startsWith("bond_point:")) {
                    cmd = cmd.substring("bond_point:".length());
                    double d;
                    try {
                        d = Double.parseDouble(cmd);
                    } catch (NumberFormatException e) {
                        Alert.alert("not a number");
                        return;
                    }
                    assert Logger.debug("setting bond point to " + d);
                    personality.setBondPoint(d);
                    return;
                }
            } else if (cmd.startsWith("animate:")) {
                cmd = cmd.substring("animate:".length());
                if ("rune:show".equals(cmd)) {
                    kokori.armRight.showRune();
                    return;
                } else if ("red_cheek:show".equals(cmd)) {
                    kokori.redCheek.show();
                    return;
                } else if ("red_cheek:hide".equals(cmd)) {
                    kokori.redCheek.hide();
                    return;
                } else if ("mouth:happy".equals(cmd)) {
                    kokori.mouth.toHappy();
                    return;
                } else if ("mouth:sad".equals(cmd)) {
                    kokori.mouth.toSad();
                    return;
                } else if ("mouth:open".equals(cmd)) {
                    kokori.mouth.toOpen();
                    return;
                } else if ("mouth:default".equals(cmd)) {
                    kokori.mouth.toDefault();
                    return;
                } else if ("mouth:left".equals(cmd)) {
                    kokori.mouth.tiltToLeft();
                    return;
                } else if ("mouth:right".equals(cmd)) {
                    kokori.mouth.tiltToRight();
                    return;
                } else if ("highlight:show".equals(cmd)) {
                    kokori.eyeLeft.addHighlight();
                    kokori.eyeRight.addHighlight();
                    return;
                } else if ("highlight:hide".equals(cmd)) {
                    kokori.eyeLeft.removeHighlight();
                    kokori.eyeRight.removeHighlight();
                    return;
                } else if (cmd.startsWith("eye:zoom:")) {
                    String sd = cmd.substring("eye:zoom:".length());
                    double d;
                    try {
                        d = Double.parseDouble(sd);
                    } catch (NumberFormatException e) {
                        Alert.alert("not a number");
                        return;
                    }
                    kokori.eyeLeft.zoom(d);
                    kokori.eyeRight.zoom(d);
                    return;
                } else if ("head:right".equals(cmd)) {
                    kokori.headJoin.tiltToRight();
                    return;
                } else if ("head:left".equals(cmd)) {
                    kokori.headJoin.tiltToLeft();
                    return;
                } else if ("head:default".equals(cmd)) {
                    kokori.headJoin.toDefaultPosition();
                    return;
                } else if ("arm_right:tighten".equals(cmd)) {
                    kokori.armRight.tighten();
                    return;
                } else if ("arm_right:down".equals(cmd)) {
                    kokori.armRight.protectCrotch();
                    return;
                } else if ("arm_right:default".equals(cmd)) {
                    kokori.armRight.moveToDefaultPosition();
                    return;
                } else if ("leg_left:tighten".equals(cmd)) {
                    kokori.legLeft.tighten(() -> {
                    });
                    return;
                } else if ("leg_left:loose".equals(cmd)) {
                    kokori.legLeft.loose();
                    return;
                }
            }
            appCallback.showMessage(KokoriWords.idontknow.select());
        });
    }

    private boolean handleMessage(String msg) {
        msg = msg.replace("?", "").replace("？", "");
        msg = msg.trim();
        msg = msg.toLowerCase();
        if (msg.equals("你叫什么") ||
            msg.equals("你叫什么名字") ||
            msg.equals("你的名字是什么") ||
            msg.equals("你的名字叫什么") ||
            msg.equals("what's your name") ||
            msg.equals("what is your name")) {
            appCallback.showMessage(KokoriWords.aboutName.select());
            return true;
        }
        return false;
    }
}
