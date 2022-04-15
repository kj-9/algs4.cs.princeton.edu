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
    private final ArrayList<Point[]> lsPoints = new ArrayList<Point[]>();

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
        if (points == null) {
            throw new IllegalArgumentException("Constructor argument: points should not be null.");
        }

        int N = points.length;

        for (int i = 0; i < N; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Constructor argument: points should not contain null elements.");
            }

            if (i > 0 && points[i] == points[i - 1]) {
                throw new IllegalArgumentException("Constructor argument: points should not contain a repeated point.");
            }
        }

        Point[] procPoints = Arrays.copyOf(points, N);

        for (int i = 0; i < N; i++) {

            ArrayList<Point> coPoints = new ArrayList<Point>();
            Double slopeBfr = null;
            Double slopeCrt = null;
            boolean isExists = false;
        
            Arrays.sort(procPoints, points[i].slopeOrder());

            for (int j = 1; j < N; j++) { // j=1 since j=0 is point[i]
                
                
                slopeBfr = slopeCrt;
                slopeCrt = procPoints[0].slopeTo(procPoints[j]);

                if (slopeBfr != null && !slopeBfr.equals(slopeCrt)) {


                    if (coPoints.size() >= 3) {
                        coPoints.add(procPoints[0]); // add self

                       
                        Point[] coPointsArr = new Point[coPoints.size()];

                        for (int k = 0; k < coPoints.size(); k++) {
                            coPointsArr[k] = coPoints.get(k);                            
                        }

                        Arrays.sort(coPointsArr);

                        Point[] ps = new Point[2];
                        ps[0] = coPointsArr[0];
                        ps[1] = coPointsArr[coPointsArr.length - 1];

                        isExists = false;

                        for (Point[] lsp : lsPoints) {
                            if (lsp[0] == ps[0] && lsp[1] == ps[1]) {
                                isExists=true;
                                break;
                            }
                        }
                        
                        if (!isExists) {
                            lsPoints.add(ps);
                        }
                    }
                    coPoints.clear();
                }
                coPoints.add(procPoints[j]);
            }
        }
    }

    public int numberOfSegments() {
        // the number of line segments
        return lsPoints.size();

    }

    public LineSegment[] segments() {
        // the line segments
        LineSegment[] arr = new LineSegment[lsPoints.size()];

        for (int i = 0; i < arr.length; i++) {
            Point[] lsp = lsPoints.get(i);
            arr[i] = new LineSegment(lsp[0], lsp[1]);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        System.out.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
     
            //segment.draw();
        }
   /*      StdDraw.show();
    */
 }
}