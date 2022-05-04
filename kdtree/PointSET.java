import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {

    private final SET<Point2D> points = new SET<Point2D>();

    // construct an empty set of points
    public PointSET() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("argument cant be null");
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("argument cant be null");

        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {

        for (Point2D p : points) {
            p.draw();
        }

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("argument cant be null");

        Stack<Point2D> out = new Stack<Point2D>();

        for (Point2D p : points) {
            if (rect.contains(p))
                out.push(p);
        }

        return out;

    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("argument cant be null");

        double distMin = Double.POSITIVE_INFINITY;
        double dist;
        Point2D out = null;

        for (Point2D pInset : points) {
            dist = p.distanceSquaredTo(pInset);
            if (dist < distMin) {
                distMin = dist;
                out = pInset;
            }
        }

        return out;

    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET ps = new PointSET();

        System.out.println(ps.isEmpty());
        System.out.println(ps.size());

        ps.insert(new Point2D(0.1, 0.2));
        ps.insert(new Point2D(0.2, 0.1));
        ps.insert(new Point2D(0.1, 0.1));

        System.out.println(ps.isEmpty());
        System.out.println(ps.size());
        System.out.println(ps.contains(new Point2D(0.1, 0.2)));

        RectHV rect = new RectHV(0.1, 0.1, 0.2, 0.2);

        System.out.println(ps.range(rect));

        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        ps.draw();
        rect.draw();

    }
}