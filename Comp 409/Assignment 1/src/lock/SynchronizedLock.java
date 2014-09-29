package lock;

/**
 * Use synchronized
 * Created by tim on 14-09-29.
 */
public class SynchronizedLock extends Locker {

    public SynchronizedLock() throws NoSuchMethodException {
        thread_class = SynchronizedLockThread.class;
        thread_constructor = SynchronizedLockThread.class.getDeclaredConstructor(SynchronizedLock.class);
    }

    public synchronized int requestLock() {
        lock_granted += 1;
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return lock_granted;
    }


    public class SynchronizedLockThread extends LockerThread {

        public SynchronizedLockThread() {

        }

        @Override
        public void run() {
            int last_counter = 0;
            int new_counter;
            max_delay = 0;
            for (int i = 0; i < n; i++) {
                new_counter = requestLock();
                if (new_counter - last_counter > max_delay) {
                    max_delay = new_counter - last_counter;
                }
                last_counter = new_counter;
            }
        }
    }
}
