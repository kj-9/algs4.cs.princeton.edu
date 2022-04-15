import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {

    private final ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        // Corner cases. Throw an IllegalArgumentException if the argument to the constructor is null, 
        // if any point in the array is null, or if the argument to the constructor contains a repeated point.

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
            if (i > 0 && procPoints[i].compareTo(procPoints[i-1]) == 0)
                throw new IllegalArgumentException("Constructor argument: points should not contain a repeated point.");
        }


        for (int i = 0; i < N - 3; i++) {
            for (int j = i + 1; j < N - 2; j++) {
                double slopeJ = procPoints[i].slopeTo(procPoints[j]);

                for (int k = j + 1; k < N - 1; k++) {
                    double slopeK = procPoints[i].slopeTo(procPoints[k]);

                    if (slopeJ != slopeK) 
                        continue;

                    for (int l = k + 1; l < N; l++) {
                        if (slopeJ == procPoints[i].slopeTo(procPoints[l])) {
                            LineSegment ls = new LineSegment(procPoints[i], procPoints[l]);
                            lineSegments.add(ls);
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        // the number of line segments
        return lineSegments.size();

    }

    public LineSegment[] segments() {
        // the line segments
        LineSegment[] arr = new LineSegment[lineSegments.size()];

        for (int i = 0; i < lineSegments.size(); i++) {
            arr[i] = lineSegments.get(i);
        }
        return arr;

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
/*         StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
 */    
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        System.out.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
     
            //segment.draw();
        }
    }
}