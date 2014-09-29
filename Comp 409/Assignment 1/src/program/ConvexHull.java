package program;

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

//        points = new Point[]{new Point(500000000, 0), new Point(500000000, 500000000), new Point(1000000000, 1000000000), new Point(2000000000, 2000000000),
//                new Point(-1200000000, 2000000000), new Point(-1200000000, -1200000000), new Point(1200000000, -1200000000)};
        points = new Point[]{
                new Point(88190166, -1475992943),
                new Point(1990026033, 1030372661),
                new Point(1445248167, 909949763),
                new Point(1908510265, 1369817334),
                new Point(-441548773, 643454341),
                new Point(-1085659585, 164461608),
                new Point(-1170254906, -1045927675),
                new Point(-1085659585, -1475992943),
                new Point(776916738, -921796446),
                new Point(1133044524, -55393059),
                new Point(1885952741, -2112155)
        };
        points = new Point[]{new Point(0, -1500000000), new Point(500000000, -500000000), new Point(1000000000, 1000000000),
                new Point(2000000000, 2000000000), new Point(-1500000000, 1500000000)};


        Sorter.sort(points, thread_nb); //Sort the remaining of the array
        int minimum_index = MinimumFinder.findMinimumIndex(points, thread_nb);
        Sorter.swap(points, 0, minimum_index); //Put the element with the smallest y value in the first place
        Collections.rotate(Arrays.asList(points), -minimum_index);

        Stack<Point> hull = new Stack<Point>();
        hull.push(points[0]);
        hull.push(points[1]);
        int j = 2;
        int i;
        boolean running = true;
        while (running) {
            i = j % points.length;
            System.out.println(hull + "  ---- " + points[i]);
            Point head = points[i];
            Point middle = hull.pop();
            Point tail = hull.peek();

            Point.ANGLE turn = Point.ccw(tail, middle, head);
            switch (turn) {
                case COUNTERCLOCKWISE: //Counter Clockwise
                    System.out.println("CounterClockwise");
                    hull.push(middle);
                    hull.push(head);
                    break;
                case CLOCKWISE:
                    System.out.println("Clockwise");
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

    public class ConvexHullThread implements Runnable {
        private int start_index, end_index;

        public ConvexHullThread(int start_index, int end_index) {
            this.start_index = start_index;
            this.end_index = end_index;
        }

        public void run() {

            for (int i = start_index; i != end_index; i++) {

            }
        }

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
//                System.out.printf("(%d, %d)\n", x, y);
//                System.out.printf("Angle: %f\n", point.angle());
                System.out.printf("new Point(%d, %d),\n", point.x, point.y);
                g.drawLine(x, y, x + 5, y + 1);
            }
            if (hull == null) {
                return;
            }
//            for (int i = 1; i < points.length; i++) {
//                g.setColor(new Color((int)((float) i * 255 / points.length), 10, 10));
//                Point first = points[i - 1];
//                Point last = points[i];
//                System.out.printf("(%d, %d)\n", first.x, first.y);
//                g.drawLine(getPositionX(first.x), getPositionY(first.y), getPositionX(last.x), getPositionY(last.y));
//            }
            g.setColor(Color.blue);
            System.out.println("Hull: " + hull.size());
            for (int i = 1; i < hull.size(); i++) {

                Point first = hull.get(i - 1);
                Point last = hull.get(i);
                g.drawLine(getPositionX(first.x), getPositionY(first.y), getPositionX(last.x), getPositionY(last.y));
            }

        }

        private int getPositionX(int x) {
            return (int) (((float) x) / Integer.MAX_VALUE * FRAME_X / 2) + FRAME_X / 2;
        }

        private int getPositionY(int y) {
            return (int) (((float) -y) / Integer.MAX_VALUE * FRAME_Y / 2) + FRAME_Y / 2;
        }
    }
}

