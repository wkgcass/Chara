// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.elithya.parts;

import javafx.scene.Group;
import net.cassite.desktop.chara.chara.elithya.util.JoinedLoopingAnima;
import net.cassite.desktop.chara.chara.elithya.util.LoopingAnima;
import net.cassite.desktop.chara.chara.parts.AbstractPart;
import net.cassite.desktop.chara.graphic.Div;

public abstract class AbstractLeg extends AbstractPart {
    private final Div shoeFront = new Div();
    private final Div leg = new Div();
    private final Div shoeBack = new Div();

    public AbstractLeg(Group parent) {
        super(parent);
        parent.getChildren().addAll(shoeBack, leg, shoeFront);
    }

    private JoinedLoopingAnima joined;

    protected void init() {
        joined = new JoinedLoopingAnima(
            45, 60, 15,
            getPurpleStocking(),
            getBlackStocking(),
            getNoneStocking(),
            getPurpleShoeFront(),
            getPurpleShoeBack(),
            getBlackShoeFront(),
            getBlackShoeBack()
        );
        joined.setFps(28);
    }

    protected abstract LoopingAnima getPurpleStocking();

    protected abstract LoopingAnima getBlackStocking();

    protected abstract LoopingAnima getNoneStocking();

    protected abstract LoopingAnima getPurpleShoeFront();

    protected abstract LoopingAnima getBlackShoeFront();

    protected abstract LoopingAnima getPurpleShoeBack();

    protected abstract LoopingAnima getBlackShoeBack();

    private int stockingState = 0; // 0->init,1->purple,2->black,3->none

    public int getStockingState() {
        return stockingState;
    }

    private int shoeState = 0; // 0->init,1->purple,2->black

    public int getShoeState() {
        return shoeState;
    }

    public void showPurpleStocking() {
        if (stockingState == 1) {
            return;
        }
        stockingState = 1;
        getPurpleStocking().removeFrom(leg);
        getBlackStocking().removeFrom(leg);
        getNoneStocking().removeFrom(leg);
        getPurpleStocking().addTo(leg);
    }

    public void showBlackStocking() {
        if (stockingState == 2) {
            return;
        }
        stockingState = 2;
        getPurpleStocking().removeFrom(leg);
        getBlackStocking().removeFrom(leg);
        getNoneStocking().removeFrom(leg);
        getBlackStocking().addTo(leg);
    }

    public void showNoneStocking() {
        if (stockingState == 3) {
            return;
        }
        stockingState = 3;
        getPurpleStocking().removeFrom(leg);
        getBlackStocking().removeFrom(leg);
        getNoneStocking().removeFrom(leg);
        getNoneStocking().addTo(leg);
    }

    public void showPurpleShoe() {
        if (shoeState == 1) {
            return;
        }
        shoeState = 1;
        getPurpleShoeFront().removeFrom(shoeFront);
        getPurpleShoeBack().removeFrom(shoeBack);
        getBlackShoeFront().removeFrom(shoeFront);
        getBlackShoeBack().removeFrom(shoeBack);
        getPurpleShoeFront().addTo(shoeFront);
        getPurpleShoeBack().addTo(shoeBack);
    }

    public void showBlackShoe() {
        if (shoeState == 2) {
            return;
        }
        shoeState = 2;
        getPurpleShoeFront().removeFrom(shoeFront);
        getPurpleShoeBack().removeFrom(shoeBack);
        getBlackShoeFront().removeFrom(shoeFront);
        getBlackShoeBack().removeFrom(shoeBack);
        getBlackShoeFront().addTo(shoeFront);
        getBlackShoeBack().addTo(shoeBack);
    }

    public void play(Runnable cb) {
        joined.play(cb);
    }

    public void stop() {
        joined.stop();
    }

    public void setFps(double fps) {
        joined.setFps(fps);
    }
}
