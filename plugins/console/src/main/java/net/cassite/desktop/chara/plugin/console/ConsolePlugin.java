// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.plugin.console;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.graphic.HZ;
import net.cassite.desktop.chara.graphic.UStage;
import net.cassite.desktop.chara.graphic.UStageConfig;
import net.cassite.desktop.chara.manager.FontManager;
import net.cassite.desktop.chara.plugin.Plugin;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.ResourceHandler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class ConsolePlugin implements Plugin {
    private static final double WIDTH = 850;
    private static final double HEIGHT = 540;
    private static final double PADDING = 6;
    private static final double FONT_SIZE = 12;
    private static final int CHECK_ELEMENTS_SIZE = 1000;
    private static final int MAX_ELEMENTS = 500;

    private static final Color STDERR = new Color(0xff / 255d, 0x6b / 255d, 0x68 / 255d, 1);

    private final UStage consoleStage;
    private final TextFlow textFlow;
    private final ScrollPane scrollPane;
    private final ByteArrayOutputStream stdout;
    private final ByteArrayOutputStream stderr;
    private Thread stdoutThread;
    private Thread stderrThread;
    private final AsciiEscapeParser parser = new AsciiEscapeParser();

    private final PrintStream originalStdout;
    private final PrintStream originalStderr;

    private volatile boolean stopped = true;
    private boolean needToFixScroll = false;

    public ConsolePlugin() {
        consoleStage = new UStage(new UStageConfig().setNoBorder(true).setResizable(true));
        consoleStage.setIcon(Global.charaDefaultIcon);
        consoleStage.setStageX(0);
        consoleStage.setStageY(0);
        consoleStage.setPaneWidth(WIDTH);
        consoleStage.setPaneHeight(HEIGHT);
        consoleStage.setTitle(ConsoleI18n.title.get()[0]);
        consoleStage.setOnCloseRequest(e -> {
            e.consume();
            stop();
        });

        textFlow = new TextFlow();
        textFlow.setPrefWidth(WIDTH);
        textFlow.setMinHeight(HEIGHT);
        textFlow.setBorder(Border.EMPTY);
        textFlow.setLineSpacing(4);
        textFlow.setBackground(new Background(new BackgroundFill(
            new Color(0x0c / 255d, 0x28 / 255d, 0x32 / 255d, 1),
            CornerRadii.EMPTY,
            Insets.EMPTY
        )));
        textFlow.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));

        Pane rootPane = consoleStage.getRootPane();
        scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(WIDTH);
        scrollPane.setPrefHeight(HEIGHT);
        scrollPane.setBorder(Border.EMPTY);
        scrollPane.setPadding(Insets.EMPTY);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(textFlow);
        rootPane.getChildren().add(scrollPane);

        // scroll to bottom
        scrollPane.vvalueProperty().addListener((ob, old, now) -> {
            if (needToFixScroll && old.doubleValue() == 1 && now.doubleValue() != 1) {
                scrollPane.setVvalue(1);
                needToFixScroll = false;
            }
        });
        // menu
        ContextMenu menu = new ContextMenu();
        MenuItem copyAllMenuItem = new MenuItem(ConsoleI18n.copyAllMenuItem.get()[0]);
        copyAllMenuItem.setOnAction(e -> this.copyTextContent());
        menu.getItems().addAll(copyAllMenuItem);
        // show/hide menu
        scrollPane.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                if (menu.isShowing()) {
                    menu.hide();
                }
                menu.show(scrollPane, e.getScreenX(), e.getScreenY());
                consoleStage.requestFocus();
            } else {
                if (menu.isShowing()) {
                    menu.hide();
                }
            }
        });
        // prepare stdout/err
        this.originalStdout = System.out;
        this.originalStderr = System.err;
        this.stdout = new ByteArrayOutputStream();
        this.stderr = new ByteArrayOutputStream();
        // handle resizing
        consoleStage.paneWidth().addListener((ob, old, now) -> {
            textFlow.setPrefWidth(now.doubleValue());
            scrollPane.setPrefWidth(now.doubleValue());
        });
        consoleStage.paneHeight().addListener((ob, old, now) -> {
            textFlow.setMinHeight(now.doubleValue());
            scrollPane.setPrefHeight(now.doubleValue());
        });
    }

    private void copyTextContent() {
        Platform.runLater(() -> {
            StringBuilder sb = new StringBuilder();
            for (var node : textFlow.getChildren()) {
                Text text = (Text) node;
                sb.append(text.getText());
            }
            String str = sb.toString();
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(str);
            clipboard.setContent(content);
        });
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
            processText(text, baos == stderr);
        }
    }

    private boolean firstTimeAdding = true;

    private void processText(String text, boolean stderr) {
        List<TextInfo> list;
        if (stderr) {
            list = Collections.singletonList(new TextInfo(text, STDERR));
        } else {
            list = parser.parse(text);
        }

        Platform.runLater(() -> {
            // add text
            for (var tup : list) {
                addText(tup);
            }
            // check and remove
            int size = textFlow.getChildren().size();
            if (size > CHECK_ELEMENTS_SIZE) {
                textFlow.getChildren().remove(0, size - MAX_ELEMENTS);
            }
            if (firstTimeAdding) {
                firstTimeAdding = false;
                scrollPane.setVvalue(1);
            }
        });
    }

    private Font textFont;

    private void addText(TextInfo info) {
        var foo = new Text(info.text);
        if (textFont == null) {
            textFont = new Font(FontManager.getMonospaceFontFamily(), FONT_SIZE);
        }
        foo.setFont(textFont);
        foo.setFill(info.color);
        textFlow.getChildren().add(foo);
        needToFixScroll = true;
    }

    @Override
    public String name() {
        return "console";
    }

    @Override
    public int version() {
        return 1000000;
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
