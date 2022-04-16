import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

/* 
- Think of p as the origin.
- For each other point q, determine the slope it makes with p.
- Sort the points according to the slopes they makes with p.
- Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p. If so, these points, together with p, are collinear.

Applying this method for each of the n points in turn yields an efficient algorithm to the problem. 
The algorithm solves the problem because points that have equal slopes with respect to p are collinear,
and sorting brings such points together. The algorithm is fast because the bottleneck operation is sorting.
 */
public class FastCollinearPoints {
    private LineSegment[] lineSegment;

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
        if (points == null) {
            throw new IllegalArgumentException("Constructor argument: points should not be null.");
        }

        int N = points.length;

        for (int i = 0; i < N; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("Constructor argument: points should not contain null elements.");
        }

        Point[] procPoints = Arrays.copyOf(points, N);
        Arrays.sort(procPoints);

        for (int i = 0; i < N; i++) {
            if (i > 0 && procPoints[i].compareTo(procPoints[i - 1]) == 0)
                throw new IllegalArgumentException("Constructor argument: points should not contain a repeated point.");
        }

        ArrayList<Point[]> lsPoints = new ArrayList<Point[]>();

        for (int i = 0; i < N; i++) {

            ArrayList<Point> coPoints = new ArrayList<Point>();

            Double slopeBfr = null;
            Double slopeCrt = null;

            Arrays.sort(procPoints, points[i].slopeOrder());

            for (int j = 1; j <= N; j++) { // j=1 since j=0 is point[i]
                slopeBfr = slopeCrt;

                if (j != N)
                    slopeCrt = procPoints[0].slopeTo(procPoints[j]);

                if (slopeBfr != null && !slopeBfr.equals(slopeCrt) || (j == N)) {

                    if (coPoints.size() >= 3) {
                        coPoints.add(procPoints[0]); // add self

                        Point[] coPointsArr = new Point[coPoints.size()];

                        for (int k = 0; k < coPoints.size(); k++) {
                            coPointsArr[k] = coPoints.get(k);
                        }

                        Arrays.sort(coPointsArr);

                        Point[] lsPoint = new Point[2];
                        lsPoint[0] = coPointsArr[0];
                        lsPoint[1] = coPointsArr[coPointsArr.length - 1];

                        if (lsPoints.size() == 0)
                            lsPoints.add(lsPoint);
                        else {
                            for (int m = 0; m < lsPoints.size(); m++) {
                                if (lsPoints.get(m)[0] == lsPoint[0] && lsPoints.get(m)[1] == lsPoint[1]) {
                                    break;
                                }

                                if (m == (lsPoints.size() - 1))
                                    lsPoints.add(lsPoint);
                            }
                        }
                    }
                    coPoints.clear();
                }

                if (j != N) // need to check constant also...
                    coPoints.add(procPoints[j]);
            }
        }
        lineSegment = new LineSegment[lsPoints.size()];
        for (int i = 0; i < lsPoints.size(); i++) {
            lineSegment[i] = new LineSegment(lsPoints.get(i)[0], lsPoints.get(i)[1]);
        }
    }

    public int numberOfSegments() {
        // the number of line segments
        return lineSegment.length;

    }

    public LineSegment[] segments() {
        // the line segments
        return lineSegment.clone();

    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        /*
         * StdDraw.enableDoubleBuffering();
         * StdDraw.setXscale(0, 32768);
         * StdDraw.setYscale(0, 32768);
         * for (Point p : points) {
         * p.draw();
         * }
         * StdDraw.show();
         */
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        System.out.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);

            // segment.draw();
        }
        /*
         * StdDraw.show();
         */
    }
}