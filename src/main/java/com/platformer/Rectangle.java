package com.platformer;

// imports
import javafx.scene.paint.Color;

/**
 * Custom {@link javafx.scene.shape.Rectangle} with utility
 * methods aid in more efficient and readable code.
 *
 * @author Samuel Zhang
 */
public class Rectangle extends javafx.scene.shape.Rectangle {

    /**
     * Type of the {@link Rectangle}.
     *
     * @see RectangleTypes
     */
    private final char type;

    /**
     * Creates a {@link Rectangle} with specified dimensions, colour, and type.
     *
     * @param width width of {@link Rectangle}
     * @param height height of {@link Rectangle}
     * @param color colour of of {@link Rectangle}
     * @param type type of of {@link Rectangle}
     */
    public Rectangle(int width, int height, Color color, char type) {
        super(width, height, color);
        this.type = type;
    }

    /**
     * Creates a {@link Rectangle} with specified coordinates, dimensions, colour, and type.
     *
     * @param x x coordinate of {@link Rectangle}
     * @param y y coordinate of {@link Rectangle}
     * @param width width of {@link Rectangle}
     * @param height height of {@link Rectangle}
     * @param color colour of {@link Rectangle}
     * @param type type of {@link Rectangle}
     */
    public Rectangle(int x, int y, int width, int height, Color color, char type) {
        this(width, height, color, type);
        setCoords(new int[]{x, y});
    }

    /**
     * @return type of {@link Rectangle} as a {@code char}
     */
    public char getType() {
        return type;
    }

    /**
     * @return the x coordinate of the left side of the {@link Rectangle}
     */
    public int getLeft() {
        return (int) getTranslateX();
    }

    /**
     * @return the x coordinate of the right side of the {@link Rectangle}
     */
    public int getRight() {
        return (int) (getTranslateX() + getWidth());
    }

    /**
     * @return the y coordinate of the top side of the {@link Rectangle}
     */
    public int getTop() {
        return (int) getTranslateY();
    }

    /**
     * @return the y coordinate of the bottom side of the {@link Rectangle}
     */
    public int getBottom() {
        return (int) (getTranslateY() + getHeight());
    }

    /**
     * @param coords coordinates to set the {@link Rectangle} to
     */
    public void setCoords(int[] coords) {
        setTranslateX(coords[0]);
        setTranslateY(coords[1]);
    }

    /**
     * Moves the {@link Rectangle} by some amount in the x-axis.
     *
     * @param v steps to move
     */
    public void moveX(int v) {
        setTranslateX(getTranslateX() + v);
    }

    /**
     * Moves the {@link Rectangle} by some amount in the y-axis.
     *
     * @param v steps to move
     */
    public void moveY(int v) {
        setTranslateY(getTranslateY() + v);
    }

    /**
     * Checks if the {@link Rectangle} is a specific colour.
     *
     * @param color colour to check if matching
     * @return if {@link Rectangle} is the same colour
     */
    public boolean isColor(Color color) {
        return getFill().equals(color);
    }

}