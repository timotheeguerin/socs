package question1;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Question 1: Splitter.
 * Created by tim on 14-10-12.
 */
public class Splitter {

    private int thread_nb;
    private int n;
    private int[][] grid;
    private AtomicIntegerArray[] lockGrid;
    private AtomicIntegerArray idCount;
    private CountDownLatch latch;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Missing arguments: expecting 2 (thread number, number of run)");
        }

        int p = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);

        //Run
        Splitter splitter = new Splitter(p, n);
        splitter.run();

        //Display results
        int sum = 0;
        for (int i = 0; i < splitter.idCount.length(); i++) {
            sum += splitter.idCount.get(i);
        }

        System.out.println("----------------------------------------");
        System.out.println(" Grid ids:");
        System.out.println("----------------------------------------");
        for (int i = 0; i < splitter.thread_nb; i++) {
            System.out.print("\t");
            for (int j = 0; j < splitter.thread_nb - i; j++) {
                System.out.print(splitter.grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println(" Grid hits");
        System.out.println("----------------------------------------");
        for (int i = 0; i < splitter.thread_nb; i++) {
            System.out.print("\t");
            for (int j = 0; j < splitter.thread_nb - i; j++) {
                System.out.print(splitter.idCount.get(splitter.grid[i][j]) + " ");
            }
            System.out.println();
        }
        System.out.println("----------------------------------------");

        System.out.println(sum);
    }

    public Splitter(int thread_nb, int n) {
        this.thread_nb = thread_nb;
        this.n = n;
        lockGrid = new AtomicIntegerArray[thread_nb];
        grid = new int[thread_nb][thread_nb];

        int count = 1;
        for (int i = 0; i < thread_nb; i++) {

            for (int j = 0; j < thread_nb - i; j++) {
                grid[i][j] = count;
                count++;
            }
        }
        idCount = new AtomicIntegerArray(count);
    }

    /**
     * Reset the locking grid
     * Set all values to 0
     */
    private void resetGrid() {
        for (int i = 0; i < thread_nb; i++) {
            lockGrid[i] = new AtomicIntegerArray(thread_nb - i);
            for (int j = 0; j < thread_nb - i; j++) {
                lockGrid[i].set(j, 0);
            }
        }
    }


    public void run() {
        //Create threads
        SplitterThread[] threads = new SplitterThread[thread_nb];
        for (int i = 0; i < thread_nb; i++) {
            threads[i] = new SplitterThread();
        }

        //Start threads
        for (Thread thread : threads) thread.start();

        //Start renaming rounds
        for (int renaming_round = 0; renaming_round < n; renaming_round++) {
            //Reset locking grid
            resetGrid();
            //Create a countdown latch to keep track of thread that still in the process of renaming
            latch = new CountDownLatch(thread_nb);
            //Notify the threads they can start renaming
            for (SplitterThread thread : threads) {
                thread.waiting.set(false);
                synchronized (thread) {
                    thread.notify();
                }
            }
            try {
                latch.await(); // Wait for all threads to finish renaming
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //We have finished all rounds so we terminate the threads
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    class SplitterThread extends Thread {
        private int id = -1;
        private Random rand = new Random();
        private AtomicBoolean waiting = new AtomicBoolean();

        // Semaphore to keep the thread waiting between renaming rounds
        // Using wait and notify can fail if notify is being called before wait.

        public SplitterThread() {
            waiting.set(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    while (waiting.get()) {
                        synchronized (this) {
                            wait();
                        }
                    }

                    //Mean we have been notified(We can start renaming the thread)
                    int i = 0;
                    int j = 0;
                    // Until the thread found a valid id it continues
                    while (true) {
                        //If found a unused id flag it as locked
                        if (lockGrid[i].compareAndSet(j, 0, 1)) {
                            //Register id
                            id = grid[i][j];
                            idCount.incrementAndGet(id);
                            //Tell the latchCountDown that this thread is finished with renaming
                            latch.countDown();
                            break;
                        } else {
                            // If id is already used go randomly down or right
                            if (rand.nextFloat() < 0.5) {
                                i++;
                            } else {
                                j++;
                            }
                        }
                    }
                    waiting.set(true);
                } catch (InterruptedException e) {
                    return;
                }
            }

        }
    }
}
