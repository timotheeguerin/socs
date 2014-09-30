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

    /**
     * Request a lock
     *
     * @return number of lock requested.
     */
    @Override
    public int lock() {
        final Node node = this.node.get();      //Get thread node
        node.locked = true;                     //Lock it
        Node prev = this.tail.getAndSet(node);  //Get previous node
        this.prev.set(prev);                    //Remember the previous node for this thread
        while (prev.locked) {                   //Wait for the previous node to unlock before granting the lock
        }
        return ++lock_granted;
    }


    @Override
    public void unlock() {
        final Node node = this.node.get();      //Get thread node
        node.locked = false;                    //Unlock it
        this.node.set(this.prev.get());         //Set thread not to be the previous node
    }

    private class Node {
        public volatile boolean locked = false;
    }
}
