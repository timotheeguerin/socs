package question1;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Elimination array.
 * Created by tim on 14-10-30.
 */
class EliminationArray<T> {
    private int duration = 10;
    private Random random;
    private LockFreeExchanger<T>[] exchanger;

    @SuppressWarnings("unchecked")
    EliminationArray(int capacity, int duration) {
        this.duration = duration;
        exchanger = (LockFreeExchanger<T>[]) new LockFreeExchanger[capacity];
        for (int i = 0; i < capacity; i++) {
            exchanger[i] = new LockFreeExchanger<T>();
        }
        random = new Random();
    }

    public T visit(T value, int range) throws TimeoutException {
        int slot = random.nextInt(range);
        return (exchanger[slot].exchange(value, duration, TimeUnit.MILLISECONDS));
    }
}
//Thread 1
// CAS(flag, <empty, null>, <"waiting", x>
// Works

//Thread 2
// CAS(flag, <empty, null>, <"waiting", null>
// fail
// get flag value
// CAS(flag, <"waiting", x>, <"busy", null>)

//Thread 1
//get flag value.
// set flag value to empty