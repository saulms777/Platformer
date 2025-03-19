package com.platformer;

import javafx.scene.input.KeyCode;

/**
 * Keybind constants for the game.
 *
 * @author Samuel Zhang
 */
public interface GameKeybinds {

    // control keys
    KeyCode JUMP_KEY = KeyCode.UP;
    KeyCode LEFT_KEY = KeyCode.LEFT;
    KeyCode RIGHT_KEY = KeyCode.RIGHT;
    KeyCode FALL_KEY = KeyCode.DOWN;
    KeyCode INVINCIBLE_KEY = KeyCode.SHIFT;

    // navigation keys
    KeyCode PAUSE_KEY = KeyCode.ESCAPE;
    KeyCode INTERACT_KEY = KeyCode.ENTER;
    KeyCode EXIT_KEY = KeyCode.BACK_SPACE;


}