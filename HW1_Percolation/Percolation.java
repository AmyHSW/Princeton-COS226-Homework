import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int N;
    private final boolean[] isOpen;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufb;
    private int nOpenSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive: " + n);
        }
        N = n;
        isOpen = new boolean[n * n + 1];
        uf = new WeightedQuickUnionUF(n * n + 1);
        ufb = new WeightedQuickUnionUF(n * n + 2);
        nOpenSites = 0;
    }

    private int toIndex(int row, int col) {        
        if (row < 1 || row > N) {
            throw new IllegalArgumentException("Row index out of range: " + row);
        }
        if (col < 1 || col > N) {
            throw new IllegalArgumentException("Column index out of range: " + col);
        }
        return (row - 1) * N + col;
    }

    public void open(int row, int col) {
        int i = toIndex(row, col);
        if (isOpen[i]) return;
        isOpen[i] = true;
        nOpenSites++;
        connectNeighboringOpenSites(row, col);
    }

    private void connectNeighboringOpenSites(int row, int col) {
        int i = toIndex(row, col);

        if (row == 1) {
            uf.union(0, i);
            ufb.union(0, i);
        } else if (isOpen[i - N]) {
            uf.union(i, i - N);
            ufb.union(i, i - N);
        }

        if (row == N) {
            ufb.union(i, N * N + 1);
        } else if (isOpen[i + N]) {
            uf.union(i, i + N);
            ufb.union(i, i + N);
        }

        if (col > 1 && isOpen[i - 1]) {
            uf.union(i, i - 1);
            ufb.union(i, i - 1);
        }

        if (col < N && isOpen[i + 1]) {
            uf.union(i, i + 1);
            ufb.union(i, i + 1);
        }
    }

    public boolean isOpen(int row, int col) {
        return isOpen[toIndex(row, col)];
    }

    public boolean isFull(int row, int col) {
        int i = toIndex(row, col);
        return isOpen[i] && uf.connected(0, i);
    }

    public int numberOfOpenSites() {
        return nOpenSites;
    }

    public boolean percolates() {
        return ufb.connected(0, N * N + 1);
    }
}
