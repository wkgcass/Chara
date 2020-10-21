// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chat;

import java.util.function.Consumer;

public interface Chatbot {
    /**
     * Name of the chatbot
     *
     * @return name of the chatbot
     */
    String name();

    /**
     * Initiate the chatbot
     *
     * @param config config string for the chatbot
     * @throws Exception any exception when initiating
     */
    void init(String config) throws Exception;

    /**
     * Take a message from user and (optionally) give feedback
     *
     * @param msg the message
     */
    void takeMessage(String msg);

    /**
     * Register message callback to the chatbot.
     * If the callback is already registered, nothing will happen.
     *
     * @param messageCallback the callback function for the chatbot to write message back to user
     */
    void registerMessageCallback(Consumer<String[]> messageCallback);

    /**
     * Deregister message callback from the chatbot.
     * If the callback is not currently registered, nothing will happen.
     *
     * @param messageCallback the callback function previously registered
     */
    void deregisterMessageCallback(Consumer<String[]> messageCallback);
}
