import graham.*;

/**
 * Graham
 * Created by tim on 14-09-17.
 */
public class Graham {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Wrong number of arguments, expected 3(q, n,thread_nb)");
            return;
        }
        int q = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        int p = Integer.parseInt(args[2]);

        int[] thread_nbs;
        int sample_size;

        //Added a 4th optional argument that correspond to a sample size for benchmarking
        if (args.length > 3) {
            thread_nbs = new int[]{1, 2, 4, 8, 16, 32};
            sample_size = Integer.parseInt(args[3]);
        } else {
            thread_nbs = new int[]{Integer.parseInt(args[2])};
            sample_size = 1;
        }
        //For each thread number(Only p if no 4th argument)
        for (int thread_nb : thread_nbs) {
            long start_time, end_time;
            long sum = 0;
            int total = sample_size <= 1 ? 1 : sample_size + 10;
            //Try each program total times(Once only if no 4th argument)
            for (int i = 0; i < total; i++) {
                Program program = init(q, n, thread_nb);
                start_time = System.currentTimeMillis();
                program.run();
                end_time = System.currentTimeMillis();
                if (sample_size <= 1 || (i > 5 && i <= sample_size + 5)) {
                    sum += end_time - start_time;
                }
            }
            System.out.printf("Thread %d: Running time: %f\n", thread_nb, ((double) sum / sample_size));
        }
    }

    public static Program init(int q, int n, int p) {
        Program program;
        switch (q) {
            case 1:
                program = new CoordinatesGenerator();
                break;
            case 2:
                MinimumFinder finder = new MinimumFinder();
                finder.points = CoordinatesGenerator.generateNonUnique(n, p);
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
                break;
            default:
                System.err.println("Wrong value for q, should be 1,2,3 or 4!");
                return null;
        }
        program.n = n;
        program.thread_nb = p;
        return program;
    }

}
