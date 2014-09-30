package lock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Parent class for each locker
 * Created by tim on 14-09-29.
 */
public abstract class Locker {
    public int thread_nb;
    public int n;

    protected Constructor thread_constructor;
    protected int lock_granted = 0;


    /**
     * Acquires the lock.
     */
    public abstract int lock();

    /**
     * Releases the lock.
     */
    public abstract void unlock();


    public void run() {
        Thread[] threads = new Thread[thread_nb];
        LockerThread[] runnables = new LockerThread[thread_nb];
        try {
            for (int i = 0; i != thread_nb; i++) {
                runnables[i] = new LockerThread();
                threads[i] = new Thread(runnables[i]);
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }
            for (LockerThread runnable : runnables) {
                System.out.println("Max: " + runnable.max_delay);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public class LockerThread implements Runnable {
        protected int max_delay = 0;


        @Override
        public void run() {
            int last_counter = 0;
            int new_counter;
            max_delay = 0;
            for (int i = 0; i < n; i++) {
                new_counter = lock();
                Thread.yield();
                unlock();
                Thread.yield();
                if (new_counter - last_counter > max_delay) {
                    max_delay = new_counter - last_counter;
                }
                last_counter = new_counter;
            }
        }
    }
}
