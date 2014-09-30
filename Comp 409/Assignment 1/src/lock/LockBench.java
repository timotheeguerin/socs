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

        int arg_q = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        int thread_nb = Integer.parseInt(args[2]);

        Locker locker;
        int[] qs;
        if (arg_q == -1) { //Do it for all q
            qs = new int[]{1, 2, 3, 4};
        } else {
            qs = new int[]{arg_q};
        }
        for (int q : qs) {
            try {
                locker = getLocker(q);
                locker.n = n;
                locker.thread_nb = thread_nb;

                long start_time = System.currentTimeMillis();
                int max = locker.run();
                long end_time = System.currentTimeMillis();
                System.out.println("Locker: " + q);
                System.out.println("\tTime: " + (end_time - start_time));
                System.out.println("\tMax: " + max);
                System.out.println("----------------------------------");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private static Locker getLocker(int q) throws NoSuchMethodException {
        Locker locker;
        switch (q) {
            case 1:
                locker = new SynchronizedLock();
                break;
            case 2:
                locker = new SimpleTTASLocker();
                break;
            case 3:
                locker = new DelayTTASLocker();
                break;
            case 4:
                locker = new CLHLocker();
                break;
            default:
                return null;
        }
        return locker;
    }
}
