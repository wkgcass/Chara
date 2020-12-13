// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.fgo;

public class Skill {
    private Servant servant;
    private String name;
    private Action action;
    private int index;

    public Servant getServant() {
        return servant;
    }

    public Skill setServant(Servant servant) {
        this.servant = servant;
        return this;
    }

    public String getName() {
        return name;
    }

    public Skill setName(String name) {
        this.name = name;
        return this;
    }

    public Action getAction() {
        return action;
    }

    public Skill setAction(Action action) {
        this.action = action;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public Skill setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public String toString() {
        return "Skill{" +
            "servant=" + (servant == null ? "null" : servant.getName()) +
            ", name='" + name + '\'' +
            ", action=" + action +
            ", index=" + index +
            '}';
    }
}
