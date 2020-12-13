// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.fgo;

public class SelectAction extends Action {
    private int options;

    public SelectAction() {
        super(ActionType.select);
    }

    public int getOptions() {
        return options;
    }

    public SelectAction setOptions(int options) {
        this.options = options;
        return this;
    }

    @Override
    public String toString() {
        return "SelectAction{" +
            "options=" + options +
            '}';
    }
}
