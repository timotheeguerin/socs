package program;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tim on 14-09-17.
 * Generate n random points using p threads
 */
public class CoordinatesGenerator extends Program {

    private Map<Point, Boolean> points;

    public CoordinatesGenerator(int q, int n, int p) {
        this.setArgs(q, n, p);

        this.points = new ConcurrentHashMap<Point, Boolean>();
    }

    /**
     * Add a new point to the array only if the point doesn't exist yet.
     *
     * @param point Point to add
     * @return boolean if more points can be added
     */
    public boolean addPoint(Point point) {
        if (points.size() < n) {
            points.put(point, true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void run() {
        Thread[] threads = new Thread[n];
        for (int i = 0; i != p; i++) {
            threads[i] = new Thread(new CoordinatesGeneratorThread(this));
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
        System.out.println("Point gen: " + points.size());
    }

    public class CoordinatesGeneratorThread implements Runnable {
        public CoordinatesGenerator generator;

        public CoordinatesGeneratorThread(CoordinatesGenerator generator) {
            this.generator = generator;
        }

        public void run() {
            while (addPoint(Point.random())) {

            }
        }
    }
}
