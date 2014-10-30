package question1;

import java.util.*;

/**
 * Main app.
 * Created by tim on 14-10-30.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 5) {
            System.err.println("Missing arguments, expecting 5");
            return;
        }
        Engine engine = new Engine();
        engine.threadNb = Integer.parseInt(args[0]);
        engine.delayUpperBound = Integer.parseInt(args[1]);
        engine.operationAmount = Integer.parseInt(args[2]);
        engine.timeoutFactor = Integer.parseInt(args[3]);
        engine.eliminationArrayCapacity = Integer.parseInt(args[4]);

        engine.run();

    }

}

class Engine {
    public int threadNb, delayUpperBound, operationAmount, timeoutFactor, eliminationArrayCapacity;

    private EliminationStack<Integer> stack;

    public void run() {
        stack = new EliminationStack<Integer>(eliminationArrayCapacity, timeoutFactor);
        StackThread[] threads = new StackThread[threadNb];
        for (int i = 0; i < threadNb; i++) {
            threads[i] = new StackThread();
        }
        long start = System.currentTimeMillis();
        for (StackThread thread : threads) thread.start();
        for (StackThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ignore) {
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        int totalPush = 0, totalPop = 0;
        for (StackThread thread : threads) {
            totalPush += thread.pushCount;
            totalPop += thread.popCount;
        }

        System.out.printf("%d %d %d\n", totalPush, totalPop, stack.removeRemaining());

    }

    class StackThread extends Thread {
        private static final int QUEUE_CAPACITY = 10;
        private Random random = new Random();
        private Queue<Integer> lastPoped = new ArrayDeque<Integer>(QUEUE_CAPACITY);
        private int pushCount = 0;
        private int popCount = 0;

        @Override
        public void run() {
            for (int i = 0; i < operationAmount; i++) {
                if (random.nextFloat() >= 0.5) {
                    if (lastPoped.size() == 0 || random.nextFloat() >= 0.5) {
                        stack.push(random.nextInt());
                    } else {
                        stack.push(randomFromQueue());
                    }
                    pushCount++;
                } else {
                    try {
                        addToQueue(stack.pop());
                        popCount++;
                    } catch (Exception ignore) {
                        i--;
                    }
                }
                try {
                    Thread.sleep(random.nextInt(delayUpperBound));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private int randomFromQueue() {
            List<Integer> array = new ArrayList<Integer>(lastPoped);
            return array.get(random.nextInt(array.size()));
        }

        private void addToQueue(int value) {
            if (lastPoped.size() > QUEUE_CAPACITY) {
                lastPoped.remove();
            }
            lastPoped.add(value);
        }
    }
}
