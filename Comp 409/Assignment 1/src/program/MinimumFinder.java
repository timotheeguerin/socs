package program;


/**
 * Find the minimum number of points
 * Created by tim on 14-09-19.
 */
public class MinimumFinder extends Program {
    public Point[] points;

    private volatile int[] thread_results;

    public MinimumFinder() {

    }

    /**
     * Static method to find the point with the minimum Y-value
     *
     * @param points    Points to check
     * @param thread_nb Number of thread to use
     * @return the index of the point with the smallest Y-value
     */
    public static int findMinimumIndex(Point[] points, int thread_nb) {
        MinimumFinder finder = new MinimumFinder();
        finder.setArgs(points.length, thread_nb);
        finder.points = points;
        return finder.run();
    }

    /**
     * Static method to find the point with the minimum Y-value
     *
     * @param points    Points to check
     * @param thread_nb Number of thread to use
     * @return the point with the smallest Y-value
     */
    public static Point findMinimum(Point[] points, int thread_nb) {
        return points[findMinimumIndex(points, thread_nb)];
    }

    /**
     * @return the index of the smallest point
     */
    @Override
    public Integer run() {
        this.thread_results = new int[thread_nb];
        Thread[] threads = new Thread[n];
        for (int i = 0; i != thread_nb; i++) {
            threads[i] = new Thread(new MinimumFinderThread(i));
            threads[i].start();

        }

        for (Thread thread : threads) {
            if (thread != null) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        int best = thread_results[0];
        for (int i = 1; i != thread_nb; i++) {
            if (points[thread_results[i]].compareByY(points[best]) == -1) {
                best = thread_results[i];
            }
        }
        return best;
    }

    public class MinimumFinderThread implements Runnable {
        private int thread_index, start_index, end_index;

        public MinimumFinderThread(int thread_index) {
            this.thread_index = thread_index;
            this.start_index = thread_index * n / thread_nb;
            this.end_index = (thread_index + 1) * n / thread_nb;
        }

        public void run() {
            int best = 0;
            for (int i = start_index + 1; i != end_index; i++) {
                if (points[i].compareByY(points[best]) == -1) {
                    best = i;
                }
            }
            thread_results[thread_index] = best;
        }
    }
}
