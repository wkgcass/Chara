// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;

import java.util.Objects;

/**
 * Defines the character.
 */
public interface Chara {
    /**
     * This method is called when the app is ready. It will only be called once on initialization.
     *
     * @param params parameters
     */
    void ready(ReadyParams params);

    /**
     * The mouse moves on the character.<br>
     * The coordinates are relative to the raw image.<br>
     * You are not required to do any transformation on the provided coordinates.
     *
     * @param x x
     * @param y y
     */
    void mouseMove(double x, double y);

    /**
     * Inform that the mouse leaves the app.
     */
    void mouseLeave();

    /**
     * Inform that the app is dragged.
     */
    void dragged();

    /**
     * The mouse clicked on the character.<br>
     * The coordinates are relative to the raw image.<br>
     * You are not required to do any transformation on the provided coordinates.<br>
     * If the mouse did not click on the character, you may call
     * {@link net.cassite.desktop.chara.AppCallback#clickNothing(double, double)} to inform the app.
     *
     * @param x x
     * @param y y
     * @see net.cassite.desktop.chara.AppCallback#clickNothing(double, double)
     */
    void click(double x, double y);

    /**
     * Key is pressed
     *
     * @param e key event
     */
    void keyPressed(KeyEvent e);

    /**
     * Key is released
     *
     * @param e key event
     */
    void keyReleased(KeyEvent e);

    /**
     * Get constant data of the character<br>
     * You MUST NOT change any value on the returned object between any two calls.<br>
     * You should store the returned object in a field and reuse the object everytime this method is called.<br>
     * It's recommended to initialize this object in the constructor.
     *
     * @return constant data of the character.
     */
    Data data();

    /**
     * Release resources held by this character.
     */
    void release();

    /**
     * Take a message from user.
     *
     * @param msg message string
     */
    void takeMessage(String msg);

    /**
     * Try to get debug info from the character.<br>
     * The debug info will be written to the clipboard.
     *
     * @param content the clipboard content object
     * @return true if something is written into the content object, and it will be written into clipboard, false otherwise
     */
    boolean getDebugInfo(ClipboardContent content);

    /**
     * The user sends some debug messages to the character.<br>
     * The debug message should be retrieved from the clipboard.<br>
     * Also note that the there may not contain any information in the current clipboard. You have to check before acting.<br>
     *
     * @param clipboard the clipboard object.
     */
    void takeDebugMessage(Clipboard clipboard);

    class ReadyParams {
        public ReadyParams() {
        }
    }

    class Data {
        /**
         * raw image width
         */
        public final int imageWidth;
        /**
         * raw image height
         */
        public final int imageHeight;
        /**
         * the minimum width that this app can scale into
         */
        public final int minWidth;
        /**
         * the scaled initial width when the user opens this character for the first time
         */
        public final int initialWidth;
        /**
         * the minimum x position of the character
         */
        public final int minX;
        /**
         * the maximum x position of the character
         */
        public final int maxX;
        /**
         * the x position considered to be human visually middle when looking at the top of the character.
         */
        public final int topMiddleX;
        /**
         * the x position considered to be human visually middle when looking at the bottom of the character.
         */
        public final int bottomMiddleX;
        /**
         * the length to {@link #topMiddleX} on x length that message bubbles should pop up
         */
        public final int messageOffsetX;
        /**
         * the y position that message bubbles show up at
         */
        public final int messageAtMinY;
        /**
         * the minimum y position of the character
         */
        public final int minY;
        /**
         * the maximum y position of the character
         */
        public final int maxY;

        /**
         * the character impl supports messages (in other words, the chat feature).
         */
        public final boolean messageSupported;
        /**
         * the character impl may perform active interaction<br>
         * active interaction means that the character may perform attractive things without user interaction.
         */
        public final boolean activeInteractionSupported;

        private Data(int imageWidth,
                     int imageHeight,
                     int minWidth,
                     int initialWidth,
                     int minX,
                     int maxX,
                     int topMiddleX,
                     int bottomMiddleX,
                     int messageOffsetX,
                     int messageAtMinY,
                     int minY,
                     int maxY,
                     boolean messageSupported,
                     boolean activeInteractionSupported) {
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            this.minWidth = minWidth;
            this.initialWidth = initialWidth;
            this.minX = minX;
            this.maxX = maxX;
            this.topMiddleX = topMiddleX;
            this.bottomMiddleX = bottomMiddleX;
            this.messageOffsetX = messageOffsetX;
            this.messageAtMinY = messageAtMinY;
            this.minY = minY;
            this.maxY = maxY;
            this.messageSupported = messageSupported;
            this.activeInteractionSupported = activeInteractionSupported;
        }
    }

    class DataBuilder {
        private Integer imageWidth;
        private Integer imageHeight;
        private Integer minWidth;
        private Integer initialWidth;
        private Integer minX;
        private Integer maxX;
        private Integer topMiddleX;
        private Integer bottomMiddleX;
        private Integer messageOffsetX;
        private Integer messageAtMinY;
        private Integer minY;
        private Integer maxY;
        private boolean messageSupported = false;
        private boolean activeInteractionSupported = false;

        /**
         * Check null values and construct the {@link Data} object
         *
         * @return {@link Data} object
         * @throws NullPointerException non-null field is null
         */
        public Data build() {
            Objects.requireNonNull(imageWidth, "imageWidth");
            Objects.requireNonNull(imageHeight, "imageHeight");
            Objects.requireNonNull(minX, "minX");
            Objects.requireNonNull(maxX, "maxX");
            Objects.requireNonNull(topMiddleX, "topMiddleX");
            Objects.requireNonNull(bottomMiddleX, "bottomMiddleX");
            Objects.requireNonNull(messageOffsetX, "messageOffsetX");
            Objects.requireNonNull(messageAtMinY, "messageAtMinY");
            Objects.requireNonNull(minY, "minY");
            Objects.requireNonNull(maxY, "maxY");
            return new Data(
                imageWidth,
                imageHeight,
                minWidth,
                initialWidth,
                minX,
                maxX,
                topMiddleX,
                bottomMiddleX,
                messageOffsetX,
                messageAtMinY,
                minY,
                maxY,
                messageSupported,
                activeInteractionSupported
            );
        }

        /**
         * @param imageWidth {@link Data#imageWidth}
         * @return this
         */
        public DataBuilder setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
            return this;
        }

        /**
         * @param imageHeight {@link Data#imageHeight}
         * @return this
         */
        public DataBuilder setImageHeight(int imageHeight) {
            this.imageHeight = imageHeight;
            return this;
        }

        /**
         * @param minWidth {@link Data#minWidth}
         * @return this
         */
        public DataBuilder setMinWidth(int minWidth) {
            this.minWidth = minWidth;
            return this;
        }

        /**
         * @param initialWidth {@link Data#initialWidth}
         * @return this
         */
        public DataBuilder setInitialWidth(int initialWidth) {
            this.initialWidth = initialWidth;
            return this;
        }

        /**
         * @param minX {@link Data#minX}
         * @return this
         */
        public DataBuilder setMinX(int minX) {
            this.minX = minX;
            return this;
        }

        /**
         * @param maxX {@link Data#maxX}
         * @return this
         */
        public DataBuilder setMaxX(int maxX) {
            this.maxX = maxX;
            return this;
        }

        /**
         * @param topMiddleX {@link Data#topMiddleX}
         * @return this
         */
        public DataBuilder setTopMiddleX(int topMiddleX) {
            this.topMiddleX = topMiddleX;
            return this;
        }

        /**
         * @param bottomMiddleX {@link Data#bottomMiddleX}
         * @return this
         */
        public DataBuilder setBottomMiddleX(int bottomMiddleX) {
            this.bottomMiddleX = bottomMiddleX;
            return this;
        }

        /**
         * @param messageOffsetX {@link Data#messageOffsetX}
         * @return this
         */
        public DataBuilder setMessageOffsetX(int messageOffsetX) {
            this.messageOffsetX = messageOffsetX;
            return this;
        }

        /**
         * @param messageAtMinY {@link Data#messageAtMinY}
         * @return this
         */
        public DataBuilder setMessageAtMinY(int messageAtMinY) {
            this.messageAtMinY = messageAtMinY;
            return this;
        }

        /**
         * @param minY {@link Data#minY}
         * @return this
         */
        public DataBuilder setMinY(int minY) {
            this.minY = minY;
            return this;
        }

        /**
         * @param maxY {@link Data#minY}
         * @return this
         */
        public DataBuilder setMaxY(int maxY) {
            this.maxY = maxY;
            return this;
        }

        /**
         * @param messageSupported {@link Data#messageSupported}
         * @return this
         */
        public DataBuilder setMessageSupported(boolean messageSupported) {
            this.messageSupported = messageSupported;
            return this;
        }

        /**
         * @param activeInteractionSupported {@link Data#activeInteractionSupported}
         * @return this
         */
        public DataBuilder setActiveInteractionSupported(boolean activeInteractionSupported) {
            this.activeInteractionSupported = activeInteractionSupported;
            return this;
        }
    }
}
