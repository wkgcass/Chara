// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.fgo;

public class NoblePhantasmOperation extends Operation {
    private NoblePhantasm noblePhantasm;

    public NoblePhantasmOperation() {
        super(OperationType.noblePhantasm);
    }

    public NoblePhantasm getNoblePhantasm() {
        return noblePhantasm;
    }

    public NoblePhantasmOperation setNoblePhantasm(NoblePhantasm noblePhantasm) {
        this.noblePhantasm = noblePhantasm;
        return this;
    }

    @Override
    public String toString() {
        return "NoblePhantasmOperation{" +
            "noblePhantasm=" + noblePhantasm +
            '}';
    }
}
