// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

public class CharaPoints {
    /**
     * bondPoint after modification
     */
    public final double bondCurrent;
    /**
     * bondPoint before modification
     */
    public final double bondPrevious;
    /**
     * desirePoint after modification
     */
    public final double desireCurrent;
    /**
     * desirePoint before modification
     */
    public final double desirePrevious;

    /**
     * the color used on bond bar when bond point increases
     */
    public String bondIncrColor = "#ff5263";
    /**
     * the color used on bond bar when bond point decreases
     */
    public String bondDecrColor = "#4e4f5e";
    /**
     * the color used on desire bar when desire point increases
     */
    public String desireIncrColor = "#ff9519";
    /**
     * the color used on desire bar when desire point decreases
     */
    public String desireDecrColor = "#169bed";

    /**
     * Constructor
     *
     * @param bondCurrent    {@link #bondCurrent}
     * @param bondPrevious   {@link #bondPrevious}
     * @param desireCurrent  {@link #desireCurrent}
     * @param desirePrevious {@link #desirePrevious}
     */
    public CharaPoints(double bondCurrent, double bondPrevious, double desireCurrent, double desirePrevious) {
        this.bondCurrent = bondCurrent;
        this.bondPrevious = bondPrevious;
        this.desireCurrent = desireCurrent;
        this.desirePrevious = desirePrevious;
    }
}
