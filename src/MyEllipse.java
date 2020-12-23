import java.awt.*;
import java.io.Serializable;

public class MyEllipse extends MyShapes implements Serializable {

    private int oldX, oldY, newX, newY; // Old and new co-ordinates.
    private Color color; // Colour chosen to draw the shape in.
    private boolean isFilled; // Boolean to check if the user wants to fill the shape.

    public MyEllipse(int oldX, int oldY, int newX, int newY, Color color, boolean isFilled) {

        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
        this.color = color;
        this.isFilled = isFilled;

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

    public boolean isFilled() {
        return isFilled;
    }
}
