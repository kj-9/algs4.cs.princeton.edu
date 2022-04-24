import java.util.Comparator;
import edu.princeton.cs.algs4.Stack;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

//Implementation requirement.  To implement the A* algorithm,
//you must use the MinPQ data type for the priority queue.

//Corner cases. 
//Throw an IllegalArgumentException in the constructor if the argument is null.
//Return -1 in moves() if the board is unsolvable.
//Return null in solution() if the board is unsolvable.

public class Solver {

    private final Stack<Board> solutions = new Stack<Board>();
    private final boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("argument can't be null.");

        MinPQ<Node> pq = new MinPQ<Node>(manhattanPriorityOrder());
        pq.insert(new Node(null, initial, 0));
        Node nCur = pq.delMin();

        MinPQ<Node> pqTwin = new MinPQ<Node>(manhattanPriorityOrder());
        pqTwin.insert(new Node(null, initial.twin(), 0));
        Node nCurTwin = pqTwin.delMin();

        while (!nCur.board.isGoal() && !nCurTwin.board.isGoal()) {
            for (Board b : nCur.board.neighbors()) {
                if (!doesExistsParentBoard(nCur, b))
                    pq.insert(new Node(nCur, b, nCur.move + 1));
            }
            nCur = pq.delMin();

            for (Board b : nCurTwin.board.neighbors()) {
                if (!doesExistsParentBoard(nCurTwin, b))
                    pqTwin.insert(new Node(nCurTwin, b, nCur.move + 1));
            }
            nCurTwin = pqTwin.delMin();
        }

        solvable = nCur.board.isGoal();

        if (solvable)

        {
            while (true) {
                solutions.push(nCur.board);
                if (nCur.parent == null)
                    break;
                nCur = nCur.parent;
            }
        }
    }

    private boolean doesExistsParentBoard(Node n, Board b) {

        int count = 0;

        while (count < 2) {
            if (n.parent == null)
                return false;
            n = n.parent;

            if (n.board.equals(b))
                return true;

            count++;
        }
        return false;
    }

    private class Node {
        private final Board board;
        private final int move;
        private final int priority;
        private final Node parent;

        public Node(Node p, Board b, int m) {
            parent = p;
            board = b;
            move = m;
            priority = m + b.manhattan();
        }
    }

    private Comparator<Node> manhattanPriorityOrder() {
        return new ByManhattanPriority();
    }

    private class ByManhattanPriority implements Comparator<Node> {
        public int compare(Node n1, Node n2) {
            if (n1.priority < n2.priority)
                return -1;
            else if (n1.priority > n2.priority)
                return 1;
            else
                return 0;

        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable())
            return -1;

        return solutions.size() - 1;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        return solutions;

    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

        solver = new Solver(null);
    }

}