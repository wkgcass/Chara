// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.fgo;

import java.util.Collections;
import java.util.List;

public class Configuration {
    private Preset preset;
    private List<Operation> operations = Collections.emptyList();

    public Preset getPreset() {
        return preset;
    }

    public Configuration setPreset(Preset preset) {
        this.preset = preset;
        return this;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public Configuration setOperations(List<Operation> operations) {
        this.operations = operations;
        return this;
    }

    @Override
    public String toString() {
        return "Configuration{" +
            "preset=" + preset +
            ", operations=" + operations +
            '}';
    }
}
