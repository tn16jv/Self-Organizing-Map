public class Topology {
    private int latHeight, latWidth, latThick;

    public Topology(int height, int width, int thickness) {
        latHeight = height;
        latWidth = width;
        latThick = thickness;
    }

    public double latticeDistance(int index1, int index2) {
        int row1, row2, thick1, col1, col2, thick2;

        row1 = (index1 % (latHeight * latWidth)) / latWidth;
        col1 = (index1 % (latHeight * latWidth)) - (row1 * latWidth);
        thick1 = index1 / (latHeight * latWidth);

        row2 = (index2 % (latHeight * latWidth)) / latWidth;
        col2 = (index2 % (latHeight * latWidth)) - (row2 * latWidth);
        thick2 = index2 / (latHeight * latWidth);

        return (double)(Math.abs(row1 - row2) + Math.abs(col1 - col2) + Math.abs(thick1 - thick2));
    }

    public double latticeDistance2D(int index1, int index2) {
        int row1, col1, row2, col2;

        row1 = index1 / latWidth;
        col1 = index1 - (row1 * latWidth);

        row2 = index2 / latWidth;
        col2 = index2 - (row2 * latWidth);

        return (double) (Math.abs(row1 - row2) + Math.abs(col1 - col2));
    }

    public double cellCount() {
        return latHeight * latWidth * latThick;
    }
}
