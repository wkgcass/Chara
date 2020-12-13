// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.fgo;

public class MasterSkill {
    private String name;
    private Action action;
    private int index;

    public String getName() {
        return name;
    }

    public MasterSkill setName(String name) {
        this.name = name;
        return this;
    }

    public Action getAction() {
        return action;
    }

    public MasterSkill setAction(Action action) {
        this.action = action;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public MasterSkill setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public String toString() {
        return "MasterSkill{" +
            "name='" + name + '\'' +
            ", action=" + action +
            ", index=" + index +
            '}';
    }
}
