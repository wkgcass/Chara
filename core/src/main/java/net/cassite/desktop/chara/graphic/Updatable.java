// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

public interface Updatable {
    /**
     * The updating function for {@link HZ} to call
     *
     * @param current current time millis
     */
    void update(long current);
}
