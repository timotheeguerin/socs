package graham;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Sort
 * Created by tim on 14-09-19.
 */
public class Sorter extends Program {
    private ExecutorService executor;
    private int sort_start_index = 0;
    private int sort_end_index = -1;
    public Point[] points;

    private int minPartitionSize;

    private int runningThreads;

    protected synchronized void threadStarted() {
        ++runningThreads;
    }

    protected synchronized void threadFinished() {
        --runningThreads;
    }

    /**
     * Check if the number of slave threads reporting they have finished working
     * is equal to the number of slave threads spawned.
     *
     * @return True if all spawned threads have finished, false otherwise.
     */
    private synchronized boolean waitingForThreads() {
        if (runningThreads == 0) {
            return true;
        }
        return false;
    }


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
     * Sort in place the given array of points from the start index to the end index
     *
     * @param points    Points to sort
     * @param thread_nb Number of thread to use
     */
    public static void sort(Point[] points, int thread_nb, int start_index, int end_index) {
        Sorter sorter = new Sorter();
        sorter.setArgs(points.length, thread_nb);
        sorter.points = points;
        sorter.sort_start_index = start_index;
        sorter.sort_end_index = end_index;
        sorter.run();
    }

    /**
     * Debug function
     * Check if an array is sorted
     *
     * @param points Points to check
     */
    public static boolean isSorted(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i - 1].angle() > points[i].angle()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Point[] run() {
        if (sort_end_index == -1) {
            sort_end_index = points.length - 1;
        }
        this.minPartitionSize = (sort_end_index - sort_start_index + 1) / thread_nb;
        executor = Executors.newFixedThreadPool(this.thread_nb);
        threadStarted();
        executor.execute(new SorterThread(sort_start_index, sort_end_index));
        while (!waitingForThreads()) {
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return points;
    }

    public static void swap(Object[] array, int index1, int index2) {
        Object tmp = array[index1];
        array[index1] = array[index2];
        array[index2] = tmp;
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
            threadFinished();
        }

        /**
         * Quicksort recursive function
         * @param start start
         * @param end end
         */
        public void quicksort(int start, int end) {
            int len = end - start + 1;
            if (start > end || len <= 1)
                return;

            int pivot_index = medianOfThree(start, end);
            Point pivotValue = points[pivot_index];

            swap(points, pivot_index, end);

            int storeIndex = start;
            for (int i = start; i < end; i++) {
                if (points[i].compareTo(pivotValue) != 1) {
                    swap(points, i, storeIndex);
                    storeIndex++;
                }
            }

            swap(points, storeIndex, end);

            if (len > minPartitionSize) {
                // Sort the 2 part of the array
                // Current thread sort the right part and a new thread will sort the left
                threadStarted();
                SorterThread quick = new SorterThread(start, storeIndex - 1);
                executor.execute(quick);
                quicksort(storeIndex + 1, end);

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


    }
}
