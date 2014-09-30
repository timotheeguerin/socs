package lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CLH queue locker
 * Created by tim on 14-09-29.
 */
public class CLHLocker extends Locker {
    private final ThreadLocal<Node> pred;
    private final ThreadLocal<Node> node;
    private final AtomicReference<Node> tail = new AtomicReference<Node>(new Node());

    public CLHLocker() {
        this.node = new ThreadLocal<Node>() {
            protected Node initialValue() {
                return new Node();
            }
        };

        this.pred = new ThreadLocal<Node>() {
            protected Node initialValue() {
                return null;
            }
        };
    }

    @Override
    public int lock() {
        final Node node = this.node.get();
        node.locked = true;
        Node pred = this.tail.getAndSet(node);
        this.pred.set(pred);
        while (pred.locked) {
        }
        return ++lock_granted;
    }


    @Override
    public void unlock() {
        final Node node = this.node.get();
        node.locked = false;
        this.node.set(this.pred.get());
    }

    private class Node {
        public volatile boolean locked = false;
    }
}
