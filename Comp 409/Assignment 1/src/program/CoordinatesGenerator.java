package program;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tim on 14-09-17.
 * Generate n random points using thread_nb threads
 */
public class CoordinatesGenerator extends Program {
    public Map<Point, Boolean> points_hash = new ConcurrentHashMap<Point, Boolean>();
    public Point[] points;

    public boolean skip_uniqueness = false;

    public CoordinatesGenerator() {

    }

    /**
     * Static method used to generate an array of random unique points
     *
     * @param n         Amount of point to generate
     * @param thread_nb Number of thread to use
     * @return points generated
     */
    public static Point[] generate(int n, int thread_nb) {
        CoordinatesGenerator generator = new CoordinatesGenerator();
        generator.setArgs(n, thread_nb);
        return generator.run();
    }

    /**
     * Static method used to generate an array of random NON UNIQUE points
     *
     * @param n         Amount of point to generate
     * @param thread_nb Number of thread to use
     * @return points generated
     */
    public static Point[] generateNonUnique(int n, int thread_nb) {
        CoordinatesGenerator generator = new CoordinatesGenerator();
        generator.setArgs(n, thread_nb);
        generator.skip_uniqueness = true;
        return generator.run();
    }

    @Override
    public Point[] run() {
        points_hash.clear();
        points = new Point[n];
        Thread[] threads = new Thread[n];
        for (int i = 0; i != thread_nb; i++) {
            int start_index = i * n / thread_nb;
            int end_index = (i + 1) * n / thread_nb;
            threads[i] = new Thread(new CoordinatesGeneratorThread(start_index, end_index));
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
        return points;
    }

    public class CoordinatesGeneratorThread implements Runnable {
        private int start_index, end_index;

        public CoordinatesGeneratorThread(int start_index, int end_index) {
            this.start_index = start_index;
            this.end_index = end_index;
        }

        public void run() {
            for (int i = start_index; i != end_index; i++) {
                while (true) {
                    Point point = Point.random();
                    if (skip_uniqueness || points_hash.put(point, true) == null) {
                        points[i] = point;
                        break;
                    }
                }
            }
        }

    }
}