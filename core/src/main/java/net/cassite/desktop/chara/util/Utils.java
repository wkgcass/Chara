// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.manager.PluginManager;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import vproxybase.dns.Resolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipFile;

public class Utils {
    private Utils() {
    }

    /**
     * Double values difference is lower than the expected value
     *
     * @param a      value to be compared
     * @param b      value to be compared
     * @param expect the expected maximum difference
     * @return true if considered to be the same, false otherwise
     */
    public static boolean doubleEquals(double a, double b, double expect) {
        return Math.abs(a - b) <= expect;
    }

    /**
     * Build a sequence of names
     *
     * @param prefix        prefix
     * @param fromInclusive small index inclusive
     * @param toExclusive   big index exclusive
     * @param suffix        suffix
     * @return the constructed array
     */
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

    /**
     * Build a sequence of names but smaller the index is, greater the name index is.
     *
     * @param prefix        prefix
     * @param fromInclusive big index inclusive
     * @param toInclusive   small index inclusive
     * @param suffix        suffix
     * @return the constructed array
     */
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

    /**
     * Randomly check whether the event with provided probability can happen.
     *
     * @param probability p
     * @return true if happends, false otherwise
     */
    public static boolean random(double probability) {
        var rnd = ThreadLocalRandom.current();
        return rnd.nextDouble() <= probability;
    }

    private static final ConcurrentHashMap<String, Scheduled> delayedTasks = new ConcurrentHashMap<>();

    /**
     * Run delayed event. If currently an event with the same reason is being delayed, the old event will be canceled and new event takes place.
     * After the delay, the callback will be run on JavaFX thread.
     *
     * @param reason the reason to delay
     * @param delay  the delayed millis
     * @param r      callback function
     */
    public static void delay(String reason, int delay, Runnable r) {
        Scheduled old = delayedTasks.remove(reason);
        if (old != null) {
            old.cancel();
        }
        var now = ThreadUtils.get().scheduleFX(r, delay, TimeUnit.MILLISECONDS);
        delayedTasks.put(reason, now);
    }

    /**
     * Delay for some time and callback on JavaFX thread
     *
     * @param delay the delayed millis
     * @param r     callback function
     */
    public static void delayNoRecord(int delay, Runnable r) {
        ThreadUtils.get().scheduleFX(r, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Delay for some random time, maximum 1 second, the run the callback on JavaFX thread
     *
     * @param r callback function
     */
    public static void randomDelay(Runnable r) {
        var rnd = ThreadLocalRandom.current();
        ThreadUtils.get().scheduleFX(r, rnd.nextInt(1000), TimeUnit.MILLISECONDS);
    }

    /**
     * Delay for a very short time, then run the callback on JavaFX thread
     *
     * @param r callback function
     */
    public static void shortDelay(Runnable r) {
        ThreadUtils.get().scheduleFX(r, 50, TimeUnit.MILLISECONDS);
    }

    /**
     * Utility function for shutting down the program
     */
    public static void shutdownProgram() {
        if (StageUtils.closePrimaryStage()) {
            return;
        }
        // otherwise shutdown forcibly
        if (GlobalScreen.isNativeHookRegistered()) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ignore) {
            }
        }
        ConfigManager.saveNow();
        PluginManager.get().release();
        ThreadUtils.get().shutdownNow();
        try {
            Resolver.getDefault().stop();
        } catch (IOException ignore) {
        }
        Platform.runLater(Platform::exit);
    }

    /**
     * Expand the stage size because of the title and borders and margins.
     *
     * @param stage the stage to fix
     * @param style style of the stage
     */
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

    /**
     * Version number to string
     *
     * @param ver version number
     * @return version string
     */
    public static String verNum2Str(int ver) {
        return (ver / 1_000_000)
            + "." + ((ver / 1_000) % 1_000)
            + "." + (ver % 1_000);
    }

    private static Boolean isWindows;
    private static Boolean isMac;
    private static Boolean isLinux;

    /**
     * Current os is windows
     *
     * @return true if is windows, false otherwise
     */
    public static boolean isWindows() {
        determineOS();
        return isWindows;
    }

    /**
     * Current os is macos
     *
     * @return true if is macos, false otherwise
     */
    public static boolean isMac() {
        determineOS();
        return isMac;
    }

    /**
     * Current os is linux
     *
     * @return true if is linux, false otherwise
     */
    public static boolean isLinux() {
        determineOS();
        return isLinux;
    }

    private static void determineOS() {
        if (isWindows != null && isMac != null && isLinux != null) {
            return;
        }
        String os = System.getProperty("os.name");
        if (os == null) {
            isWindows = false;
            isMac = false;
            isLinux = false;
            return;
        }
        os = os.toLowerCase();
        if (os.contains("mac")) {
            isMac = true;
            isWindows = false;
            isLinux = false;
        } else if (os.contains("win")) {
            isMac = false;
            isWindows = true;
            isLinux = false;
        } else if (os.contains("nux")) {
            isMac = false;
            isWindows = false;
            isLinux = true;
        } else {
            isMac = false;
            isWindows = false;
            isLinux = false;
        }
    }

    public static Class<?> loadClassFromZipFile(ZipFile zipFile, String prefix, String classname) throws Exception {
        // try to directly load class
        try {
            Class<?> ret = Class.forName(classname);
            assert Logger.debug("using classpath class, no need to load jar from zip file");
            return ret; // return if init succeeded
        } catch (ClassNotFoundException ignore) {
            // only catch ClassNotFound here
            // other exception will be thrown
        }
        // try to load jar file
        var entries = zipFile.entries();
        List<URL> tempFileUrls = new LinkedList<>();
        int tempFileCount = 0;
        while (entries.hasMoreElements()) {
            var entry = entries.nextElement();
            if (entry.getName().startsWith("code/") && entry.getName().endsWith(".jar") && !entry.isDirectory()) {
                Logger.info("releasing jar: " + entry.getName());

                InputStream inputStream = zipFile.getInputStream(entry);
                File tempFile = File.createTempFile("release-" + prefix + "-" + (tempFileCount++), ".jar");
                tempFile.deleteOnExit();

                FileOutputStream fos = new FileOutputStream(tempFile);

                byte[] buf = new byte[1024 * 1024];
                int n;
                while ((n = inputStream.read(buf)) >= 0) {
                    fos.write(buf, 0, n);
                }
                fos.flush();
                try {
                    fos.close();
                } catch (Exception ignore) {
                }
                try {
                    inputStream.close();
                } catch (Exception ignore) {
                }

                tempFileUrls.add(tempFile.toURI().toURL());
            }
        }
        if (tempFileUrls.isEmpty()) {
            throw new Exception("no code found in zip file");
        }
        URL[] urlArray = new URL[tempFileUrls.size()];
        tempFileUrls.toArray(urlArray);

        // do load
        URLClassLoader urlClassLoader = new URLClassLoader(urlArray);
        return urlClassLoader.loadClass(classname);
    }

    public static InputStream getEntryFromZipFile(String zipFilePath, String name) {
        ZipFile file;
        try {
            file = new ZipFile(zipFilePath);
        } catch (IOException e) {
            Logger.fatal("failed to open model file", e);
            return null;
        }
        var entry = file.getEntry(name);
        if (entry == null) {
            assert Logger.debug("entry " + name + " not found");
            try {
                file.close();
            } catch (IOException ignore) {
            }
            return null;
        }
        try {
            return new ZipFileInputStreamDelegate(file, file.getInputStream(entry));
        } catch (IOException e) {
            Logger.fatal("failed to get input stream from entry " + name, e);
            return null;
        }
    }

    public static InputStream getEntryFromZipFile(ZipFile zipFile, String name) {
        var entry = zipFile.getEntry(name);
        if (entry == null) {
            assert Logger.debug("entry " + name + " not found");
            return null;
        }
        try {
            return zipFile.getInputStream(entry);
        } catch (IOException e) {
            Logger.fatal("failed to get input stream from entry " + name, e);
            return null;
        }
    }
}
