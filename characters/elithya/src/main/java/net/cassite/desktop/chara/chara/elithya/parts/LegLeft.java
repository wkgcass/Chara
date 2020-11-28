// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.ElithyaUtils;
import net.cassite.desktop.chara.chara.elithya.util.LoopingAnima;

public class LegLeft extends AbstractLeg {
    private final LoopingAnima purple;
    private final LoopingAnima black;
    private final LoopingAnima none;

    private final LoopingAnima shoePurpleFront;
    private final LoopingAnima shoePurpleBack;
    private final LoopingAnima shoeBlackFront;
    private final LoopingAnima shoeBlackBack;

    public LegLeft(Group parent) {
        super(parent);

        purple = ElithyaUtils.buildLoopingAnima("animation/027_001_leg_left_purple/leg_left_purple_", 15, ".png");
        black = ElithyaUtils.buildLoopingAnima("animation/027_002_leg_left_black/leg_left_black_", 15, ".png");
        none = ElithyaUtils.buildLoopingAnima("animation/027_003_leg_left_none/leg_left_none_", 15, ".png");
        shoePurpleFront = ElithyaUtils.buildLoopingAnima("animation/026_001_shoe_front_left_purple/shoe_front_left_purple_", 15, ".png");
        shoePurpleBack = ElithyaUtils.buildLoopingAnima("animation/028_001_shoe_back_left_purple/shoe_back_left_purple_", 15, ".png");
        shoeBlackFront = ElithyaUtils.buildLoopingAnima("animation/026_002_shoe_front_left_black/shoe_front_left_black_", 15, ".png");
        shoeBlackBack = ElithyaUtils.buildLoopingAnima("animation/028_002_shoe_back_left_black/shoe_back_left_black_", 15, ".png");

        init();
    }

    @Override
    protected LoopingAnima getPurpleStocking() {
        return purple;
    }

    @Override
    protected LoopingAnima getBlackStocking() {
        return black;
    }

    @Override
    protected LoopingAnima getNoneStocking() {
        return none;
    }

    @Override
    protected LoopingAnima getPurpleShoeFront() {
        return shoePurpleFront;
    }

    @Override
    protected LoopingAnima getBlackShoeFront() {
        return shoeBlackFront;
    }

    @Override
    protected LoopingAnima getPurpleShoeBack() {
        return shoePurpleBack;
    }

    @Override
    protected LoopingAnima getBlackShoeBack() {
        return shoeBlackBack;
    }
}
