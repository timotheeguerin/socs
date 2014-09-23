package program;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tim on 14-09-17.
 * Generate n random points using p threads
 */
public class CoordinatesGenerator extends Program {
    public Map<Point, Boolean> points_hash = new ConcurrentHashMap<Point, Boolean>();
    public Point[] points;

    public CoordinatesGenerator() {

    }

    @Override
    public void init() {
    }

    @Override
    public Point[] run() {
        points_hash.clear();
        points = new Point[n];
        Thread[] threads = new Thread[n];
        for (int i = 0; i != p; i++) {
            int start_index = i * n / p;
            int end_index = (i + 1) * n / p;
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
//        System.out.println("Point: " + points_hash.size());
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
                    if (points_hash.put(point, true) == null) {
                        points[i] = point;
                        break;
                    }
                }
            }
        }

    }
}