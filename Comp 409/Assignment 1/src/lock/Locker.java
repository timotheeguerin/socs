package lock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Parent class for each locker
 * Created by tim on 14-09-29.
 */
public class Locker {
    public int thread_nb;
    public int n;


    protected Class thread_class;
    protected Constructor thread_constructor;
    protected int lock_granted = 0;

    public void run() {
        Thread[] threads = new Thread[thread_nb];
        LockerThread[] runnables = new LockerThread[thread_nb];
        try {
            for (int i = 0; i != thread_nb; i++) {
                runnables[i] = (LockerThread) thread_constructor.newInstance(this);
                threads[i] = new Thread(runnables[i]);
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }
            for (LockerThread runnable : runnables) {
                System.out.println("Max: " + runnable.max_delay);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public abstract class LockerThread implements Runnable {
        protected int max_delay = 0;
    }
}
