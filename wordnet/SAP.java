import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import java.util.Arrays;
import java.util.Iterator;

public class SAP {
    private Digraph G;
    private int root;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);

        for (int i = 0; i < this.G.V(); i++) {
            if (this.G.outdegree(i) == 0) {
                root = i;
                break;
            }

        }
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

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {

        BreadthFirstDirectedPaths bfpV = new BreadthFirstDirectedPaths(G, v);
        Iterable<Integer> pathV = bfpV.pathTo(root);

        if (pathV == null)
            return -1;
        Iterator<Integer> pathViter = pathV.iterator();

        BreadthFirstDirectedPaths bfpW = new BreadthFirstDirectedPaths(G, w);
        Iterable<Integer> pathW = bfpW.pathTo(root);

        if (pathW == null)
            return -1;
        Iterator<Integer> pathWiter = pathW.iterator();

        int v2w = bfpV.distTo(root) - bfpW.distTo(root);

        while (v2w > 0) {
            pathViter.next();
            v2w--;
        }

        while (v2w < 0) {
            pathWiter.next();
            v2w++;
        }

        while (pathViter.hasNext()) {
            int ancV = pathViter.next();
            int ancW = pathWiter.next();

            if (ancV == ancW)
                return ancV;
        }

        return -1;

    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        int anc = ancestor(v, w);

        if (anc == -1)
            return -1;

        BreadthFirstDirectedPaths bfpV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfpW = new BreadthFirstDirectedPaths(G, w);

        return bfpV.distTo(anc) + bfpW.distTo(anc);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {

        BreadthFirstDirectedPaths bfpV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfpW = new BreadthFirstDirectedPaths(G, w);

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

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int[][] inputs = new int[][] { { 3, 11 }, { 9, 12 }, { 7, 2 }, { 1, 6 } };

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