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
        nonblockingThreads = new EventLoopGroup("nonblocking-threads");
        for (int i = 0; i < 1; ++i) { // we only add one thread for now
            try {
                nonblockingThreads.add("nonblocking-thread-" + i);
            } catch (AlreadyExistException | IOException | ClosedException e) {
                Logger.shouldNotHappen("adding nonblocking thread failed", e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Retrieve the <code>ThreadUtils</code> instance.
     *
     * @return <code>ThreadUtils</code> instance
     */
    public static ThreadUtils get() {
        return instance;
    }

    private volatile boolean isShutdown = false;

    /**
     * Stop all running threads
     */
    public void shutdownNow() {
        isShutdown = true;
        nonblockingThreads.close();
    }

    /**
     * Check whether the instance is shutdown
     *
     * @return true if shutdown, false otherwise
     */
    public boolean isShutdown() {
        return isShutdown;
    }

    /**
     * Retrieve an event loop<br>
     * There might be multiple event loops in the instance, so the retrieved loop may be different between different calls.
     *
     * @return event loop
     */
    public NetEventLoop getLoop() {
        return nonblockingThreads.next();
    }

    /**
     * Execute the runnable after specific delay
     *
     * @param runnable task to run
     * @param delay    time to delay
     * @param unit     the unit of <code>delay</code>
     * @return an object for you to manage the task
     */
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

    /**
     * Execute the runnable on <code>JavaFX</code> thread after specific delay
     *
     * @param runnable task to run
     * @param delay    time to delay
     * @param unit     the unit of <code>delay</code>
     * @return an object for you to manage the task
     */
    public Scheduled scheduleFX(Runnable runnable, int delay, TimeUnit unit) {
        return schedule(() -> Platform.runLater(runnable), delay, unit);
    }

    /**
     * Periodically execute the runnable
     *
     * @param runnable     task to run
     * @param initialDelay delay time before the task is ran for the first time
     * @param period       interval between two tasks
     * @param unit         the unit of <code>initialDelay</code> and <code>period</code>
     * @return an object for you to manage the task
     */
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

    /**
     * Periodically execute the runnable on <code>JavaFX</code> thread
     *
     * @param runnable     task to run
     * @param initialDelay delay time before the task is ran for the first time
     * @param period       interval between two tasks
     * @param unit         the unit of <code>initialDelay</code> and <code>period</code>
     * @return an object for you to manage the task
     */
    public Scheduled scheduleAtFixedRateFX(Runnable runnable, int initialDelay, int period, TimeUnit unit) {
        return scheduleAtFixedRate(() -> Platform.runLater(runnable), initialDelay, period, unit);
    }

    /**
     * Execute the runnable on event loop thread
     *
     * @param runnable task to run
     */
    public void submit(Runnable runnable) {
        if (isShutdown()) {
            return;
        }
        nonblockingThreads.next().getSelectorEventLoop().runOnLoop(runnable);
    }

    /**
     * Run on JavaFX thread.<br>
     * If current thread is JavaFX thread, the runnable is directly called.<br>
     * Otherwise {@link Platform#runLater(Runnable)} will be called.
     *
     * @param runnable task to run
     */
    public void runOnFX(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }
}
