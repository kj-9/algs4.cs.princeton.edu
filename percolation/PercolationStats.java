import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        fraction = new double[trials];

        for (int trial = 0; trial < trials; trial++) {

            Percolation pc = new Percolation(n);

            int[][] randomIdx = new int[n * n][2];

            for (int row = 1; row <= n; row++) {
                for (int col = 1; col <= n; col++) {
                    randomIdx[(row - 1) * n + col - 1] = new int[] { row, col };
                }

            }
            StdRandom.shuffle(randomIdx);

            for (int[] idx : randomIdx) {
                pc.open(idx[0], idx[1]);
                if (pc.percolates()) {
                    break;
                }
            }

            fraction[trial] = (double) pc.numberOfOpenSites() / (n * n);

        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fraction);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fraction);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(fraction.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(fraction.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, t);

        System.out.println("mean = " + ps.mean());
        System.out.println("stddev = " + ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }

    final private double[] fraction;

}