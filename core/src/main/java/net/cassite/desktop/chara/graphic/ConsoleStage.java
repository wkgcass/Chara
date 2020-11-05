// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

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
import net.cassite.desktop.chara.i18n.I18nConsts;
import net.cassite.desktop.chara.manager.FontManager;

import java.util.Collections;
import java.util.List;

/**
 * A stage used as a console to display text logs.
 */
public class ConsoleStage extends UStage {
    private static final double INITIAL_WIDTH = 850;
    private static final double INITIAL_HEIGHT = 540;
    private static final double PADDING = 6;
    private static final double FONT_SIZE = 12;

    private static final Color STDERR = new Color(0xff / 255d, 0x6b / 255d, 0x68 / 255d, 1);

    private final int maxElements;
    private final TextFlow textFlow;
    private final ScrollPane scrollPane;
    private final AsciiEscapeParser parser = new AsciiEscapeParser();

    private boolean needToFixScroll = false;

    /**
     * Constructor
     *
     * @param maxElements max elements that this console can show.
     */
    public ConsoleStage(int maxElements) {
        super(new UStageConfig().setNoBorder(true).setResizable(true));
        this.maxElements = maxElements;
        setIcon(Global.charaDefaultIcon);
        setStageX(0);
        setStageY(0);
        setPaneWidth(INITIAL_WIDTH);
        setPaneHeight(INITIAL_HEIGHT);

        textFlow = new TextFlow();
        textFlow.setPrefWidth(INITIAL_WIDTH);
        textFlow.setMinHeight(INITIAL_HEIGHT);
        textFlow.setBorder(Border.EMPTY);
        textFlow.setLineSpacing(4);
        textFlow.setBackground(new Background(new BackgroundFill(
            new Color(0x0c / 255d, 0x28 / 255d, 0x32 / 255d, 1),
            CornerRadii.EMPTY,
            Insets.EMPTY
        )));
        textFlow.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));

        Pane rootPane = getRootPane();
        scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(INITIAL_WIDTH);
        scrollPane.setPrefHeight(INITIAL_HEIGHT);
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
        MenuItem copyAllMenuItem = new MenuItem(I18nConsts.copyAllMenuItem.get()[0]);
        copyAllMenuItem.setOnAction(e -> this.copyTextContent());
        menu.getItems().addAll(copyAllMenuItem);
        // show/hide menu
        scrollPane.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                if (menu.isShowing()) {
                    menu.hide();
                }
                menu.show(scrollPane, e.getScreenX(), e.getScreenY());
                requestFocus();
            } else {
                if (menu.isShowing()) {
                    menu.hide();
                }
            }
        });
        // handle resizing
        paneWidth().addListener((ob, old, now) -> {
            textFlow.setPrefWidth(now.doubleValue());
            scrollPane.setPrefWidth(now.doubleValue());
        });
        paneHeight().addListener((ob, old, now) -> {
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

    private boolean firstTimeAdding = true;

    /**
     * Add text into the console
     *
     * @param text   text to be added
     * @param stderr whether the input text should be sent to stderr
     */
    public void addText(String text, boolean stderr) {
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
            if (size > maxElements) {
                textFlow.getChildren().remove(0, size - maxElements);
            }
            if (firstTimeAdding) {
                firstTimeAdding = false;
                scrollPane.setVvalue(1);
            }
        });
    }

    private Font textFont;

    private void addText(TextInfo info) {
        ThreadUtils.get().runOnFX(() -> {
            var foo = new Text(info.text);
            if (textFont == null) {
                textFont = new Font(FontManager.getMonospaceFontFamily(), FONT_SIZE);
            }
            foo.setFont(textFont);
            foo.setFill(info.color);
            textFlow.getChildren().add(foo);
            needToFixScroll = true;
        });
    }
}
