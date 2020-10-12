// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.cassite.desktop.chara.ThreadUtils;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Utils {
    private Utils() {
    }

    private static final Random rnd = ThreadLocalRandom.current();

    public static boolean doubleEquals(double a, double b, double expect) {
        return Math.abs(a - b) <= expect;
    }

    public static String[] buildSeqNames(String prefix, int fromInclusive, int toExclusive, String suffix) {
        String[] animationImages = new String[toExclusive - fromInclusive];
        for (int i = 0; i < animationImages.length; ++i) {
            String n = "" + (i + fromInclusive);
            if (n.length() < 2) {
                n = "0" + n;
            }
            if (n.length() < 3) {
                n = "0" + n;
            }
            animationImages[i] = prefix + n + suffix;
        }
        return animationImages;
    }

    public static String[] buildSeqNamesReverse(String prefix, int fromInclusive, int toInclusive, String suffix) {
        String[] animationImages = new String[fromInclusive - toInclusive + 1];
        for (int i = 0; i < animationImages.length; ++i) {
            String n = "" + (fromInclusive - i);
            if (n.length() < 2) {
                n = "0" + n;
            }
            if (n.length() < 3) {
                n = "0" + n;
            }
            animationImages[i] = prefix + n + suffix;
        }
        return animationImages;
    }

    public static boolean random(double probability) {
        return rnd.nextDouble() <= probability;
    }

    private static final ConcurrentHashMap<String, ScheduledFuture<?>> delayedTasks = new ConcurrentHashMap<>();

    public static void delay(String reason, int delay, Runnable r) {
        ScheduledFuture<?> old = delayedTasks.remove(reason);
        if (old != null) {
            old.cancel(true);
        }
        var now = ThreadUtils.get().scheduleFX(r, delay, TimeUnit.MILLISECONDS);
        delayedTasks.put(reason, now);
    }

    public static void delayNoRecord(int delay, Runnable r) {
        ThreadUtils.get().scheduleFX(r, delay, TimeUnit.MILLISECONDS);
    }

    public static void randomDelay(Runnable r) {
        ThreadUtils.get().scheduleFX(r, rnd.nextInt(1000), TimeUnit.MILLISECONDS);
    }

    public static void cancelAllTimers() {
        delayedTasks.values().forEach(f -> f.cancel(true));
        delayedTasks.clear();
    }

    public static void shutdownProgram() {
        if (GlobalScreen.isNativeHookRegistered()) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ignore) {
            }
        }
        ThreadUtils.get().shutdownNow();
        Platform.runLater(Platform::exit);
    }

    public static void shortDelay(Runnable r) {
        ThreadUtils.get().scheduleFX(r, 50, TimeUnit.MILLISECONDS);
    }

    public static void fixStageSize(Stage stage, StageStyle style) {
        // build a stage to calculate dW and dH
        Stage s = new Stage();
        s.initStyle(style);
        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        s.setScene(scene);
        s.setOpacity(0);
        s.show();

        double dW = s.getWidth() - scene.getWidth();
        double dH = s.getHeight() - scene.getHeight();

        s.hide();

        stage.setWidth(stage.getWidth() + dW);
        stage.setHeight(stage.getHeight() + dH);
    }
}
