// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.robot;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.FgoClick;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickUtils;
import vproxybase.selector.SelectorEventLoop;
import vproxybase.util.ByteArray;
import vserver.HttpServer;
import vserver.RoutingContext;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HtmlRobot {
    private final Robot robot;
    private final RobotRun robotRun;
    private final FgoClick fgoClick;

    public HtmlRobot(Robot robot, RobotRun robotRun, FgoClick fgoClick) {
        this.robot = robot;
        this.robotRun = robotRun;
        this.fgoClick = fgoClick;
    }

    private boolean started;
    private HttpServer httpServer;

    public void start(int port) throws IOException {
        if (started) {
            return;
        }
        started = true;

        httpServer = HttpServer.create();
        httpServer
            .all("/*", rctx -> {
                Logger.warn(rctx.method() + " " + rctx.uri() + " from user-agent: " + rctx.header("user-agent"));
                rctx.next();
            })
            .get("/", rctx -> {
                var loop = SelectorEventLoop.current();
                Platform.runLater(() -> {
                        boolean isPaused = robotRun.isPaused();
                        String op = isPaused ? robotRun.peekNextOp() : robotRun.getLastOp();
                        loop.runOnLoop(() ->
                            rctx.response().end("" +
                                "<html>" +
                                "<head>\n" +
                                "<meta charset='UTF-8'>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "<p>" + op + "</p>\n" +
                                "<div>" +
                                "<form action='pause' style='display: inline'><button style='width:120px;height:40px;' " + (isPaused ? "disabled='disabled'" : "") + ">pause</button></form>" +
                                "<form action='resume' style='display: inline'><button style='width:120px;height:40px;' " + (!isPaused ? "disabled='disabled'" : "") + ">resume</button></form>" +
                                "<form action='next' style='display: inline'><button style='width:120px;height:40px;' " + (!isPaused ? "disabled='disabled'" : "") + ">next</button></form>" +
                                "<p></p>" +
                                "</div>\n" +
                                (isPaused ? "<a href='click'>\n" : "") +
                                "<img src='snapshot.jpg' alt='snapshot' " + (isPaused ? "ismap" : "") + ">\n" +
                                (isPaused ? "</a>\n" : "") +
                                "</body>" +
                                "</html>"
                            )
                        );
                    }
                );
            })
            .get("/pause", rctx -> {
                var loop = SelectorEventLoop.current();
                Platform.runLater(() -> fgoClick.pause(() -> loop.runOnLoop(() -> redirect(rctx))));
            })
            .get("/resume", rctx -> {
                var loop = SelectorEventLoop.current();
                Platform.runLater(() -> {
                    fgoClick.resume();
                    loop.delay(500, () -> redirect(rctx));
                });
            })
            .get("/next", rctx -> {
                var loop = SelectorEventLoop.current();
                Platform.runLater(() -> fgoClick.skip(() -> loop.runOnLoop(() -> redirect(rctx))));
            })
            .get("/snapshot.jpg", rctx -> {
                var loop = SelectorEventLoop.current();
                Platform.runLater(() -> {
                    Image img = robot.getScreenCapture(null,
                        ConfigManager.get().getDoubleValue(FgoClickConsts.boundsX),
                        ConfigManager.get().getDoubleValue(FgoClickConsts.boundsY),
                        ConfigManager.get().getDoubleValue(FgoClickConsts.boundsWidth),
                        ConfigManager.get().getDoubleValue(FgoClickConsts.boundsHeight)
                    );
                    FgoClickUtils.debugImage(img, "full shot for http");
                    var bi = SwingFXUtils.fromFXImage(img, null);
                    var rgbBi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
                    for (int x = 0; x < rgbBi.getWidth(); ++x) {
                        for (int y = 0; y < rgbBi.getHeight(); ++y) {
                            rgbBi.setRGB(x, y, bi.getRGB(x, y) & 0xffffff);
                        }
                    }
                    var output = new ByteArrayOutputStream();
                    try (ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(output)) {
                        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();

                        ImageWriteParam param = writer.getDefaultWriteParam();
                        if (param.canWriteCompressed()) {
                            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                            param.setCompressionQuality(0.25f);
                        }

                        writer.setOutput(imageOutputStream);
                        writer.write(null, new IIOImage(rgbBi, null, null), param);
                        writer.dispose();
                    } catch (IOException e) {
                        Logger.warn("should not happen: writing JPG to memory failed", e);
                        rctx.response().status(500).end(e.getMessage());
                        return;
                    }
                    var bytes = ByteArray.from(output.toByteArray());
                    loop.runOnLoop(() -> rctx.response().end(bytes));
                });
            })
            .get("/click", rctx -> {
                if (!robotRun.isPaused()) {
                    redirect(rctx);
                    return;
                }
                String uri = rctx.uri();
                Logger.info("click " + uri);
                if (!uri.contains("?")) {
                    rctx.response().status(400).end("no query found, expecting ?{x},{y}");
                    return;
                }
                String coordinates = uri.substring(uri.lastIndexOf("?") + 1);
                if (!coordinates.contains(",")) {
                    rctx.response().status(400).end("no coordinates found, expecting ?{x},{y} but got " + coordinates);
                    return;
                }
                String[] split = coordinates.split(",");
                if (split.length != 2) {
                    rctx.response().status(400).end("invalid coordinates, expecting ?{x},{y} but got " + coordinates);
                    return;
                }
                int x;
                try {
                    x = Integer.parseInt(split[0]);
                } catch (NumberFormatException e) {
                    rctx.response().status(400).end("coordinate x is not an integer " + split[0]);
                    return;
                }
                int y;
                try {
                    y = Integer.parseInt(split[1]);
                } catch (NumberFormatException e) {
                    rctx.response().status(400).end("coordinate y is not an integer " + split[1]);
                    return;
                }
                if (x < 0 || x > ConfigManager.get().getDoubleValue(FgoClickConsts.boundsWidth)
                    || y < 0 || y > ConfigManager.get().getDoubleValue(FgoClickConsts.boundsWidth)) {
                    rctx.response().status(403).end("cannot click out of the scope");
                    return;
                }
                var loop = SelectorEventLoop.current();
                Platform.runLater(() -> {
                    robot.mouseMove(ConfigManager.get().getDoubleValue(FgoClickConsts.boundsX) + x, ConfigManager.get().getDoubleValue(FgoClickConsts.boundsY) + y);
                    robot.mouseClick(MouseButton.PRIMARY);
                    loop.delay(500, () -> redirect(rctx));
                });
            })
            .listen(port);
    }

    private void redirect(RoutingContext rctx) {
        rctx.response().status(302).header("Location", "/").end("jump to /");
    }

    public void stop() {
        if (!started) {
            return;
        }
        started = false;
        httpServer.close();
    }
}
