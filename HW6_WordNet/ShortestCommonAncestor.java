import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Topological;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
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
        for (int v : top.order()) {
            root = v;
        }

        this.G = new Digraph(G);
    }

    public int length(int v, int w) {
        validateID(v);
        validateID(w);

        if (v == w) return 0;

        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        if (bfsv.hasPathTo(w)) return bfsv.distTo(w);

        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        if (bfsw.hasPathTo(v)) return bfsw.distTo(v);

        int mindist = G.V();
        for (int i : bfsv.pathTo(root)) {
            if (bfsw.hasPathTo(i)) {
                int dist = bfsv.distTo(i) + bfsw.distTo(i);
                if (dist < mindist) mindist = dist;
            } 
        }
        return mindist;
    }

    private void validateID(int v) {
        int V = G.V();
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("ID " + v + " is not between 0 and " + (V-1));
        }
    }

    public int ancestor(int v, int w) {
        validateID(v);
        validateID(w);

        if (v == w) return v;

        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        if (bfsv.hasPathTo(w)) return bfsv.distTo(w);

        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        if (bfsw.hasPathTo(v)) return bfsw.distTo(v);

        int mindist = G.V();
        int ancestor = root;
        for (int i : bfsv.pathTo(root)) {
            if (bfsw.hasPathTo(i)) {
                int dist = bfsv.distTo(i) + bfsw.distTo(i);
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

        BreadthFirstDirectedPaths bfsa = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths bfsb = new BreadthFirstDirectedPaths(G, subsetB);

        int mindist = G.V();
        for (int i : bfsa.pathTo(root)) {
            if (bfsb.hasPathTo(i)) {
                int dist = bfsa.distTo(i) + bfsb.distTo(i);
                if (dist < mindist) {
                    mindist = dist;
                }
            } 
        }
        return mindist;
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

    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        validateSubset(subsetA);
        validateSubset(subsetB);

        BreadthFirstDirectedPaths bfsa = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths bfsb = new BreadthFirstDirectedPaths(G, subsetB);

        int mindist = G.V();
        int ancestor = root;
        for (int i : bfsa.pathTo(root)) {
            if (bfsb.hasPathTo(i)) {
                int dist = bfsa.distTo(i) + bfsb.distTo(i);
                if (dist < mindist) {
                    mindist = dist;
                    ancestor = i;
                }
            } 
        }
        return ancestor;
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
