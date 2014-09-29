package graham;

/**
 * Created by tim on 14-09-17.
 * Abstract class for each of the programs
 */
public abstract class Program {
    public int n;
    public int thread_nb;

    public void setArgs(int n, int p) {
        this.n = n;
        this.thread_nb = p;

    }

    public void copyArgsFrom(Program other) {
        this.n = other.n;
        this.thread_nb = other.thread_nb;
    }

    public abstract Object run();
}
