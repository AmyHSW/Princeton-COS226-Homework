import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {

    private final SET<Point2D> set;
    
    public PointSET() {
        set = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!set.contains(p)) set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return set.contains(p);
    }

    public void draw() {
        for (Point2D p : set)
            p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        double xmin = rect.xmin();
        double ymin = rect.ymin();
        double xmax = rect.xmax();
        double ymax = rect.ymax();
        SET<Point2D> range = new SET<Point2D>();
        for (Point2D p : set) {
            double x = p.x();
            double y = p.y();
            if (x >= xmin && x <= xmax && y >= ymin && y <= ymax)
                range.add(p);
        }
        return range;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (set.isEmpty()) return null;
        Point2D nearest = null;
        double min = Double.POSITIVE_INFINITY;
        for (Point2D p2 : set) {
            double d = p.distanceSquaredTo(p2);
            if (d < min) {
                nearest = p2;
                min = d;
            }
        }
        return nearest;
    }
}
