import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

public class BurrowsWheeler {

    private static final int R = 256; // extended ASCII characters

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {

        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);

        int first = 0;
        int[] pos = new int[csa.length()];

        for (int i = 0; i < csa.length(); i++) {

            int idx = csa.index(i);
            if (idx == 0)
                first = i;

            pos[i] = idx - 1;
            if (pos[i] < 0)
                pos[i] += csa.length();
        }

        BinaryStdOut.write(first);

        for (int p : pos)
            BinaryStdOut.write(s.charAt(p));

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {

        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int N = t.length();

        int[] count = new int[R];
        int[] next = new int[N];
        int[] aux = new int[N];

        // key value counting and LSD sort
        for (int i = 0; i < N; i++)
            count[t.charAt(i)]++;

        int counter = 0;
        for (int r = 0; r < R; r++) {
            int from = 0;

            for (int i = counter; i < counter + count[r]; i++) {
                next[i] = t.indexOf(r, from);
                from = next[i] + 1;
                aux[i] = r;
            }
            counter += count[r];
        }

        int n = first;
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write((char) aux[n]);
            n = next[n];
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {

        if (args[0].equals("-"))
            transform();
        else if (args[0].equals("+"))
            inverseTransform();
    }

}