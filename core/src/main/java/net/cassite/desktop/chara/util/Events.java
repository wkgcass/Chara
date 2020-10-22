// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import net.cassite.desktop.chara.AppCallback;
import net.cassite.desktop.chara.chara.Chara;
import net.cassite.desktop.chara.graphic.MessageStage;
import net.cassite.desktop.chara.graphic.StageTransformer;

public class Events {
    private Events() {
    }

    /**
     * character is ready. this event will only fire at most once. you may store the <code>Chara</code> object.
     */
    public static final Key<Chara> CharacterReady = Key.of("character-ready", Chara.class);
    /**
     * primary stage is ready. this event will only fire at most once. you may store the <code>StageTransformer</code> object.
     */
    public static final Key<StageTransformer> PrimaryStageReady = Key.of("primary-stage-ready", StageTransformer.class);
    /**
     * primary stage is resized
     */
    public static final Key<Void> PrimaryStageResized = Key.of("primary-stage-resized", Void.class);
    /**
     * primary stage is moved
     */
    public static final Key<Void> PrimaryStageMoved = Key.of("primary-stage-moved", Void.class);
    /**
     * message stage is ready. this event will only fire at most once. you may store the <code>MessageStage</code> object.
     */
    public static final Key<MessageStage> MessageStageReady = Key.of("message-stage-ready", MessageStage.class);
    /**
     * message stage shown.
     */
    public static final Key<Void> MessageStageShown = Key.of("message-stage-shown", Void.class);
    /**
     * message stage hidden.
     */
    public static final Key<Void> MessageStageHidden = Key.of("message-stage-hidden", Void.class);
    /**
     * bond point changed. the current value will be provided.
     */
    public static final Key<Double> BondPointChanged = Key.of("bond-point-changed", Double.class);
    /**
     * desire point changed. the current value will be provided.
     */
    public static final Key<Double> DesirePointChanged = Key.of("desire-point-changed", Double.class);
    /**
     * message input by user.
     */
    public static final Key<String> MessageTaken = Key.of("message-taken", String.class);
    /**
     * message is shown.
     */
    public static final Key<String> MessageShown = Key.of("message-shown", String.class);
    /**
     * mouse clicked. the image position will be provided in an array [x, y].
     */
    public static final Key<double[]> MouseClickedImagePosition = Key.of("mouse-clicked-image-position", double[].class);
    /**
     * app callback is ready. this event will only fire at most once. you may store the <code>AppCallback</code> object.
     */
    public static final Key<AppCallback> AppCallbackReady = Key.of("app-callback-ready", AppCallback.class);
}
