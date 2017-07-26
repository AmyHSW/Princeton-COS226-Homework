import java.util.Stack;

public class Board {

    private final int[][] blocks;

    public Board(int[][] blocks) {
        int n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                this.blocks[i][j] = blocks[i][j];
    }

    public int dimension() {
        return blocks.length;
    }

    public int manhattan() {
        int n = blocks.length;
        int m = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int v = blocks[i][j];
                if (v == 0) continue;
                m += Math.abs((v - 1) / n - i) + Math.abs((v - 1) % n - j);
            }
        }
        return m;
    }

    public int hamming() {
        int n = blocks.length;
        int h = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int v = blocks[i][j];
                if (v == 0) continue;
                if (v != i * n + j + 1) h++;
            }
        }
        return h;
    }

    public boolean isGoal() {
        int n = blocks.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1) return true;
                if (blocks[i][j] != i * n + j + 1) return false;
            }
        }
        return true;
    }

    public Board twin() {
        int n = blocks.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (blocks[i][j] != 0 && blocks[i][j + 1] != 0)
                    return new Board(getTwinMat(blocks, i, j, i, j + 1));
            }
        }
        return null;
    }

    private static int[][] getTwinMat(int[][] mat, int p, int q, int s, int t) {
        int n = mat.length;
        int[][] twin = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                twin[i][j] = mat[i][j];

        int temp = twin[p][q];
        twin[p][q] = twin[s][t];
        twin[s][t] = temp;

        return twin;
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        int n = this.dimension();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (this.blocks[i][j] != that.blocks[i][j]) return false;
        return true;
    }

    public Iterable<Board> neighbors() {
        int n = blocks.length;
        int[] index0 = getBlankIndex(blocks);
        int i0 = index0[0], j0 = index0[1];
        Stack<Board> neighbors = new Stack<Board>();
        if (i0 > 0) {
            neighbors.push(new Board(getTwinMat(blocks, i0, j0, i0 - 1, j0)));
        }
        if (i0 < n - 1) {
            neighbors.push(new Board(getTwinMat(blocks, i0, j0, i0 + 1, j0)));
        }
        if (j0 > 0) {
            neighbors.push(new Board(getTwinMat(blocks, i0, j0, i0, j0 - 1)));
        }
        if (j0 < n - 1) {
            neighbors.push(new Board(getTwinMat(blocks, i0, j0, i0, j0 + 1)));
        }
        return neighbors;
    }

    private static int[] getBlankIndex(int[][] mat) {
        int n = mat.length;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (mat[i][j] == 0) {
                    int[] index0 = {i, j};
                    return index0;
                }
        return new int[0];
    }

    public String toString() {
        int n = blocks.length;
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}
