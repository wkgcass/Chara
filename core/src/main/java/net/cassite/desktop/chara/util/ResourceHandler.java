// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.util;

import vproxybase.util.Callback;

import java.io.InputStream;

public class ResourceHandler {
    /**
     * this filed will be used to calculate the progress of the loading bar.<br>
     * note that: the <code>progressRatio</code> of images of a model is <code>1</code>.<br>
     * {@link javafx.scene.control.ProgressBar#setProgress(double)}.
     */
    public final double progressRatio;
    /**
     * the suffix entry of the resource.<br>
     * it will be prepended with the model/plugin name when retrieving entry.
     */
    public final String entrySuffix;
    /**
     * handler of the resource.<br>
     * the handler takes an inputstream object and a callback object.<br>
     * when the handling finishes, the {@link Callback#succeeded(Object)} or {@link Callback#failed(Throwable)} should be called.<br>
     * exceptions are also allowed to be thrown, which will terminate the loading process
     */
    public final BiConsumerThrowsException<InputStream, Callback<Void, Exception>> handler;

    /**
     * Construct with <code>progressRatio</code> = 1
     *
     * @param entrySuffix {@link #entrySuffix}
     * @param handler     {@link #handler}
     */
    public ResourceHandler(String entrySuffix,
                           BiConsumerThrowsException<InputStream, Callback<Void, Exception>> handler) {
        this(1, entrySuffix, handler);
    }

    /**
     * Constructor
     *
     * @param progressRatio {@link #progressRatio}
     * @param entrySuffix   {@link #entrySuffix}
     * @param handler       {@link #handler}
     */
    public ResourceHandler(double progressRatio,
                           String entrySuffix,
                           BiConsumerThrowsException<InputStream, Callback<Void, Exception>> handler) {
        this.progressRatio = progressRatio;
        this.entrySuffix = entrySuffix;
        this.handler = handler;
    }
}
