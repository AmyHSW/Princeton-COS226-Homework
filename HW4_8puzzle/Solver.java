import edu.princeton.cs.algs4.MinPQ;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Comparator;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private final boolean isSolvable;
    private final int moves;
    private final Deque<Board> solution;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        Node node = new Node(null, initial, 0);
        MinPQ<Node> pq = new MinPQ<Node>(node.priorityOrder());
        pq.insert(node);

        Node nodeT = new Node(null, initial.twin(), 0);
        MinPQ<Node> pqT = new MinPQ<Node>(node.priorityOrder());
        pqT.insert(nodeT);

        while (true) {
            Node min = explore(pq);
            Node minT = explore(pqT);
            if (min.board.isGoal()) {
                isSolvable = true;
                moves = min.moves;
                solution = new ArrayDeque<Board>();
                while (min != null) {
                    solution.push(min.board);
                    min = min.prev;
                }
                break;
            }
            if (minT.board.isGoal()) {
                isSolvable = false;
                moves = -1;
                solution = null;
                break;
            }
        }
    }

    private class Node {
        private final Node prev;
        private final Board board;
        private final int moves;
        private final int priority;

        private Node(Node prev, Board board, int moves) {
            this.prev = prev;
            this.board = board;
            this.moves = moves;
            this.priority = moves + board.manhattan();
        }

        private Comparator<Node> priorityOrder() {
            return new ByManhattan();
        }

        private class ByManhattan implements Comparator<Node> {
            public int compare(Node a, Node b) {
                int ma = a.priority;
                int mb = b.priority;
                if (ma < mb) return -1;
                if (ma > mb) return 1;
                return 0;
            }
        }
    }

    private Node explore(MinPQ<Node> pq) {
        Node min = pq.delMin();
        for (Board neighbor : min.board.neighbors()) {
            if (min.prev != null && neighbor.equals(min.prev.board)) continue;
            Node n = new Node(min, neighbor, min.moves + 1);
            pq.insert(n);
        }
        return min;
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        Solver solver = new Solver(initial);

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
