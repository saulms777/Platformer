package com.platformer;

// imports
import javafx.scene.paint.Color;
import java.util.Set;

/**
 * Constants of all {@link Rectangle} colours for the game.
 */
public interface RectangleColors {

    // display colours
    Color BACKGROUND_COLOR = Color.WHITESMOKE;
    Color GOTO_GAME_COLOR = Color.BURLYWOOD;
    Color LEVEL_SELECTION_COLOR = Color.LIGHTBLUE;
    Color OPTIONS_COLOR = Color.LIGHTGREEN;
    Color LEVEL_COLOR = Color.LIGHTSALMON;
    Color BACK_COLOR = Color.DEEPSKYBLUE;
    Color LEADERBOARD_COLOR = Color.AQUAMARINE;
    Color SAVE_DATA_COLOR = Color.SANDYBROWN;
    Color INSTRUCTIONS_COLOR = Color.ANTIQUEWHITE;
    Color PAUSE_COLOR = Color.LIGHTYELLOW;
    Color DEATH_BG_COLOR = Color.CRIMSON;
    Color FINISH_DISPLAY_COLOR = Color.BISQUE;
    Color SAVE_DISPLAY_COLOR = Color.LIGHTCYAN;
    Color LEADERBOARD_DISPLAY_COLOR = Color.GOLD;

    // level block colours
    Color PLAYER_COLOR = Color.BLUE;
    Color SPAWN_COLOR = Color.GREEN;
    Color CHECKPOINT_COLOR = Color.ORANGE;
    Color FINISH_COLOR = Color.CYAN;
    Color GROUND_COLOR = Color.DIMGRAY;
    Color PLANK_COLOR = Color.SADDLEBROWN;
    Color LAVA_COLOR = Color.RED;

    /**
     * {@link Set} of all colours that cannot be collided with.
     */
    Set<Color> NO_COLLISION_COLORS = Set.of(
            BACKGROUND_COLOR,
            LEVEL_SELECTION_COLOR,
            GOTO_GAME_COLOR,
            OPTIONS_COLOR,
            LEVEL_COLOR,
            BACK_COLOR,
            SAVE_DATA_COLOR,
            INSTRUCTIONS_COLOR,
            LEADERBOARD_COLOR,
            LEADERBOARD_DISPLAY_COLOR,
            SPAWN_COLOR,
            CHECKPOINT_COLOR,
            FINISH_COLOR
    );

}