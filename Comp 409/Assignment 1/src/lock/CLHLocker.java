package lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CLH queue locker
 * Created by tim on 14-09-29.
 */
public class CLHLocker extends Locker {
    private final ThreadLocal<Node> prev;
    private final ThreadLocal<Node> node;
    private final AtomicReference<Node> tail = new AtomicReference<Node>(new Node());

    public CLHLocker() {
        this.node = new ThreadLocal<Node>() {
            protected Node initialValue() {
                return new Node();
            }
        };

        this.prev = new ThreadLocal<Node>() {
            protected Node initialValue() {
                return null;
            }
        };
    }

    @Override
    public int lock() {
        final Node node = this.node.get();
        node.locked = true;
        node.prev = this.tail.getAndSet(node);
        while (node.prev.locked) {
        }
        return ++lock_granted;
    }


    @Override
    public void unlock() {
        final Node node = this.node.get();
        node.locked = false;
        this.node.set(node.prev);
    }

    private class Node {
        public volatile boolean locked = false;
        public Node prev;
    }
}
