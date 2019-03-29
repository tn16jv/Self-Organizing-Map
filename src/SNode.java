import java.util.ArrayList;

public class SNode {
    public ArrayList<Double> weights;
    public double dx, dy;
    public int eTop, eRight, eBottom, eLeft;

    public SNode() {

    }

    public double distanceSimilar(SNode targetNode) {
        double distance = 0.0;
        for (int i=0; i<weights.size(); i++) {
            distance = distance + Math.pow(targetNode.weights.get(i) - this.weights.get(i), 2);
        }
        return Math.sqrt(distance);
    }

    public void adjustWeights(SNode targetNode, double hrs) {
        double result;
        for (int i=0; i<weights.size(); i++) {
            result = (targetNode.weights.get(i) - this.weights.get(i)) * hrs;
            this.weights.set(i, this.weights.get(i) + result);
        }
    }
}
