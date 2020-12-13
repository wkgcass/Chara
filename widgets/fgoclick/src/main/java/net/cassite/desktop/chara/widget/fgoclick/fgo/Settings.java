// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.fgo;

public class Settings {
    private int totalBattles;
    private boolean confirmDialog;
    private String support;
    private String useApple;
    private boolean loop;

    public int getTotalBattles() {
        return totalBattles;
    }

    public void setTotalBattles(int totalBattles) {
        this.totalBattles = totalBattles;
    }

    public boolean isConfirmDialog() {
        return confirmDialog;
    }

    public void setConfirmDialog(boolean confirmDialog) {
        this.confirmDialog = confirmDialog;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getUseApple() {
        return useApple;
    }

    public void setUseApple(String useApple) {
        this.useApple = useApple;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    public String toString() {
        return "Settings{" +
            "totalBattles=" + totalBattles +
            ", confirmDialog=" + confirmDialog +
            ", support='" + support + '\'' +
            ", useApple='" + useApple + '\'' +
            ", loop=" + loop +
            '}';
    }
}
