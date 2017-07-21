import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    private final ArrayList<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
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
        ArrayList<Point[]> longLine = new ArrayList<Point[]>();
        ArrayList<Double> slopes = new ArrayList<Double>();
        for (int i = 0; i < n - 3; i++) {
            Arrays.sort(copy, i, n);
            Arrays.sort(copy, i, n, copy[i].slopeOrder());
            Point p1 = copy[i];
            for (int j = i + 1; j < n - 1; j++) {
                int count = 1;
                double s1 = p1.slopeTo(copy[j]);
                while (s1 == p1.slopeTo(copy[j + 1])) {
                    j++;
                    count++;
                    if (j >= n - 1) break;
                }
                if (count >= 3) {
                    Point p2 = copy[j];
                    if (!slopes.contains(s1) || !isCounted(longLine, s1, p2)) {
                        LineSegment segment = new LineSegment(p1, p2);
                        segments.add(segment);
                        if (count >= 4) {
                            Point[] line = {p1, p2};
                            longLine.add(line);
                            slopes.add(s1);
                        }
                    }
                }
            }
        }
    }

    private boolean isCounted(ArrayList<Point[]> longLine, double slope, Point p2) {
        for (Point[] points : longLine) {
            if (p2.compareTo(points[1]) == 0 && slope == points[0].slopeTo(points[1]))
                return true;
        }
        return false;
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

        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
