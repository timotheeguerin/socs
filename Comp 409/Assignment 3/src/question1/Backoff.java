package question1;

import java.util.Random;

/**
 * question1.Backoff
 * Created by tim on 14-10-30.
 */
public class Backoff {
    final int minDelay, maxDelay;
    final Random random;
    int limit;

    Backoff(int min, int max) {
        minDelay = min;
        maxDelay = max;
        limit = minDelay;
        random = new Random();
    }

    public void backoff() throws InterruptedException {
        int delay = random.nextInt(limit);
        limit = Math.min(maxDelay, 2 * limit);
        Thread.sleep(delay);
    }
}
