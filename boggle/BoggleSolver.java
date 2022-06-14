import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {

    private final TST<Integer> dictionary;

    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)
    public BoggleSolver(String[] dictionary) {

        this.dictionary = new TST<Integer>();

        int point;

        for (String word : dictionary) {

            int len = word.length();

            if (len == 3 || len == 4)
                point = 1;
            else if (len == 5)
                point = 2;
            else if (len == 6)
                point = 3;
            else if (len == 7)
                point = 5;
            else if (len >= 8)
                point = 11;
            else
                point = 0;

            this.dictionary.put(word, point);

        }

    }

    private Iterable<int[]> neighbors(int i, int j, BoggleBoard board) {

        Stack<int[]> out = new Stack<int[]>();
        int[][] moves = {
                { 0, 1 },
                { 1, 1 },
                { 1, 0 },
                { 1, -1 },
                { 0, -1 },
                { -1, -1 },
                { -1, 0 },
                { -1, 1 }
        };

        for (int[] move : moves) {
            int iNext = i + move[0];
            int jNext = j + move[1];

            if (0 <= iNext && iNext < board.rows() && 0 <= jNext && jNext < board.cols()) {
                int[] next = { iNext, jNext };
                out.push(next);
            }

        }
        return out;
    }

    private void dfs(int i, int j, StringBuilder strb, boolean marked[][], SET<String> out, BoggleBoard board) {

        boolean[][] _marked = new boolean[board.rows()][board.cols()];

        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                _marked[row][col] = marked[row][col];
            }
        }
        _marked[i][j] = true;

        StringBuilder _strb = new StringBuilder(strb);

        char letter = board.getLetter(i, j);
        if (letter == 'Q') {
            _strb.append(letter);
            _strb.append('U');
        } else {
            _strb.append(letter);
        }

        String str = _strb.toString();

        if (dictionary.keysWithPrefix(str).iterator().hasNext()) {

            if (str.length() > 2 && dictionary.get(str) != null) {
                out.add(str);
            }

            for (int[] n : neighbors(i, j, board)) {
                if (!_marked[n[0]][n[1]])
                    dfs(n[0], n[1], _strb, _marked, out, board);
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

        SET<String> out = new SET<String>();
        StringBuilder strb = new StringBuilder();
        boolean marked[][] = new boolean[board.rows()][board.cols()];

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                dfs(i, j, strb, marked, out, board);
            }
        }
        return out;
    }

    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return dictionary.get(word);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
