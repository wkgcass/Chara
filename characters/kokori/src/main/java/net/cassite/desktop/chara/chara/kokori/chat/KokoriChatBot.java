// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.chat;

import javafx.application.Platform;
import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.chara.kokori.Kokori;
import net.cassite.desktop.chara.chara.kokori.personality.KokoriPersonality;
import net.cassite.desktop.chara.chara.kokori.personality.KokoriWords;
import net.cassite.desktop.chara.chara.kokori.util.Consts;
import net.cassite.desktop.chara.chat.Chatbot;
import net.cassite.desktop.chara.graphic.Alert;
import net.cassite.desktop.chara.i18n.I18nConsts;
import net.cassite.desktop.chara.manager.ChatbotManager;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.util.Logger;

import java.util.function.Consumer;

public class KokoriChatBot {
    private final Kokori kokori;
    private final KokoriPersonality personality;
    private final AppCallback appCallback;
    private final Consumer<String[]> messageHandler;

    public KokoriChatBot(Kokori kokori, KokoriPersonality personality, AppCallback appCallback) {
        this.kokori = kokori;
        this.personality = personality;
        this.appCallback = appCallback;
        this.messageHandler = this::handleMessageFromBot;
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
        if (handleSpecialMessage(msg)) {
            return;
        }

        // use a chatbot
        Chatbot chatbot = ChatbotManager.get();
        if (chatbot == null) {
            Alert.alert(I18nConsts.chatbotNotConfigured.get()[0]);
            return;
        }
        chatbot.registerMessageCallback(messageHandler);

        appCallback.showMessage("...");
        chatbot.takeMessage(msg);
    }

    private void handleMessageFromBot(String[] msgs) {
        if (msgs == null || msgs.length == 0) {
            assert Logger.debug("message from bot is empty");
            return;
        }
        appCallback.showMessage(msgs);
    }

    private void runCommand(String cmd0) {
        if (!Global.debugFeatures()) {
            appCallback.showMessage(KokoriWords.idontknow.select());
            return;
        }
        Platform.runLater(() -> {
            String cmd = cmd0;

            if (cmd.startsWith("repeat:")) {
                appCallback.showMessage(cmd.substring("repeat:".length()));
                return;
            } else if (cmd.startsWith("get:")) {
                cmd = cmd.substring("get:".length());
                //noinspection IfCanBeSwitch
                if (cmd.equals("bond_point")) {
                    appCallback.showMessage(String.format("%.3f", personality.getBondPoint()));
                    return;
                } else if (cmd.equals("desire_point")) {
                    appCallback.showMessage(String.format("%.3f", personality.getDesirePoint()));
                    return;
                } else if (cmd.equals("proposing_accepted")) {
                    appCallback.showMessage("" + ConfigManager.get().getBoolValue(Consts.PROPOSING_ACCEPTED));
                    return;
                } else if (cmd.equals("proposing_count")) {
                    appCallback.showMessage("" + ConfigManager.get().getIntValue(Consts.PROPOSING_COUNT));
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
                } else if (cmd.startsWith("desire_point:")) {
                    cmd = cmd.substring("desire_point:".length());
                    double d;
                    try {
                        d = Double.parseDouble(cmd);
                    } catch (NumberFormatException e) {
                        Alert.alert("not a number");
                        return;
                    }
                    assert Logger.debug("setting desire point to " + d);
                    personality.setDesirePoint(d);
                    return;
                } else if (cmd.startsWith("proposing_count:")) {
                    cmd = cmd.substring("proposing_count:".length());
                    int n;
                    try {
                        n = Integer.parseInt(cmd);
                    } catch (NumberFormatException e) {
                        Alert.alert("not a number");
                        return;
                    }
                    ConfigManager.get().setIntValue(Consts.PROPOSING_COUNT, n);
                    return;
                } else if (cmd.startsWith("proposing_accepted:")) {
                    cmd = cmd.substring("proposing_accepted:".length());
                    if (cmd.equals("true")) {
                        kokori.setProposeAccepted(true);
                    } else if (cmd.equals("false")) {
                        kokori.setProposeAccepted(false);
                    } else {
                        Alert.alert("not bool");
                    }
                    return;
                } else if (cmd.startsWith("propose_menu_item:")) {
                    cmd = cmd.substring("propose_menu_item:".length());
                    if (cmd.equals("enabled")) {
                        kokori.setProposeMenuItemDisabled(false);
                    } else if (cmd.equals("disabled")) {
                        kokori.setProposeMenuItemDisabled(true);
                    } else {
                        Alert.alert("must be enabled or disabled");
                    }
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
                } else if ("breathe:start".equals(cmd)) {
                    kokori.mouth.startAnimatingOpenAndShut();
                    return;
                } else if ("breathe:stop".equals(cmd)) {
                    kokori.mouth.stopAnimatingOpenAndShut();
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
                } else if (cmd.equals("eye:close")) {
                    kokori.eyeLeft.close();
                    kokori.eyeRight.close();
                    return;
                } else if (cmd.equals("eye:open")) {
                    kokori.eyeLeft.open();
                    kokori.eyeRight.open();
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
            } else if (cmd.startsWith("action:")) {
                cmd = cmd.substring("action:".length());
                if (cmd.equals("sex")) {
                    kokori.r18.animateSex(() -> {
                    });
                    return;
                }
            } else if (cmd.startsWith("sys:")) {
                cmd = cmd.substring("sys:".length());
                if (cmd.startsWith("get:")) {
                    cmd = cmd.substring("get:".length());
                    //noinspection IfCanBeSwitch
                    if (cmd.equals("sex_count")) {
                        appCallback.showMessage("" + ConfigManager.get().getIntValue(Consts.SEX_COUNT));
                        return;
                    } else if (cmd.equals("bad_sex_count")) {
                        appCallback.showMessage("" + ConfigManager.get().getIntValue(Consts.BAD_SEX_COUNT));
                        return;
                    } else if (cmd.equals("normal_sex_count")) {
                        appCallback.showMessage("" + ConfigManager.get().getIntValue(Consts.NORMAL_SEX_COUNT));
                        return;
                    } else if (cmd.equals("orgasm_count")) {
                        appCallback.showMessage("" + ConfigManager.get().getIntValue(Consts.ORGASM_COUNT));
                        return;
                    } else if (cmd.equals("sex_total_time")) {
                        appCallback.showMessage("" + ConfigManager.get().getIntValue(Consts.SEX_TOTAL_TIME));
                        return;
                    } else if (cmd.equals("love_potion_used")) {
                        appCallback.showMessage("" + ConfigManager.get().getBoolValue(Consts.LOVE_POTION_USED));
                        return;
                    }
                } else if (cmd.equals("love_potion:add")) {
                    kokori.r18.addLovePotion();
                    return;
                }
            }
            appCallback.showMessage(KokoriWords.idontknow.select());
        });
    }

    private boolean handleSpecialMessage(@SuppressWarnings("unused") String msg) {
        return false;
    }
}
