package question3;

import java.awt.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Snake
 * Created by tim on 14-10-12.
 */
public class Snake {

    private AtomicIntegerArray[] grid;

    private long snake_size;
    private int grid_size;
    private long waiting_time;
    private int snake_nb;
    private SnakeThread[] snakes;

    public static void main(String[] args) {
        Snake snake = new Snake(1, 20, 140, 50);
        snake.run();
    }

    public Snake(int snake_nb, int snake_size, int grid_size, int waiting_time) {
        this.snake_nb = snake_nb;
        this.grid_size = grid_size;
        this.waiting_time = waiting_time;
        this.snake_size = snake_size;
        grid = new AtomicIntegerArray[grid_size];
        for (int i = 0; i < grid_size; i++) {
            grid[i] = new AtomicIntegerArray(grid_size);
        }
    }

    public void run() {
        snakes = new SnakeThread[snake_nb];
        Thread[] threads = new Thread[snake_nb];
        for (int i = 0; i < snake_nb; i++) {
            snakes[i] = new SnakeThread(i);
            threads[i] = new Thread(snakes[i]);
        }

        for (Thread thread : threads) {
            thread.start();
        }
        long start_time = System.currentTimeMillis();
        PaintingAndStroking paint = new PaintingAndStroking();
        while (System.currentTimeMillis() - start_time <= 1000 * 60) {
            boolean all_locked = true;

            for (SnakeThread snake : snakes) {
                if (!snake.locked) {
                    all_locked = false;
                    break;
                }
            }
            if (all_locked) {
                System.out.println("All locked");
                break;
            }
            paint.repaint();
            try {
                Thread.sleep(waiting_time / 4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    class PaintingAndStroking extends Frame {
        int FRAME_X = 1000;
        int FRAME_Y = 750;
        int OFFSET_X = 10;
        int OFFSET_Y = 30;
        int CELL_SIZE = 5;
        private Color[] colors;

        public PaintingAndStroking() {
            setTitle("PaintingAndStroking v1.0");
            setSize(FRAME_X, FRAME_Y);
            setVisible(true);

            Random rand = new Random();
            colors = new Color[snake_nb];
            for (int i = 0; i < snakes.length; i++) {

                colors[i] = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
            }
        }

        public void paint(Graphics g) {
            if(colors == null) {
                return;
            }
            g.drawRect(OFFSET_X, OFFSET_Y, CELL_SIZE * grid_size, CELL_SIZE * grid_size);
            int snake_id = 0;
            for (SnakeThread snake : snakes) {
                int count = 0;
                g.setColor(colors[snake_id]);
                for (Point p : snake.queue) {
                    if (count >= snake_size - 1) {
                        if (snake.locked) {
                            g.setColor(new Color(200, 50, 50));
                        } else {
                            g.setColor(new Color(50, 50, 200));
                        }
                    }
                    g.fillRect(p.x * CELL_SIZE + OFFSET_X, p.y * CELL_SIZE + OFFSET_Y, CELL_SIZE, CELL_SIZE);
                    count++;
                }
                snake_id++;
            }
        }
    }

    class SnakeThread implements Runnable {

        private boolean locked = false;
        private Queue<Point> queue = new ArrayDeque<Point>();

        public SnakeThread(int row) {
            for (int i = 0; i < snake_size; i++) {
                grid[row].set(i, 1);
                queue.add(new Point(row, i));
            }
        }

        @Override
        public void run() {
            while (true) {
                ArrayList<Point> list = getNextPositionList();
                boolean found = false;
                for (Point next : list) {
                    if (grid[next.x].compareAndSet(next.y, 0, 1)) {
                        queue.add(next);
                        Point lastPoint = queue.remove();
                        grid[lastPoint.x].set(lastPoint.y, 0);
                        found = true;
                        locked = false;
                        break;
                    }
                }

                if (!found) {
                    locked = true;
                }
                try {
                    Thread.sleep(waiting_time);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

        private ArrayList<Point> getNextPositionList() {
            ArrayList<Point> list = new ArrayList<Point>();
            ArrayList<Point> snakePos = new ArrayList<Point>(queue);
            Point head = snakePos.get(snakePos.size() - 1);
            Point beforeHead = snakePos.get(snakePos.size() - 2);
            list.add(new Point(head.x, head.y + 1));
            list.add(new Point(head.x, head.y - 1));
            list.add(new Point(head.x + 1, head.y));
            list.add(new Point(head.x - 1, head.y));

            ArrayList<Point> results = new ArrayList<Point>();
            for (Point pos : list) {
                if (!pos.equals(beforeHead)) {
                    Point point = new Point();
                    point.x = (((pos.x % grid_size) + grid_size) % grid_size); // Modulo for negative number
                    point.y = (((pos.y % grid_size) + grid_size) % grid_size);
                    results.add(point);
                }
            }
            Collections.shuffle(results);
            return results;
        }
    }
}
