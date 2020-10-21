// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Implements some general methods for the {@link Chatbot}
 */
public abstract class AbstractChatbot implements Chatbot {
    private static final Object _VALUE_ = new Object();
    private final String name;
    private final Map<Consumer<String[]>, Object> messageCallbacks = new ConcurrentHashMap<>();

    /**
     * The sub classes must call <code>super("constant value");</code> in constructor
     *
     * @param name name of the chatbot
     */
    protected AbstractChatbot(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void registerMessageCallback(Consumer<String[]> messageCallback) {
        messageCallbacks.put(messageCallback, _VALUE_);
    }

    @Override
    public void deregisterMessageCallback(Consumer<String[]> messageCallback) {
        messageCallbacks.remove(messageCallback);
    }

    protected void sendMessage(String[] messageBatch) {
        for (var cb : messageCallbacks.keySet()) {
            cb.accept(messageBatch);
        }
    }
}
