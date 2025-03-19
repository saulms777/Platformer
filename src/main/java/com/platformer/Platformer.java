package com.platformer;

// javafx imports
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Pair;

// utility imports
import java.io.*;
import java.util.*;

/**
 * Main class for Platformer game. Uses {@code javafx} library to create a working GUI.
 *
 * @author Samuel Zhang
 */
@SuppressWarnings("unchecked")
public class Platformer extends Application
        implements GameValues, GameLevels, GameKeybinds, RectangleColors, RectangleTypes, TextBoxes {

    /**
     * Root layer of the UI.
     */
    private final Pane appRoot = new Pane();

    /**
     * Game layer of the UI, where the player interacts.
     */
    private final Pane gameLayer = new Pane();

    /**
     * Top layer of UI for pop-ups.
     */
    private final Pane topLayer = new Pane();

    /**
     * A stack to help navigation through the pages. When a page is loaded,
     * it is added to the navigator. When the back button is pressed, the
     * level is popped from the stack.
     */
    private final PageStack pageNavigator = new PageStack();

    /**
     * Map of keys being currently pressed. True if the key is being pressed and vice versa.
     */
    private final HashMap<KeyCode, Boolean> keyMap = new HashMap<>();

    /**
     * Main game loop, implemented using a {@link AnimationTimer}.
     * The method {@link Platformer#updateGame()} is run each tick of the timer.
     */
    private final AnimationTimer gameTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            updateGame();
        }
    };

    /**
     * Loop that runs during a pause, using an {@link AnimationTimer}.
     * The method being run each tick is {@link Platformer#updatePause()}.
     */
    private final AnimationTimer pauseTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            updatePause();
        }
    };

    /**
     * Reason for pause.
     *
     * @see PauseTypes
     */
    private PauseTypes pauseReason;

    /**
     * Width of the level being displayed.
     */
    private int levelWidth;

    /**
     * Height of the level being displayed.
     */
    private int levelHeight;

    /**
     * Main LinkedList of all platforms in the {@link Platformer#gameLayer}.
     */
    private final RectangleList platforms = new RectangleList();

    /**
     * The {@link Rectangle} that represents the player.
     */
    private Rectangle player;

    /**
     * Spawnpoint of player.
     */
    private int[] spawn;

    /**
     * The player's velocity as a vector in two dimensions.
     */
    private Point2D playerVelocity = new Point2D(0, 0);

    /**
     * If the player can jump.
     */
    private boolean canJump = false;

    /**
     * If the player is invincible.
     */
    private boolean invincible = false;

    /**
     * Start point of a level.
     */
    private Rectangle startPoint;

    /**
     * Records the death count of each level.
     */
    private final int[] deathCounts = new int[10];

    /**
     * Records the completion of each level.
     */
    private final boolean[] finishedLevels = new boolean[10];

    /**
     * Leaderboard of all levels.
     */
    private final Pair<String, Integer>[][] leaderboard = new Pair[10][5];

    /**
     * Initializes the content for the application. Loads instructions,
     * adds the main parts of the GUI, and initializes leaderboard.
     */
    private void initContent() {
        // show instructions
        loadLevel(INSTRUCTIONS, true);

        // add layers of GUI to app
        appRoot.add(
                new Rectangle(WINDOW_WIDTH, WINDOW_HEIGHT, BACKGROUND_COLOR, UNINTERACTABLE_TYPE), // background
                gameLayer,
                topLayer
        );

        // initialize leaderboard
        for (int levelNum = 1; levelNum < 10; levelNum++)
            for (int placement = 0; placement < 5; placement++)
                leaderboard[levelNum][placement] = new Pair<>("Default", Integer.MAX_VALUE);
    }

    /**
     * Loads all of a level. Adds the page to the navigator if specified.
     *
     * @param level level to load
     * @param addNavigator whether to add page to navigator stack
     */
    private void loadLevel(int level, boolean addNavigator) {
        // reset screen
        if (addNavigator)
            pageNavigator.add(level);
        gameLayer.clear();
        topLayer.clear();
        platforms.clear();

        // load all blocks on screen
        String[] currentLevel = PAGES[level];
        levelWidth = currentLevel[0].length() * PLATFORM_SIZE;
        levelHeight = currentLevel.length * PLATFORM_SIZE;
        for (int i = 0; i < currentLevel.length; i++)
            for (int j = 0; j < currentLevel[i].length(); j++)
                loadBlocks(
                        currentLevel[i].charAt(j),
                        j * PLATFORM_SIZE, i * PLATFORM_SIZE
                );

        // set spawnpoint of special levels
        if (level == INSTRUCTIONS) {
            spawn = new int[]{22 * PLATFORM_SIZE, 21 * PLATFORM_SIZE};
            gameLayer.add(INSTRUCTIONS_TEXT);
        }
        else if (level == MAIN_MENU)
            spawn = new int[]{22 * PLATFORM_SIZE, 21 * PLATFORM_SIZE};
        else if (level == LEVEL_SELECTION)
            spawn = new int[]{3 * PLATFORM_SIZE, 21 * PLATFORM_SIZE};
        else if (level == OPTIONS_MENU)
            spawn = new int[]{22 * PLATFORM_SIZE, 18 * PLATFORM_SIZE};
        else if (level == LEADERBOARDS) {
            spawn = new int[]{22 * PLATFORM_SIZE, 17 * PLATFORM_SIZE};
            gameLayer.add(LEADERBOARD(leaderboard));
        }

        // add death count display
        if (level < 10) {
            topLayer.add(DEATHCOUNT_DISPLAY);
            finishedLevels[level] = false;
            deathCounts[pageNavigator.top()] = 0;
        }

        // initialize the player
        initPlayer();
    }

    /**
     * Takes in a type of {@link Rectangle} and the coordinates, creates a
     * {@link Rectangle} at the coordinates, and adds it to the platforms list.
     *
     * @param type type of {@link Rectangle}
     * @param x x coordinate
     * @param y y coordinate
     */
    private void loadBlocks(char type, int x, int y) {
        switch (type) {
            // empty space
            case ' ' -> {}

            // level start point
            case START_TYPE -> {
                spawn = new int[]{x, y};
                platforms.add(startPoint = createRectangle(
                        x + PLATFORM_SIZE / 4, y + PLATFORM_SIZE / 4,
                        PLATFORM_SIZE / 2, PLATFORM_SIZE / 2,
                        SPAWN_COLOR, START_TYPE
                ));
            }

            // level checkpoint
            case CHECKPOINT_TYPE -> platforms.add(createRectangle(
                    x + PLATFORM_SIZE / 4, y + PLATFORM_SIZE / 4,
                    PLATFORM_SIZE / 2, PLATFORM_SIZE / 2,
                    CHECKPOINT_COLOR, CHECKPOINT_TYPE
            ));

            // level finish
            case FINISH_TYPE -> platforms.add(createRectangle(
                    x + PLATFORM_SIZE / 4, y + PLATFORM_SIZE / 4,
                    PLATFORM_SIZE / 2, PLATFORM_SIZE / 2,
                    FINISH_COLOR, FINISH_TYPE
            ));

            // thick ground
            case GROUND_TYPE -> platforms.add(createRectangle(
                    x, y, PLATFORM_SIZE, PLATFORM_SIZE,
                    GROUND_COLOR, GROUND_TYPE
            ));

            // thin ground
            case THIN_GROUND_TYPE -> platforms.add(createRectangle(
                    x, y, PLATFORM_SIZE, PLATFORM_SIZE / 5,
                    GROUND_COLOR, THIN_GROUND_TYPE
            ));

            // wooden plank
            case PLANK_TYPE -> platforms.add(createRectangle(
                    x, y, PLATFORM_SIZE, PLATFORM_SIZE / 5,
                    PLANK_COLOR, PLANK_TYPE
            ));

            // lava
            case LAVA_TYPE -> platforms.add(createRectangle(
                    x, y + PLATFORM_SIZE * 4 / 5,
                    PLATFORM_SIZE, PLATFORM_SIZE / 5,
                    LAVA_COLOR, LAVA_TYPE
            ));

            // main menu button
            case GOTO_GAME_TYPE -> platforms.add(createRectangle(
                    x, y,
                    4 * PLATFORM_SIZE,
                    GOTO_GAME_COLOR, GOTO_GAME_TYPE,
                    "Go to Game"
            ));

            // level selection button
            case LEVEL_SELECTION_TYPE -> platforms.add(createRectangle(
                    x, y,
                    3 * PLATFORM_SIZE,
                    LEVEL_SELECTION_COLOR, LEVEL_SELECTION_TYPE,
                    "Levels"
            ));

            // options button
            case OPTIONS_TYPE -> platforms.add(createRectangle(
                    x, y,
                    3 * PLATFORM_SIZE,
                    OPTIONS_COLOR, OPTIONS_TYPE,
                    "Options"
            ));

            // back button
            case BACK_TYPE -> platforms.add(createRectangle(
                    x, y,
                    3 * PLATFORM_SIZE,
                    BACK_COLOR, BACK_TYPE,
                    "Back"
            ));

            // leaderboard button
            case LEADERBOARD_TYPE -> platforms.add(createRectangle(
                    x, y,
                    4 * PLATFORM_SIZE,
                    LEADERBOARD_COLOR, LEADERBOARD_TYPE,
                    "Leaderboard"
            ));

            // save data button
            case SAVE_DATA_TYPE -> platforms.add(createRectangle(
                    x, y,
                    4 * PLATFORM_SIZE,
                    SAVE_DATA_COLOR, SAVE_DATA_TYPE,
                    "Save Data"
            ));

            // instructions button
            case INSTRUCTIONS_TYPE -> platforms.add(createRectangle(
                    x, y,
                    4 * PLATFORM_SIZE,
                    INSTRUCTIONS_COLOR, INSTRUCTIONS_TYPE,
                    "Instructions"
            ));

            // level buttons
            default -> {
                if (type < '1' || type > '9')
                    throw new RuntimeException("Illegal blocks in code: " + type);

                platforms.add(createRectangle(
                        x, y,
                        3 * PLATFORM_SIZE,
                        LEVEL_COLOR, type,
                        "Level " + type
                ));
            }
        }
    }

    /**
     * Initializes the player. Creates the player, adds scrolling functionality,
     * and sets the player to spawn position. See {@link javafx.beans.value.ChangeListener}
     * for more information about the lambda used for the scrolling.
     */
    private void initPlayer() {
        player = createRectangle(
                spawn[0], spawn[1],
                PLAYER_SIZE, PLAYER_SIZE,
                PLAYER_COLOR, PLAYER_TYPE
        );
        player.translateXProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    int offset = newValue.intValue();
                    if (offset > WINDOW_WIDTH / 2 && offset < levelWidth - WINDOW_WIDTH / 2)
                        gameLayer.setLayoutX((double) WINDOW_WIDTH / 2 - offset);
                }
        );
        player.translateYProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    int offset = newValue.intValue();
                    if (offset > WINDOW_HEIGHT / 2 && offset < levelHeight - WINDOW_HEIGHT / 2)
                        gameLayer.setLayoutY((double) WINDOW_HEIGHT / 2 - offset);
                }
        );
        playerDeath(false);
    }

    /**
     * Updates the main game. Checks for key press and updates things such as death count display,
     * gravity, and block collision and interaction
     */
    private void updateGame() {
        // detect key press
        if (isPressed(JUMP_KEY) && player.getTop() >= 5)
            jumpPlayer();
        if (isPressed(LEFT_KEY) && player.getLeft() >= 5)
            movePlayerX(-5);
        if (isPressed(RIGHT_KEY) && player.getRight() <= levelWidth - 5)
            movePlayerX(5);
        if (isPressed(FALL_KEY))
            fallThroughPlank();
        if (isPressed(PAUSE_KEY) && isLevel()) {
            pauseReason = PauseTypes.LEVEL_PAUSE;
            gameTimer.stop();
            pauseTimer.start();
            topLayer.add(PAUSE_TEXT);
        }

        // player invicibility check
        invincible = isPressed(INVINCIBLE_KEY);

        // deathcount display
        if (isLevel())
            DEATHCOUNT_DISPLAY.setText(
                    deathCounts[pageNavigator.top()] < 1000 ? "Deaths: " + deathCounts[pageNavigator.top()] : "You suck"
            );

        // gravity
        if (playerVelocity.getY() < 10)
            playerVelocity = playerVelocity.add(0, 1);
        movePlayerY((int) playerVelocity.getY());

        // check collision and interactions
        checkBlockType();
    }

    /**
     * Creates a {@link Rectangle} and adds it to the screen.
     *
     * @param x x coordinate of {@link Rectangle}
     * @param y y coordinate of {@link Rectangle}
     * @param width width of {@link Rectangle}
     * @param height height of {@link Rectangle}
     * @param color colour of {@link Rectangle}
     * @param type type of {@link Rectangle}
     * @return the {@link Rectangle} that was created
     */
    private Rectangle createRectangle(int x, int y, int width, int height, Color color, char type) {
        Rectangle rectangle = new Rectangle(x, y, width, height, color, type);
        gameLayer.add(rectangle);
        return rectangle;
    }

    /**
     * Creates a {@link TextRectangle}
     *
     * @param x x coordinate of {@link TextRectangle}
     * @param y y coordinate of {@link TextRectangle}
     * @param width width of {@link TextRectangle}
     * @param color colour of {@link TextRectangle}
     * @param type type of {@link TextRectangle}
     * @param text text on the {@link TextRectangle}
     * @return the {@link TextRectangle} that was created
     */
    private Rectangle createRectangle(int x, int y, int width, Color color, char type, String text) {
        TextRectangle textRectangle = new TextRectangle(
                x - (width - PLATFORM_SIZE) / 2, y - 10,
                width, PLATFORM_SIZE,
                color, type,
                text, true
        );
        gameLayer.add(textRectangle);
        return textRectangle;
    }

    /**
     * Checks if the current screen is a level or not.
     *
     * @return If the current page is a level.
     */
    private boolean isLevel() {
        return pageNavigator.top() < 10;
    }

    /**
     * Checks if a key is being pressed or not.
     *
     * @param key Key to check
     * @return If the key is being pressed
     */
    private boolean isPressed(KeyCode key) {
        return keyMap.getOrDefault(key, false);
    }

    /**
     * Checks if a platform is being "interacted" with.
     *
     * @param platform Intersecting platform
     * @param buttonColor Color of platform
     * @return If there is interaction
     */
    private boolean buttonPressed(Rectangle platform, Color buttonColor) {
        return platform.isColor(buttonColor) && isPressed(INTERACT_KEY);
    }

    /**
     * Checks player collision and interactions with blocks.
     * Carry out different actions based on each.
     */
    private void checkBlockType() {
        for (final Rectangle platform : platforms) { // platform
            boolean touchingPlatform = player.getBoundsInParent().intersects(platform.getBoundsInParent());
            if (!touchingPlatform) continue;

            // goes to main menu
            if (buttonPressed(platform, GOTO_GAME_COLOR))
                loadLevel(MAIN_MENU, true);

                // goes to level selection page
            else if (buttonPressed(platform, LEVEL_SELECTION_COLOR))
                loadLevel(LEVEL_SELECTION, true);

                // goes to options page
            else if (buttonPressed(platform, OPTIONS_COLOR))
                loadLevel(OPTIONS_MENU, true);

                // goes to leaderboard page
            else if (buttonPressed(platform, LEADERBOARD_COLOR))
                loadLevel(LEADERBOARDS, true);

                // loads a level
            else if (buttonPressed(platform, LEVEL_COLOR))
                loadLevel(platform.getType() - '0', true);

                // goes back a page
            else if (buttonPressed(platform, BACK_COLOR)) {
                pageNavigator.pop();
                loadLevel(pageNavigator.top(), false);
            }

            // saves data to leaderboard
            else if (buttonPressed(platform, SAVE_DATA_COLOR)) {
                loadData();
                mergeData();
                saveData();
                gameTimer.stop();
                pauseTimer.start();
                topLayer.add(SAVE_DATA_TEXT);
                pauseReason = PauseTypes.SAVE_PAUSE;
            }

            // goes to instructions page
            else if (buttonPressed(platform, INSTRUCTIONS_COLOR)) {
                pageNavigator.clear();
                loadLevel(INSTRUCTIONS, true);
            }

            // sets spawnpoint and updates checkpoint color
            else if (platform.isColor(CHECKPOINT_COLOR)) {
                spawn = new int[]{platform.getLeft() - PLATFORM_SIZE / 4, platform.getTop() - PLATFORM_SIZE / 4};
                startPoint.setFill(CHECKPOINT_COLOR);
                platform.setFill(SPAWN_COLOR);
                startPoint = platform;
            }

            // finish level
            else if (platform.isColor(FINISH_COLOR)) {
                finishedLevels[pageNavigator.top()] = true;
                gameTimer.stop();
                pauseTimer.start();
                topLayer.add(FINISH_TEXT);
                pauseReason = PauseTypes.FINISH_PAUSE;
            }

            // kills the player if not invincible
            else if (platform.isColor(LAVA_COLOR) && !invincible)
                playerDeath(true);

                // return if any interaction for efficiency
            else continue;
            return;
        }
    }

    /**
     * Allows the player to jump once. Do not jump if in the air or already jumped.
     */
    private void jumpPlayer() {
        if (!canJump) return; // exit if cannot jump

        for (final Rectangle platform : platforms) { // platform
            boolean touchingPlatform = player.getBoundsInParent().intersects(platform.getBoundsInParent());
            boolean underPlatform = player.getTop() == platform.getBottom();
            if (!touchingPlatform || underPlatform) continue; // exit if cannot jump

            // player jumps
            playerVelocity = new Point2D(0, -JUMP_HEIGHT);
            canJump = false;
            return;
        }
    }

    /**
     * Moves the player in the x direction. If the player hits a wall stop moving.
     *
     * @param x Steps to move
     */
    private void movePlayerX(int x) {
        boolean movingRight = x > 0;
        for (int i = 0; i < Math.abs(x); i++) { // step
            for (final Rectangle platform : platforms) { // platform
                boolean touchingPlatform = player.getBoundsInParent().intersects(platform.getBoundsInParent());
                boolean onPlatform = player.getBottom() == platform.getTop();
                // TT player is on top of a platform                    continue
                // TF player is on the side of a platform               go to wall check
                // FT player is on the same y-level but not touching    go to wall check
                // FF player is in the air                              continue
                if (touchingPlatform == onPlatform) continue; // logical XNOR

                // don't collide with no collision blocks
                if (NO_COLLISION_COLORS.contains((Color) platform.getFill())) continue;

                // if hits wall stop moving
                boolean onPlatformLeft = movingRight && player.getRight() == platform.getLeft();
                boolean onPlatformRight = !movingRight && player.getLeft() == platform.getRight();
                if (onPlatformLeft || onPlatformRight) return;
            }

            // update player x position
            player.moveX(movingRight ? 1 : -1);
        }
    }

    /**
     * Moves the player in the y direction. If the player falls onto the ground or hits roof stop moving.
     *
     * @param y Steps to move
     */
    private void movePlayerY(int y) {
        boolean movingDown = y > 0;
        for (int i = 0; i < Math.abs(y); i++) { // step
            for (final Rectangle platform : platforms) { // platform
                boolean touchingPlatform = player.getBoundsInParent().intersects(platform.getBoundsInParent());
                if (!touchingPlatform) continue;

                // don't collide with no collision blocks
                if (NO_COLLISION_COLORS.contains((Color) platform.getFill())) continue;

                boolean onSide = player.getRight() == platform.getLeft() || player.getLeft() == platform.getRight();
                boolean underPlatform = player.getTop() == platform.getBottom() && !onSide;
                boolean onPlatform = player.getBottom() == platform.getTop() && !onSide;
                boolean isPlank = platform.isColor(PLANK_COLOR);

                // hits bottom of platform
                if (!movingDown && underPlatform && !isPlank) {
                    playerVelocity = new Point2D(0, 0); // reset y-velocity
                    return;
                }

                // lands on ground
                else if (movingDown && onPlatform) {
                    canJump = true;
                    return;
                }
            }

            // update player y position
            player.moveY(movingDown ? 1 : -1);
        }
    }

    /**
     * Makes the player fall through a plank. If the block is not a plank do not fall through.
     */
    private void fallThroughPlank() {
        for (final Rectangle platform : platforms) { // platform
            boolean touchingPlatform = player.getBoundsInParent().intersects(platform.getBoundsInParent());
            boolean onPlatform = player.getBottom() == platform.getTop();
            boolean onSide = player.getRight() == platform.getLeft() || player.getLeft() == platform.getRight();
            boolean isPlank = platform.isColor(PLANK_COLOR);
            if (!touchingPlatform || !onPlatform || onSide || !isPlank) continue; // exit if not plank

            // fall through plank
            player.moveY(1);
            return;
        }
    }

    /**
     * Kills the player, and sets it back to spawn.
     * Updates the screen scrolling to match.
     *
     * @param fromLava if the death was from lava
     */
    private void playerDeath(boolean fromLava) {
        // go to spawn point
        player.setCoords(spawn);

        // increment death count
        if (fromLava && isLevel())
            deathCounts[pageNavigator.top()]++;

        // update x scrolling
        int xOffset = (int) player.getTranslateX();
        if (xOffset < WINDOW_WIDTH / 2)
            gameLayer.setLayoutX(0);
        else if (xOffset > levelWidth - WINDOW_WIDTH / 2)
            gameLayer.setLayoutX(WINDOW_WIDTH - levelWidth);
        else
            gameLayer.setLayoutX((double) WINDOW_WIDTH / 2 - xOffset);

        // update y scrolling
        int yOffset = (int) player.getTranslateY();
        if (yOffset < WINDOW_HEIGHT / 2)
            gameLayer.setLayoutY(0);
        else if (yOffset > levelHeight - WINDOW_HEIGHT / 2)
            gameLayer.setLayoutY(WINDOW_HEIGHT - levelHeight);
        else
            gameLayer.setLayoutY((double) WINDOW_HEIGHT / 2 - yOffset);
    }

    /**
     * Updates the game when it is paused.
     * Will exit or unpause based on key press.
     */
    private void updatePause() {
        // skip if keys are not pressed
        if (!isPressed(INTERACT_KEY) && !isPressed(EXIT_KEY)) return;

        // reasons for pause
        boolean isFinish = pauseReason.equals(PauseTypes.FINISH_PAUSE);
        boolean isSave = pauseReason.equals(PauseTypes.SAVE_PAUSE);

        // exit page
        if (isPressed(EXIT_KEY) || isFinish) {
            pageNavigator.pop();
            loadLevel(pageNavigator.top(), false);
            isSave = false;
        }

        // if block is save do not continue
        if (isSave) return;

        // unpause
        pauseTimer.stop();
        gameTimer.start();

        // update display
        topLayer.clear();
        if (isLevel()) topLayer.add(DEATHCOUNT_DISPLAY);
    }

    /**
     * Loads the data from the {@code leaderboard.txt} file.
     */
    private void loadData() {
        // create file
        File file = new File("leaderboard.txt");
        try { if (file.createNewFile()) return; }
        catch (IOException e) { throw new RuntimeException("i/o exception idk"); }

        // create file reader
        Scanner reader;
        try { reader = new Scanner(file); }
        catch (FileNotFoundException e) { throw new RuntimeException("this exception should be unreachable"); }

        for (int levelNum = 1; levelNum < 10; levelNum++) { // level
            int placement = 0;
            String line = "";
            boolean emptyLine = false;
            try { line = reader.nextLine(); }
            catch (NoSuchElementException e) { emptyLine = true; }

            // read data
            while (!(emptyLine || line.equals(""))) {
                int index = line.lastIndexOf(": ");
                leaderboard[levelNum][placement++] = new Pair<>(
                        line.substring(0, index), Integer.parseInt(line.substring(index + 2))
                );

                try { line = reader.nextLine(); }
                catch (NoSuchElementException e) { emptyLine = true; }
            }
        }

        // close reader
        reader.close();
    }

    /**
     * Merges current death count data with leaderboard. If the
     * leaderboard is full, numbers will be pushed out.
     */
    private void mergeData() {
        for (int levelNum = 1; levelNum < 10; levelNum++) { // level
            // sort leaderboard
            leaderboard[levelNum] = mergeSort(leaderboard[levelNum]);

            // check if level was completed
            if (!finishedLevels[levelNum]) continue;

            // merge data
            int deaths = deathCounts[levelNum];
            for (int placement = 0; placement < 5; placement++) { // top 5
                int placementDeaths = leaderboard[levelNum][placement].getValue();
                boolean sameUser = System.getProperty("user.name").equals(leaderboard[levelNum][placement].getKey());

                // if duplicate values break
                if (deaths == placementDeaths && sameUser) break;
                else if (deaths < placementDeaths) {
                    // add new data and shift array
                    for (int i = placement; i < 4; i++)
                        leaderboard[levelNum][i + 1] = leaderboard[levelNum][i];
                    leaderboard[levelNum][placement] = new Pair<>(System.getProperty("user.name"), deaths);
                    break;
                }
            }
        }
    }

    /**
     * Saves leaderboard data to the file, using a {@link PrintWriter}. Empty
     * leaderboard values are ignored, and levels are separated by an empty line.
     */
    private void saveData() {
        // create fiie writer
        File file = new File("leaderboard.txt");
        PrintWriter writer;
        try { writer = new PrintWriter(file); }
        catch (FileNotFoundException e) { throw new RuntimeException("this exception should be unreachable"); }

        // write to file
        for (int levelNum = 1; levelNum < 10; levelNum++) {
            for (int placement = 0; placement < 5; placement++) {
                int deaths = leaderboard[levelNum][placement].getValue();
                if (deaths == Integer.MAX_VALUE) break;
                writer.println(leaderboard[levelNum][placement].getKey() + ": " + deaths);
            }
            writer.println();
        }

        // close writer
        writer.close();
    }

    /**
     * Takes an array of pairs and sorts it using merge sort, a recursive sorting algorithm.
     * Merge sort works by splitting the array in half recursively, then merging the broken arrays
     * back with each other recursively while sorting it.
     *
     * @param list Array to sort
     * @return Sorted array
     */
    private static Pair<String, Integer>[] mergeSort(Pair<String, Integer>[] list) {
        int size = list.length;
        if (size == 1) return list;

        // split array
        int median = (size + 1) / 2;
        Pair<String, Integer>[] list1 = new Pair[median];
        System.arraycopy(list, 0, list1, 0, median);
        Pair<String, Integer>[] list2 = new Pair[size - median];
        System.arraycopy(list, median, list2, 0, size - median);

        // recursive call and return
        return merge(mergeSort(list1), mergeSort(list2));
    }

    /**
     * Merge method for merge sort. Takes two separate arrays and returns a combined sorted array.
     *
     * @param list1 An array
     * @param list2 Another array
     * @return Sorted combined array
     */
    private static Pair<String, Integer>[] merge(Pair<String, Integer>[] list1, Pair<String, Integer>[] list2) {
        // variable initialization
        int size1 = list1.length;
        int size2 = list2.length;
        Pair<String, Integer>[] merged = new Pair[size1 + size2];

        // iterate over both arrays, adding them to the combined array
        int index = 0;
        int current1 = 0;
        int current2 = 0;
        while (current1 < size1 && current2 < size2) {
            if (list1[current1].getValue() < list2[current2].getValue()) {
                merged[index] = new Pair<>(list1[current1].getKey(), list1[current1].getValue());
                current1++;
            } else {
                merged[index] = new Pair<>(list2[current2].getKey(), list2[current2].getValue());
                current2++;
            }
            index++;
        }

        // cleanup extra elements
        while (current1 < size1) {
            merged[index] = new Pair<>(list1[current1].getKey(), list1[current1].getValue());
            current1++;
            index++;
        }
        while (current2 < size2) {
            merged[index] = new Pair<>(list2[current2].getKey(), list2[current2].getValue());
            current2++;
            index++;
        }

        return merged;
    }

    /**
     * Initializes the game. Intializes values, creates the screen, and sets the timers.
     *
     * @param stage Stage for the application
     */
    @Override
    public void start(Stage stage) {
        initContent();

        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keyMap.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keyMap.put(event.getCode(), false));

        stage.setTitle("Platformer");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        gameTimer.start();
        pauseTimer.stop();
    }

    /**
     * Entry point of program. Launches {@code javafx} GUI and calls on internal methods.
     *
     * @param args Unused
     */
    public static void main(String... args) {
        launch(args);
    }

}