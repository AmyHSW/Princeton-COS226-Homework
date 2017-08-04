import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class KdTree {

    private Node root;
    private int n;
    
    public KdTree() {
        root = null;
        n = 0;
    }

    private static class Node {
        private final Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;

        private Node(Point2D p) {
            this.p = p;
        }
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, 0, 0.0, 0.0, 1.0, 1.0);
    }

    private Node insert(Node x, Point2D p, int i, double xmin, double ymin, double xmax, double ymax) {
        if (x == null) {
            Node node = new Node(p);
            node.rect = new RectHV(xmin, ymin, xmax, ymax);
            n++;
            return node;
        }
        if (p.equals(x.p)) return x;
        double cmp;
        switch (i % 2) {
        case 0: 
            cmp = p.x() - x.p.x();
            if (cmp < 0) {
                xmax = x.p.x();
                x.lb = insert(x.lb, p, i + 1, xmin, ymin, xmax, ymax);
            } else {
                xmin = x.p.x();
                x.rt = insert(x.rt, p, i + 1, xmin, ymin, xmax, ymax);
            }
            break;
        case 1:
            cmp = p.y() - x.p.y();
            if (cmp < 0) {
                ymax = x.p.y();
                x.lb = insert(x.lb, p, i + 1, xmin, ymin, xmax, ymax);
            } else {
                ymin = x.p.y();
                x.rt = insert(x.rt, p, i + 1, xmin, ymin, xmax, ymax);
            }
            break;
        }
        return x;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node x = root;
        int i = 0;
        while (x != null) {
            if (p.equals(x.p)) return true;
            double cmp = 0;
            switch (i++ % 2) {
            case 0:
                cmp = p.x() - x.p.x();
                break;
            case 1:
                cmp = p.y() - x.p.y();
                break;
            }
            if (cmp < 0) x = x.lb;
            else x = x.rt;
        }
        return false;
    }

    public void draw() {
        draw(root, 0);
    }

    private void draw(Node x, int i) {
        if (x == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();
        StdDraw.setPenRadius();
        switch (i % 2) {
        case 0: 
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
            break;
        case 1: 
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
            break;
        }
        draw(x.lb, i + 1);
        draw(x.rt, i + 1);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> q = new Queue<Point2D>();
        range(root, rect, q);
        return q;
    }

    private void range(Node x, RectHV rect, Queue<Point2D> q) {
        if (x == null) return;
        if (!x.rect.intersects(rect)) return;
        double px = x.p.x();
        double py = x.p.y();
        if (px >= rect.xmin() && px <= rect.xmax() && py >= rect.ymin() && py <= rect.ymax())
            q.enqueue(x.p);
        range(x.lb, rect, q);
        range(x.rt, rect, q);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        Point2D r = root.p;
        return nearest(root, p, r, 0);
    }

    private Point2D nearest(Node x, Point2D p, Point2D r, int i) {
        if (x == null) return r;
        if (x.p.distanceSquaredTo(p) < r.distanceSquaredTo(p)) r = x.p;
        double cmp = 0;
        switch (i % 2) {
        case 0:
            cmp = p.x() - x.p.x();
            break;
        case 1:
            cmp = p.y() - x.p.y();
            break;
        }
        if (cmp < 0) {
            r = nearest(x.lb, p, r, i + 1);
            if (x.rt != null && r.distanceSquaredTo(p) > x.rt.rect.distanceSquaredTo(p))
                r = nearest(x.rt, p, r, i + 1);
        } else {
            r = nearest(x.rt, p, r, i + 1);
            if (x.lb != null && r.distanceSquaredTo(p) > x.lb.rect.distanceSquaredTo(p))
                r = nearest(x.lb, p, r, i + 1);
        }
        return r;
    }
}
