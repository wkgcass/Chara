// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.statem;

import net.cassite.desktop.chara.widget.fgoclick.robot.ExecutableAction;
import net.cassite.desktop.chara.widget.fgoclick.fgo.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Statem {
    private final Configuration configuration;

    public Statem(Configuration configuration) {
        this.configuration = configuration;
    }

    private int state; // 0: battle scene, 1: attack scene
    private int cardsSelectedCount = 0;
    private final boolean[] cardsSelected = new boolean[5];
    private int battleCount = 1;
    private final List<NoblePhantasm> noblePhantasmList = new ArrayList<>(3);

    public List<ExecutableAction> compile() {
        LinkedList<ExecutableAction> ls = new LinkedList<>();
        for (var op : configuration.getOperations()) {
            if (state == 0) {
                if (op.getType() == OperationType.skill) {
                    var skillOp = ((SkillOperation) op);
                    var skill = skillOp.getSkill();
                    ls.add(new ClickSkill(skill));
                    if (configuration.getPreset().getSettings().isConfirmDialog()) {
                        ls.add(new ClickOk());
                    }
                    if (skill.getAction().getType() == ActionType.select) {
                        ls.add(new SelectItem(
                            ((SelectAction) skill.getAction()).getOptions(),
                            skillOp.getSelect()
                        ));
                    }
                    ls.add(new WaitForSkill(skill));
                } else if (op.getType() == OperationType.masterSkill) {
                    var masterSkillOp = (MasterSkillOperation) op;
                    var skill = masterSkillOp.getSkill();
                    ls.add(new ClickMasterItem());
                    ls.add(new ClickMasterSkill(skill));
                    if (configuration.getPreset().getSettings().isConfirmDialog()) {
                        ls.add(new ClickOk());
                    }
                    if (skill.getAction().getType() == ActionType.select) {
                        ls.add(new SelectItem(
                            ((SelectAction) skill.getAction()).getOptions(),
                            masterSkillOp.getSelect()
                        ));
                    }
                    ls.add(new WaitForMasterSkill(skill));
                } else if (op.getType() == OperationType.noblePhantasm) {
                    var npOp = (NoblePhantasmOperation) op;
                    ls.add(new ClickAttack());
                    ls.add(new ClickNoblePhantasm(npOp.getNoblePhantasm()));
                    noblePhantasmList.add(npOp.getNoblePhantasm());
                    cardsSelectedCount += 1;
                    state = 1;
                } else {
                    throw new IllegalStateException("cannot run op " + op + " in the battle scene");
                }
            } else {
                assert state == 1;
                if (op.getType() == OperationType.nextBattle) {
                    for (int i = 0; i < 3 - cardsSelectedCount; ++i) {
                        ls.add(new ClickAnyCard());
                    }
                    cardsSelectedCount = 0;
                    Arrays.fill(cardsSelected, false);
                    // wait for noble phantasm
                    for (var np : noblePhantasmList) {
                        ls.add(new WaitForNoblePhantasm(np));
                    }
                    noblePhantasmList.clear();
                    // wait for battle
                    ls.add(new WaitForNextBattle(configuration.getPreset().getSettings().getTotalBattles(), battleCount));
                    ++battleCount;
                    state = 0;
                } else {
                    throw new IllegalStateException("cannot run op " + op + " in the attack scene");
                }
            }
        }
        return ls;
    }
}
