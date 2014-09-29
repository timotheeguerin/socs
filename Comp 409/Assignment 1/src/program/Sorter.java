package program;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Sort
 * Created by tim on 14-09-19.
 */
public class Sorter extends Program {
    private ExecutorService executor;

    public Point[] points;

    private int minPartitionSize;


    /**
     * Sort in place the given array of points
     *
     * @param points    Points to sort
     * @param thread_nb Number of thread to use
     */
    public static void sort(Point[] points, int thread_nb) {
        Sorter sorter = new Sorter();
        sorter.setArgs(points.length, thread_nb);
        sorter.points = points;
        sorter.run();
    }

    /**
     * Check if an array is sorted
     *
     * @param points Points to check
     */
    public static boolean isSorted(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i - 1].angle() > points[i].angle()) {
                System.out.println("Error not sorted");
                return false;
            }
        }
        return true;
    }

    @Override
    public Point[] run() {
        this.minPartitionSize = points.length / thread_nb;
        executor = Executors.newFixedThreadPool(this.thread_nb);
        Thread thread = new Thread(new SorterThread(0, points.length - 1));
        thread.run();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        executor.shutdown();
        return points;
    }


    class SorterThread implements Runnable {

        private int start_index, end_index;

        public SorterThread(int start_index, int end_index) {
            this.start_index = start_index;
            this.end_index = end_index;
        }

        @Override
        public void run() {
            quicksort(start_index, end_index);
        }

        public void quicksort(int start, int end) {
            int len = end - start + 1;

            if (len <= 1)
                return;

            int pivot_index = medianOfThree(start, end);
            Point pivotValue = points[pivot_index];

            swap(pivot_index, end);

            int storeIndex = start;
            for (int i = start; i < end; i++) {
                if (points[i].compareTo(pivotValue) != 1) {
                    swap(i, storeIndex);
                    storeIndex++;
                }
            }

            swap(storeIndex, end);

            if (len > minPartitionSize) {
                // Sort the 2 part of the array
                // Current thread sort the right part and a new thread will sort the left
                SorterThread quick = new SorterThread(start, storeIndex - 1);
                Future<?> future = executor.submit(quick);
                quicksort(storeIndex + 1, end);

                try {
                    future.get(1000, TimeUnit.SECONDS);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                quicksort(start, storeIndex - 1);
                quicksort(storeIndex + 1, end);
            }
        }

        /**
         * Find the median of three
         *
         * @param start Start index
         * @param end   End index
         * @return the point beginning the median of the start, middle and end point in the array range given
         */
        private int medianOfThree(int start, int end) {
            int mid_index = (end - start) / 2 + start;
            Point start_point = points[start];
            Point end_point = points[end];
            Point mid_point = points[mid_index];
            if (start_point.compareTo(mid_point) == mid_point.compareTo(end_point)) {
                return mid_index;
            } else if (start_point.compareTo(end_point) == end_point.compareTo(mid_point)) {
                return start;
            } else {
                return end;
            }

        }

        private void swap(int index1, int index2) {
            Point tmp = points[index1];
            points[index1] = points[index2];
            points[index2] = tmp;
        }


    }
}
