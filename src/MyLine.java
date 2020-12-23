import java.awt.*;
import java.io.Serializable;

public class MyLine extends MyShapes implements Serializable {

    private int oldX, oldY, newX, newY; // Old and new co-ordinates.
    private Color color; // Colour chosen to draw the line in.

    public MyLine(int oldX, int oldY, int newX, int newY, Color color) {

        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
        this.color = color;

    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public int getNewX() {
        return newX;
    }

    public int getNewY() {
        return newY;
    }

    public Color getColor() {
        return color;
    }

}
