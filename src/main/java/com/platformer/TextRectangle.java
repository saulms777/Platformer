package com.platformer;

// imports
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * {@link TextRectangle} that inherits from {@link Rectangle}. A {@link TextRectangle} is a
 * {@link Rectangle} with a {@link Text} as an attribute. When displayed, the {@link Text} is also shown.
 *
 * @author Samuel Zhang
 */
public class TextRectangle extends Rectangle {

    /**
     * x coordinate of the {@link TextRectangle}.
     */
    private final int x;

    /**
     * y coordinate of the {@link TextRectangle}.
     */
    private final int y;

    /**
     * Width of the {@link TextRectangle}.
     */
    private final int width;

    /**
     * Height of the {@link TextRectangle}.
     */
    private final int height;

    /**
     * {@link Text} attached to the {@link TextRectangle}.
     */
    private final Text text;

    /**
     * {@code boolean} value determining if the {@link Text} in the {@link TextRectangle} is centered or not
     */
    private final boolean centered;

    /**
     * Creates a new {@link TextRectangle} with all specified attributes.
     *
     * @param x x coordinate of the {@link TextRectangle}
     * @param y y coordinate of the {@link TextRectangle}
     * @param width width of the {@link TextRectangle}
     * @param height height of the {@link TextRectangle}
     * @param color colour of the {@link TextRectangle}
     * @param type type of the {@link TextRectangle}
     * @param text text to display on the {@link TextRectangle}
     * @param centered if the text is centered
     */
    public TextRectangle(int x, int y, int width, int height, Color color, char type, String text, boolean centered) {
        super(x, y, width, height, color, type);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.centered = centered;

        this.text = new Text();
        this.text.setFont(javafx.scene.text.Font.font(null, javafx.scene.text.FontWeight.NORMAL, height * 0.6));
        setText(text);
    }

    /**
     * @return the {@link Text} of the {@link TextRectangle}
     */
    public Text getText() {
        return text;
    }

    /**
     * Updates the message displayed on the {@link Text} of the {@link TextRectangle}.
     *
     * @param str new message to display
     */
    public void setText(String str) {
        text.setText(str);
        text.setY(y + height - text.getBoundsInLocal().getHeight() / 2);

        if (centered) text.setX(x + (width - text.getBoundsInLocal().getWidth()) / 2);
        else text.setX(x + 5);
    }

}