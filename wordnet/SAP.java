import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import java.util.Arrays;

public class SAP {
    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        int acr = ancestor(v, w);

        if (acr == -1)
            return -1;
        else {
            int vDist = new BreadthFirstDirectedPaths(G, v).distTo(acr);
            int wDist = new BreadthFirstDirectedPaths(G, w).distTo(acr);

            return vDist + wDist;
        }
    }

    private int ancestor(BreadthFirstDirectedPaths bfpV, BreadthFirstDirectedPaths bfpW) {

        Integer minDist = Integer.MAX_VALUE;
        Integer curDist = null;
        Integer anc = null;

        for (int i = 0; i < G.V(); i++) {
            if (bfpV.hasPathTo(i) && bfpW.hasPathTo(i)) {
                curDist = bfpV.distTo(i) + bfpW.distTo(i);

                if (curDist < minDist) {
                    minDist = curDist;
                    anc = i;
                }
            }
        }

        if (anc == null)
            return -1;
        else
            return anc;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new IllegalArgumentException("argument is out of range.");

        return ancestor(new BreadthFirstDirectedPaths(G, v), new BreadthFirstDirectedPaths(G, w));

    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        int acr = ancestor(v, w);

        if (acr == -1)
            return -1;
        else {
            int vDist = new BreadthFirstDirectedPaths(G, v).distTo(acr);
            int wDist = new BreadthFirstDirectedPaths(G, w).distTo(acr);

            return vDist + wDist;
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {

        if (v == null || w == null)
            throw new IllegalArgumentException("arguments must not be null.");

        for (Integer vi : v) {
            if (vi == null || vi < 0 || G.V() <= vi)
                throw new IllegalArgumentException("v must not contain null.");
        }

        for (Integer wi : w) {
            if (wi == null || wi < 0 || G.V() <= wi)
                throw new IllegalArgumentException("w must not contain null.");
        }

        try {
            return ancestor(new BreadthFirstDirectedPaths(G, v), new BreadthFirstDirectedPaths(G, w));

        } catch (IllegalArgumentException e) {
            return -1;
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int[][] inputs = { { 3, 11 }, { 9, 12 }, { 7, 2 }, { 1, 6 }, { 6, 6 } };

        for (int[] input : inputs) {
            int length = sap.length(input[0], input[1]);
            int ancestor = sap.ancestor(input[0], input[1]);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

        in = new In(args[1]);
        G = new Digraph(in);
        sap = new SAP(G);

        Iterable<Integer> a = Arrays.asList(new Integer[] { 13, 23, 24 });
        Iterable<Integer> b = Arrays.asList(new Integer[] { 6, 16, 17 });
        StdOut.print(sap.ancestor(a, b));
        StdOut.print(sap.length(a, b));
    }
}