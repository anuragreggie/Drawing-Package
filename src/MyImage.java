import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyImage extends MyShapes implements Serializable {

    private int oldX, newY; // New co-ordinates to place image at.
    private transient BufferedImage scaledImage; // Scaled image is made transient so it is not serialized in the normal way.
    // This is done because buffered images are not serializable.

    // Constructor for the image.
    public MyImage(BufferedImage scaledImage, int oldX, int newY) {

        this.oldX = oldX;
        this.newY = newY;
        this.scaledImage = scaledImage;

    }

    public BufferedImage getScaledImage() {
        return scaledImage;
    }

    public int getOldX() {
        return oldX;
    }

    public int getNewY() {
        return newY;
    }

    // Modified code from https://stackoverflow.com/questions/15058663/how-to-serialize-an-object-that-includes-bufferedimages by Sam Barnum
    // Manually write out byte data.
    private void writeObject(ObjectOutputStream out) throws IOException {

        out.defaultWriteObject();
        ImageIO.write(this.scaledImage, "png", out);

    }
    // Manually read in bytes.
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

        in.defaultReadObject();
        this.scaledImage = ImageIO.read(in);
    }


}
