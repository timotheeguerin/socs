package lock;

/**
 * Use synchronized
 * Created by tim on 14-09-29.
 */
public class SynchronizedLock extends Locker {

    protected boolean locked = false;

    public SynchronizedLock() {
    }

    /**
     * Request a lock
     * The synchronized already take care of locking the object
     * so we are certain locked cannot be modified by another thread while we are in this function
     *
     * @return number of lock requested.
     */
    @Override
    public synchronized int lock() {
        locked = true;
        lock_granted += 1;
        return lock_granted;
    }

    /**
     * Unlock. Synchronized take care of locking the object
     */
    @Override
    public synchronized void unlock() {
        locked = false;
    }
}
