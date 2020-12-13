// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.robot;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.graphic.TimeBasedAnimationHelper;
import net.cassite.desktop.chara.manager.ConfigManager;
import net.cassite.desktop.chara.util.Consts;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.util.Utils;
import net.cassite.desktop.chara.widget.fgoclick.control.SleepIndicator;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.CalculatedAnchor;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.Point;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.Rec;
import net.cassite.desktop.chara.widget.fgoclick.fgo.OrderCardType;
import net.cassite.desktop.chara.widget.fgoclick.fgo.Settings;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickConsts;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;

public class RunningContext {
    private final Robot robot;
    public final CalculatedAnchor anchor;
    private final SleepIndicator sleepIndicator;
    public final FgoClickConsts consts;
    public final Settings settings;
    private final Consumer<Boolean> doneCb;
    boolean stopped = false;
    boolean skipping = false;

    public RunningContext(Robot robot, CalculatedAnchor anchor, SleepIndicator sleepIndicator, FgoClickConsts consts, Settings settings, Consumer<Boolean> doneCb) {
        this.robot = robot;
        this.anchor = anchor;
        this.sleepIndicator = sleepIndicator;
        this.consts = consts;
        this.settings = settings;
        this.doneCb = doneCb;
    }

    public boolean isStopped() {
        return stopped;
    }

    public boolean isSkipping() {
        return skipping;
    }

    public void done() {
        done(false);
    }

    public void done(boolean willBreak) {
        doneCb.accept(willBreak);
    }

    private final TimeBasedAnimationHelper mouseMoveHelper = new TimeBasedAnimationHelper(200, this::changeMousePosition);
    private double mouseStartX;
    private double mouseStartY;
    private double mouseEndX;
    private double mouseEndY;

    private void changeMousePosition(double percentage) {
        robot.mouseMove(
            mouseStartX + (mouseEndX - mouseStartX) * percentage,
            mouseStartY + (mouseEndY - mouseStartY) * percentage);
    }

    private double lastClickedX;
    private double lastClickedY;

    private void mouseMoveTo(double x, double y, Runnable cb) {
        if (lastClickedX == 0) {
            lastClickedX = robot.getMouseX();
        }
        if (lastClickedY == 0) {
            lastClickedY = robot.getMouseY();
        }
        mouseStartX = lastClickedX;
        mouseStartY = lastClickedY;
        mouseEndX = anchor.getStage().x + x;
        mouseEndY = anchor.getStage().y + y;
        mouseMoveHelper.setFinishCallbackOnce(() -> {
            robot.mouseMove(mouseEndX, mouseEndY);
            lastClickedX = mouseEndX;
            lastClickedY = mouseEndY;
            Utils.delayNoRecord(50, cb);
        }).play();
    }

    @SuppressWarnings("DuplicatedCode")
    public void clickAt(Point p, Runnable cb) {
        if (skipping || stopped) {
            cb.run();
            return;
        }
        double originalMouseX = robot.getMouseX();
        double originalMouseY = robot.getMouseY();
        robot.mouseMove(anchor.getStage().x + anchor.getTitle().x, anchor.getStage().y + anchor.getTitle().y);
        robot.mouseClick(MouseButton.PRIMARY);
        mouseMoveTo(p.x, p.y, () -> {
            robot.mouseClick(MouseButton.PRIMARY);
            robot.mouseClick(MouseButton.PRIMARY); // double click in a very shot time (sometimes click events are missing)
            Utils.delayNoRecord(50, () -> {
                robot.mouseMove(originalMouseX, originalMouseY);
                cb.run();
            });
        });
    }

    public void clickAt(Rec rec, Runnable cb) {
        clickAt(new Point((rec.topLeft.x + rec.bottomRight.x) / 2, (rec.topLeft.y + rec.bottomRight.y) / 2), cb);
    }

    @SuppressWarnings("DuplicatedCode")
    public void drag(Point from, Point to, Runnable cb) {
        if (skipping || stopped) {
            cb.run();
            return;
        }
        double originalMouseX = robot.getMouseX();
        double originalMouseY = robot.getMouseY();
        robot.mouseMove(anchor.getStage().x + anchor.getTitle().x, anchor.getStage().y + anchor.getTitle().y);
        robot.mouseClick(MouseButton.PRIMARY);
        mouseMoveTo(from.x, from.y, () -> {
            robot.mousePress(MouseButton.PRIMARY);
            mouseMoveTo(to.x, to.y, () -> {
                robot.mouseRelease(MouseButton.PRIMARY);
                Utils.delayNoRecord(50, () -> {
                    robot.mouseMove(originalMouseX, originalMouseY);
                    cb.run();
                });
            });
        });
    }

    private final boolean[] selectedNoblePhantasms = new boolean[3];
    private final boolean[] selectedOrderCards = new boolean[5];
    private final OrderCardType[] typeOfOrderCard = new OrderCardType[5];

    public int selectedOrderCardsCount() {
        int total = 0;
        for (boolean b : selectedNoblePhantasms) {
            if (b) ++total;
        }
        for (boolean b : selectedOrderCards) {
            if (b) ++total;
        }
        return total;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean orderCardSelected(int index) {
        if (index < 1 || index > 5) {
            throw new IllegalArgumentException("unknown order card " + index);
        }
        return selectedOrderCards[index - 1];
    }

    public Image getScreenCapture(Rec rec) {
        Image image = robot.getScreenCapture(null,
            anchor.getStage().x + rec.topLeft.x, anchor.getStage().y + rec.topLeft.y,
            rec.bottomRight.x - rec.topLeft.x + 1, rec.bottomRight.y - rec.topLeft.y + 1);
        if (Global.debugFeatures()) {
            var stack = Thread.currentThread().getStackTrace();
            if (stack.length == 0) {
                FgoClickUtils.debugImage(image, "unknown");
            } else {
                String classMethod = null;
                for (int i = 0; i < stack.length; i++) {
                    var s = stack[i];
                    if (s.getMethodName().equals("getScreenCapture")) {
                        if (i + 1 < stack.length) {
                            s = stack[i + 1];
                            classMethod = s.getClassName() + "." + s.getMethodName();
                        }
                        break;
                    }
                }
                FgoClickUtils.debugImage(image, classMethod == null ? "null" : classMethod);
            }
        }
        return image;
    }

    public void saveScreenCapture(String name) {
        if (skipping || stopped) {
            return;
        }
        if (!ConfigManager.get().getBoolValue(FgoClickConsts.autoSnapshot)) {
            return;
        }
        Image img = robot.getScreenCapture(null, anchor.getStage().x + anchor.getOrigin().x, anchor.getStage().y + anchor.getOrigin().y,
            anchor.getMaxXY().x - anchor.getOrigin().x, anchor.getMaxXY().y - anchor.getOrigin().y);
        FgoClickUtils.debugImage(img, "full shot for saving");
        var bi = SwingFXUtils.fromFXImage(img, null);
        File snapshotDir = new File(System.getProperty("user.home") + "/" + Consts.SNAPSHOT_BASE_DIR);
        if (snapshotDir.exists()) {
            if (!snapshotDir.isDirectory()) {
                Logger.warn(snapshotDir + " exists but is not a directory");
                return;
            }
        } else {
            if (!snapshotDir.mkdirs()) {
                Logger.warn("make dirs " + snapshotDir + " failed");
                return;
            }
        }
        Date now = new Date();
        //noinspection deprecation
        String datetimeStr = (1900 + now.getYear()) + "-" + intToTenNum(now.getMonth() + 1) + "-" + intToTenNum(now.getDate()) + "_"
            + intToTenNum(now.getHours()) + "-" + intToTenNum(now.getMinutes()) + "-" + intToTenNum(now.getSeconds());
        File imageFile = new File(snapshotDir.getAbsolutePath() + "/" + name + "-" + datetimeStr + ".png");
        try (var fos = new FileOutputStream(imageFile)) {
            ImageIO.write(bi, "PNG", fos);
            fos.flush();
        } catch (IOException e) {
            Logger.warn("writing " + imageFile + " failed", e);
            return;
        }
        Logger.info("screen capture saved at " + imageFile.getAbsolutePath());
    }

    private String intToTenNum(int n) {
        if (n < 10) {
            return "0" + n;
        }
        return "" + n;
    }

    public OrderCardType getTypeOfOrderCard(int index) {
        if (index < 1 || index > 5) {
            throw new IllegalArgumentException("unknown order card " + index);
        }
        if (typeOfOrderCard[index - 1] != null) return typeOfOrderCard[index - 1];
        Rec rec;
        if (index == 1) {
            rec = anchor.getOrderCard1();
        } else if (index == 2) {
            rec = anchor.getOrderCard2();
        } else if (index == 3) {
            rec = anchor.getOrderCard3();
        } else if (index == 4) {
            rec = anchor.getOrderCard4();
        } else {
            rec = anchor.getOrderCard5();
        }
        Image img = getScreenCapture(rec);
        Color color = FgoClickUtils.calcColor(img, rec.topLeft, rec);
        OrderCardType orderCardType;
        if (color.getRed() > color.getGreen() && color.getRed() > color.getBlue()) {
            orderCardType = OrderCardType.buster;
        } else if (color.getBlue() == 0 || (color.getGreen() / color.getBlue() > 1.5)) {
            orderCardType = OrderCardType.quick;
        } else {
            orderCardType = OrderCardType.arts;
        }
        typeOfOrderCard[index - 1] = orderCardType;
        Logger.info("order card " + index + " is " + orderCardType);
        return orderCardType;
    }

    public void clickOrderCard(int index, Runnable cb) {
        Rec rec;
        if (index == 1) {
            rec = anchor.getOrderCard1();
        } else if (index == 2) {
            rec = anchor.getOrderCard2();
        } else if (index == 3) {
            rec = anchor.getOrderCard3();
        } else if (index == 4) {
            rec = anchor.getOrderCard4();
        } else if (index == 5) {
            rec = anchor.getOrderCard5();
        } else throw new IllegalArgumentException("unknown order card " + index);

        selectedOrderCards[index - 1] = true;
        checkAndClearOrderCards();

        Logger.info("click order card " + index);
        clickAt(rec, cb);
    }

    public void clickNoblePhantasm(int servant, Runnable cb) {
        Point p;
        if (servant == 1) {
            p = anchor.getNoblePhantasm1();
        } else if (servant == 2) {
            p = anchor.getNoblePhantasm2();
        } else if (servant == 3) {
            p = anchor.getNoblePhantasm3();
        } else throw new IllegalArgumentException("unknown servant: " + servant);

        selectedNoblePhantasms[servant - 1] = true;
        checkAndClearOrderCards();

        Logger.info("click noble phantasm of servant " + servant);
        clickAt(p, cb);
    }

    private void checkAndClearOrderCards() {
        if (selectedOrderCardsCount() == 3) {
            Logger.info("3 order cards are all selected");
            Arrays.fill(selectedNoblePhantasms, false);
            Arrays.fill(selectedOrderCards, false);
            Arrays.fill(typeOfOrderCard, null);
        }
    }

    public void sleep(double seconds, Runnable cb) {
        if (skipping || stopped) {
            cb.run();
            return;
        }
        int millis = (int) (seconds * 1000);
        if (millis > 300) {
            sleepIndicator.show(millis - 300);
        }
        sleep0(System.currentTimeMillis() + millis, cb);
    }

    private void sleep0(long endTime, Runnable cb) {
        if (skipping || stopped) {
            cb.run();
            return;
        }
        long current = System.currentTimeMillis();
        if (endTime - current < 500) {
            Utils.delayNoRecord((int) (endTime - current), cb);
        } else {
            Utils.delayNoRecord(500, () -> sleep0(endTime, cb));
        }
    }

    public Runnable sleepAndDone(double seconds) {
        return () -> sleep(seconds, this::done);
    }

    public void keepRunning(double durationSeconds, double triggerIntervalSeconds, Consumer<Runnable> trigger, Runnable cb) {
        long begin = System.currentTimeMillis();
        keepRunning0(begin + (long) (durationSeconds * 1000), triggerIntervalSeconds, trigger, cb);
    }

    private void keepRunning0(long endTimestamp, double triggerIntervalSeconds, Consumer<Runnable> trigger, Runnable cb) {
        if (skipping || stopped) {
            cb.run();
            return;
        }
        long cur = System.currentTimeMillis();
        if (cur > endTimestamp) {
            cb.run();
            return;
        }
        Utils.delayNoRecord((int) (triggerIntervalSeconds * 1000), () ->
            trigger.accept(() ->
                keepRunning0(endTimestamp, triggerIntervalSeconds, trigger, cb)));
    }

    public Color[] getSupportSkillColors() {
        return FgoClickUtils.getSupportSkillColors(robot, anchor);
    }
}
