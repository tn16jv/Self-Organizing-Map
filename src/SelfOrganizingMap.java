import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Random;

public class SelfOrganizingMap {
    private ArrayList<SNode> nodes;
    private Topology toplogy;
    private double learningRate, neighSize;
    private int epochs;
    private int xSize, ySize, zSize;
    int[][] data;
    Random rand = new Random();
    // Ordered pairs
    private int [][] pairArray;
    // Weights fitted during training
    private double [][] weights;
    // Final node assigned to each observation in training
    private int finalNodes[];
    // The distance from each data point to the final node
    private double finalDistances[];

    public SelfOrganizingMap(int xSize, int ySize, double neighSize, double learningRate, int epochs) {
        this.neighSize = neighSize;
        this.learningRate = learningRate;
        this.epochs = epochs;
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = 3;
        this.toplogy = new Topology(ySize, xSize, zSize);
        nodes = new ArrayList<SNode>(xSize * ySize);

        ColorManager manager = ColorManager.getInstance();
        //data = new int[3][xSize * ySize];
        //data[0] = manager.randomData(xSize, ySize);
        //data[1] = manager.randomData(xSize, ySize);
        //data[2] = manager.randomData(xSize, ySize);
        int[][] data2 = {{255, 255, 0, 0, 0, 255},
                {0, 255, 255, 255, 0, 0},
                {0, 0, 0, 255, 255, 255}};
        data = data2;
        initialWeights();
    }

    private SNode createNode() {
        SNode aNode = new SNode();
        ArrayList<Double> min = new ArrayList<Double>();
        ArrayList<Double> max = new ArrayList<Double>();
        ArrayList<Double> weights = new ArrayList<Double>();
        for (int i=0; i<data.length; i++) {
            min.add((double)i);
            max.add((double)i);
            for (int j=0; j<data[0].length; j++) {
                double currentVal = data[0][j];
                if (currentVal < min.get(i))
                    min.set(i, currentVal);
                if (currentVal > max.get(i))
                    max.set(i, currentVal);
            }
            weights.add(rand.nextDouble() * (max.get(i) - min.get(i)) + min.get(i));
        }
        aNode.weights = weights;
        return aNode;
    }

    public void initialWeights() {
        nodes = new ArrayList<SNode>();
        for (int i=0; i<xSize*ySize; i++) {
            nodes.add(createNode());
        }
    }

    public double euclideanDistance() {

        return 1.0;
    }

    public int bestMatchingUnit(SNode targetNode, int randIndex) {
        int index = 0;
        double currentClosest = Double.MAX_VALUE;
        ArrayList<Integer> indices = new ArrayList<Integer>();
        for (int i=0; i<nodes.size(); i++) {
            double thisDistance = targetNode.distanceSimilar(nodes.get(i));
            if (thisDistance < currentClosest) {
                currentClosest = thisDistance;
                indices.clear();
                indices.add(i);
            }
        }
        return indices.get(0);
    }

    public void getNeighbourhood() {

    }

    public void train() {
        for (int i=0; i<epochs; i++) {
            learningRate = learningRate * (1 - (double)i/epochs);
            neighSize = neighSize * (1 - (double)i/epochs);
            double epsilon = 0.1 * Math.pow(0.005/.1, (double)i/epochs);

            //int randIndex = rand.nextInt(nodes.size());
            //SNode currentCell = nodes.get(randIndex);
            int randIndex = rand.nextInt(data[0].length);
            SNode currentCell = new SNode((double)data[0][randIndex],
                    (double)data[1][randIndex], (double)data[2][randIndex]);
            int closestIndex = bestMatchingUnit(currentCell, randIndex);
            for (int j=0; j<nodes.size(); j++) {
                double cellDistance = toplogy.latticeDistance2D(closestIndex, j);
                double hrs = Math.exp(- (cellDistance*cellDistance)/(2.0 * neighSize * neighSize));
                nodes.get(j).adjustWeights(currentCell, hrs);
            }
        }
    }

    public void compress(BufferedImage image) {
        int tiles = 8;
        ArrayList<Color> rgb = ColorManager.getInstance().imageRGB(image);


        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        SNode tmpNode, targetNode;
        int index, bmu, red, green, blue;
        for (int i=0; i<image.getHeight(); i++) {
            for (int j=0; j<image.getWidth(); j++) {
                index = i * image.getWidth() + j;
                tmpNode = new SNode((double)rgb.get(index).getRed(),
                        (double)rgb.get(index).getGreen(), (double)rgb.get(index).getBlue());
                bmu = bestMatchingUnit(tmpNode, 0);
                targetNode = nodes.get(bmu);
                red = targetNode.weights.get(0).intValue();
                green = targetNode.weights.get(1).intValue();
                blue = targetNode.weights.get(2).intValue();
                Color newColor = new Color(red, green, blue);
                bufferedImage.setRGB(j, i, newColor.getRGB());
            }
        }
        File output = new File("result.png");
        try {
            ImageIO.write(bufferedImage, "png", output);
        } catch (Exception e) {}
    }

    private void printWeights() {
        System.out.println("Weights");
        for (int i=0; i<nodes.size(); i++) {
            System.out.println(nodes.get(i).weights.get(0) + " " +
                    nodes.get(i).weights.get(1) + " " + nodes.get(i).weights.get(2));
        }
    }

    public ArrayList<ArrayList<Integer>> getWeights() {
        ArrayList<ArrayList<Integer>> weights = new ArrayList<ArrayList<Integer>>();
        Integer aWeight;
        for (int i=0; i<zSize; i++) {
            ArrayList<Integer> zDimension = new ArrayList<Integer>();
            for (SNode aNode : nodes) {
                aWeight = aNode.weights.get(i).intValue();
                zDimension.add(aWeight);
            }
            weights.add(zDimension);
        }
        return weights;
    }

    public int getLength() {return this.ySize;}
    public int getWidth() {return this.xSize;}

    public ArrayList<SNode> getNodes() {
        return this.nodes;
    }

    private int[] randomPattern(int size) {
        int[] pattern = new int[size];
        for (int i=0; i<size; i++) {
            pattern[i] = rand.nextInt(size);
        }
        return pattern;
    }

    private int[][] tile(int[] oneD, int n) {
        int[][] result = new int[n][oneD.length];
        System.out.println("woot");
        for (int i=0; i<n; i++) {
            result[i] = oneD;
            System.out.println(result[i][0] + " " + result[i][1] + " " + result[i][2]);
        }
        System.out.println();
        return result;
    }
}
