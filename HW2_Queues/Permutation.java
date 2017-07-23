import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        if (k < 0) throw new IllegalArgumentException("k should not be negative: " + k);
        if (k == 0) return;

        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        for (int i = 0; i < k; i++) {
            if (StdIn.isEmpty())
                throw new IllegalArgumentException("Not enough values in input file");
            queue.enqueue(StdIn.readString());
        }

        int j = k;
        while (!StdIn.isEmpty()) {
            int r = StdRandom.uniform(++j);
            if (r < k) {
                queue.dequeue();
                queue.enqueue(StdIn.readString());
            } else {
                StdIn.readString();
            }
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}
