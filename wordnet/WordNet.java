import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.stream.Stream;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.SeparateChainingHashST;

public class WordNet {
    private final ArrayList<String> synsets;
    private final SeparateChainingHashST<String, Stack<Integer>> words;
    private final Digraph G;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("arg is null.");

        In inSynsets = new In(synsets);

        this.synsets = new ArrayList<String>();
        words = new SeparateChainingHashST<String, Stack<Integer>>();

        while (inSynsets.hasNextLine()) {

            String[] line = inSynsets.readLine().split(",");

            this.synsets.add(line[1]);

            String[] words = line[1].split(" ");
            Integer id = Integer.parseInt(line[0]);

            for (String word : words) {
                Stack<Integer> s = this.words.get(word);
                if (s == null) {
                    s = new Stack<Integer>();
                    s.push(id);
                } else {
                    s.push(id);
                }
                this.words.put(word, s);
            }
        }
        G = new Digraph(this.synsets.size());

        In inHypernyms = new In(hypernyms);

        while (inHypernyms.hasNextLine()) {
            int[] edge = Stream.of(inHypernyms.readLine().split(",")).mapToInt(Integer::parseInt).toArray();

            for (int i = 1; i < edge.length; i++) {
                G.addEdge(edge[0], edge[i]);
            }
        }

        if (new DirectedCycle(G).hasCycle())
            throw new IllegalArgumentException("input graph is not a DAG.");

        int roots = 0;
        for (int i = 0; i < this.G.V(); i++) {
            if (this.G.outdegree(i) == 0)
                roots++;

            if (roots > 1)
                throw new IllegalArgumentException("input graph has more than one root.");

        }

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return words.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return words.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("noun must be in wordnet.");

        SAP sap = new SAP(G);
        Integer len = sap.length(words.get(nounA), words.get(nounB));
        return len;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of
    // nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("noun must be in wordnet.");

        SAP sap = new SAP(G);
        Integer anc = sap.ancestor(words.get(nounA), words.get(nounB));

        return synsets.get(anc);

    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");

        StdOut.println(wn.G.V());
        StdOut.println(wn.G.E());
        StdOut.println(wn.words.size());
        StdOut.println(wn.synsets.size());

        StdOut.println(wn.words.get("Aberdeen"));
        StdOut.println(wn.synsets.get(62));
        StdOut.println(wn.synsets.get(63));
        StdOut.println(wn.synsets.get(64));
        StdOut.println(wn.synsets.get(65));

        StdOut.println(wn.sap("worm", "bird"));
        StdOut.println(wn.distance("worm", "bird"));

    }
}