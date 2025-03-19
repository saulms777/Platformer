package com.platformer;

// imports
import javafx.util.Pair;
import static com.platformer.GameValues.PLATFORM_SIZE;
import static com.platformer.RectangleColors.*;
import static com.platformer.RectangleTypes.UNINTERACTABLE_TYPE;

/**
 * Constants of all {@link TextRectangle}s in the game.
 * They are grouped into arrays relating to their use.
 */
public interface TextBoxes {

    /**
     * {@link TextRectangle}s for the instructions display.
     */
    TextRectangle[] INSTRUCTIONS_TEXT = {
            new TextRectangle(
                    300, 180,
                    750, 60,
                    BACKGROUND_COLOR, UNINTERACTABLE_TYPE,
                    "Welcome to Cube Jump!", true
            ),
            new TextRectangle(
                    300, 240,
                    750, 30,
                    BACKGROUND_COLOR, UNINTERACTABLE_TYPE,
                    "Use arrow keys to move, and ENTER to interact with a button.", true
            ),
            new TextRectangle(
                    300, 270,
                    750, 30,
                    BACKGROUND_COLOR, UNINTERACTABLE_TYPE,
                    "In a level, press ESCAPE to pause the game.", true
            ),
            new TextRectangle(
                    14 * PLATFORM_SIZE, 12 * PLATFORM_SIZE,
                    10 * PLATFORM_SIZE, PLATFORM_SIZE,
                    BACKGROUND_COLOR, UNINTERACTABLE_TYPE,
                    "Spawn: will spawn here", false
            ),
            new TextRectangle(
                    14 * PLATFORM_SIZE, 13 * PLATFORM_SIZE,
                    10 * PLATFORM_SIZE, PLATFORM_SIZE,
                    BACKGROUND_COLOR, UNINTERACTABLE_TYPE,
                    "Checkpoint: will become", false
            ),
            new TextRectangle(
                    14 * PLATFORM_SIZE, 14 * PLATFORM_SIZE,
                    10 * PLATFORM_SIZE, PLATFORM_SIZE,
                    BACKGROUND_COLOR, UNINTERACTABLE_TYPE,
                    "spawn when touched", false
            ),
            new TextRectangle(
                    14 * PLATFORM_SIZE, 15 * PLATFORM_SIZE,
                    10 * PLATFORM_SIZE, PLATFORM_SIZE,
                    BACKGROUND_COLOR, UNINTERACTABLE_TYPE,
                    "Finish: end of level", false
            ),
            new TextRectangle(
                    26 * PLATFORM_SIZE + 5, 12 * PLATFORM_SIZE,
                    10 * PLATFORM_SIZE, PLATFORM_SIZE,
                    BACKGROUND_COLOR, UNINTERACTABLE_TYPE,
                    "Wall", false
            ),
            new TextRectangle(
                    26 * PLATFORM_SIZE + 5, 14 * PLATFORM_SIZE - 15,
                    10 * PLATFORM_SIZE, PLATFORM_SIZE,
                    BACKGROUND_COLOR, UNINTERACTABLE_TYPE,
                    "Lava: will kill you", false
            ),
            new TextRectangle(
                    26 * PLATFORM_SIZE + 5, 15 * PLATFORM_SIZE - 10,
                    10 * PLATFORM_SIZE, PLATFORM_SIZE,
                    BACKGROUND_COLOR, UNINTERACTABLE_TYPE,
                    "Ground", false
            ),
            new TextRectangle(
                    26 * PLATFORM_SIZE + 5, 16 * PLATFORM_SIZE - 10,
                    10 * PLATFORM_SIZE, PLATFORM_SIZE,
                    BACKGROUND_COLOR, UNINTERACTABLE_TYPE,
                    "Plank: can vertically go through", false
            ),
    };

    /**
     * {@link TextRectangle}s for the pause display.
     */
    TextRectangle[] PAUSE_TEXT = {
            new TextRectangle(
                    525, 250,
                    300, 100,
                    PAUSE_COLOR, UNINTERACTABLE_TYPE,
                    "Paused", true
            ),
            new TextRectangle(
                    525, 350,
                    300, 30,
                    PAUSE_COLOR, UNINTERACTABLE_TYPE,
                    "Press ENTER to continue", true
            ),
            new TextRectangle(
                    525, 380,
                    300, 30,
                    PAUSE_COLOR, UNINTERACTABLE_TYPE,
                    "Press BACKSPACE to exit", true
            ),
    };

    /**
     * {@link TextRectangle} of the death count display.
     */
    TextRectangle DEATHCOUNT_DISPLAY = new TextRectangle(
            10, 10,
            120, 30,
            DEATH_BG_COLOR, UNINTERACTABLE_TYPE,
            "Deaths: 0", true
    );

    /**
     * {@link TextRectangle}s for the finish display.
     */
    TextRectangle[] FINISH_TEXT = {
            new TextRectangle(
                    450, 300,
                    450, 100,
                    FINISH_DISPLAY_COLOR, UNINTERACTABLE_TYPE,
                    "Level finished!", true
            ),
            new TextRectangle(
                    450, 400,
                    450, 30,
                    FINISH_DISPLAY_COLOR, UNINTERACTABLE_TYPE,
                    "Press ENTER to go back to level list", true
            ),
    };

    /**
     * {@link TextRectangle}s for the save data display.
     */
    TextRectangle[] SAVE_DATA_TEXT = {
            new TextRectangle(
                    495, 300,
                    360, 100,
                    SAVE_DISPLAY_COLOR, UNINTERACTABLE_TYPE,
                    "Data saved", true
            ),
            new TextRectangle(
                    495, 400,
                    360, 30,
                    SAVE_DISPLAY_COLOR, UNINTERACTABLE_TYPE,
                    "Press BACKSPACE to exit", true
            ),
    };

    /**
     * Default method that takes a 2D array of {@link Pair}s and creates
     * an array of {@link TextRectangle}s matching the leaderboard.
     *
     * @param leaderboard leaderboard data
     * @return {@link TextRectangle}s to display
     */
    default TextRectangle[] LEADERBOARD(Pair<String, Integer>[][] leaderboard) {
        // initalize values
        TextRectangle[] textBoxes = new TextRectangle[60];
        int i = 0;
        for (int levelNum = 1; levelNum < 10; levelNum++) {
            int x = 60 + levelNum * 15 + (levelNum - 1) * 120;
            textBoxes[i++] = new TextRectangle( // level number display
                    x, 210,
                    120, 30,
                    LEADERBOARD_DISPLAY_COLOR, UNINTERACTABLE_TYPE,
                    "Level " + levelNum, true
            );

            // top scores display
            for (int placement = 0; placement < 5; placement++) {
                int deaths = leaderboard[levelNum][placement].getValue();
                if (deaths == Integer.MAX_VALUE) break; // do not display if default value
                textBoxes[i++] = new TextRectangle(
                        x, 240 + 20 * placement,
                        120, 20,
                        LEADERBOARD_DISPLAY_COLOR, UNINTERACTABLE_TYPE,
                        leaderboard[levelNum][placement].getKey() + ": " + deaths, false
                );
            }
        }

        TextRectangle[] trimmed = new TextRectangle[i];
        System.arraycopy(textBoxes, 0, trimmed, 0, i);
        return trimmed;
    }

}