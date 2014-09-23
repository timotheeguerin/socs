package program;


/**
 * Created by tim on 14-09-19.
 */
public class MinimumFinder extends Program {
    public Point[] points;

    private volatile Point[] thread_results;

    public MinimumFinder() {

    }

    public void init() {
        CoordinatesGenerator generator = new CoordinatesGenerator();
        generator.copyArgsFrom(this);
        generator.init();
        this.points = generator.run();
    }

    @Override
    public Point run() {
        this.thread_results = new Point[p];
        Thread[] threads = new Thread[n];
        for (int i = 0; i != p; i++) {
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

        Point best = thread_results[0];
        for (int i = 1; i != p; i++) {
            if (points[i].compareByY(best) == -1) {
                best = points[i];
            }
        }
        return best;
    }

    public class MinimumFinderThread implements Runnable {
        private int thread_index, start_index, end_index;

        public MinimumFinderThread(int thread_index) {
            this.thread_index = thread_index;
            this.start_index = thread_index * n / p;
            this.end_index = (thread_index + 1) * n / p;
        }

        public void run() {
            Point best = points[start_index];
            for (int i = start_index + 1; i != end_index; i++) {
//                if (points[i].compareByY(best) == -1) {
//                    best = points[i];
//                }
            }
            thread_results[thread_index] = best;
        }
    }
}
