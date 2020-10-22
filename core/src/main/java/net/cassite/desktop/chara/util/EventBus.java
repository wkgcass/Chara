// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import javafx.application.Platform;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

/**
 * Event bus
 */
public class EventBus {
    @SuppressWarnings("rawtypes")
    private static final ConcurrentHashMap<Key<?>, ConcurrentLinkedDeque<Consumer>> events = new ConcurrentHashMap<>();

    private EventBus() {
    }

    /**
     * Send the message to all consumers watching the event
     *
     * @param event   event ref
     * @param message message to send
     * @param <T>     type of the message
     */
    public static <T> void publish(Key<T> event, T message) {
        var consumers = events.get(event);
        if (consumers == null) {
            return;
        }
        for (var consumer : consumers) {
            Platform.runLater(() -> {
                try {
                    //noinspection unchecked
                    consumer.accept(message);
                } catch (Throwable t) {
                    Logger.error("consumer thrown exception when handling event " + event + " with message " + message, t);
                }
            });
        }
    }

    /**
     * Watch an event
     *
     * @param event    event ref
     * @param consumer handler of the event messages
     * @param <T>      type of the messages
     * @return an object for you to manage the watching registration
     */
    public static <T> WatchingRegistration<T> watch(Key<T> event, Consumer<T> consumer) {
        events.computeIfAbsent(event, x -> new ConcurrentLinkedDeque<>()).add(consumer);
        return new WatchingRegistration<>(event, consumer);
    }

    /**
     * A watching registration for an event
     *
     * @param <T> type of the message of the event
     */
    public static class WatchingRegistration<T> {
        private boolean canceled = false;
        private final Key<T> event;
        private final Consumer<T> consumer;

        private WatchingRegistration(Key<T> event, Consumer<T> consumer) {
            this.event = event;
            this.consumer = consumer;
        }

        /**
         * Cancel the registration
         */
        public void cancel() {
            if (canceled) {
                return;
            }
            canceled = true;
            var consumers = events.get(event);
            if (consumers == null) {
                return;
            }
            consumers.remove(consumer);
        }
    }
}
