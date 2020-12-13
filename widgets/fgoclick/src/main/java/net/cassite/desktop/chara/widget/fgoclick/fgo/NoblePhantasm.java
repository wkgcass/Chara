// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.fgo;

public class NoblePhantasm {
    private Servant servant;
    private String name;
    private NormalAction action;

    public Servant getServant() {
        return servant;
    }

    public NoblePhantasm setServant(Servant servant) {
        this.servant = servant;
        return this;
    }

    public String getName() {
        return name;
    }

    public NoblePhantasm setName(String name) {
        this.name = name;
        return this;
    }

    public NormalAction getAction() {
        return action;
    }

    public NoblePhantasm setAction(NormalAction action) {
        this.action = action;
        return this;
    }

    @Override
    public String toString() {
        return "NoblePhantasm{" +
            "servant=" + (servant == null ? "null" : servant.getName()) +
            ", name='" + name + '\'' +
            ", action=" + action +
            '}';
    }
}
