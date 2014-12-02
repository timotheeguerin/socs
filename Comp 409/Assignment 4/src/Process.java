import java.util.List;
import java.util.Random;

import javafx.util.Pair;

/**
 * Process
 * Created by tim on 14-12-01.
 */
abstract class Process extends Thread {
    protected int id;
    protected Random random = new Random();

    /**
     * Equivalent of sender?x
     * Try to read a message send by sender to this.
     * Until a message is sent we lock and try again
     */
    public Object waitFrom(Process sender) throws InterruptedException {
        while (true) {
            Object message = MessagePool.getMessage(sender, this);
            if (message != null) {
                synchronized (this) {
                    notifyAll();
                }
                return message;
            }
            sleep(1);
        }
    }

    /**
     * Implementation of sender[0]?x | ... | sender[n-1]?x
     * Going to try to read a message from the given list of sender to this.
     * Until a message from any of the senders is found we lock and try again
     */
    public Pair<Process, Object> waitFrom(List<?> senders) throws InterruptedException {
        while (true) {
            for (Object sender : senders) {

                Object message = MessagePool.getMessage((Process) sender, this);
                if (message != null) {
                    return new Pair<>((Process) sender, message);
                }
            }
            sleep(1);
        }
    }

    /**
     * Equivalent of dest!msg
     * Going to send the message to the destination given and
     * is going to wait for the destination to read the message before continuing
     *
     * @param dest Destination process for the message
     * @param msg  Message to send
     */
    public void sendLock(Process dest, Object msg) throws InterruptedException {
        MessagePool.sendMessage(this, dest, msg);
        synchronized (this) {
            notifyAll();
        }
        while (true) {
            if (!MessagePool.hasMessage(this, dest)) {
                return;
            }
            sleep(1);
        }
    }


    /**
     * Helper method
     */
    public String toString() {
        return this.getClass() + " " + id;
    }

    public void run() {
        while (true) {
            try {
                loop();
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public abstract void loop() throws InterruptedException;

    /**
     * Function to call on state change.
     * Print the message and wait between 50 and 300 ms
     *
     * @param message Message to print
     */
    public void printAndSleep(String message) throws InterruptedException {
        System.out.println(message);
        int sleep = random.nextInt(250) + 50;
        sleep(sleep);
    }
}