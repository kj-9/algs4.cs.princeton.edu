import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> q = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            q.enqueue(StdIn.readString());
        }

        int counter = 0;
        for (String s : q) {
            if (counter == k)
                break;
            System.out.println(s);

            counter++;

        }

    }
}

/*
 * Client. Write a client program Permutation.java that takes an integer k as a
 * command-line argument; reads a sequence of strings from standard input using
 * StdIn.readString(); and prints exactly k of them, uniformly at random. Print
 * each item from the sequence at most once.
 */