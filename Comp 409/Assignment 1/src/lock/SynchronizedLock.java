package lock;

/**
 * Use synchronized
 * Created by tim on 14-09-29.
 */
public class SynchronizedLock extends Locker {

    private boolean locked = false;

    public SynchronizedLock() {
    }

    @Override
    public synchronized int lock() {
        locked = true;
        lock_granted += 1;
        return lock_granted;
    }

    @Override
    public synchronized void unlock() {
        locked = false;
    }
}
