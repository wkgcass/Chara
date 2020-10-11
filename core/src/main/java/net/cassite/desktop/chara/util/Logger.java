// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import net.cassite.desktop.chara.ThreadUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Semaphore;

public class Logger {
    private Logger() {
    }

    public static boolean debug(String msg) {
        System.out.println(msg);
        return true;
    }

    public static void info(String msg) {
        System.out.println(msg);
    }

    public static void warn(String msg) {
        System.out.println(msg);
    }

    public static void warn(String msg, Throwable t) {
        System.out.println(msg + " " + t);
        t.printStackTrace(System.out);
    }

    public static void error(String msg) {
        System.out.println(msg);

        if (ThreadUtils.get().isShutdown()) {
            return;
        }

        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(() -> {
            var alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle(msg.split("\n")[0].trim());
            alert.setContentText(msg);
            alert.showAndWait();
            semaphore.release();
        });
        if (!Platform.isFxApplicationThread()) {
            try {
                semaphore.acquire();
            } catch (InterruptedException ignore) {
            }
        }
    }

    public static void error(String msg, Throwable t) {
        System.out.println(msg + " " + t);
        t.printStackTrace(System.out);

        if (ThreadUtils.get().isShutdown()) {
            return;
        }

        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(() -> {
            var alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle(msg.split("\n")[0].trim());
            alert.setContentText(msg);

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();

            semaphore.release();
        });
        if (!Platform.isFxApplicationThread()) {
            try {
                semaphore.acquire();
            } catch (InterruptedException ignore) {
            }
        }
    }

    public static void fatal(String msg) {
        new Thread(() -> {
            error(msg);
            Utils.shutdownProgram();
        }).start();
    }

    public static void fatal(String msg, Throwable t) {
        // ensure not running on UI thread
        new Thread(() -> {
            error(msg, t);
            Utils.shutdownProgram();
        }).start();
    }
}
