import program.Point;

/**
 * Created by tim on 14-09-29.
 */
public class Test {

    public static void main(String[] args) {
        Point a = new Point(50000000, 0);
        Point b = new Point(50000000, 50000000);
        Point c = new Point(100000000, 100000000);
        System.out.println(Point.ccw(a, b, c));
    }
}
