import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

public class ImageReader {
    public static ImageReader instance = null;

    public static ImageReader getInstance() {
        if (instance == null) instance = new ImageReader();
        return instance;
    }

    public BufferedImage loadImage(String path) {
        BufferedImage newImage = null;
        try {
            newImage = ImageIO.read(new File(path));
        } catch (Exception e) {
        }
        return newImage;
    }
}
