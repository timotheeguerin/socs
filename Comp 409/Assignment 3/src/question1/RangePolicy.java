package question1;

/**
 * question1.RangePolicy
 * Created by tim on 14-10-30.
 */
public class RangePolicy {
    int maxRange;
    int currentRange = 1;

    RangePolicy(int maxRange) {
        this.maxRange = maxRange;
    }

    public void recordEliminationSuccess() {
        if (currentRange < maxRange)
            currentRange++;
    }

    public void recordEliminationTimeout() {
        if (currentRange > 1)
            currentRange--;
    }

    public int getRange() {
        return currentRange;
    }
}
