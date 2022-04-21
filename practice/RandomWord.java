import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        int i = 1;
        String champ = null;
        double p = 1.0 / i;
        boolean do_update = StdRandom.bernoulli(p);

        while (true) {

            p = 1.0 / i;
            do_update = StdRandom.bernoulli(p);

            if (do_update) {
                champ = StdIn.readString();
            } else {
                StdIn.readString();
            }

            i++;

            if (StdIn.isEmpty()) {
                StdOut.println(champ);
                break;
            }
        }

    }

}
