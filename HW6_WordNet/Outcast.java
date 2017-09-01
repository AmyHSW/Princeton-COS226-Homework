import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new IllegalArgumentException();
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        if (nouns == null) throw new IllegalArgumentException();
        int len = nouns.length;
        int maxdist = -1;
        String outcast = null;
        for (int i = 0; i < len; i++) {
            String word = nouns[i];
            int dist = 0;
            for (int j = 0; j < len; j++) {
                if (i == j) continue;
                dist += wordnet.distance(word, nouns[j]);
            }
            if (dist > maxdist) {
                maxdist = dist;
                outcast = word;
            }
        }
        return outcast;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
