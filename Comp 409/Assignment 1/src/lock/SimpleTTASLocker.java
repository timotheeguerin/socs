package lock;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Simple TTAS Locker, no delay
 * Created by tim on 14-09-29.
 */
public class SimpleTTASLocker extends Locker {

    private AtomicBoolean locked = new AtomicBoolean(false);


    /**
     * Locks the lock.
     */
    public int lock() {
        boolean acquired = false;
        while (!acquired) {
            /* First test the lock without invalidating
               any cache lines. */
            if (!locked.get()) {
                /* Attempt to lock the lock with an atomic CAS. */
                acquired = locked.compareAndSet(false, true);
            }
        }
        lock_granted++;
        return lock_granted;
    }

    /**
     * Locks the lock if available. This will not
     * block.
     */
    public boolean tryLock() {
        if (locked.get()) {
            return false;
        }

        return locked.compareAndSet(false, true);
    }

    /**
     * Unlocks the lock. This will not block.
     */
    public void unlock() {
        locked.set(false);
    }
}
