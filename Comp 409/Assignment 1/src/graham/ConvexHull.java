package graham;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

/**
 * Build the convex hull
 * Created by tim on 14-09-26.
 */
public class ConvexHull extends Program {

    public Point[] points;

    @Override
    public Object run() {
        Sorter.sort(points, thread_nb); //Sort the remaining of the array
        int minimum_index = MinimumFinder.findMinimumIndex(points, thread_nb);
        Collections.rotate(Arrays.asList(points), -minimum_index);

        Stack<Point> hull = new Stack<Point>();
        hull.push(points[0]);
        hull.push(points[1]);
        int j = 2;
        int i;
        boolean running = true;
        while (running) {
            i = j % points.length;
            Point head = points[i];
            Point middle = hull.pop();
            Point tail = hull.peek();

            Point.ANGLE turn = Point.ccw(tail, middle, head);
            switch (turn) {
                case COUNTERCLOCKWISE: //Counter Clockwise
                    hull.push(middle);
                    hull.push(head);
                    break;
                case CLOCKWISE:
                    if (hull.size() < 2) {
                        hull.push(head);
                    } else {
                        j--;
                    }
                    break;
                case COLINEAR:
                    hull.push(head);
                    break;
            }

            j++;
            if (j > points.length) {
                running = false;
            }
        }
        hull.push(hull.get(0));
        new PaintingAndStroking(hull);
        return null;
    }

    class PaintingAndStroking
            extends Frame {
        private ArrayList<Point> hull;
        int FRAME_X = 1000;
        int FRAME_Y = 800;

        public PaintingAndStroking(Stack<Point> hull) {
            setTitle("PaintingAndStroking v1.0");
            setSize(FRAME_X, FRAME_Y);
            setVisible(true);
            this.hull = new ArrayList<Point>(hull);
        }

        public void paint(Graphics g) {

            for (Point point : points) {
                int x = getPositionX(point.x);
                int y = getPositionY(point.y);
                g.drawLine(x, y, x , y );
            }
            if (hull == null) {
                return;
            }
            g.setColor(Color.blue);
            for (int i = 1; i < hull.size(); i++) {

                Point first = hull.get(i - 1);
                Point last = hull.get(i);
                g.drawLine(getPositionX(first.x), getPositionY(first.y), getPositionX(last.x), getPositionY(last.y));
            }

        }

        private int getPositionX(int x) {
            return (int) (((double) x) * 0.75 / (double) Integer.MAX_VALUE * FRAME_X / 2) + FRAME_X / 2;
        }

        private int getPositionY(int y) {
            return (int) (((double) -y) * 0.75 / (double) Integer.MAX_VALUE * FRAME_Y / 2) + FRAME_Y / 2;
        }
    }
}

