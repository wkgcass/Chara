// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import net.cassite.desktop.chara.chara.Chara;
import net.cassite.desktop.chara.graphic.MessageStage;
import net.cassite.desktop.chara.graphic.StageTransformer;

public class Events {
    private Events() {
    }

    public static final Key<Chara> CharacterReady = Key.of("character-ready", Chara.class);
    public static final Key<StageTransformer> PrimaryStageReady = Key.of("primary-stage-ready", StageTransformer.class);
    public static final Key<Void> PrimaryStageResized = Key.of("primary-stage-resized", Void.class);
    public static final Key<Void> PrimaryStageMoved = Key.of("primary-stage-moved", Void.class);
    public static final Key<MessageStage> MessageStageReady = Key.of("message-stage-ready", MessageStage.class);
    public static final Key<Void> MessageStageShown = Key.of("message-stage-shown", Void.class);
    public static final Key<Void> MessageStageHidden = Key.of("message-stage-hidden", Void.class);
    public static final Key<Double> BondPointChanged = Key.of("bond-point-changed", Double.class);
    public static final Key<Double> DesirePointChanged = Key.of("desire-point-changed", Double.class);
    public static final Key<String> MessageTaken = Key.of("message-taken", String.class);
    public static final Key<String> MessageShown = Key.of("message-shown", String.class);
}
