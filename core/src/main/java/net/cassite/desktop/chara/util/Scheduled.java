// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import vproxybase.selector.PeriodicEvent;
import vproxybase.selector.SelectorEventLoop;
import vproxybase.selector.TimerEvent;

public class Scheduled {
    private TimerEvent initialDelayEvent;
    private PeriodicEvent periodicEvent;

    public Scheduled(SelectorEventLoop loop, int initialDelay, int duration, Runnable runnable) {
        initialDelayEvent = loop.delay(initialDelay, () -> {
            initialDelayEvent = null;
            if (duration == -1) {
                runnable.run();
            } else {
                periodicEvent = loop.period(duration, runnable);
            }
        });
    }

    public void cancel() {
        if (initialDelayEvent != null) {
            initialDelayEvent.cancel();
        }
        if (periodicEvent != null) {
            periodicEvent.cancel();
        }
    }
}
