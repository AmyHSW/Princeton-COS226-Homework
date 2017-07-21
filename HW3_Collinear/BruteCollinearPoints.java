import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private final ArrayList<LineSegment> segments;    

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();

        if (Arrays.asList(points).contains(null))
            throw new IllegalArgumentException();

        int n = points.length;
        Point[] copy = Arrays.copyOf(points, n);
        Arrays.sort(copy);        
        for (int i = 0; i < n - 1; i++) {
            if (copy[i].compareTo(copy[i + 1]) == 0) 
                throw new IllegalArgumentException();
        }

        segments = new ArrayList<LineSegment>();
        for (int i = 0; i < n - 3; i++) {
            for (int j = i + 1; j < n - 2; j++) {
                for (int k = j + 1; k < n - 1; k++) {
                    if (isCollinear(copy[i], copy[j], copy[k])) {
                        for (int m = k + 1; m < n; m++) {
                            if (isCollinear(copy[i], copy[j], copy[m])) {
                                LineSegment segment = new LineSegment(copy[i], copy[m]);
                                segments.add(segment);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isCollinear(Point p, Point q, Point r) {
        return p.slopeTo(q) == p.slopeTo(r);
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        int len = segments.size();
        LineSegment[] temp = new LineSegment[len];
        for (int i = 0; i < len; i++) {
            temp[i] = segments.get(i);
        }
        return temp;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
