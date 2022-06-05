import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {

    private Picture picture;
    private Double[][] energies;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {

        if (picture == null)
            throw new IllegalArgumentException("argument is null");

        this.picture = new Picture(picture);

    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    private double squareGradient(int colorA, int colorB) {
        int rA = (colorA >> 16) & 0xFF;
        int gA = (colorA >> 8) & 0xFF;
        int bA = (colorA >> 0) & 0xFF;

        int rB = (colorB >> 16) & 0xFF;
        int gB = (colorB >> 8) & 0xFF;
        int bB = (colorB >> 0) & 0xFF;

        return Math.pow(rA - rB, 2) + Math.pow(gA - gB, 2)
                + Math.pow(bA - bB, 2);
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {

        if (x < 0 || y < 0 || x > width() - 1 || y > height() - 1)
            throw new IllegalArgumentException("input is out of range");

        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1)
            return 1000;

        int left = picture.getRGB(x - 1, y);
        int right = picture.getRGB(x + 1, y);
        int up = picture.getRGB(x, y + 1);
        int down = picture.getRGB(x, y - 1);

        return Math.sqrt(squareGradient(left, right) + squareGradient(up, down));

    }

    // vertical adjancesies
    private int[][] adj(int x, int y) {

        if (y == height() - 1)
            return null;

        if (x == 0) {
            int[][] out = { { x, y + 1 }, { x + 1, y + 1 } };
            return out;
        }

        if (x == width() - 1) {
            int[][] out = { { x, y + 1 }, { x - 1, y + 1 } };
            return out;
        }

        int[][] out = { { x, y + 1 }, { x + 1, y + 1 }, { x - 1, y + 1 } };
        return out;

    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        Picture p = this.picture;
        Double[][] e = this.energies;

        Picture tp = new Picture(height(), width());
        Double[][] te = new Double[height()][width()];

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                tp.setRGB(y, x, p.getRGB(x, y));

                if (e == null)
                    continue;
                te[y][x] = e[x][y];
            }
        }

        this.picture = tp;
        this.energies = te;

        int[] seam = findVerticalSeam();

        this.picture = p;
        this.energies = e;

        return seam;

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        if (energies == null)
            energies = new Double[width()][height()];

        if (height() == 1) {
            int[] seam = { 0 };
            return seam;
        }

        if (width() == 1) {
            int[] seam = new int[height()];

            for (int y = 0; y < seam.length; y++) {
                seam[y] = 0;
            }
            return seam;
        }

        double[][] distTo = new double[width()][height()];
        Integer[][] edgeTo = new Integer[width()][height()];

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (y == 0)
                    distTo[x][y] = energy(x, y);
                else
                    distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {
                for (int[] adjXY : adj(x, y)) {

                    int adjX = adjXY[0];
                    int adjY = adjXY[1];

                    if (energies[adjX][adjY] == null)
                        energies[adjX][adjY] = energy(adjX, adjY);

                    if (distTo[adjX][adjY] > distTo[x][y] + energies[adjX][adjY]) {
                        distTo[adjX][adjY] = distTo[x][y] + energies[adjX][adjY];
                        edgeTo[adjX][adjY] = x + y * width();
                    }
                }
            }
        }

        Double minDist = Double.POSITIVE_INFINITY;
        int[] seam = new int[height()];

        for (int x = 0; x < width(); x++) {
            if (minDist > distTo[x][height() - 1]) {
                minDist = distTo[x][height() - 1];
                seam[height() - 1] = x;
            }
        }

        for (int y = height() - 1; y > 0; y--) {

            int e = edgeTo[seam[y]][y];
            seam[y - 1] = e - (y - 1) * width();

        }
        return seam;

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

        Picture tp = new Picture(height(), width());
        Double[][] te = new Double[height()][width()];

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                tp.setRGB(y, x, picture.getRGB(x, y));

                if (energies == null)
                    continue;
                te[y][x] = energies[x][y];
            }
        }

        this.picture = tp;
        this.energies = te;

        removeVerticalSeam(seam);

        Picture p = new Picture(height(), width());
        Double[][] e = new Double[height()][width()];

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                p.setRGB(y, x, picture.getRGB(x, y));

                if (energies == null)
                    continue;
                e[y][x] = energies[x][y];
            }
        }

        this.picture = p;
        this.energies = e;

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

        if (seam == null)
            throw new IllegalArgumentException("argument is null");
        if (seam.length != height())
            throw new IllegalArgumentException("argument length does not match");

        for (int i = 1; i < seam.length; i++) {
            int diff = seam[i - 1] - seam[i];

            if (diff < -1 || diff > 1)
                throw new IllegalArgumentException("successive entries in seam[] must differ by -1, 0, or +1.");
        }

        if (width() <= 1)
            throw new IllegalArgumentException("too small to decrease picture size");

        Picture newPicture = new Picture(width() - 1, height());

        for (int y = 0; y < picture.height(); y++) {
            for (int x = 0; x < picture.width(); x++) {

                if (x < seam[y])
                    newPicture.setRGB(x, y, picture.getRGB(x, y));
                if (x > seam[y])
                    newPicture.setRGB(x - 1, y, picture.getRGB(x, y));
            }
        }

        this.picture = newPicture;
        Double[][] newEnergies = new Double[width()][height()];

        if (!(energies == null)) {
            for (int y = 0; y < picture.height(); y++) {
                for (int x = 0; x < picture.width(); x++) {

                    if (x < seam[y] - 1)
                        newEnergies[x][y] = energies[x][y];
                    else if (x == seam[y] - 1 || x == seam[y])
                        newEnergies[x][y] = energy(x, y);
                    else
                        newEnergies[x][y] = energies[x - 1][y];
                }
            }
        }

        this.energies = newEnergies;

    }

    // unit testing (optional)
    public static void main(String[] args) {
        SeamCarver sc = new SeamCarver(new Picture("6x5.png"));

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++) {
                StdOut.printf("%s %s ", row, col);

                double energy = sc.energy(col, row);
                StdOut.printf("%7.2f ", energy);
            }
            StdOut.println();
        }

        sc.findVerticalSeam();

        StdOut.println(sc.width());
        StdOut.println(sc.height());

        sc = new SeamCarver(new Picture("HJoceanSmall.png"));

    }

}