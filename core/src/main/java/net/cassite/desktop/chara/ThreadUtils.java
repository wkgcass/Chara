// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import javafx.application.Platform;
import net.cassite.desktop.chara.util.Scheduled;
import vproxybase.component.elgroup.EventLoopGroup;
import vproxybase.connection.NetEventLoop;
import vproxybase.util.Logger;
import vproxybase.util.exception.AlreadyExistException;
import vproxybase.util.exception.ClosedException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {
    private static final ThreadUtils instance = new ThreadUtils();

    private final EventLoopGroup nonblockingThreads;

    private ThreadUtils() {
        int cores = Runtime.getRuntime().availableProcessors();
        nonblockingThreads = new EventLoopGroup("nonblocking-threads");
        for (int i = 0; i < cores; ++i) {
            try {
                nonblockingThreads.add("nonblocking-thread-" + i);
            } catch (AlreadyExistException | IOException | ClosedException e) {
                Logger.shouldNotHappen("adding nonblocking thread failed", e);
                throw new RuntimeException(e);
            }
        }
    }

    public static ThreadUtils get() {
        return instance;
    }

    private volatile boolean isShutdown = false;

    public void shutdownNow() {
        isShutdown = true;
        nonblockingThreads.close();
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    public NetEventLoop getLoop() {
        return nonblockingThreads.next();
    }

    public Scheduled schedule(Runnable runnable, int delay, TimeUnit unit) {
        if (isShutdown()) {
            return null;
        }
        int millis = (int) TimeUnit.MILLISECONDS.convert(delay, unit);
        return new Scheduled(
            nonblockingThreads.next().getSelectorEventLoop(),
            millis,
            -1,
            runnable
        );
    }

    public Scheduled scheduleFX(Runnable runnable, int delay, TimeUnit unit) {
        return schedule(() -> Platform.runLater(runnable), delay, unit);
    }

    public Scheduled scheduleAtFixedRate(Runnable runnable, int initialDelay, int period, TimeUnit unit) {
        if (isShutdown()) {
            return null;
        }
        int initialMillis = (int) TimeUnit.MILLISECONDS.convert(initialDelay, unit);
        int periodMillis = (int) TimeUnit.MILLISECONDS.convert(period, unit);
        return new Scheduled(
            nonblockingThreads.next().getSelectorEventLoop(),
            initialMillis,
            periodMillis,
            runnable
        );
    }

    public Scheduled scheduleAtFixedRateFX(Runnable runnable, int initialDelay, int period, TimeUnit unit) {
        return scheduleAtFixedRate(() -> Platform.runLater(runnable), initialDelay, period, unit);
    }

    public void submit(Runnable runnable) {
        if (isShutdown()) {
            return;
        }
        nonblockingThreads.next().getSelectorEventLoop().runOnLoop(runnable);
    }
}
