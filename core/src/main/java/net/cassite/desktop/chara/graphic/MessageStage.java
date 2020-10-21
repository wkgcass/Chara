// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.util.StageUtils;
import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.util.Consts;
import net.cassite.desktop.chara.util.Logger;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class MessageStage extends Stage {
    private final StageTransformer primaryStage;
    private final Stage tmpStage;
    private final LinkedList<MessageBubble> messageBubbles = new LinkedList<>();
    private final LinkedList<InputMessage> messageBubblesToBeAdded = new LinkedList<>();
    private final LinkedList<MessageBubble> messageBubblesToBePopped = new LinkedList<>();
    private final Pane root = new Pane();
    private boolean pointToRight = true;
    private double ySum = 0;

    public MessageStage(StageTransformer primaryStage) {
        this.primaryStage = primaryStage;
        this.tmpStage = StageUtils.createTransparentTemporaryUtilityStage();
        initStyle(StageStyle.TRANSPARENT);
        initOwner(this.tmpStage);
        setResizable(false);
        setAlwaysOnTop(true);
        root.setBackground(Background.EMPTY);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        setWidth(Consts.MSG_STAGE_WIDTH);
        setScene(scene);
        getIcons().add(Global.modelIcon);

        ThreadUtils.get().scheduleAtFixedRateFX(this::checkPopBubble, 0, 500, TimeUnit.MILLISECONDS);
    }

    public void pushMessage(InputMessage inputMessage) {
        if (isPlaying) {
            messageBubblesToBeAdded.add(inputMessage);
            assert Logger.debug("pushMessage when animating");
            return;
        }
        pushMessage0(inputMessage);

        postOperation();
    }

    private void pushMessage0(InputMessage inputMessage) {
        assert Logger.debug("do pushMessage");

        String message = inputMessage.message;

        MessageBubble msg = new MessageBubble(message, pointToRight,
            Consts.MSG_BUBBLE_COLORS[inputMessage.colorHash % Consts.MSG_BUBBLE_COLORS.length]);
        msg.createTime = System.currentTimeMillis();
        msg.setOnMouseClick(() -> popMessage(msg));
        msg.setY(ySum);
        ySum += msg.getHeight() + Consts.MSG_BUBBLE_MARGIN_VERTICAL;
        messageBubbles.add(msg);
        setHeight(ySum);
        msg.addTo(root);
        if (!isShowing()) {
            showAll();
            assert Logger.debug("message stage show()");
            // focus the primary stage to make pushMessage behavior consistent
            primaryStage.getStage().requestFocus();
        }
    }

    private void postOperation() {
        ensureBubbleCount();
    }

    private void ensureBubbleCount() {
        if (messageBubbles.size() > Consts.MAX_MSG_COUNT) {
            var msg = messageBubbles.getFirst();
            assert msg != null;
            popMessage(msg);
        } else if (messageBubbles.isEmpty()) {
            assert messageBubblesToBeAdded.isEmpty();
            assert messageBubblesToBePopped.isEmpty();
            hide();
            assert Logger.debug("message stage hide()");
            resetStageState();
        }
    }

    private void resetStageState() {
        messageBubbles.clear();
        messageBubblesToBeAdded.clear();
        messageBubblesToBePopped.clear();
        root.getChildren().clear();
        ySum = 0;
    }

    @Override
    public void hide() {
        super.hide();
        tmpStage.hide();
    }

    public void showAll() {
        tmpStage.show();
        show();
    }

    private static final int REMOVING_ANIMATION_DURATION = 100;
    private final TimeBasedAnimationHelper animationHelper =
        new TimeBasedAnimationHelper(REMOVING_ANIMATION_DURATION, this::popMessageUpdate)
            .setFinishCallback(this::afterAnimation);
    private double pixelsToRemove = 0;
    private double originalYSum = 0;
    private MessageBubble handlingBubble = null;
    private boolean isPlaying = false;

    private void popMessage(MessageBubble msg) {
        if (!messageBubbles.contains(msg)) {
            // already removed
            msg.removeFrom(root); // ensure removed
            assert Logger.debug("pop an already removed message");
            return;
        }
        if (msg.equals(handlingBubble)) {
            assert Logger.debug("the message to be popped is being handled");
            return; // is handling
        }

        if (isPlaying) {
            if (!messageBubblesToBePopped.contains(msg)) {
                messageBubblesToBePopped.add(msg);
                assert Logger.debug("popMessage when animating");
            } else {
                assert Logger.debug("the message is already recorded to be popped");
            }
            return;
        }

        assert Logger.debug("do popMessage");

        handlingBubble = msg;
        pixelsToRemove = msg.getHeight() + Consts.MSG_BUBBLE_MARGIN_VERTICAL;
        // prepare initial data
        originalYSum = ySum;
        boolean found = false;
        for (var e : messageBubbles) {
            if (found) {
                e.posY = e.getY();
            } else {
                if (e.equals(msg)) {
                    found = true;
                    e.posY = -2;
                } else {
                    e.posY = -1;
                }
            }
        }

        play();
    }

    private void play() {
        isPlaying = true;
        animationHelper.play();
    }

    private void popMessageUpdate(double percentage) {
        var toRemove = pixelsToRemove * percentage;
        int count = 0;
        for (MessageBubble msg : messageBubbles) {
            ++count;
            if (msg.posY == -1) {
                continue;
            }
            if (msg.posY == -2) {
                // is being handled
                if (count != 1) {
                    // not first, simply hide the bubble, no animation
                    msg.hide();
                    continue;
                }
            }
            msg.setY(msg.posY - toRemove);
        }
        ySum = originalYSum - toRemove;
        setHeight(ySum);
    }

    private void afterAnimation() {
        // real remove
        handlingBubble.removeFrom(root);
        messageBubbles.remove(handlingBubble);
        handlingBubble = null;

        isPlaying = false;

        // check added during animation
        var e = messageBubblesToBePopped.pollFirst();
        if (e != null) {
            popMessage(e);
            return;
        }
        if (!messageBubblesToBeAdded.isEmpty()) {
            while (!messageBubblesToBeAdded.isEmpty()
                && messageBubbles.size() <= Consts.MAX_MSG_COUNT + 1 // let the size reach count + 1 to trigger pop operation
            ) {
                var msg = messageBubblesToBeAdded.pollFirst();
                pushMessage0(msg);
            }
        }

        postOperation();
    }

    private long lastPopTime = -1;

    private void checkPopBubble() {
        if (messageBubbles.isEmpty()) {
            return;
        }
        var first = messageBubbles.peekFirst();
        assert first != null;
        long createTime = first.createTime;
        long current = System.currentTimeMillis();
        if (current - createTime > getDuration(first)) {
            if (current - lastPopTime > 1500) {
                popMessage(first);
                lastPopTime = current;
            }
        }
    }

    private long getDuration(MessageBubble msg) {
        int len = msg.message.getBytes().length;
        int duration = 3000;
        if (len > 30) {
            duration += (len - 30) * 100;
        }
        return duration;
    }

    public void pointToLeft() {
        if (!pointToRight) {
            return;
        }
        pointToRight = false;
        changeDirection();
    }

    public void pointToRight() {
        if (pointToRight) {
            return;
        }
        pointToRight = true;
        changeDirection();
    }

    private void changeDirection() {
        messageBubbles.forEach(e -> {
            e.setPointToRight(pointToRight);
            e.calculatePosition();
        });
    }

    public boolean isPointingToRight() {
        return pointToRight;
    }

    public void click(double x, double y) {
        var stageX = getX();
        var stageY = getY();
        var relativeX = x - stageX;
        var relativeY = y - stageY;

        double sumY = 0;
        MessageBubble messageBubble = null;
        for (var msg : messageBubbles) {
            var xMin = pointToRight
                ? Consts.MSG_STAGE_WIDTH - msg.getWidth()
                : 0;
            var xMax = pointToRight
                ? Consts.MSG_STAGE_WIDTH
                : msg.getWidth();
            var yMin = sumY;
            var yMax = sumY + msg.getHeight();

            if (xMin <= relativeX && relativeX <= xMax &&
                yMin <= relativeY && relativeY <= yMax) {
                messageBubble = msg;
                break;
            }

            sumY += msg.getHeight() + Consts.MSG_BUBBLE_MARGIN_VERTICAL;
        }

        if (messageBubble == null) {
            return;
        }
        requestFocus();
        popMessage(messageBubble);
    }

    public void clearAllMessages() {
        messageBubblesToBeAdded.clear();
        for (var msg : messageBubbles) {
            popMessage(msg);
        }
    }
}
