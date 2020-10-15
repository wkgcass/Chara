// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.parts;

import javafx.scene.Group;

public interface Eye {
    boolean hasHighlight();

    void addHighlight();

    void removeHighlight();

    void beginAnimatingPupilColor();

    void stopAnimatingPupilColor();

    void zoom(double ratio);

    void resetPupilColor();

    void track(double x, double y);

    void restorePosition();

    Group getRoot();

    void move(double relativeX, double relativeY);
}
