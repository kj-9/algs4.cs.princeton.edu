import java.util.Stack;

public class Board {

    private final int[][] state;
    private Integer manhattanAns = null;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    // Constructor. You may assume that the constructor receives an n-by-n array
    // containing the n2 integers between 0 and n2 − 1, where 0 represents the blank
    // square. You may also assume that 2 ≤ n < 128.
    public Board(int[][] tiles) {
        state = copyTile(tiles);
    }

    // string representation of this board
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(state[0].length);
        out.append("\n");

        for (int[] row : state) {
            for (int el : row) {
                out.append(" ");
                out.append(el);
            }
            out.append("\n");
        }
        return out.toString();
    }

    // board dimension n
    public int dimension() {
        return state.length;
    }

    // number of tiles out of place
    public int hamming() {
        int out = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (state[i][j] == 0)
                    continue;

                if (state[i][j] != ((j + 1) + i * dimension()) % (dimension() * dimension()))
                    out++;
            }
        }
        return out;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {

        if (manhattanAns == null) {
            manhattanAns = 0;
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {

                    int ans;
                    if (state[i][j] == 0)
                        continue;
                    else
                        ans = (state[i][j] % (dimension() * dimension())) - 1;

                    int iAns = (ans / dimension());
                    int jAns = ans % dimension();

                    manhattanAns += Math.abs(i - iAns) + Math.abs(j - jAns);

                }
            }

        }
        return manhattanAns;

    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (state[i][j] != ((j + 1) + i * dimension()) % (dimension() * dimension()))
                    return false;
            }
        }
        return true;
    }

    // does this board equal y?
    // Comparing two boards for equality. Two boards are equal if they are have the
    // same size and their corresponding tiles are in the same positions. The
    // equals() method is inherited from java.lang.Object, so it must obey all of
    // Java’s requirements.

    private int[] getBlank() {

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (state[i][j] == 0) {
                    int[] pos = { i, j };
                    return pos;
                }
            }
        }
        throw new IllegalStateException("There is no blank square.");
    }

    public boolean equals(Object y) {
        if (!(y instanceof Board))
            return false;

        Board yb = (Board) y;

        if (!(yb.dimension() == dimension()))
            return false;

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (state[i][j] != yb.state[i][j])
                    return false;
            }
        }

        return true;
    }

    private int[][] copyTile(int[][] tile) {
        int[][] newTile = new int[tile.length][tile.length];

        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile.length; j++) {
                newTile[i][j] = tile[i][j];
            }
        }
        return newTile;
    }

    // all neighboring boards
    // Neighboring boards. The neighbors() method returns an iterable containing the
    // neighbors of the board. Depending on the location of the blank square, a
    // board can have 2, 3, or 4 neighbors.
    public Iterable<Board> neighbors() {
        Stack<Board> nbs = new Stack<Board>();
        int[] posB = getBlank();
        int[][] s;
        int swap;

        if (posB[0] > 0) {
            s = copyTile(state);
            swap = s[posB[0] - 1][posB[1]];
            s[posB[0] - 1][posB[1]] = 0;
            s[posB[0]][posB[1]] = swap;
            nbs.push(new Board(s));
        }
        if (posB[0] < dimension() - 1) {
            s = copyTile(state);
            swap = s[posB[0] + 1][posB[1]];
            s[posB[0] + 1][posB[1]] = 0;
            s[posB[0]][posB[1]] = swap;
            nbs.push(new Board(s));
        }
        if (posB[1] > 0) {
            s = copyTile(state);
            swap = s[posB[0]][posB[1] - 1];
            s[posB[0]][posB[1] - 1] = 0;
            s[posB[0]][posB[1]] = swap;
            nbs.push(new Board(s));
        }
        if (posB[1] < dimension() - 1) {
            s = copyTile(state);
            swap = s[posB[0]][posB[1] + 1];
            s[posB[0]][posB[1] + 1] = 0;
            s[posB[0]][posB[1]] = swap;
            nbs.push(new Board(s));
        }
        return nbs;

    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

        int[][] newState = copyTile(state);
        Stack<Integer> swaps = new Stack<Integer>();

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (state[i][j] != 0) {
                    swaps.push(i);
                    swaps.push(j);
                }

                if (swaps.size() == 4) {
                    int swap = newState[swaps.get(0)][swaps.get(1)];
                    newState[swaps.get(0)][swaps.get(1)] = newState[swaps.get(2)][swaps.get(3)];
                    newState[swaps.get(2)][swaps.get(3)] = swap;

                    return new Board(newState);
                }
            }
        }

        throw new IllegalAccessError("this shoud not be happening");

    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = new int[2][2];

        tiles[0][0] = 0;
        tiles[0][1] = 1;
        tiles[1][0] = 3;
        tiles[1][1] = 2;

        Board b = new Board(tiles);

        System.out.print(b.toString());
        System.out.println(b.hamming());
        System.out.println(b.manhattan());
        System.out.println(b.isGoal());

        for (Board n : b.neighbors()) {
            System.out.println(n.toString());
        }
        System.out.print(b.toString());
        System.out.print(b.twin().toString());

    }

}