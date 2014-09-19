package program;

import java.util.Objects;

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

    public void copyArgsFrom(Program other) {
        this.q = other.q;
        this.n = other.n;
        this.p = other.p;
    }
    public abstract void init();
    public abstract Object run();
}
