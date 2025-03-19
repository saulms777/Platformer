package com.platformer;

/**
 * Constants of {@link Rectangle} types for the game.
 *
 * @author Samuel Zhang
 */
public interface RectangleTypes {

    // button types
    char UNINTERACTABLE_TYPE = 'U';
    char GOTO_GAME_TYPE = 'G';
    char LEVEL_SELECTION_TYPE = 'S';
    char OPTIONS_TYPE = 'O';
    char BACK_TYPE = 'B';
    char LEADERBOARD_TYPE = 'L';
    char INSTRUCTIONS_TYPE = 'I';
    char SAVE_DATA_TYPE = 'D';

    // game elements types
    char PLAYER_TYPE = 'p';
    char START_TYPE = 's';
    char CHECKPOINT_TYPE = 'c';
    char FINISH_TYPE = 'f';
    char GROUND_TYPE = 'w';
    char THIN_GROUND_TYPE = 'g';
    char PLANK_TYPE = 'p';
    char LAVA_TYPE = 'l';

}