import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node root;

    private class Node {
        private Point2D p;
        private Node left, right;
        private boolean cordinate; // true if using xCord
        private int size;

        public Node(Point2D p, boolean cordinate, int size) {
            this.p = p;
            this.cordinate = cordinate;
            this.size = size;
        }
    }

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null)
            return 0;
        else
            return x.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("argument cant be null");

        root = insert(root, p, true);
    }

    private Node insert(Node n, Point2D p, boolean cordinate) {
        if (n == null)
            return new Node(p, cordinate, 1);

        if (cordinate) {
            if (p.x() < n.p.x())
                n.left = insert(n.left, p, !cordinate);
            else
                n.right = insert(n.right, p, !cordinate);
        } else {
            if (p.y() < n.p.y())
                n.left = insert(n.left, p, !cordinate);
            else
                n.right = insert(n.right, p, !cordinate);
        }
        n.size = 1 + size(n.left) + size(n.right);
        return n;

    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return get(root, p) != null;

    }

    private Point2D get(Node n, Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("argument cant be null");

        if (n == null)
            return null;

        if (n.p.equals(p))
            return p;

        if (n.cordinate) {
            if (p.x() < n.p.x())
                return get(n.left, p);
            else
                return get(n.right, p);
        } else {
            if (p.x() < n.p.x())
                return get(n.left, p);
            else
                return get(n.right, p);
        }

    }

    // draw all points to standard draw
    public void draw() {
        if (root == null)
            return;
        draw(root, 0, 0, 1, 1);
    }

    private void draw(Node n, double x0, double y0, double x1, double y1) {

        if (n == null)
            return;

        if (n.cordinate) {
            StdDraw.setPenRadius(0.001);
            StdDraw.setPenColor(StdDraw.RED);

            StdDraw.line(n.p.x(), y0, n.p.x(), y1);

            if (n.left != null)
                draw(n.left, x0, y0, n.p.x(), y1);

            if (n.right != null)
                draw(n.right, n.p.x(), y0, x1, y1);

        } else {
            StdDraw.setPenRadius(0.001);
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x0, n.p.y(), x1, n.p.y());

            if (n.left != null)
                draw(n.left, x0, y0, x1, n.p.y());

            if (n.right != null)
                draw(n.right, x0, n.p.y(), x1, y1);
        }

        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        n.p.draw();

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("argument cant be null");

        Stack<Point2D> out = new Stack<Point2D>();

        return range(rect, root, 0, 0, 1, 1, out);

    }

    private Stack<Point2D> range(RectHV rect, Node n, double x0, double y0, double x1, double y1, Stack<Point2D> out) {
        if (n == null)
            return out;

        if (rect.contains(n.p))
            out.push(n.p);

        if (n.cordinate) {

            if (n.left != null && rect.intersects(new RectHV(x0, y0, n.p.x(), y1)))
                range(rect, n.left, x0, y0, n.p.x(), y1, out);

            if (n.right != null && rect.intersects(new RectHV(n.p.x(), y0, x1, y1)))
                range(rect, n.right, n.p.x(), y0, x1, y1, out);

        } else {
            if (n.left != null && rect.intersects(new RectHV(x0, y0, x1, n.p.y())))
                range(rect, n.left, x0, y0, x1, n.p.y(), out);

            if (n.right != null && rect.intersects(new RectHV(x0, n.p.y(), x1, y1)))
                range(rect, n.right, x0, n.p.y(), x1, y1, out);

        }

        return out;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("argument cant be null");

        Point2D out = nearest(root, p, root.p, Double.POSITIVE_INFINITY, 0, 0, 1, 1);

        return out;

    }

    private Point2D nearest(Node n, Point2D p, Point2D closest, double distsq, double x0, double y0, double x1,
            double y1) {

        double distsqCur = n.p.distanceSquaredTo(p);

        if (distsqCur < distsq) {
            closest = n.p;
            distsq = distsqCur;
        }
        if (n.left == null && n.right == null)
            return closest;

        if (n.cordinate) {
            if (n.left != null && n.right != null) {
                double distsqL = new RectHV(x0, y0, n.p.x(), y1).distanceSquaredTo(p);
                double distsqR = new RectHV(n.p.x(), y0, x1, y1).distanceSquaredTo(p);

                if (distsqL < distsqR)
                    return nearest(n.left, p, closest, distsq, x0, y0, x1, y1);
                else
                    return nearest(n.right, p, closest, distsq, n.p.x(), y0, x1, y1);

            } else if (n.left != null)
                return nearest(n.left, p, closest, distsq, x0, y0, x1, y1);
            else if (n.right != null)
                return nearest(n.right, p, closest, distsq, n.p.x(), y0, x1, y1);
            else
                return closest;

        } else {
            if (n.left != null && n.right != null) {
                double distsqL = new RectHV(x0, y0, x1, n.p.y()).distanceSquaredTo(p);
                double distsqR = new RectHV(x0, n.p.y(), x1, y1).distanceSquaredTo(p);

                if (distsqL < distsqR)
                    return nearest(n.left, p, closest, distsq, x0, y0, x1, n.p.y());
                else
                    return nearest(n.right, p, closest, distsq, x0, n.p.y(), x1, y1);
            } else if (n.left != null)
                return nearest(n.left, p, closest, distsq, x0, y0, x1, n.p.y());
            else if (n.right != null)
                return nearest(n.right, p, closest, distsq, x0, n.p.y(), x1, y1);
            else
                return closest;

        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree ps = new KdTree();

        System.out.println(ps.isEmpty());
        System.out.println(ps.size());

        ps.insert(new Point2D(0.7, 0.2));
        ps.insert(new Point2D(0.5, 0.4));
        ps.insert(new Point2D(0.2, 0.3));
        ps.insert(new Point2D(0.4, 0.7));
        ps.insert(new Point2D(0.9, 0.6));
        ps.insert(new Point2D(0.1, 0.2));
        ps.insert(new Point2D(0.25, 0.34));

        System.out.println(ps.root.left.right.p);

        System.out.println(ps.isEmpty());
        System.out.println(ps.size());
        System.out.println(ps.contains(new Point2D(0.1, 0.2)));

        RectHV rect = new RectHV(0.1, 0.1, 0.2, 0.2);

        System.out.println(ps.range(rect));

        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        ps.draw();
        // rect.draw();

    }
}