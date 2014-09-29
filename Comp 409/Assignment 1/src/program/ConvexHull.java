package program;

/**
 * Build the convex hull
 * Created by tim on 14-09-26.
 */
public class ConvexHull extends Program {

    public Point[] points;

    @Override
    public Object run() {
        return null;
    }


    public class ConvexHullThread implements Runnable {
        private int start_index, end_index;

        public ConvexHullThread(int start_index, int end_index) {
            this.start_index = start_index;
            this.end_index = end_index;
        }

        public void run() {
            for (int i = start_index; i != end_index; i++) {

            }
        }

        /**
         * Check if the points are counter-clockwise
         *
         * @param p1 First point
         * @param p2 Middle point
         * @param p3 Last point
         * @return true if  the points are counter clockwise, false if clockwise or linear
         */
        private boolean isCounterClockWise(Point p1, Point p2, Point p3) {
            return (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x) > 0;
        }

    }
}
