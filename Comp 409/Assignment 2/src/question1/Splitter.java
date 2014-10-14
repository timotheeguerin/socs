package question1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Question 1: Splitter.
 * Created by tim on 14-10-12.
 */
public class Splitter {

    private int thread_nb;
    private int n;
    private int[][] idGrid;
    private AtomicIntegerArray[] grid;
    private AtomicIntegerArray idCount;
    private CountDownLatch latch;

    public static void main(String[] args) {
        Splitter splitter = new Splitter(4, 1);
        splitter.run();
        System.out.println(splitter.idCount);
        int sum = 0;
        for (int i = 0; i < splitter.idCount.length(); i++) {
            sum += splitter.idCount.get(i);
        }

        for (int i = 0; i < splitter.thread_nb; i++) {

            for (int j = 0; j < splitter.thread_nb - i; j++) {
                System.out.print(splitter.idGrid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("----------------------------------------");
        for (int i = 0; i < splitter.thread_nb; i++) {
            for (int j = 0; j < splitter.thread_nb - i; j++) {
                System.out.print(splitter.idCount.get(splitter.idGrid[i][j]) + " ");
            }
            System.out.println();
        }
        System.out.println("----------------------------------------");

        System.out.println(sum);
    }

    public Splitter(int thread_nb, int n) {
        this.thread_nb = thread_nb;
        this.n = n;
        grid = new AtomicIntegerArray[thread_nb];
        idGrid = new int[thread_nb][thread_nb];

        int count = 1;
        for (int i = 0; i < thread_nb; i++) {

            for (int j = 0; j < thread_nb - i; j++) {
                idGrid[i][j] = count;
                count++;
            }
        }
        idCount = new AtomicIntegerArray(count);
    }

    private void resetGrid() {
        for (int i = 0; i < thread_nb; i++) {
            grid[i] = new AtomicIntegerArray(thread_nb - i);
            for (int j = 0; j < thread_nb - i; j++) {
                grid[i].set(j, 0);
            }
        }
    }


    public void run() {
        LinkedList<SplitterThread> threads = new LinkedList<SplitterThread>();
        for (int i = 0; i < thread_nb; i++) {
            threads.add(new SplitterThread());
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (int renaming_round = 0; renaming_round < n; renaming_round++) {
            resetGrid();
            latch = new CountDownLatch(thread_nb);
            for (SplitterThread thread : threads) {
                System.out.println("releasing");
                thread.semaphore.release();
//                synchronized (thread) {
//                    thread.notify();
//                }
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    class SplitterThread extends Thread {
        private int id = -1;
        private Random rand = new Random();

        private Semaphore semaphore = new Semaphore(1);

        public SplitterThread() {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
//                    synchronized (this) {
//                        wait();
//                    }

                    semaphore.acquire();
                    //Mean we have been notified(Rename the thread)
                    int i = 0;
                    int j = 0;
                    while (true) {
                        System.out.println(Arrays.toString(grid));
                        if (grid[i].compareAndSet(j, 0, 1)) {
                            id = idGrid[i][j];
                            idCount.incrementAndGet(id);
                            latch.countDown();
                            break;
                        } else {
                            if (rand.nextFloat() < 0.5) {
                                i++;
                            } else {
                                j++;
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }

        }
    }
}
