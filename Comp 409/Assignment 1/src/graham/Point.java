package graham;


import java.util.Random;

/**
 * Created by tim on 14-09-17.
 * Point container class
 */
public final class Point implements Comparable<Point> {
    public int x;
    public int y;

    public enum ANGLE {
        CLOCKWISE, COUNTERCLOCKWISE, COLLINEAR
    }

    @SuppressWarnings("unused")
    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point random() {
        Random r = new Random();
        int x = r.nextInt();
        int y = r.nextInt();
        return new Point(x, y);
    }

    public double r() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public double angle() {
        double val = Math.atan((double) y / (double) Math.abs(x));
        if (x < 0) {
            val = Math.PI - val;
        }
        return val >= 0 ? val : 2 * Math.PI + val;
    }

    @Override
    public int hashCode() {
        return (Long.toString(x) + "," + Long.toString(y)).hashCode();
    }

    @Override
    public int compareTo(Point other) {
        if (this.equals(other)) {
            return 0;
        } else {
            if (this.angle() > other.angle()) {
                return 1;
            } else if (this.angle() < other.angle()) {
                return -1;
            } else {
                return Double.compare(this.r(), other.r());
            }
        }
    }

    public int compareByY(Point other) {

        if (this.y < other.y) {
            return -1;
        } else if (this.y > other.y) {
            return 1;
        } else {
            return Long.compare(other.x, this.x);
        }
    }

    /**
     * Test the angle of 3 points.
     *
     * @param a First point
     * @param b Middle point
     * @param c Last point
     * @return > 0 if counterclockwise
     * = 0 if linear
     * < 0 if clockwise
     */
    public static ANGLE ccw(Point a, Point b, Point c) {
        double area2 = ((double) b.x - (double) a.x) * ((double) c.y - (double) a.y) - ((double) b.y - (double) a.y) * ((double) c.x - (double) a.x);
        if (area2 < 0) return ANGLE.CLOCKWISE;
        else if (area2 > 0) return ANGLE.COUNTERCLOCKWISE;
        else return ANGLE.COLLINEAR;
    }


    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}
