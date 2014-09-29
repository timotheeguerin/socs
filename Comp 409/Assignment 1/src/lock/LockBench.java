package lock;

/**
 * Lock bench
 * Created by tim on 14-09-29.
 */
public class LockBench {

    public static void main(String[] args) {
        if (args.length < 3) {
            return;
        }

        int q = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        int thread_nb = Integer.parseInt(args[2]);

        Locker locker = null;
        try {
            locker = getLocker(q);
            locker.n = n;
            locker.thread_nb = thread_nb;

            long start_time = System.currentTimeMillis();
            locker.run();
            long end_time = System.currentTimeMillis();
            System.out.println("Time: " + (end_time - start_time));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static Locker getLocker(int q) throws NoSuchMethodException {
        Locker locker;
        switch (q) {
            case 1:
                locker = new SynchronizedLock();
                break;
            case 2:
                locker = null;
                break;
            case 3:
                locker = null;
                break;
            case 4:
                locker = null;
                break;
            default:
                return null;
        }
        return locker;
    }
}
