// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.fgo;

public abstract class Operation {
    private final OperationType type;

    public Operation(OperationType type) {
        this.type = type;
    }

    public OperationType getType() {
        return type;
    }
}
