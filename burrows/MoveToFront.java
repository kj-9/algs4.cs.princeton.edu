import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {

    private final static int R = 256; // extended ASCII characters

    // apply move-to-front encoding, reading from standard input and writing to
    // standard output
    public static void encode() {
        StringBuilder chars = new StringBuilder();

        for (int r = 0; r < R; r++)
            chars.append((char) r);

        String sChars = chars.toString();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int pos = sChars.indexOf(c);

            BinaryStdOut.write((char) pos);

            chars.deleteCharAt(pos);
            chars.insert(0, c);
            sChars = chars.toString();
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to
    // standard output
    public static void decode() {
        StringBuilder chars = new StringBuilder();

        for (int r = 0; r < R; r++)
            chars.append((char) r);

        String sChars = chars.toString();

        while (!BinaryStdIn.isEmpty()) {
            char pos = BinaryStdIn.readChar();
            char c = sChars.charAt(pos);

            BinaryStdOut.write(c);

            chars.deleteCharAt(pos);
            chars.insert(0, c);
            sChars = chars.toString();
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
        else
            StdOut.println("Invalid argument");
    }

}