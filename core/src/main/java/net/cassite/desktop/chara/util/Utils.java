// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.control.GlobalMouse;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.manager.PluginManager;
import vproxybase.dns.Resolver;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
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
        if (GlobalMouse.isRunning()) {
            GlobalMouse.disable();
        }
        ConfigManager.saveNow();
        Platform.runLater(() -> PluginManager.get().release());
        ThreadUtils.get().shutdownNow();
        try {
            Resolver.getDefault().stop();
        } catch (IOException ignore) {
        }
        Platform.runLater(Platform::exit);
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

    /**
     * Check whether the current platform scales coordinates.<br>
     * The jnativehook provides different coordinates from jfx on some platforms when screen is scaled.
     *
     * @return true if the stage is scaled on the current platform, false otherwise
     */
    public static boolean isCoordinatesScaled() {
        Boolean coordinatesScaled = ConfigManager.get().getCoordinatesScaled();
        if (coordinatesScaled == null) {
            coordinatesScaled = Utils.isCoordinatesScaled0();
        }
        return coordinatesScaled;
    }

    private static boolean isCoordinatesScaled0() {
        return isWindows();
    }

    /**
     * Load all jars matching the prefix in the zipFile and return the specified <code>Class</code> object.
     *
     * @param zipFile   zip file
     * @param prefix    jar entry prefix
     * @param classname name of the class to load
     * @return <code>Class</code>
     * @throws Exception any exception when loading
     */
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

    /**
     * Open <code>ZipFile</code> and retrieve inputStream from it.<br>
     * When closing the inputStream, the opened zipfile will be closed as well.
     *
     * @param zipFilePath path to the zip file
     * @param name        full name of the entry
     * @return inputStream and closing it will close zipfile as well
     */
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

    /**
     * Retrieve inputStream from <code>ZipFile</code>
     *
     * @param zipFile zip file
     * @param name    full name of the entry
     * @return inputStream
     */
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

    /**
     * Set icon on a stage
     *
     * @param stage stage
     * @param icon  icon
     */
    public static void setIcon(Stage stage, Image icon) {
        if (icon == null) {
            return; // do not set if not found
        }
        stage.getIcons().add(icon);
        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                var image = SwingFXUtils.fromFXImage(icon, null);
                taskbar.setIconImage(image);
            }
        }
    }

    /**
     * Get screen of the given coordinate
     *
     * @param x x
     * @param y y
     * @return screen of the coordinate or null if out of all screens
     */
    public static Screen getScreen(double x, double y) {
        var screens = Screen.getScreens();
        for (var s : screens) {
            var bounds = s.getBounds();
            if (bounds.contains(x, y)) {
                return s;
            }
        }
        return null;
    }
}
