import program.CoordinatesGenerator;
import program.Program;

/**
 * Created by tim on 14-09-17.
 */
public class Graham {
    private int q;
    private int n;
    private int p;

    public Graham() {

    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Wrong number of arguments, expected 3(q, n,p)");
            return;
        }
        Graham engine = new Graham();
        engine.q = Integer.parseInt(args[0]);
        engine.n = Integer.parseInt(args[1]);
        engine.p = Integer.parseInt(args[2]);
        engine.p = 8;
        for(int i = 0; i < 10; i ++) {
            long start_time = System.currentTimeMillis();
            engine.run();
            long end_time = System.currentTimeMillis();
            System.out.printf("Running time: %d\n", (end_time - start_time));
        }
    }

    public void run() {
        Program program;
        switch (q) {
            case 1:
                program = new CoordinatesGenerator(q,n,p);
                break;
            default:
                System.err.println("Wrong value for q, should be 1,2,3 or 4!");
                return;
        }
        program.run();
    }


}
