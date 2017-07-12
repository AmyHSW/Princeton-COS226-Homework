import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

    private final double[] threshold;
    private final double mean;
    private final double stddev;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be positive");
        }
        threshold = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation expt = new Percolation(n);
            runExpt(expt, n);
            threshold[i] = (double) expt.numberOfOpenSites() / (n * n);
        }
        mean = StdStats.mean(threshold);
        stddev = StdStats.stddev(threshold);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return mean - 1.96 * stddev / Math.sqrt(threshold.length);
    }

    public double confidenceHi() {
        return mean + 1.96 * stddev / Math.sqrt(threshold.length);
    }

    private void runExpt(Percolation expt, int n) {
        while (!expt.percolates()) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            expt.open(row, col);
        }
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        Stopwatch watch = new Stopwatch();
        PercolationStats stats = new PercolationStats(n, trials);
        double time = watch.elapsedTime();

        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = ["
                       + stats.confidenceLo()
                       + ", "
                       + stats.confidenceHi()
                       + "]");
        StdOut.println("elapsed time (s)        = " + time);
    }
}
