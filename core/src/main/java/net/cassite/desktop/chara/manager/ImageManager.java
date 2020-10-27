// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.manager;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.util.Consts;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.XImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ImageManager {
    private ImageManager() {
    }

    private static final Map<String, XImage> cache = new HashMap<>();

    /**
     * Get image from cache or load image from model.
     *
     * @param name entry name of the image, it is automatically prepended with the model name
     * @return the minified image and corresponding offset coordinates
     */
    public static XImage load(String name) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        }
        XImage tryFileCacheImage = tryFileCache(name);
        if (tryFileCacheImage != null) {
            cache.put(name, tryFileCacheImage);
            return tryFileCacheImage;
        }

        final String modelFile = Global.modelFilePath;
        assert modelFile != null;

        ZipFile zipFile;
        try {
            zipFile = new ZipFile(modelFile);
        } catch (IOException e) {
            Logger.fatal("open model file " + modelFile + " failed", e);
            return null;
        }
        ZipEntry entry = zipFile.getEntry(Global.model.name() + "/" + name);
        if (entry == null) {
            Logger.fatal("entry " + name + " not found, please check and upgrade the model file version");
            return null;
        }
        long timeOfEntry = entry.getTime();

        InputStream inputStream;
        try {
            inputStream = zipFile.getInputStream(entry);
        } catch (IOException e) {
            Logger.fatal("read entry " + name + " failed", e);
            return null;
        }
        Image image = new Image(inputStream);

        try {
            inputStream.close();
            zipFile.close();
        } catch (IOException e) {
            Logger.error("closing open files failed", e);
        }

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        var reader = image.getPixelReader();
        int startX = width;
        int startY = height;
        int endX = 0;
        int endY = 0;

        // scan the image and remove argb = 0 pixels
        // search for x
        // 1. foreach line: find the first pixel whose argb is not 0
        // 2.               find the last pixel whose argb is not 0
        // 3.               the first pixel must be kept, so check and record in startX
        // 4.               the last pixel must be kept, so check and record in endX (exclusive, so +1)
        for (int y = 0; y < height; ++y) {
            int first = width;
            int last = 0;
            for (int x = 0; x < width; ++x) {
                if (reader.getArgb(x, y) != 0) {
                    if (first == width) {
                        first = x;
                    }
                    last = x;
                }
            }
            if (first < startX) {
                startX = first;
            }
            if (last >= endX) {
                endX = last + 1;
            }
        }
        // search for y
        // 1. foreach column: find the first pixel whose argb is not 0
        // 2.                 find the last pixel whose argb is not 0
        // 3.                 the first pixel must be kept, so check and record in startY
        // 4.                 the last pixel must be kept, so check and record in endY (exclusive, so +1)
        for (int x = 0; x < width; ++x) {
            int first = height;
            int last = 0;
            for (int y = 0; y < height; ++y) {
                if (reader.getArgb(x, y) != 0) {
                    if (first == height) {
                        first = y;
                    }
                    last = y;
                }
            }
            if (first < startY) {
                startY = first;
            }
            if (last >= endY) {
                endY = last;
            }
        }

        int newWidth = endX - startX;
        int newHeight = endY - startY;
        WritableImage writableImage = new WritableImage(newWidth, newHeight);
        writableImage.getPixelWriter().setPixels(0, 0, newWidth, newHeight, reader, startX, startY);

        Logger.info("loading image: " + name + " from [" + width + "," + height + "] to [" + newWidth + "," + newHeight + "]+(" + startX + "," + startY + ")");

        XImage xImage = new XImage(timeOfEntry, startX, startY, writableImage);
        cache.put(name, xImage);
        writeCacheFile(name, xImage);
        return xImage;
    }

    private static XImage tryFileCache(String name) {
        File cacheFile = new File(System.getProperty("user.home") + "/" + Consts.CACHE_DIR_NAME + "/" + Global.model.name() + "/" + name);
        if (!cacheFile.isDirectory()) {
            return null;
        }

        File imageFile = new File(cacheFile.getAbsolutePath() + "/image.png");
        File metaFile = new File(cacheFile.getAbsolutePath() + "/meta");

        if (!imageFile.isFile()) {
            assert Logger.debug(name + " dir exists but image file not found");
            return null;
        }
        if (!metaFile.isFile()) {
            assert Logger.debug(name + " dir exists but meta file not found");
            return null;
        }

        String metaStr;
        try {
            metaStr = Files.readString(metaFile.toPath());
        } catch (IOException e) {
            Logger.error("reading " + metaFile + " failed", e);
            return null;
        }
        String[] meta = metaStr.split("\n");
        if (meta.length < 3) {
            Logger.error(name + " has meta file but content is wrong:\n" + metaStr);
            return null;
        }
        long lastTime;
        try {
            lastTime = Long.parseLong(meta[0].trim());
        } catch (NumberFormatException e) {
            Logger.error(name + " has meta file but first line is not timestamp:\n" + metaStr);
            return null;
        }
        int x;
        try {
            x = Integer.parseInt(meta[1].trim());
        } catch (NumberFormatException e) {
            Logger.error(name + " has meta file but second line is not x offset:\n" + metaStr);
            return null;
        }
        int y;
        try {
            y = Integer.parseInt(meta[2].trim());
        } catch (NumberFormatException e) {
            Logger.error(name + " has meta file but third line is not y offset:\n" + metaStr);
            return null;
        }

        String modelFile = Global.modelFilePath;
        assert modelFile != null;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(modelFile);
        } catch (IOException e) {
            Logger.error("open model file failed", e);
            return null;
        }
        ZipEntry entry = zipFile.getEntry(Global.model.name() + "/" + name);
        if (entry == null) {
            Logger.error("cannot find entry " + name + " in model");
            return null;
        }

        long expectedTime = entry.getTime();

        try {
            zipFile.close();
        } catch (IOException e) {
            Logger.error("closing model open file failed", e);
        }

        if (expectedTime != lastTime) {
            Logger.info(name + " cache expired");
            deleteDir(cacheFile);
            return null;
        }

        Logger.info("loading from cache: " + name);
        try {
            return new XImage(lastTime, x, y, new Image(imageFile.toURI().toURL().toString()));
        } catch (MalformedURLException e) {
            Logger.shouldNotReachHere(e);
            return null;
        }
    }

    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

    private static void writeCacheFile(String name, XImage image) {
        File cacheFile = new File(System.getProperty("user.home") + "/" + Consts.CACHE_DIR_NAME + "/" + Global.model.name() + "/" + name);
        if (cacheFile.exists()) {
            if (!cacheFile.isDirectory()) {
                Logger.error(cacheFile.getAbsolutePath() + " exists but is not directory");
                return;
            }
        } else if (!cacheFile.mkdirs()) {
            Logger.error(cacheFile.getAbsolutePath() + " mkdirs failed");
            return;
        }

        File metaFile = new File(cacheFile.getAbsolutePath() + "/meta");
        try {
            Files.write(metaFile.toPath(), ("" +
                "" + image.time + "\n" +
                "" + image.x + "\n" +
                "" + image.y + "\n" +
                "").getBytes());
        } catch (IOException e) {
            Logger.error("writing " + metaFile + " failed", e);
            return;
        }

        File imageFile = new File(cacheFile.getAbsolutePath() + "/image.png");
        BufferedImage bi = SwingFXUtils.fromFXImage(image.image, null);
        try {
            ImageIO.write(bi, "PNG", imageFile);
        } catch (IOException e) {
            Logger.error("writing " + imageFile + " failed", e);
            return;
        }
        Logger.info("caching " + name + " done");
    }
}
