// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.fgo;

public abstract class Action {
    private final ActionType type;
    private int time;

    protected Action(ActionType type) {
        this.type = type;
    }

    public ActionType getType() {
        return type;
    }

    public int getTime() {
        return time;
    }

    public Action setTime(int time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return "Action{" +
            "type=" + type +
            ", time=" + time +
            '}';
    }
}
