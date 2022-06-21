import java.io.File;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {

    private final int n;
    private final CircularSuffix[] suffixes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {

        if (s == null)
            throw new IllegalArgumentException("argument is null.");

        n = s.length();
        suffixes = new CircularSuffix[n];

        for (int i = 0; i < suffixes.length; i++)
            suffixes[i] = new CircularSuffix(s, i);

        Arrays.sort(suffixes);
    }

    private static class CircularSuffix implements Comparable<CircularSuffix> {
        private final String text;
        private final int index;

        private CircularSuffix(String text, int index) {
            this.text = text;
            this.index = index;
        }

        private int index() {
            return this.index;
        }

        private int length() {
            return this.text.length();
        }

        private char charAt(int i) {
            int seti = (i + this.index) % text.length();
            return text.charAt(seti);

        }

        public int compareTo(CircularSuffix that) {
            for (int i = 0; i < this.length(); i++) {
                if (this.charAt(i) < that.charAt(i))
                    return -1;
                if (this.charAt(i) > that.charAt(i))
                    return +1;
            }
            return this.length() - that.length();
        }

    }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {

        if (i < 0 || i >= length())
            throw new IllegalArgumentException("argument is out of range.");

        return suffixes[i].index();
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(new File(args[0]));
        String str = in.readLine();
        CircularSuffixArray csa = new CircularSuffixArray(str);

        StdOut.println(csa.length());
        for (int i = 0; i < csa.length(); i++) {
            StdOut.println(csa.index(i));
        }

    }
}