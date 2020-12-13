// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.fgo;

public class SkillOperation extends Operation {
    private Skill skill;
    private int select;

    public SkillOperation() {
        super(OperationType.skill);
    }

    public Skill getSkill() {
        return skill;
    }

    public SkillOperation setSkill(Skill skill) {
        this.skill = skill;
        return this;
    }

    public int getSelect() {
        return select;
    }

    public SkillOperation setSelect(int select) {
        this.select = select;
        return this;
    }

    @Override
    public String toString() {
        return "SkillOperation{" +
            "skill=" + skill +
            ", select=" + select +
            '}';
    }
}
