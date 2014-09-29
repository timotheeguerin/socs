import graham.Point;

import java.util.Arrays;
import java.util.Collections;

/**
 *
 * Created by tim on 14-09-29.
 */
public class Test {

    public static void main(String[] args) {
        Point a = new Point(0, 0);
        Point b = new Point(1, 0);
        Point c = new Point(2, 0);
        Point d = new Point(4, 0);
        Point e = new Point(5, 0);
        Point f = new Point(6, 0);
        Point[] points = {a, b, c, d, e, f};

        for(Point p : points)
        {
            System.out.print(p);
        }
        System.out.println();
        Collections.rotate(Arrays.asList(points), -3);

        for(Point p : points)
        {
            System.out.print(p);
        }
        System.out.println();
    }
}
