import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int N;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufb;
    private boolean[] isOpened;
    private int nOpenSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n is not positive");
        }
        N = n;
        uf = new WeightedQuickUnionUF(n * n + 1);
        ufb = new WeightedQuickUnionUF(n * n + 2);
        isOpened = new boolean[n * n + 1];
        for (int i = 0; i < isOpened.length; i++) {
            isOpened[i] = false;
        }
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
        if (isOpened[i]) return;
        isOpened[i] = true;
        nOpenSites++;
        connectNeighboringOpenSites(row, col);
    }

    private void connectNeighboringOpenSites(int row, int col) {
        int i = toIndex(row, col);

        if (row == 1) {
            uf.union(0, i);
            ufb.union(0, i);
        } else if (isOpened[i - N]) {
            uf.union(i, i - N);
            ufb.union(i, i - N);
        }

        if (row == N) {
            ufb.union(i, N * N + 1);
        } else if (isOpened[i + N]) {
            uf.union(i, i + N);
            ufb.union(i, i + N);
        }

        if (col > 1 && isOpened[i - 1]) {
            uf.union(i - 1, i);
            ufb.union(i - 1, i);
        }

        if (col < N && isOpened[i + 1]) {
            uf.union(i, i + 1);
            ufb.union(i, i + 1);
        }
    }

    public boolean isOpen(int row, int col) {
        return isOpened[toIndex(row, col)];
    }

    public boolean isFull(int row, int col) {
        int i = toIndex(row, col);
        return isOpened[i] && uf.connected(0, i);
    }

    public int numberOfOpenSites() {
        return nOpenSites;
    }

    public boolean percolates() {
        return ufb.connected(0, N * N + 1);
    }
}
