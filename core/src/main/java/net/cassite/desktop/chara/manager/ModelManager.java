// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.manager;

import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsSelector;
import net.cassite.desktop.chara.model.Model;
import net.cassite.desktop.chara.model.ModelInitConfig;
import net.cassite.desktop.chara.util.*;
import vjson.JSON;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModelManager {
    public static Model load(String modelFile) {
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(modelFile);
        } catch (IOException e) {
            Logger.fatal("open model file failed", e);
            return null;
        }
        ZipEntry modelJsonEntry = zipFile.getEntry("model.json");
        if (modelJsonEntry == null) {
            Logger.fatal("invalid model file, model configuration not found in " + modelFile);
            return null;
        }
        JSON.Instance<?> modelJsonInst = readJson(zipFile, modelJsonEntry);
        if (modelJsonInst == null) {
            return null;
        }

        String name;
        int version;
        int minVer;
        int maxVer;
        String modelClass;
        try {
            JSON.Object o = (JSON.Object) modelJsonInst;
            name = o.getString("name");
            version = o.getInt("version");
            minVer = o.getInt("compatibleMinCodeVersion");
            maxVer = o.getInt("compatibleMaxCodeVersion");
            modelClass = o.getString("modelClass");
        } catch (RuntimeException e) {
            Logger.fatal("invalid model configuration format: " + modelJsonEntry.getName(), e);
            return null;
        }
        String minVerStr = (minVer / 1_000_000) + "." + ((minVer / 1_000) % 1_000) + "." + (minVer % 1000);
        String maxVerStr = (maxVer / 1_000_000) + "." + ((maxVer / 1_000) % 1_000) + "." + (maxVer % 1000);

        // check version
        if (minVer > Consts.VERSION_NUM) {
            Logger.fatal("the model requires higher code version: " + minVerStr);
            return null;
        }
        if (maxVer != -1 && maxVer < Consts.VERSION_NUM) {
            Logger.fatal("the model requires lower code version: " + maxVerStr);
            return null;
        }

        // init code
        Model selected;
        try {
            Class<?> cls = Utils.loadClassFromZipFile(zipFile, "model-" + name, modelClass);
            selected = (Model) cls.getConstructor().newInstance();
        } catch (Exception e) {
            Logger.fatal("init code failed: " + e.getMessage(), e);
            return null;
        }

        Logger.info("getting model " + selected.name());

        // check config name and version
        if (!name.equals(selected.name())) {
            Logger.fatal("name in the model config " + name +
                " is not the same as model.name() in code " + selected.name());
            return null;
        }
        if (version != selected.version()) {
            Logger.fatal("version in the model config " + Utils.verNum2Str(version) +
                " is not the same as model.version() in code " + Utils.verNum2Str(selected.version()));
            return null;
        }

        // prepare init config
        ModelInitConfig modelInitConfig = new ModelInitConfig();

        // get words
        var words = new HashMap<String, WordsSelector>();
        var entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            var entry = entries.nextElement();
            var prefix = "words/";
            if (!entry.isDirectory() && entry.getName().startsWith(prefix)) {
                var wordsKey = entry.getName().substring(prefix.length());
                WordsSelector wordsSelector;
                try {
                    wordsSelector = getWords(zipFile, entry);
                } catch (Exception e) {
                    Logger.fatal("getting words from model failed: " + entry.getName(), e);
                    return null;
                }
                if (wordsSelector == null) {
                    Logger.warn("no words retrieved from " + entry.getName());
                } else {
                    words.put(wordsKey, wordsSelector);
                }
            }
        }
        modelInitConfig.setInteractionWordsSelectors(words);

        // get values
        var intMap = new HashMap<String, Integer>();
        var doubleMap = new HashMap<String, Double>();
        var intRecMap = new HashMap<String, Rec>();
        entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            var entry = entries.nextElement();
            var prefix = "values/";
            if (!entry.isDirectory() && entry.getName().startsWith(prefix) && entry.getName().endsWith(".json")) {
                var valuesJsonInst = readJson(zipFile, entry);
                if (valuesJsonInst == null) {
                    return null;
                }
                try {
                    var o = (JSON.Object) valuesJsonInst;
                    if (o.containsKey("integers")) {
                        var integers = o.getObject("integers");
                        for (String key : integers.keySet()) {
                            intMap.put(key, integers.getInt(key));
                        }
                    }
                    if (o.containsKey("doubles")) {
                        var doubles = o.getObject("doubles");
                        for (String key : doubles.keySet()) {
                            doubleMap.put(key, doubles.getDouble(key));
                        }
                    }
                    if (o.containsKey("integerRectangles")) {
                        var integerRec = o.getObject("integerRectangles");
                        for (String key : integerRec.keySet()) {
                            var arr = integerRec.getArray(key);
                            Rec rec = new Rec(arr.getInt(0), arr.getInt(1), arr.getInt(2), arr.getInt(3));
                            intRecMap.put(key, rec);
                        }
                    }
                } catch (Exception e) {
                    Logger.fatal("invalid model configuration format: " + entry.getName(), e);
                    return null;
                }
            }
        }
        modelInitConfig.setIntegerValuesMap(intMap);
        modelInitConfig.setDoubleValuesMap(doubleMap);
        modelInitConfig.setIntegerRectanglesMap(intRecMap);

        // init
        selected.init(modelInitConfig);

        // additional init
        try {
            selected.customizeInit(zipFile);
        } catch (Exception e) {
            Logger.fatal("model implementation raise an exception when initiating", e);
            return null;
        }

        // done
        try {
            zipFile.close();
        } catch (IOException ignore) {
        }
        return selected;
    }

    private static JSON.Instance<?> readJson(ZipFile zipFile, ZipEntry entry) {
        InputStream inputStream;
        try {
            inputStream = zipFile.getInputStream(entry);
        } catch (IOException e) {
            Logger.fatal("get input stream from model configuration failed: " + entry.getName(), e);
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            inputStream.close();
        } catch (IOException e) {
            Logger.fatal("reading model configuration failed: " + entry.getName(), e);
            return null;
        }

        //noinspection rawtypes
        JSON.Instance inst;
        try {
            inst = JSON.parse(sb.toString());
        } catch (Exception e) {
            Logger.fatal("invalid model configuration format: " + entry.getName(), e);
            return null;
        }
        return inst;
    }

    private static WordsSelector getWords(ZipFile zipFile, ZipEntry wordsEntry) throws Exception {
        var inputStream = zipFile.getInputStream(wordsEntry);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        List<Words> wordsList = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        int state = 0; // 0: normal ==(met -----BEGIN WORDS-----)==> 1: reading ==(met -----END WORDS-----)==> 0
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (state == 0) {
                if (line.equals("-----BEGIN WORDS-----")) {
                    state = 1;
                }
            } else {
                if (line.equals("-----END WORDS-----")) {
                    Words words = getWords(sb.toString());
                    if (words != null) {
                        wordsList.add(words);
                    }
                    sb.delete(0, sb.length());
                    state = 0;
                } else {
                    sb.append(line).append("\n");
                }
            }
        }
        if (state != 0) {
            throw new Exception("words content not ending correctly, leaving buffer: " + sb);
        }

        if (wordsList.isEmpty()) {
            assert Logger.debug("load nothing from words config");
            return null;
        }
        assert Logger.debug("load " + wordsList.size() + " from words config");
        Words[] wordsArray = new Words[wordsList.size()];
        wordsList.toArray(wordsArray);

        inputStream.close();

        return new WordsSelector(wordsArray);
    }

    private static Words getWords(String linesStr) {
        String[] lines = linesStr.split("\n");

        int state = 0; // 0: default ==(met -----BEGIN ${lang}-----)==> 1: reading ==(met -----END ${lang}-----)==> 0
        String lang = null;
        List<String> currentWordsList = new LinkedList<>();
        Map<String, String[]> records = new HashMap<>();
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            if (state == 0) {
                if (line.startsWith("-----BEGIN ") && line.endsWith("-----")) {
                    lang = line.substring(
                        "-----BEGIN ".length(),
                        line.length() - "-----".length()
                    );
                    state = 1;
                }
            } else {
                //noinspection ConstantConditions
                assert lang != null;
                if (line.equals("-----END " + lang + "-----")) {
                    if (currentWordsList.isEmpty()) {
                        assert Logger.debug("discard a words-entry because no content");
                    } else {
                        String[] strs = new String[currentWordsList.size()];
                        currentWordsList.toArray(strs);
                        records.put(lang, strs);
                    }
                    currentWordsList.clear();
                    lang = null;
                    state = 0;
                } else {
                    currentWordsList.add(line);
                }
            }
        }

        if (state != 0) {
            Logger.fatal("words content not ending correctly, leaving ls buffer: " + currentWordsList);
            return null;
        }

        return Words.fromMap(records);
    }

    public static InputStream getEntryFromModel(String name) {
        ZipFile file;
        try {
            file = new ZipFile(Global.modelFilePath);
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
}
