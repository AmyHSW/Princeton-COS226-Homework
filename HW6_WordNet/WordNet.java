import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Bag;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    private final String[] synsets;
    private final RedBlackBST<String, Bag<Integer>> bst;
    private final Digraph G;
    private final ShortestCommonAncestor sca;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null) {
            throw new IllegalArgumentException("synsets is null");
        }
        if (hypernyms == null) {
            throw new IllegalArgumentException("hypernyms is null");
        }

        String[] synLines = readFileLines(synsets);
        String[] hypLines = readFileLines(hypernyms);

        int V = synLines.length;;
        this.synsets = new String[V];
        bst = new RedBlackBST<String, Bag<Integer>>();
        G = new Digraph(V);

        buildNounsBST(synLines);
        buildDigraph(hypLines);

        sca = new ShortestCommonAncestor(G);
    }

    private String[] readFileLines(String fileName) {
        try {
            In in = new In(fileName);
            return in.readAllLines();
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid file name in Wordnet constructor", e);
        }
    }

    private void buildNounsBST(String[] synLines) {
        int V = synLines.length;
        for (int i = 0; i < V; i++) {
            String line = synLines[i];
            int begin = line.indexOf(",");
            int end = line.indexOf(",", begin + 1);
            //Store synsets in String[] synsets
            String syn = line.substring(begin + 1, end);
            synsets[i] = syn;

            StringTokenizer st = new StringTokenizer(syn);
            while (st.hasMoreTokens()) {
                String noun = st.nextToken();
                if (!bst.contains(noun)) {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(i);
                    bst.put(noun, bag);
                }
                else {
                    bst.get(noun).add(i);
                }
            }
        }
    }

    private void buildDigraph(String[] hypLines) {
        int V = hypLines.length;
        for (int i = 0; i < V; i++) {
            String line = hypLines[i];
            int begin = line.indexOf(",");
            if (begin == -1) continue;
            StringTokenizer st = new StringTokenizer(line.substring(begin + 1), ",");
            while (st.hasMoreTokens()) {
                int hyp = Integer.parseInt(st.nextToken());
                G.addEdge(i, hyp);
            }
        }
    }

    public Iterable<String> nouns() {
        return bst.keys();
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("word is null");
        }
        return bst.contains(word);
    }

    public String sca(String noun1, String noun2) {
        validateNoun(noun1);
        validateNoun(noun2);
        return synsets[sca.ancestor(bst.get(noun1), bst.get(noun2))];
    }

    public int distance(String noun1, String noun2) {
        validateNoun(noun1);
        validateNoun(noun2);
        return sca.length(bst.get(noun1), bst.get(noun2));
    }

    private void validateNoun(String noun) {
        if (!isNoun(noun)) {
            throw new IllegalArgumentException("noun " + noun + " is not a WordNet noun");
        }
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String noun1 = StdIn.readString();
            String noun2 = StdIn.readString();
            if (!wordnet.isNoun(noun1) || !wordnet.isNoun(noun2)) continue;
            int distance = wordnet.distance(noun1, noun2);
            String sca = wordnet.sca(noun1, noun2);
            StdOut.println("distance = " + distance + ", ancestor = " + sca);
        }
    }
}
