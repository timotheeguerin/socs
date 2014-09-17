package program;

/**
 * Created by tim on 14-09-17.
 * Abstract class for each of the programs
 */
public abstract class Program {
    public int q;
    public int n;
    public int p;

    public void setArgs(int q, int n, int p) {
        this.q = q;
        this.n = n;
        this.p = p;

    }

    public abstract void run();
}
