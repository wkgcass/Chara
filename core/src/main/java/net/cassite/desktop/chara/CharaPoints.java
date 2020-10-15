// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

public class CharaPoints {
    public final double bondCurrent;
    public final double bondPrevious;
    public final double desireCurrent;
    public final double desirePrevious;

    public String bondIncrColor = "#ff5263";
    public String bondDecrColor = "#4e4f5e";
    public String desireIncrColor = "#ff9519";
    public String desireDecrColor = "#169bed";

    public CharaPoints(double bondCurrent, double bondPrevious, double desireCurrent, double desirePrevious) {
        this.bondCurrent = bondCurrent;
        this.bondPrevious = bondPrevious;
        this.desireCurrent = desireCurrent;
        this.desirePrevious = desirePrevious;
    }
}
