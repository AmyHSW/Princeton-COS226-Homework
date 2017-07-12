import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufb;
    private boolean[][] isOpened;
    private int nOpenSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n is not positive");
        }
        uf = new WeightedQuickUnionUF(n * n + 1);
        ufb = new WeightedQuickUnionUF(n * n + 2);
        isOpened = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                isOpened[i][j] = false;
            }
        }
        nOpenSites = 0;
    }

    public void open(int row, int col) {
        validateIndices(row, col);
        if (isOpened[row - 1][col - 1]) return;
        isOpened[row - 1][col - 1] = true;
        nOpenSites++;
        connectNeighboringOpenSites(row, col);
    }

    private void validateIndices(int row, int col) {        
        if (row < 1 || row > isOpened.length) {
            throw new IllegalArgumentException("Row index out of range: " + row);
        }
        if (col < 1 || col > isOpened.length) {
            throw new IllegalArgumentException("Column index out of range: " + col);
        }
    }

    private void connectNeighboringOpenSites(int row, int col) {
        int n = isOpened.length;
        int i = (row - 1) * n + col;

        if (n == 1) {
            uf.union(0, i);
            ufb.union(0, i);
            ufb.union(i, 2);
            return;
        }

        if (row == 1) {
            uf.union(0, i);
            ufb.union(0, i);
            if (isOpened[row][col - 1]) {
                uf.union(i, i + n);
                ufb.union(i, i + n);
            }
        } else if (row == n) {
            ufb.union(i, n * n + 1);
            if (isOpened[row - 2][col - 1]) {
                uf.union(i, i - n);
                ufb.union(i, i - n);
            }
        } else {
            if (isOpened[row - 2][col - 1]) {
                uf.union(i, i - n);
                ufb.union(i, i - n);
            }
            if (isOpened[row][col - 1]) {
                uf.union(i, i + n);
                ufb.union(i, i + n);
            }
        }
        if (col > 1 && isOpened[row - 1][col - 2]) {
            uf.union(i - 1, i);
            ufb.union(i - 1, i);
        }
        if (col < n && isOpened[row - 1][col]) {
            uf.union(i, i + 1);
            ufb.union(i, i + 1);
        }
    }

    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return isOpened[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        int i = (row - 1) * isOpened.length + col;
        return isOpened[row - 1][col - 1] && uf.connected(0, i);
    }

    public int numberOfOpenSites() {
        return nOpenSites;
    }

    public boolean percolates() {
        int n = isOpened.length;
        return ufb.connected(0, n * n + 1);
    }
}
