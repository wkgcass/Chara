// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.fgo;

import java.util.Collections;
import java.util.List;

public class Servant {
    private String name;
    private List<Skill> skills = Collections.emptyList();
    private NoblePhantasm noblePhantasm;
    private int index;

    public String getName() {
        return name;
    }

    public Servant setName(String name) {
        this.name = name;
        return this;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public Servant setSkills(List<Skill> skills) {
        this.skills = skills;
        return this;
    }

    public NoblePhantasm getNoblePhantasm() {
        return noblePhantasm;
    }

    public Servant setNoblePhantasm(NoblePhantasm noblePhantasm) {
        this.noblePhantasm = noblePhantasm;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public Servant setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public String toString() {
        return "Servant{" +
            "name='" + name + '\'' +
            ", skills=" + skills +
            ", noblePhantasm=" + noblePhantasm +
            ", index=" + index +
            '}';
    }
}
