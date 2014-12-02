import java.util.List;
import java.util.Random;

import javafx.util.Pair;

/**
 * Process
 * Created by tim on 14-12-01.
 */
class Process extends Thread {
    protected int id;
    protected int lock = 0;
    protected Random random = new Random();

    private static final class Lock {
    }


    public Object waitFrom(Process sender) {
//        System.out.println(this + "is now waiting....");
        while (true) {
//            System.out.println("Waiting from " + this + " for " + sender.getClass() + " " + sender.id);
            Object message = MessagePool.getMessage(sender, this);
//            MessagePool.print();
//            System.out.println("Message: " + message);
            if (message != null) {
                synchronized (this) {
                    notifyAll();
                }
                return message;
            }
//            try {
//                synchronized (this) {
//                    down();
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public Pair<Process, Object> waitFrom(List<?> senders) {
//        System.out.println(this + " is now waiting....");
        while (true) {
//            System.out.println(this + " is now waking up....");
            for (Object sender : senders) {

                Object message = MessagePool.getMessage((Process) sender, this);
                if (this instanceof Main.Elevator) {
//                    MessagePool.print();
//                    System.out.println("Waiting form " + this + " for " + sender.getClass() + " " + ((Process) sender).id);
//                    System.out.println("Message: " + message);
                }
                if (message != null) {
//                    System.out.println("\tStop waiting from: " + this + ",for: " + sender);
//                    MessagePool.print();
//                    synchronized (sender) {
//                        ((Process) sender).up();
//                    }
                    return new Pair(sender, message);
                }
            }

//            try {
//                synchronized (this) {
//                    wait();
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void sendLock(Process dest, Object o) {
        MessagePool.sendMessage(this, dest, o);
        synchronized (this) {
            notifyAll();
        }
        while (true) {
//                MessagePool.print();
//                System.out.println(dest + ", msg: " + o + ", trying: " + MessagePool.hasMessage(this, dest));
            if (!MessagePool.hasMessage(this, dest)) {
//                System.out.println("\tStop locking: " + this + ", dest: " + dest);
                return;
            }
//            try {
//                synchronized (this) {
//                    down();
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public String toString() {
        return this.getClass() + " " + id;
    }

    public void printAndSleep(String message) {
        System.out.println(message);
        int sleep = random.nextInt(250) + 50;
        try {
            sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}