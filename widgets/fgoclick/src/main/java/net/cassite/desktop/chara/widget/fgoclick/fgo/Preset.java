// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.fgo;

import java.util.Collections;
import java.util.List;

public class Preset {
    private List<Servant> servants;
    private List<MasterSkill> masterSkills = Collections.emptyList();
    private Settings settings;

    public List<Servant> getServants() {
        return servants;
    }

    public Preset setServants(List<Servant> servants) {
        this.servants = servants;
        return this;
    }

    public List<MasterSkill> getMasterSkills() {
        return masterSkills;
    }

    public Preset setMasterSkills(List<MasterSkill> masterSkills) {
        this.masterSkills = masterSkills;
        return this;
    }

    public Settings getSettings() {
        return settings;
    }

    public Preset setSettings(Settings settings) {
        this.settings = settings;
        return this;
    }

    @Override
    public String toString() {
        return "Preset{" +
            "servants=" + servants +
            ", masterSkills=" + masterSkills +
            ", settings=" + settings +
            '}';
    }
}
