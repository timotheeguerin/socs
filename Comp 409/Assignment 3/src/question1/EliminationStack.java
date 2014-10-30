package question1;

import java.util.concurrent.TimeoutException;

/**
 * question1.EliminationStack
 * Created by tim on 14-10-30.
 */

public class EliminationStack<T> extends LockFreeStack<T> {
    private EliminationArray<T> eliminationArray;
    private static ThreadLocal<RangePolicy> policy;
    private int exchangerCapacity, exchangerWaitDuration;

    /**
     * @param exchangerCapacity     Elimination array size
     * @param exchangerWaitDuration Elimination array wait duration
     */
    EliminationStack(final int exchangerCapacity, int exchangerWaitDuration) {
        super(0, 0);
        this.exchangerCapacity = exchangerCapacity;
        this.exchangerWaitDuration = exchangerWaitDuration;
        eliminationArray = new EliminationArray<T>(exchangerCapacity, exchangerWaitDuration);
        policy = new ThreadLocal<RangePolicy>() {
            protected synchronized RangePolicy initialValue() {
                return new RangePolicy(exchangerCapacity);
            }
        };
    }

    public String getStackInfo() {
        return exchangerCapacity + "," + exchangerWaitDuration;
    }

    public void push(T value) {
        RangePolicy rangePolicy = policy.get();
        Node<T> node = new Node<T>(value);

        while (true) {
            if (this.tryPush(node))
                return;
            else {
                try {
                    T otherValue = eliminationArray.visit(value, rangePolicy.getRange());
                    if (otherValue == null) {
                        rangePolicy.recordEliminationSuccess();
                        return;
                    }
                } catch (TimeoutException e) {
                    rangePolicy.recordEliminationTimeout();
                }
            }
        }
    }

    public T pop() throws Exception {
        RangePolicy rangePolicy = policy.get();

        while (true) {
            Node<T> returnNode = tryPop();
            if (returnNode != null)
                return returnNode.value;
            else {
                try {
                    T otherValue = eliminationArray.visit(null, rangePolicy.getRange());
                    if (otherValue != null) {
                        rangePolicy.recordEliminationSuccess();
                        return otherValue;
                    }
                } catch (TimeoutException e) {
                    rangePolicy.recordEliminationTimeout();
                }
            }
        }
    }

    public int removeRemaining() {
        int count = 0;
        while (true) {
            try {
                pop();
                count++;
            } catch (Exception e) {
                return count;
            }
        }
    }
}