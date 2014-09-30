package lock;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TTAS with exponential back off
 * Created by tim on 14-09-29.
 */
public class DelayTTASLocker extends Locker {
    private AtomicBoolean locked = new AtomicBoolean(false);
    private long WAIT_CONST = 52;

    /**
     * Locks the lock.
     */
    @Override
    public int lock() {
        boolean acquired = false;
        int fail = 0;

        Random rand = new Random();
        while (!acquired) {
            /* First test the lock without invalidating
               any cache lines. */
            if (!locked.get()) {
                /* Attempt to lock the lock with an atomic CAS. */
                acquired = locked.compareAndSet(false, true);
            } else {
                fail++;
                try {
                    Thread.sleep(WAIT_CONST * rand.nextInt((int) Math.pow(2, fail) - 1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return ++lock_granted;
    }


    /**
     * Unlocks the lock. This will not block.
     */
    @Override
    public void unlock() {
        locked.set(false);
    }
}
