import program.*;

/**
 * Graham
 * Created by tim on 14-09-17.
 */
public class Graham {
    private int q;
    private int n;
    private int p;
    private Program program;

    public Graham() {

    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Wrong number of arguments, expected 3(q, n,thread_nb)");
            return;
        }
        Graham engine = new Graham();
        engine.q = Integer.parseInt(args[0]);
        engine.n = Integer.parseInt(args[1]);
        engine.p = Integer.parseInt(args[2]);

        int[] thread_nbs;
        int sample_size;
        engine.init();
        if (args.length > 3) {
            thread_nbs = new int[]{1, 2, 4, 8, 16, 32, 64};
            sample_size = Integer.parseInt(args[3]);

        } else {
            thread_nbs = new int[]{Integer.parseInt(args[2])};
            sample_size = 1;
        }
        for (int thread_nb : thread_nbs) {
            engine.p = thread_nb;
            long start_time, end_time;
            long sum = 0;
            for (int i = 0; i < sample_size; i++) {
                start_time = System.currentTimeMillis();
                engine.run();
                end_time = System.currentTimeMillis();
                if (sample_size <= 1 || i > 0) {
                    sum += end_time - start_time;
                }
            }
            System.out.printf("Thread %d: Running time: %f\n", thread_nb, ((double) sum / sample_size));
        }
    }

    public void init() {
        switch (q) {
            case 1:
                program = new CoordinatesGenerator();
                break;
            case 2:
                MinimumFinder finder = new MinimumFinder();
                finder.points = CoordinatesGenerator.generate(n, p);
                program = finder;
                break;
            case 3:
                Sorter sorter = new Sorter();
                sorter.points = CoordinatesGenerator.generate(n, p);
                program = sorter;
                break;

            case 4:
                ConvexHull hull = new ConvexHull();
                hull.points = CoordinatesGenerator.generate(n, p);
                program = hull;
            default:
                System.err.println("Wrong value for q, should be 1,2,3 or 4!");
                return;
        }
        program.n = n;
        program.thread_nb = p;
    }

    public void run() {
        program.run();
    }


}
