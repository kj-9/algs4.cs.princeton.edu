import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class BoggleSolver {

    private final TrieSET dictionary;

    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)
    public BoggleSolver(String[] dictionary) {

        this.dictionary = new TrieSET();

        for (String word : dictionary) {
            this.dictionary.add(word);
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

    private void dfs(int i, int j, StringBuilder strb, boolean marked[][], TrieSET.Node node, TrieSET out,
            BoggleBoard board) {

        marked[i][j] = true;
        int lenBefore = strb.length();

        char letter = board.getLetter(i, j);
        if (letter == 'Q') {
            strb.append(letter);
            strb.append('U');
        } else {
            strb.append(letter);
        }

        String str = strb.toString();
        TrieSET.Node nextNode = dictionary.get(node, str, lenBefore);

        if (nextNode != null) {

            if (str.length() > 2 && dictionary.contains(str)) {
                out.add(str);
            }

            for (int[] n : neighbors(i, j, board)) {
                if (!marked[n[0]][n[1]])
                    dfs(n[0], n[1], strb, marked, nextNode, out, board);
            }

        }
        marked[i][j] = false;

        if (letter == 'Q') {
            strb.deleteCharAt(strb.length() - 1);
            strb.deleteCharAt(strb.length() - 1);
        } else {
            strb.deleteCharAt(strb.length() - 1);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

        TrieSET out = new TrieSET();
        StringBuilder strb = new StringBuilder();
        boolean marked[][] = new boolean[board.rows()][board.cols()];

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                dfs(i, j, strb, marked, dictionary.root, out, board);
            }
        }
        return out.keysWithPrefix("");
    }

    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dictionary.contains(word)) {
            int len = word.length();

            if (len == 3 || len == 4)
                return 1;
            else if (len == 5)
                return 2;
            else if (len == 6)
                return 3;
            else if (len == 7)
                return 5;
            else if (len >= 8)
                return 11;
            else
                return 0;
        } else {
            return 0;
        }
    }

    private class TrieSET implements Iterable<String> {
        private static final int R = 26; // alphabet

        public Node root; // root of trie
        private int n; // number of keys in trie
        // R-way trie node

        public class Node {
            private Node[] next = new Node[R];
            private boolean isString;
        }

        /**
         * Initializes an empty set of strings.
         */
        public TrieSET() {
        }

        private int posAlphabet(char c) {
            return c - 65;
        }

        public boolean contains(String key) {
            if (key == null)
                throw new IllegalArgumentException("argument to contains() is null");
            Node x = get(root, key, 0);
            if (x == null)
                return false;
            return x.isString;
        }

        private Node get(Node x, String key, int d) {
            if (x == null)
                return null;
            if (d == key.length())
                return x;
            int c = posAlphabet(key.charAt(d));
            return get(x.next[c], key, d + 1);
        }

        public void add(String key) {
            if (key == null)
                throw new IllegalArgumentException("argument to add() is null");
            root = add(root, key, 0);
        }

        private Node add(Node x, String key, int d) {
            if (x == null)
                x = new Node();
            if (d == key.length()) {
                if (!x.isString)
                    n++;
                x.isString = true;
            } else {
                int c = posAlphabet(key.charAt(d));
                x.next[c] = add(x.next[c], key, d + 1);
            }
            return x;
        }

        public Iterator<String> iterator() {
            return keysWithPrefix("").iterator();
        }

        public Iterable<String> keysWithPrefix(String prefix) {
            Queue<String> results = new Queue<String>();
            Node x = get(root, prefix, 0);
            collect(x, new StringBuilder(prefix), results);
            return results;
        }

        private void collect(Node x, StringBuilder prefix, Queue<String> results) {
            if (x == null)
                return;
            if (x.isString)
                results.enqueue(prefix.toString());
            for (char c = 0; c < R; c++) {
                prefix.append((char) (c + 65));
                collect(x.next[c], prefix, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
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
