import java.util.ArrayList;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("row or col index is not in range.");
        }

        size = n;
        grid = new int[n * n];
        uf = new WeightedQuickUnionUF(n * n + 1); // number of grids + start/end edge
        opn_btm = new ArrayList<Integer>();

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        isValidRowCol(row, col);
        if (!this.isOpen(row, col)) {
            grid[rowColtoN(row, col)] = 1;

            // if row is top/bottom, union with start/end edges
            if (row == 1) {
                uf.union(rowColtoN(row, col), size * size);
            }

            if (row == size) {
                opn_btm.add(col);
            }

            // union with other open sites

            int[][] dir = new int[4][];

            dir[0] = new int[] { 0, 1 };
            dir[1] = new int[] { 0, -1 };
            dir[2] = new int[] { 1, 0 };
            dir[3] = new int[] { -1, 0 };

            for (int[] ij : dir) {
                try {
                    if (this.isOpen(row - ij[0], col - ij[1])) {
                        uf.union(rowColtoN(row, col), rowColtoN(row - ij[0], col - ij[1]));
                    }
                } catch (IllegalArgumentException e) {
                    continue;
                }

            }

        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        isValidRowCol(row, col);
        return grid[rowColtoN(row, col)] == 1;

    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        isValidRowCol(row, col);

        return uf.find(size * size) == uf.find(rowColtoN(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int sum = 0;
        for (int i = 0; i < grid.length; i++) {
            sum += grid[i];
        }
        return sum;
    }

    // does the system percolate?
    public boolean percolates() {
        for (Integer i : opn_btm) {
            if (uf.find(size * size) == uf.find(rowColtoN(size, i))) {
                return true;
            }
        }

        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation pc = new Percolation(3);

        pc.open(1, 1);
        pc.open(2, 1);
        System.out.println(pc.percolates());
        pc.open(3, 1);
        System.out.println(pc.percolates());
        pc.open(3, 3);

        System.out.println(pc.isFull(3, 3));

    }

    final private int size;
    final private int[] grid;
    final private WeightedQuickUnionUF uf;
    final private ArrayList<Integer> opn_btm;

    private int rowColtoN(int row, int col) {
        return (row - 1) * size + col - 1;
    }

    private void isValidRowCol(int row, int col) {
        if ((1 <= row && row <= size) &&
                (1 <= col && col <= size)) {
            return;
        } else {
            throw new IllegalArgumentException("row or col index is not in range.");
        }
    }

}