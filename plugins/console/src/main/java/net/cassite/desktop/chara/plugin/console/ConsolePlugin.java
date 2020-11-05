// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.console;

import javafx.application.Platform;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.graphic.ConsoleStage;
import net.cassite.desktop.chara.graphic.HZ;
import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.ResourceHandler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class ConsolePlugin implements Plugin {
    private static final int MAX_ELEMENTS = 1000;

    private final ConsoleStage consoleStage;
    private final ByteArrayOutputStream stdout;
    private final ByteArrayOutputStream stderr;
    private Thread stdoutThread;
    private Thread stderrThread;

    private final PrintStream originalStdout;
    private final PrintStream originalStderr;

    private volatile boolean stopped = true;

    public ConsolePlugin() {
        consoleStage = new ConsoleStage(MAX_ELEMENTS);
        consoleStage.setTitle(ConsoleI18n.title.get()[0]);
        consoleStage.setOnCloseRequest(e -> {
            e.consume();
            stop();
        });
        // prepare stdout/err
        this.originalStdout = System.out;
        this.originalStderr = System.err;
        this.stdout = new ByteArrayOutputStream();
        this.stderr = new ByteArrayOutputStream();
    }

    private void readLog(ByteArrayOutputStream baos) {
        while (true) {
            if (stopped) {
                return;
            }
            try {
                //noinspection BusyWait
                Thread.sleep(HZ.UNIT * 10);
            } catch (InterruptedException ignore) {
            }
            int size = baos.size();
            if (size == 0) {
                continue;
            }
            byte[] bytes = baos.toByteArray();
            baos.reset();
            String text = new String(bytes, StandardCharsets.UTF_8);
            consoleStage.addText(text, baos == stderr);
        }
    }

    @Override
    public String name() {
        return "console";
    }

    @Override
    public int version() {
        return 1000000; // _THE_VERSION_
    }

    @Override
    public double priority() {
        return Double.MAX_VALUE;
    }

    @Override
    public List<ResourceHandler> resourceHandlers() {
        return Collections.emptyList();
    }

    @Override
    public void launch() {
        start();
    }

    @Override
    public void clicked() {
        if (consoleStage.isShowing()) {
            stop();
        } else {
            start();
        }
    }

    private void start() {
        if (!stopped) {
            return;
        }
        stopped = false;
        Logger.info("switching stdout/stderr");
        System.setOut(new PrintStream(this.stdout));
        System.setErr(new PrintStream(this.stderr));
        Logger.info("switched stdout/stderr");
        stdout.reset();
        stdoutThread = new Thread(() -> readLog(stdout), "console-plugin-stdout");
        stdoutThread.start();
        stderr.reset();
        stderrThread = new Thread(() -> readLog(stderr), "console-plugin-stderr");
        stderrThread.start();

        ThreadUtils.get().runOnFX(() -> {
            consoleStage.show();
            consoleStage.setAlwaysOnTop(true);
            Platform.runLater(() -> consoleStage.setAlwaysOnTop(false));
        });
    }

    private void stop() {
        if (stopped) {
            return;
        }
        stopped = true;
        ThreadUtils.get().runOnFX(consoleStage::hide);
        stdoutThread.interrupt();
        stderrThread.interrupt();
        System.setOut(originalStdout);
        System.setErr(originalStderr);
        Logger.info("restored stdout/stderr");
    }

    @Override
    public void release() {
        stop();
    }

    @Override
    public String about() {
        return "" +
            "author: wkgcass\n" +
            "code license: GPLv2 with classpath exception" +
            "";
    }
}
