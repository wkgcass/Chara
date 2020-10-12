// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import javafx.application.Platform;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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

    public void schedule(Runnable runnable, int delay, TimeUnit unit) {
        exec.schedule(runnable, delay, unit);
    }

    public ScheduledFuture<?> scheduleFX(Runnable runnable, int delay, TimeUnit unit) {
        return exec.schedule(() -> Platform.runLater(runnable), delay, unit);
    }

    public void scheduleAtFixedRate(Runnable runnable, int initialDelay, int period, TimeUnit unit) {
        exec.scheduleAtFixedRate(runnable, initialDelay, period, unit);
    }

    public void scheduleAtFixedRateFX(Runnable runnable, int initialDelay, long period, TimeUnit unit) {
        exec.scheduleAtFixedRate(() -> Platform.runLater(runnable), initialDelay, period, unit);
    }

    public void submit(Runnable runnable) {
        exec.submit(runnable);
    }
}
