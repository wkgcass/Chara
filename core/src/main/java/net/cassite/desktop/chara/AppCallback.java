// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara;

import net.cassite.desktop.chara.i18n.Words;

/**
 * The interface for <code>Chara</code> impl to call methods in the <code>App</code>.
 */
public interface AppCallback {
    /**
     * Set the chara points.<br>
     * When calling this method, the chara point bars will show and animate to the correct position.<br>
     * You may also set the color of bars in the <code>points</code> object.
     *
     * @param points an object containing the chara points
     */
    void setCharaPoints(CharaPoints points);

    /**
     * Wakeup message stage and show message bubbles with the contents inside <code>words</code>.
     *
     * @param words words to show
     */
    void showMessage(Words words);

    /**
     * Similar to {@link AppCallback#showMessage(Words)} but the input is an array of strings
     *
     * @param msg words to show
     */
    default void showMessage(String... msg) {
        showMessage(new Words(msg, null));
    }

    /**
     * Clear all messages in the message stage
     */
    void clearAllMessages();

    /**
     * A util function for performing active interaction<br>
     * If active interaction is allowed, the callback function will be called.<br>
     * Otherwise nothing happens.
     *
     * @param cb the callback function containing the active interaction.
     */
    void activeInteraction(Runnable cb);

    /**
     * Alert the base app that user had clicked nothing.<br>
     * The base app will use the coordinates to do some generic operations.
     *
     * @param x x
     * @param y y
     */
    void clickNothing(double x, double y);

    /**
     * Move the primary stage with specific length to move.<br>
     * The delta parameters should be the length before scaling, in another words, the actual length relative to the original image.<br>
     * For example, if the current scale is 0.5, and specified in arguments to move 100 pixels, the stage will move 50 pixels.<br>
     * The anchor parameters should be the real positions, they are used as anchors which help you animate the stage back to
     * exactly the original position.
     *
     * @param anchorX the base x position
     * @param deltaX  length to move in X direction
     * @param anchorY the base y position
     * @param deltaY  length to move in Y direction
     */
    void moveWindow(double anchorX, double deltaX, double anchorY, double deltaY);

    /**
     * Retrieve current window position
     *
     * @return array representing the position: [x, y]
     */
    double[] getWindowPosition();

    /**
     * Make the app to be draggable or not
     *
     * @param draggable true to be draggable, false otherwise
     */
    void setDraggable(boolean draggable);

    /**
     * Enable or disable the <code>GlobalScreen</code> feature<br>
     * The <code>GlobalScreen</code> will keep handling events when the app is not focused or mouse not on the app.
     *
     * @param globalScreen true to enable, false otherwise
     */
    void setGlobalScreen(boolean globalScreen);

    /**
     * Always show chara points bars or not. Setting this option to true will override {@link #setAlwaysHideBar(boolean)}.
     *
     * @param alwaysShowBar true to show, false otherwise
     */
    void setAlwaysShowBar(boolean alwaysShowBar);

    /**
     * Always hide chara points bar or not. Setting this option to true will overrdie {@link #setAlwaysShowBar(boolean)}.
     *
     * @param alwaysHideBar true to hide, false otherwise
     */
    void setAlwaysHideBar(boolean alwaysHideBar);
}
