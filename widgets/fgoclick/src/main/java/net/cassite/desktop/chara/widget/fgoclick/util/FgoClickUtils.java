// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.util;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import net.cassite.desktop.chara.Global;
import net.cassite.desktop.chara.util.Logger;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.CalculatedAnchor;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.Point;
import net.cassite.desktop.chara.widget.fgoclick.coordinate.Rec;
import net.cassite.desktop.chara.widget.fgoclick.fgo.*;
import vjson.CharStream;
import vjson.JSON;
import vjson.cs.CharArrayCharStream;
import vjson.deserializer.rule.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FgoClickUtils {
    private FgoClickUtils() {
    }

    private static class ActionBuilder {
        private String type;
        private int options;
        private int time;

        public void setType(String type) {
            this.type = type;
        }

        public void setOptions(int options) {
            this.options = options;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public Action build() {
            Objects.requireNonNull(type, "action.type");
            Action ret;
            if (type.equals("normal")) {
                if (options != 0) {
                    throw new IllegalArgumentException("action.type == normal but options is not 0");
                }
                ret = new NormalAction();
            } else if (type.equals("select")) {
                if (options == 0) {
                    throw new IllegalArgumentException("action.type == select but options is 0");
                }
                ret = new SelectAction().setOptions(options);
            } else {
                throw new IllegalArgumentException("unknown action.type " + type);
            }
            if (time != 0) {
                ret.setTime(time);
            }
            return ret;
        }
    }

    private static class OperationBuilder {
        private OperationType type;
        private OperationSkill skill;
        private OperationMasterSkill masterSkill;
        private OperationNoblePhantasm noblePhantasm;

        public void setType(String type) {
            this.type = OperationType.valueOf(type);
        }

        public void setSkill(OperationSkill skill) {
            this.skill = skill;
        }

        public void setMasterSkill(OperationMasterSkill masterSkill) {
            this.masterSkill = masterSkill;
        }

        public void setNoblePhantasm(OperationNoblePhantasm noblePhantasm) {
            this.noblePhantasm = noblePhantasm;
        }

        public Operation build(Preset preset) {
            Objects.requireNonNull(type, "operation.type");
            switch (type) {
                case skill: {
                    if (masterSkill != null)
                        throw new IllegalArgumentException("type == skill but operation.masterSkill is present");
                    if (noblePhantasm != null)
                        throw new IllegalArgumentException("type == skill but operation.noblePhantasm is present");
                    Objects.requireNonNull(skill, "operation.skill");
                    if (skill.servant < 1)
                        throw new IllegalArgumentException("skill.servant < 1: " + skill.servant);
                    if (preset.getServants().size() < skill.servant)
                        throw new IllegalArgumentException("skill.servant " + skill.servant + " not found");
                    Servant servant = preset.getServants().get(skill.servant - 1);
                    if (skill.skill < 1)
                        throw new IllegalArgumentException("skill.skill < 1: " + skill.skill);
                    if (servant.getSkills().size() < skill.skill)
                        throw new IllegalArgumentException("skill.skill " + skill.skill + " not found");
                    Skill theSkill = servant.getSkills().get(skill.skill - 1);
                    switch (theSkill.getAction().getType()) {
                        case normal:
                            if (skill.select != 0)
                                throw new IllegalArgumentException("skill.select is " + skill.select + " but referenced skill is " + theSkill);
                            return new SkillOperation().setSkill(theSkill);
                        case select:
                            if (skill.select == 0)
                                throw new IllegalArgumentException("skill.select is not set but referenced skill is " + theSkill);
                            return new SkillOperation().setSkill(theSkill).setSelect(skill.select);
                        default:
                            throw new IllegalArgumentException("unknown skill.action.type " + theSkill.getAction().getType());
                    }
                }
                case masterSkill: {
                    if (skill != null)
                        throw new IllegalArgumentException("type == masterSkill but operation.skill is present");
                    if (noblePhantasm != null)
                        throw new IllegalArgumentException("type == masterSkill but operation.noblePhantasm is present");
                    Objects.requireNonNull(masterSkill, "operation.masterSkill");
                    if (masterSkill.skill < 1)
                        throw new IllegalArgumentException("masterSkill.skill < 1: " + masterSkill.skill);
                    if (preset.getMasterSkills().size() < masterSkill.skill)
                        throw new IllegalArgumentException("masterSkill.skill " + masterSkill.skill + " not found");
                    MasterSkill theSkill = preset.getMasterSkills().get(masterSkill.skill - 1);
                    switch (theSkill.getAction().getType()) {
                        case normal:
                            if (masterSkill.select != 0)
                                throw new IllegalArgumentException("masterSkill.select is " + skill.select + " but referenced skill is " + theSkill);
                            return new MasterSkillOperation().setSkill(theSkill);
                        case select:
                            if (masterSkill.select == 0)
                                throw new IllegalArgumentException("masterSkill.select is not set but referenced skill is " + theSkill);
                            return new MasterSkillOperation().setSkill(theSkill).setSelect(masterSkill.select);
                        default:
                            throw new IllegalArgumentException("unknown masterSkill.action.type " + theSkill.getAction().getType());
                    }
                }
                case noblePhantasm: {
                    if (skill != null)
                        throw new IllegalArgumentException("type == noblePhantasm but operation.skill is present");
                    if (masterSkill != null)
                        throw new IllegalArgumentException("type == noblePhantasm but operation.masterSkill is present");
                    Objects.requireNonNull(noblePhantasm, "operation.noblePhantasm");
                    if (noblePhantasm.servant < 1)
                        throw new IllegalArgumentException("noblePhantasm.servant < 1: " + noblePhantasm.servant);
                    if (preset.getServants().size() < noblePhantasm.servant)
                        throw new IllegalArgumentException("noblePhantasm.servant " + noblePhantasm.servant + " not found");
                    Servant servant = preset.getServants().get(noblePhantasm.servant - 1);
                    if (servant.getNoblePhantasm() == null)
                        throw new IllegalArgumentException("servant " + servant + " is not defined with a noble phantasm");
                    NoblePhantasm noblePhantasm = servant.getNoblePhantasm();
                    return new NoblePhantasmOperation().setNoblePhantasm(noblePhantasm);
                }
                case nextBattle:
                    if (skill != null)
                        throw new IllegalArgumentException("type == " + type + " but operation.skill is present");
                    if (masterSkill != null)
                        throw new IllegalArgumentException("type == " + type + " but operation.masterSkill is present");
                    if (noblePhantasm != null)
                        throw new IllegalArgumentException("type == " + type + " but operation.noblePhantasm is present");
                    return new NextBattleOperation();
                default:
                    throw new IllegalArgumentException("unknown operation.type: " + type);
            }
        }
    }

    private static class OperationSkill {
        private int servant;
        private int skill;
        private int select;

        public void setServant(int servant) {
            this.servant = servant;
        }

        public void setSkill(int skill) {
            this.skill = skill;
        }

        public void setSelect(int select) {
            this.select = select;
        }
    }

    private static class OperationMasterSkill {
        private int skill;
        private int select;

        public void setSkill(int skill) {
            this.skill = skill;
        }

        public void setSelect(int select) {
            this.select = select;
        }
    }

    private static class OperationNoblePhantasm {
        private int servant;

        public void setServant(int servant) {
            this.servant = servant;
        }
    }

    public static Configuration deserialize(CharStream cs) {
        Preset[] preset = new Preset[1];
        Rule<Configuration> rule =
            new ObjectRule<>(Configuration::new)
                .put("preset", Configuration::setPreset,
                    new ObjectRule<>(() -> preset[0] = new Preset())
                        .put("servants", Preset::setServants, new ArrayRule<>(ArrayList::new, List::add,
                            new ObjectRule<>(Servant::new)
                                .put("name", Servant::setName, new StringRule())
                                .put("skills", Servant::setSkills, new ArrayRule<>(ArrayList::new, List::add,
                                    new ObjectRule<>(Skill::new)
                                        .put("name", Skill::setName, new StringRule())
                                        .put("action", (s, a) -> s.setAction(a.build()),
                                            new ObjectRule<>(ActionBuilder::new)
                                                .put("type", ActionBuilder::setType, new StringRule())
                                                .put("options", ActionBuilder::setOptions, new IntRule())
                                                .put("time", ActionBuilder::setTime, new IntRule())
                                        )
                                ))
                                .put("noblePhantasm", Servant::setNoblePhantasm,
                                    new ObjectRule<>(NoblePhantasm::new)
                                        .put("name", NoblePhantasm::setName, new StringRule())
                                        .put("action", NoblePhantasm::setAction,
                                            new ObjectRule<>(NormalAction::new)
                                                .put("time", Action::setTime, new IntRule())
                                        )
                                )
                        ))
                        .put("masterSkills", Preset::setMasterSkills, new ArrayRule<>(ArrayList::new, List::add,
                            new ObjectRule<>(MasterSkill::new)
                                .put("name", MasterSkill::setName, new StringRule())
                                .put("action", (s, a) -> s.setAction(a.build()),
                                    new ObjectRule<>(ActionBuilder::new)
                                        .put("type", ActionBuilder::setType, new StringRule())
                                        .put("options", ActionBuilder::setOptions, new IntRule())
                                        .put("time", ActionBuilder::setTime, new IntRule())
                                )
                        ))
                        .put("settings", Preset::setSettings,
                            new ObjectRule<>(Settings::new)
                                .put("totalBattles", Settings::setTotalBattles, new IntRule())
                                .put("confirmDialog", Settings::setConfirmDialog, new BoolRule())
                                .put("support", Settings::setSupport, new StringRule())
                                .put("useApple", Settings::setUseApple, new StringRule())
                                .put("loop", Settings::setLoop, new BoolRule())
                        )
                )
                .put("operations", Configuration::setOperations, new ArrayRule<>(ArrayList::new,
                    (ls, o) -> ls.add(o.build(preset[0])),
                    new ObjectRule<>(OperationBuilder::new)
                        .put("type", OperationBuilder::setType, new StringRule())
                        .put("skill", OperationBuilder::setSkill,
                            new ObjectRule<>(OperationSkill::new)
                                .put("servant", OperationSkill::setServant, new IntRule())
                                .put("skill", OperationSkill::setSkill, new IntRule())
                                .put("select", OperationSkill::setSelect, new IntRule())
                        )
                        .put("masterSkill", OperationBuilder::setMasterSkill,
                            new ObjectRule<>(OperationMasterSkill::new)
                                .put("skill", OperationMasterSkill::setSkill, new IntRule())
                                .put("select", OperationMasterSkill::setSelect, new IntRule())
                        )
                        .put("noblePhantasm", OperationBuilder::setNoblePhantasm,
                            new ObjectRule<>(OperationNoblePhantasm::new)
                                .put("servant", OperationNoblePhantasm::setServant, new IntRule())
                        )
                ));
        var ret = JSON.deserialize(cs, rule);
        List<Servant> servants = preset[0].getServants();
        for (int iServant = 0; iServant < servants.size(); ++iServant) {
            Servant servant = servants.get(iServant);
            List<Skill> skills = servant.getSkills();
            for (int iSkill = 0; iSkill < skills.size(); ++iSkill) {
                Skill skill = skills.get(iSkill);
                skill.setServant(servant);
                skill.setIndex(iSkill + 1);
            }
            if (servant.getNoblePhantasm() != null) {
                servant.getNoblePhantasm().setServant(servant);
            }
            servant.setIndex(iServant + 1);
        }
        for (int iSkill = 0; iSkill < preset[0].getMasterSkills().size(); ++iSkill) {
            MasterSkill skill = preset[0].getMasterSkills().get(iSkill);
            skill.setIndex(iSkill + 1);
        }
        // check settings
        {
            String support = ret.getPreset().getSettings().getSupport();
            if (support != null) {
                List<String> allowed = List.of(
                    FgoClickI18nConsts.ScathachSkathi.get()[0],
                    FgoClickI18nConsts.ZhugeKongming.get()[0]);
                if (!allowed.contains(support)) {
                    throw new IllegalArgumentException("unknown support servant: " + support + ", only allow " + allowed);
                }
            }
            String apple = ret.getPreset().getSettings().getUseApple();
            if (apple != null) {
                List<String> allowed = List.of("stone", "golden", "silver", "copper", "no");
                if (!allowed.contains(apple)) {
                    throw new IllegalArgumentException("unknown apple type: " + apple + ", only allow " + allowed);
                }
            }
        }
        return ret;
    }

    public static Configuration deserialize(File file) throws Exception {
        try (var fis = new FileInputStream(file)) {
            var reader = new InputStreamReader(fis);
            var br = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return FgoClickUtils.deserialize(new CharArrayCharStream(sb.toString().toCharArray()));
        }
    }

    public static Image getSupportSkillImage(Robot robot, CalculatedAnchor anchor) {
        Image ret = robot.getScreenCapture(null,
            anchor.getStage().x + anchor.getSupport1Skill1().topLeft.x,
            anchor.getStage().y + anchor.getSupport1Skill1().topLeft.y,
            anchor.getSupport2Skill3().bottomRight.x - anchor.getSupport1Skill1().topLeft.x + 1, // +1 to avoid out of bounds
            anchor.getSupport2Skill3().bottomRight.y - anchor.getSupport1Skill1().topLeft.y + 1);
        FgoClickUtils.debugImage(ret, "support skill");
        return ret;
    }

    public static Color[] getSupportSkillColors(Robot robot, CalculatedAnchor anchor) {
        var img = getSupportSkillImage(robot, anchor);
        Color[] ret = new Color[6];
        Point origin = anchor.getSupport1Skill1().topLeft;
        ret[0] = calcColor(img, origin, anchor.getSupport1Skill1());
        ret[1] = calcColor(img, origin, anchor.getSupport1Skill2());
        ret[2] = calcColor(img, origin, anchor.getSupport1Skill3());
        ret[3] = calcColor(img, origin, anchor.getSupport2Skill1());
        ret[4] = calcColor(img, origin, anchor.getSupport2Skill2());
        ret[5] = calcColor(img, origin, anchor.getSupport2Skill3());
        return ret;
    }

    public static Color calcColor(Image img, Point origin, Rec rec) {
        int x = (int) (rec.topLeft.x - origin.x);
        int y = (int) (rec.topLeft.y - origin.y);
        double w = (int) (rec.bottomRight.x - rec.topLeft.x);
        double h = (int) (rec.bottomRight.y - rec.topLeft.y);
        int iw = (int) w;
        int ih = (int) h;
        long totalR = 0;
        long totalG = 0;
        long totalB = 0;
        long count = 0;
        for (int dx = 0; dx < iw; ++dx) {
            for (int dy = 0; dy < ih; ++dy) {
                ++count;
                int argb = img.getPixelReader().getArgb(x + dx, y + dy);
                int r = vproxybase.util.Utils.positive((byte) ((argb >> 16) & 0xff));
                int g = vproxybase.util.Utils.positive((byte) ((argb >> 8) & 0xff));
                int b = vproxybase.util.Utils.positive((byte) (argb & 0xff));
                totalR += r;
                totalG += g;
                totalB += b;
            }
        }
        int r = (int) (totalR / count);
        int g = (int) (totalG / count);
        int b = (int) (totalB / count);
        Logger.info("original RGB(" + r + ", " + g + ", " + b + ")");
        return new Color(r / 255d, g / 255d, b / 255d, 1);
    }

    public static Color calcColorToShow(Image img, Point origin, Rec rec, FgoClickConsts consts) {
        Color color = calcColor(img, origin, rec);
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        // increase saturation
        {
            int newR;
            int newG;
            int newB;
            double rgbMax = Math.max(Math.max(r, g), b);
            double rgbMin = Math.min(Math.min(r, g), b);
            double delta = (rgbMax - rgbMin) / 255d;
            if (delta == 0) {
                newR = r;
                newG = g;
                newB = b;
            } else {
                double value = (rgbMax + rgbMin) / 255d;
                double L = value / 2;
                double S;
                if (L < 0.5) {
                    S = delta / value;
                } else {
                    S = delta / (2 - value);
                }
                double percent = consts.enhanceColorSaturation;
                double alpha;
                if (percent >= 0) {
                    if (percent + S >= 1) {
                        alpha = S;
                    } else {
                        alpha = 1 - percent;
                    }
                    alpha = (1 / alpha) - 1;
                    newR = (int) (r + (r - L * 255) * alpha);
                    newG = (int) (g + (g - L * 255) * alpha);
                    newB = (int) (b + (b - L * 255) * alpha);
                } else {
                    alpha = percent;
                    newR = (int) (L * 255 + (r - L * 255) + (1 + alpha));
                    newG = (int) (L * 255 + (g - L * 255) + (1 + alpha));
                    newB = (int) (L * 255 + (b - L * 255) + (1 + alpha));
                }
            }
            r = newR;
            g = newG;
            b = newB;
            Logger.info("increase saturation RGB(" + r + ", " + g + ", " + b + ")");
        }
        // increase brightness
        {
            int newR = (int) (r * consts.enhanceColorBrightness);
            int newG = (int) (g * consts.enhanceColorBrightness);
            int newB = (int) (b * consts.enhanceColorBrightness);
            if (newR > 255) {
                newR = 255;
            }
            if (newG > 255) {
                newG = 255;
            }
            if (newB > 255) {
                newB = 255;
            }
            r = newR;
            g = newG;
            b = newB;
            Logger.info("increase brightness RGB(" + r + ", " + g + ", " + b + ")");
        }
        return new Color(r / 255d, g / 255d, b / 255d, 1);
    }

    public static final class DebugImage {
        public final Image image;
        public final String msg;

        public DebugImage(Image image, String msg) {
            this.image = image;
            this.msg = msg;
        }
    }

    private static volatile DebugImage lastDebugImage;

    public static void debugImage(Image img, String msg) {
        if (Global.debugFeatures()) {
            lastDebugImage = new DebugImage(img, msg);
        }
    }

    public static DebugImage getLastDebugImage() {
        var foo = lastDebugImage;
        lastDebugImage = null;
        return foo;
    }

    public static boolean sameColor(Color a, Color b, FgoClickConsts consts) {
        return diffColor(a.getRed(), b.getRed()) < consts.colorDiffAllowed
            && diffColor(a.getGreen(), b.getGreen()) < consts.colorDiffAllowed
            && diffColor(a.getBlue(), b.getBlue()) < consts.colorDiffAllowed;
    }

    public static boolean sameColorWithLog(Color a, Color b, FgoClickConsts consts) {
        Logger.info("comparing color (" + a + ") / (" + b + ") with " + consts.colorDiffAllowed);
        return sameColor(a, b, consts);
    }

    private static double diffColor(double a, double b) {
        if (a > b) {
            return a - b;
        } else {
            return b - a;
        }
    }
}
