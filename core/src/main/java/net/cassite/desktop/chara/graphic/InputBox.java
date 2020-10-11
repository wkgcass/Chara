// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import net.cassite.desktop.chara.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class InputBox extends TextField {
    private final Pane rootPane;
    private final Pane rootScalePane;

    public InputBox(Pane rootPane, Pane rootScalePane) {
        this.rootPane = rootPane;
        this.rootScalePane = rootScalePane;
    }

    private boolean inputBoxVisible = false;
    private final TimeBasedAnimationHelper inputBoxAnimationHelper = new TimeBasedAnimationHelper(200,
        this::updateInputBoxAnimation)
        .setFinishCallback(this::finishInputBoxAnimation);
    private double inputBoxOpacityBegin;
    private double inputBoxOpacityTarget;

    private void finishInputBoxAnimation() {
        if (!inputBoxVisible) {
            this.clear();
            rootScalePane.requestFocus(); // remove focus from input box
            this.rootPane.getChildren().remove(this);
        }
    }

    private void updateInputBoxAnimation(double percentage) {
        var o = (inputBoxOpacityTarget - inputBoxOpacityBegin) * percentage + inputBoxOpacityBegin;
        this.setOpacity(o);
    }

    public void show() {
        if (inputBoxVisible) {
            return;
        }
        inputBoxVisible = true;
        assert Logger.debug("input box show");
        inputBoxOpacityBegin = this.getOpacity();
        inputBoxOpacityTarget = 1;
        inputBoxAnimationHelper.play();
        if (!this.rootPane.getChildren().contains(this)) {
            this.rootPane.getChildren().add(this);
        }
        this.requestFocus();
    }

    public void hide() {
        if (!inputBoxVisible) {
            return;
        }
        inputBoxVisible = false;
        assert Logger.debug("input box hide");
        inputBoxOpacityBegin = this.getOpacity();
        inputBoxOpacityTarget = 0;
        inputBoxAnimationHelper.play();
    }

    private static final int INPUT_TEXT_RECORDING_MAX_SIZE = 50;
    private final List<String> inputTextRecording = new ArrayList<>(INPUT_TEXT_RECORDING_MAX_SIZE);
    private int inputTextRecordingCursor = -1;

    public void recordInputText(String text) {
        if (inputTextRecording.size() >= INPUT_TEXT_RECORDING_MAX_SIZE) {
            inputTextRecording.remove(0);
        }
        inputTextRecording.add(text);
        inputTextRecordingCursor = inputTextRecording.size();
    }

    public void fillPreviousInputText() {
        int n = --inputTextRecordingCursor;
        if (n < 0) {
            inputTextRecordingCursor = 0;
            return;
        }
        var s = inputTextRecording.get(n);
        setText(s);
        positionCaret(s.length());
    }

    public void fillNextInputTextOrEmpty() {
        int n = ++inputTextRecordingCursor;
        if (n >= inputTextRecording.size()) {
            inputTextRecordingCursor = inputTextRecording.size();
            setText("");
            return;
        }
        var s = inputTextRecording.get(n);
        setText(s);
        positionCaret(s.length());
    }
}
