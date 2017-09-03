import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Topological;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class ShortestCommonAncestor {

    private final Digraph G;
    private int root;

    public ShortestCommonAncestor(Digraph G) {
        if (G == null) throw new IllegalArgumentException("G " + G + " is null");

        Topological top = new Topological(G);
        if (!top.hasOrder()) throw new IllegalArgumentException("G " + G + " is not a DAG");
        int V = G.V();
        for (int v = 0; v < V; v++) {
            if (G.outdegree(v) == 0) root = v;
        }
        this.G = new Digraph(G);
    }

    public int length(int v, int w) {
        validateID(v);
        validateID(w);
        if (v == w) return 0;

        RedBlackBST<Integer, Integer> mapv = bfs(G, v);
        RedBlackBST<Integer, Integer> mapw = bfs(G, w);
        int length = Integer.MAX_VALUE;
        for (int i : mapv.keys()) {
            if (mapw.contains(i)) {
                int dist = mapv.get(i) + mapw.get(i);
                if (dist < length) length = dist;
            }
        }
        return length;
    }

    public int ancestor(int v, int w) {
        validateID(v);
        validateID(w);
        if (v == w) return v;
        if (v == root || w == root) return root;

        RedBlackBST<Integer, Integer> mapv = bfs(G, v);
        RedBlackBST<Integer, Integer> mapw = bfs(G, w);
        int mindist = Integer.MAX_VALUE;
        int ancestor = root;
        for (int i : mapv.keys()) {
            if (mapw.contains(i)) {
                int dist = mapv.get(i) + mapw.get(i);
                if (dist < mindist) {
                    mindist = dist;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        validateSubset(subsetA);
        validateSubset(subsetB);

        RedBlackBST<Integer, Integer> mapA = bfs(G, subsetA);
        RedBlackBST<Integer, Integer> mapB = bfs(G, subsetB);
        int length = Integer.MAX_VALUE;
        for (int i : mapA.keys()) {
            if (mapB.contains(i)) {
                int dist = mapA.get(i) + mapB.get(i);
                if (dist < length) length = dist;
            }
        }
        return length;
    }

    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        validateSubset(subsetA);
        validateSubset(subsetB);

        RedBlackBST<Integer, Integer> mapA = bfs(G, subsetA);
        RedBlackBST<Integer, Integer> mapB = bfs(G, subsetB);
        int length = Integer.MAX_VALUE;
        int ancestor = root;
        for (int i : mapA.keys()) {
            if (mapB.contains(i)) {
                int dist = mapA.get(i) + mapB.get(i);
                if (dist < length) {
                    length = dist;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

    private RedBlackBST<Integer, Integer> bfs(Digraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        q.enqueue(s);
        RedBlackBST<Integer, Integer> map = new RedBlackBST<Integer, Integer>();
        map.put(s, 0);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!map.contains(w)) {
                    q.enqueue(w);
                    map.put(w, map.get(v) + 1);
                }
            }
        }
        return map;
    }

    private RedBlackBST<Integer, Integer> bfs(Digraph G, Iterable<Integer> subset) {
        Queue<Integer> q = new Queue<Integer>();
        RedBlackBST<Integer, Integer> map = new RedBlackBST<Integer, Integer>();
        for (int s : subset) {
            q.enqueue(s);
            map.put(s, 0);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!map.contains(w)) {
                    q.enqueue(w);
                    map.put(w, map.get(v) + 1);
                }
            }
        }
        return map;
    }

    private void validateID(int v) {
        int V = G.V();
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("ID " + v + " is not between 0 and " + (V-1));
        }
    }

    private void validateSubset(Iterable<Integer> subset) {
        if (subset == null) {
            throw new IllegalArgumentException("subset " + subset + " is null");
        }
        int V = G.V();
        for (Integer i : subset) {
            if (i == null) {
                throw new IllegalArgumentException("subset " + subset + " contains null item");
            }
            if (i < 0 || i >= V) {
                throw new IllegalArgumentException("vertex " + i + " in subset " + subset
                                                   + " is not between 0 and " + (V-1));
            }
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
