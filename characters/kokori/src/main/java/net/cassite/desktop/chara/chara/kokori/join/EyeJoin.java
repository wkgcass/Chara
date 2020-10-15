// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.join;

import net.cassite.desktop.chara.chara.kokori.parts.Eye;
import net.cassite.desktop.chara.chara.kokori.parts.EyeSocket;
import net.cassite.desktop.chara.chara.kokori.parts.Eyebrow;

public class EyeJoin {
    public final Eye eye;
    public final EyeSocket eyeSocket;
    public final Eyebrow eyebrow;

    public EyeJoin(Eye eye, EyeSocket eyeSocket, Eyebrow eyebrow) {
        this.eye = eye;
        this.eyeSocket = eyeSocket;
        this.eyebrow = eyebrow;
    }

    public void blink() {
        eyeSocket.close(eyeSocket::open);
    }

    public boolean hasHighlight() {
        return eye.hasHighlight();
    }

    public void addHighlight() {
        eye.addHighlight();
    }

    public void removeHighlight() {
        eye.removeHighlight();
    }

    public void track(double x, double y) {
        eye.track(x, y);
    }

    public void restorePosition() {
        eye.restorePosition();
    }

    public void beginAnimatingPupilColor() {
        eye.beginAnimatingPupilColor();
    }

    public void stopAnimatingPupilColor() {
        eye.stopAnimatingPupilColor();
    }

    public void resetPupilColor() {
        eye.resetPupilColor();
    }

    public void zoom(double ratio) {
        eye.zoom(ratio);
    }

    public void move(double x, double y) {
        eye.move(x, y);
    }

    public void close() {
        eyeSocket.close(() -> {
        });
    }

    public void open() {
        eyeSocket.open();
    }
}
