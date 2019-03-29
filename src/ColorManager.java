import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ColorManager {
    private static int spectrumNum = 256;
    Random rand = new Random();

    public static ColorManager instance = null;

    public static ColorManager getInstance() {
        if (instance == null) instance = new ColorManager();
        return instance;
    }

    private int getColor(String color, BufferedImage image, int x, int y) {
        Color c = new Color(image.getRGB(x, y));
        switch (color) {
            case "red":
                return c.getRed();
            case "green":
                return c.getGreen();
            case "blue":
                return c.getBlue();
            default:
                return new Integer(0);
        }
    }

    public int getRed(BufferedImage image, int x, int y) {
        return getColor("red", image, x , y);
    }

    public int getGreen(BufferedImage image, int x, int y) {
        return getColor("green", image, x , y);
    }

    public int getBlue(BufferedImage image, int x, int y) {
        return getColor("blue", image, x , y);
    }

    public Color generateColor(int r, int g, int b){
        return new Color(r, g, b);
    }

    public int[][] randomImage(int length, int width) {
        int[][] image = new int[length][width];
        for (int i=0; i<length; i++) {
            for (int j=0; j<width; j++) {
                image[i][j] = rand.nextInt(spectrumNum);
                System.out.print(image[i][j] + " ");
            }
            System.out.println("");
        }
        return image;
    }

    public int[] randomData(int width, int length) {
        int[] image = new int[length * width];
        for (int i=0; i<length; i++) {
            for (int j=0; j<width; j++) {
                image[i * width + j] = rand.nextInt(spectrumNum);
                //System.out.print(image[i * width + j] + " ");
            }
            //System.out.println("");
        }
        //System.out.println(length + " " + width);
        return image;
    }
}
