// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.fgo;

public class MasterSkillOperation extends Operation {
    private MasterSkill skill;
    private Integer select = null;

    public MasterSkillOperation() {
        super(OperationType.masterSkill);
    }

    public MasterSkill getSkill() {
        return skill;
    }

    public MasterSkillOperation setSkill(MasterSkill skill) {
        this.skill = skill;
        return this;
    }

    public Integer getSelect() {
        return select;
    }

    public MasterSkillOperation setSelect(Integer select) {
        this.select = select;
        return this;
    }

    @Override
    public String toString() {
        return "MasterSkillOperation{" +
            "skill=" + skill +
            ", select=" + select +
            '}';
    }
}
