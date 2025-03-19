package com.platformer;

// imports
import javafx.scene.Node;

/**
 * Custom {@link Pane} with helper methods to aid in cleaner code.
 *
 * @author Samuel Zhang
 */
public class Pane extends javafx.scene.layout.Pane {

    /**
     * Constructs a {@link Pane}.
     */
    public Pane() {
        super();
    }

    /**
     * Clears the {@link Pane} of all {@link Node}s.
     */
    public void clear() {
        getChildren().clear();
    }

    /**
     * Adds {@link Node}s to the {@link Pane}.
     *
     * @param rectangles varargs {@link Node}s
     */
    public void add(Node... rectangles) {
        getChildren().addAll(rectangles);
    }

    /**
     * Adds {@link TextRectangle}s to the {@link Pane}.
     *
     * @param textBoxes varargs {@link TextRectangle}s
     */
    public void add(TextRectangle... textBoxes) {
        getChildren().addAll(textBoxes);
        getChildren().addAll(
                java.util.Arrays.stream(textBoxes).map(TextRectangle::getText).toList() // creates a List of all the Texts in each TextRectangle
        );
    }

}