package question1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Question 1: Splitter.
 * Created by tim on 14-10-12.
 */
public class Splitter {

    private int thread_nb;
    private int n;
    private int[][] grid;
    private AtomicIntegerArray idCount;

    public static void main(String[] args) {
        Splitter splitter = new Splitter(4, 10);
        splitter.run();
        System.out.println(splitter.idCount);
        int sum = 0;
        for (int i = 0; i < splitter.idCount.length(); i++) {
            sum += splitter.idCount.get(i);
        }

        for (int i = 0; i < splitter.thread_nb; i++) {
            for (int j = 0; j < splitter.thread_nb - i; j++) {
                System.out.print(splitter.grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("----------------------------------------");
        for (int i = 0; i < splitter.thread_nb; i++) {
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


    public void run() {
        LinkedList<SplitterThread> threads = new LinkedList<SplitterThread>();
        for (int i = 0; i < thread_nb; i++) {
            threads.add(new SplitterThread());
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (int renaming_round = 0; renaming_round < n; renaming_round++) {
            LinkedList<SplitterThread> currentThreads = new LinkedList<SplitterThread>(threads);
            rename(0, 0, currentThreads);
        }
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    public void rename(int i, int j, LinkedList<SplitterThread> currentThreads) {
        if (currentThreads.size() == 0) {
            return;
        }
        Random rand = new Random();
        SplitterThread stayingThread = currentThreads.get(rand.nextInt(currentThreads.size()));
        currentThreads.remove(stayingThread);
        stayingThread.id = grid[i][j];
        synchronized (stayingThread) {
            stayingThread.notify();
        }
        LinkedList<SplitterThread> down = new LinkedList<SplitterThread>();
        LinkedList<SplitterThread> right = new LinkedList<SplitterThread>();
        for (SplitterThread thread : currentThreads) {
            if (rand.nextFloat() < 0.5) {
                down.add(thread);
            } else {
                right.add(thread);
            }
        }

        rename(i + 1, j, down);
        rename(i, j + 1, right);
    }

    class SplitterThread extends Thread {
        private int id = -1;

        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (this) {
                        wait();
                    }
                    //Mean we have been notified(id changed)
                    idCount.incrementAndGet(id);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
