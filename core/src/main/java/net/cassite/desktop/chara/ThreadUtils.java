// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import javafx.application.Platform;

import java.util.concurrent.*;

public class ThreadUtils {
    private static final ThreadUtils instance = new ThreadUtils();

    private final ScheduledExecutorService exec;

    private ThreadUtils() {
        exec = Executors.newScheduledThreadPool(4);
    }

    public static ThreadUtils get() {
        return instance;
    }

    private volatile boolean isShutdown = false;

    public void shutdownNow() {
        isShutdown = true;
        exec.shutdownNow();
    }

    public boolean isShutdown() {
        return isShutdown || exec.isShutdown();
    }

    public ScheduledFuture<?> schedule(Runnable runnable, int delay, TimeUnit unit) {
        if (isShutdown()) {
            return null;
        }
        return exec.schedule(runnable, delay, unit);
    }

    public ScheduledFuture<?> scheduleFX(Runnable runnable, int delay, TimeUnit unit) {
        if (isShutdown()) {
            return null;
        }
        return exec.schedule(() -> Platform.runLater(runnable), delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, int initialDelay, int period, TimeUnit unit) {
        if (isShutdown()) {
            return null;
        }
        return exec.scheduleAtFixedRate(runnable, initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRateFX(Runnable runnable, int initialDelay, long period, TimeUnit unit) {
        if (isShutdown()) {
            return null;
        }
        return exec.scheduleAtFixedRate(() -> Platform.runLater(runnable), initialDelay, period, unit);
    }

    public Future<?> submit(Runnable runnable) {
        if (isShutdown()) {
            return null;
        }
        return exec.submit(runnable);
    }
}
