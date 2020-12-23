import java.awt.*;
import java.io.Serializable;

public class MyRectangle extends MyShapes implements Serializable {

    
    private int oldX, oldY, w, h; // x and y co-ordinates of where the mouse drag started as well as width and height of shape.
    private Color color; // Colour chosen to draw the shape in.
    private boolean isFilled; // Boolean to check if the user wants to fill the shape.
    
    public MyRectangle(int oldX, int oldY, int w, int h, Color color, boolean isFilled) {
        this.oldX = oldX;
        this.oldY = oldY;
        this.w = w;
        this.h = h;
        this.color = color;
        this.isFilled = isFilled;
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public Color getColor() {
        return color;
    }

    public boolean isFilled() {
        return isFilled;
    }
    
}
