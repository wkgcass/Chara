// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.graphic;

import net.cassite.desktop.chara.ThreadUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Encapsulation of multiple {@link Anima} instances and animate through them
 */
public class AnimaList {
    private final BiConsumer<Anima, Anima> swapAnimaFunc;
    private final List<Anima> animaList;
    private Iterator<Anima> animaIterator = null;
    private Anima lastAnima = null;
    private Runnable finishCallback;

    /**
     * Constructor
     *
     * @param swapAnimaFunc the function to remove first nullable argument from parent and add second argument into parent
     * @param animaList     the list of animations to run
     */
    public AnimaList(BiConsumer<Anima, Anima> swapAnimaFunc, Anima... animaList) {
        this.swapAnimaFunc = swapAnimaFunc;
        if (animaList.length == 0) {
            throw new IllegalArgumentException("no anima specified");
        }
        this.animaList = Arrays.asList(animaList);

        swapAnimaFunc.accept(null, animaList[0]);
        lastAnima = animaList[0];
    }

    /**
     * begin to animate
     */
    public void play() {
        if (animaIterator != null) {
            // is animating
            return;
        }
        ThreadUtils.get().runOnFX(() -> {
            if (animaIterator != null) {
                return;
            }

            animaIterator = animaList.iterator();
            recursivePlay();
        });
    }

    private void recursivePlay() {
        if (!animaIterator.hasNext()) {
            // finishes
            var cb = finishCallback;
            animaIterator = null;
            ThreadUtils.get().runOnFX(() -> {
                if (cb != null) {
                    cb.run();
                }
            });
            return;
        }
        Anima anima = animaIterator.next();
        swapAnimaFunc.accept(lastAnima, anima);
        lastAnima = anima;
        anima.resetTo(0).setPauseCallbackOnce(this::recursivePlay).play();
    }

    /**
     * Set the callback function which will be called after all {@link Anima}s are played
     *
     * @param finishCallback callback function
     * @return <code>this</code>
     */
    public AnimaList setFinishCallback(Runnable finishCallback) {
        this.finishCallback = finishCallback;
        return this;
    }
}
