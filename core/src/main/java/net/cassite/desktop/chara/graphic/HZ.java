// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.application.Platform;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.util.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class HZ {
    public static final int UNIT = 10;
    private static final Object _VALUE_ = new Object();
    private static final HZ instance = new HZ();

    private final ConcurrentHashMap<Updatable, Object> registered = new ConcurrentHashMap<>();

    private volatile boolean running = false;

    private HZ() {
        ThreadUtils.get().scheduleAtFixedRate(() -> {
            if (running) {
                assert Logger.debug("frame dropped");
                return;
            }
            if (registered.isEmpty()) {
                return;
            }
            running = true;
            Platform.runLater(() -> {
                long current = System.currentTimeMillis();
                for (Updatable up : registered.keySet()) {
                    up.update(current);
                }
                running = false;
            });
        }, 0, UNIT, TimeUnit.MILLISECONDS);
    }

    /**
     * Get <code>HZ</code> instance
     *
     * @return <code>HZ</code> instance
     */
    public static HZ get() {
        return instance;
    }

    /**
     * Register a callback function
     *
     * @param up callback function
     */
    public void register(Updatable up) {
        registered.put(up, _VALUE_);
    }

    /**
     * Deregister a callback function
     *
     * @param up callback function
     */
    public void deregister(Updatable up) {
        registered.remove(up);
    }
}
