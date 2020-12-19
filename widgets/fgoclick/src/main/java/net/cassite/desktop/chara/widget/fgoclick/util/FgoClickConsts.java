// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.util;

import javafx.scene.paint.Color;
import net.cassite.desktop.chara.model.ModelInitConfig;
import net.cassite.desktop.chara.util.Key;
import net.cassite.desktop.chara.widget.fgoclick.control.ButtonColor;

public class FgoClickConsts {
    public static final ButtonColor prepareButtonColor = new ButtonColor(
        new Color(0xb5 / 255d, 0xcc / 255d, 0x18 / 255d, 1),
        new Color(0xa7 / 255d, 0xbd / 255d, 0x0d / 255d, 1),
        new Color(0x8d / 255d, 0x9e / 255d, 0x13 / 255d, 1)
    );
    public static final ButtonColor runButtonColor = new ButtonColor(
        new Color(0x21 / 255d, 0xba / 255d, 0x45 / 255d, 1),
        new Color(0x16 / 255d, 0xab / 255d, 0x39 / 255d, 1),
        new Color(0x19 / 255d, 0x8f / 255d, 0x35 / 255d, 1)
    );
    public static final ButtonColor cancelButtonColor = new ButtonColor(
        new Color(0x00 / 255d, 0xb5 / 255d, 0xad / 255d, 1),
        new Color(0x00 / 255d, 0x9c / 255d, 0x95 / 255d, 1),
        new Color(0x00 / 255d, 0x82 / 255d, 0x7c / 255d, 1)
    );
    public static final ButtonColor okButtonColor = new ButtonColor(
        new Color(0x21 / 255d, 0x85 / 255d, 0xd0 / 255d, 1),
        new Color(0x16 / 255d, 0x78 / 255d, 0xc2 / 255d, 1),
        new Color(0x1a / 255d, 0x69 / 255d, 0xa4 / 255d, 1)
    );
    public static final ButtonColor stopButtonColor = new ButtonColor(
        new Color(0xdb / 255d, 0x28 / 255d, 0x28 / 255d, 1),
        new Color(0xd0 / 255d, 0x19 / 255d, 0x19 / 255d, 1),
        new Color(0xb2 / 255d, 0x1e / 255d, 0x1e / 255d, 1)
    );
    public static final ButtonColor pauseButtonColor = new ButtonColor(
        new Color(0xfb / 255d, 0xbd / 255d, 0x08 / 255d, 1),
        new Color(0xea / 255d, 0xae / 255d, 0x00 / 255d, 1),
        new Color(0xda / 255d, 0xa3 / 255d, 0x00 / 255d, 1)
    );
    public static final ButtonColor skipButtonColor = new ButtonColor(
        new Color(0x00 / 255d, 0xb5 / 255d, 0xad / 255d, 1),
        new Color(0x00 / 255d, 0x9c / 255d, 0x95 / 255d, 1),
        new Color(0x00 / 255d, 0x82 / 255d, 0x7c / 255d, 1)
    );
    public static final Color sleepIndicatorFine = new Color(
        33 / 255d, 186 / 255d, 69 / 255d, 1
    );
    public static final Color sleepIndicatorWarn = new Color(
        242 / 255d, 113 / 255d, 28 / 255d, 1
    );
    public static final Color sleepIndicatorDanger = new Color(
        219 / 255d, 40 / 255d, 40 / 255d, 1
    );

    public static final Key<Boolean> autoSnapshot = Key.of("auto_snapshot", boolean.class);
    public static final Key<Boolean> disableSleepIndicator = Key.of("disable_sleep_indicator", boolean.class);

    public static final Key<Double> boundsX = Key.of("bounds_x", double.class);
    public static final Key<Double> boundsY = Key.of("bounds_y", double.class);
    public static final Key<Double> boundsWidth = Key.of("bounds_width", double.class);
    public static final Key<Double> boundsHeight = Key.of("bounds_height", double.class);
    public static final Key<Boolean> prepared = Key.of("prepared", boolean.class);

    public static final Key<Double> titleX = Key.of("title_x", double.class);
    public static final Key<Double> titleY = Key.of("title_y", double.class);
    public static final Key<Double> skill11X = Key.of("skill_11_x", double.class);
    public static final Key<Double> skill11Y = Key.of("skill_11_Y", double.class);
    public static final Key<Double> skill12X = Key.of("skill_12_x", double.class);
    public static final Key<Double> skill12Y = Key.of("skill_12_Y", double.class);
    public static final Key<Double> skill13X = Key.of("skill_13_x", double.class);
    public static final Key<Double> skill13Y = Key.of("skill_13_Y", double.class);
    public static final Key<Double> skill22X = Key.of("skill_22_x", double.class);
    public static final Key<Double> skill22Y = Key.of("skill_22_Y", double.class);
    public static final Key<Double> skill33X = Key.of("skill_33_x", double.class);
    public static final Key<Double> skill33Y = Key.of("skill_33_Y", double.class);
    public static final Key<Double> masterItemX = Key.of("master_item_x", double.class);
    public static final Key<Double> masterItemY = Key.of("master_item_Y", double.class);
    public static final Key<Double> masterSkill3X = Key.of("master_skill_3_x", double.class);
    public static final Key<Double> masterSkill3Y = Key.of("master_skill_3_y", double.class);
    public static final Key<Double> masterSkill1X = Key.of("master_skill_1_x", double.class);
    public static final Key<Double> masterSkill1Y = Key.of("master_skill_1_y", double.class);
    public static final Key<Double> attackX = Key.of("attack_x", double.class);
    public static final Key<Double> attackY = Key.of("attack_Y", double.class);
    public static final Key<Double> trophyX = Key.of("trophy_x", double.class);
    public static final Key<Double> trophyY = Key.of("trophy_Y", double.class);
    public static final Key<Double> backX = Key.of("back_x", double.class);
    public static final Key<Double> backY = Key.of("back_Y", double.class);

    public static final Color attackButton = new Color(88 / 255d, 149 / 255d, 184 / 255d, 1);

    public static final Color lowAP = new Color(215 / 255d, 0 / 255d, 16 / 255d, 1);

    public static final Color goldenAppleIcon = new Color(199 / 255d, 170 / 255d, 54 / 255d, 1);
    public static final Color silverAppleIcon = new Color(150 / 255d, 164 / 255d, 167 / 255d, 1);

    public static final Color NoSkill1 = new Color(221 / 255d, 232 / 255d, 240 / 255d, 1);
    public static final Color NoSkill2 = new Color(213 / 255d, 226 / 255d, 236 / 255d, 1);
    public static final Color NoSkill3 = new Color(194 / 255d, 213 / 255d, 226 / 255d, 1);

    public static final Color ScathachSkathiSkill1 = new Color(165 / 255d, 207 / 255d, 94 / 255d, 1);
    public static final Color ScathachSkathiSkill2 = new Color(118 / 255d, 145 / 255d, 181 / 255d, 1);
    public static final Color ScathachSkathiSkill3 = new Color(174 / 255d, 137 / 255d, 68 / 255d, 1);
    public static final Color ZhugeKongmingSkill1 = new Color(217 / 255d, 180 / 255d, 89 / 255d, 1);
    public static final Color ZhugeKongmingSkill2 = new Color(192 / 255d, 141 / 255d, 66 / 255d, 1);
    public static final Color ZhugeKongmingSkill3 = new Color(200 / 255d, 142 / 255d, 73 / 255d, 1);

    public final int width;
    public final int height;
    public final int margin;
    public final int cornerRadius;
    public final int borderWidth;
    public final int buttonFontSize;
    public final double defaultBoundsWidth;
    public final double defaultBoundsHeight;
    public final int clickPointCircleOuterRadius;
    public final int clickPointCircleInnerRadius;
    public final int clickPointTextFontSize;
    public final int clickPointTextMargin;
    public final double strokeWidth;
    public final double enhanceColorSaturation;
    public final double enhanceColorBrightness;
    public final double colorDiffAllowed;

    public final int sleepIndicatorRadius = 50;
    public final int sleepIndicatorBounds = 4;
    public final double sleepIndicatorFineDegree = 210;
    public final double sleepIndicatorWarnUpperDegree = 150;
    public final double sleepIndicatorWarnLowerDegree = 90;
    public final double sleepIndicatorDangerDegree = 60;

    public FgoClickConsts(ModelInitConfig config) {
        this.width = config.getInt("width");
        this.height = config.getInt("height");
        this.margin = config.getInt("margin");
        this.cornerRadius = config.getInt("cornerRadius");
        this.borderWidth = config.getInt("borderWidth");
        this.buttonFontSize = config.getInt("buttonFontSize");
        this.defaultBoundsWidth = config.getDouble("defaultBoundsWidth");
        this.defaultBoundsHeight = config.getDouble("defaultBoundsHeight");
        this.clickPointCircleOuterRadius = config.getInt("clickPointCircleOuterRadius");
        this.clickPointCircleInnerRadius = config.getInt("clickPointCircleInnerRadius");
        this.clickPointTextFontSize = config.getInt("clickPointTextFontSize");
        this.clickPointTextMargin = config.getInt("clickPointTextMargin");
        this.strokeWidth = config.getDouble("strokeWidth");
        this.enhanceColorSaturation = config.getDouble("enhanceColorSaturation");
        this.enhanceColorBrightness = config.getDouble("enhanceColorBrightness");
        this.colorDiffAllowed = config.getDouble("colorDiffAllowed");
    }
}
