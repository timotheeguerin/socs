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
        if (args.length < 4) {
            System.out.println("Error missing arguments: expecting 4.");
            return;
        }
        int snake_nb = Integer.parseInt(args[0]);
        int snake_len = Integer.parseInt(args[1]);
        int grid_size = Integer.parseInt(args[2]);
        int t = Integer.parseInt(args[3]);
        Snake snake = new Snake(snake_nb, snake_len, grid_size, t);
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
        for (int i = 0; i < snake_nb; i++) {
            snakes[i] = new SnakeThread(i);
        }

        for (SnakeThread thread : snakes) {
            thread.start();
        }
        long start_time = System.currentTimeMillis();
        PaintingAndStroking paint = new PaintingAndStroking();
        //Run for 1min
        while (System.currentTimeMillis() - start_time <= 1000 * 60) {
            boolean all_locked = true;

            for (SnakeThread snake : snakes) {
                if (!snake.locked) {
                    all_locked = false;
                    break;
                }
            }

            // If all snakes are locked then we stop
            if (all_locked) {
                System.out.println("All locked");
                break;
            }
            //Display the snake
            paint.repaint();
            try {
                Thread.sleep(waiting_time / 4); //Sleep for t/4
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (SnakeThread thread : snakes) {
            thread.interrupt();
        }
        int i = 1;
        for (SnakeThread snake : snakes) {
            System.out.printf("Snake %d moved: %d\n", i, snake.moves);
            i++;
        }
    }


    class SnakeThread extends Thread {

        private int moves = 0;
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
                //Go through each potential positions
                for (Point next : list) {
                    //If we can go to this position then move
                    if (grid[next.x].compareAndSet(next.y, 0, 1)) {
                        queue.add(next);
                        //Remove the queue of the snake and update the grid correspondingly
                        Point lastPoint = queue.remove();
                        grid[lastPoint.x].set(lastPoint.y, 0);
                        found = true;
                        locked = false; //We are not blocked anymore(if we were before)
                        moves++;
                        break;
                    }
                }
                // If the snake has not found a valid position then its marked as blocked
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

        /**
         * Get the list of next potential positions for the snake
         * It return 3 positions(NORTH, EAST, SOUTH, WEST) excluding the position used by the snake body before head)
         *
         * @return list of points with the index of the positions
         */
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

    /**
     * Display class
     */
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
            if (colors == null) {
                return;
            }
            g.drawRect(OFFSET_X, OFFSET_Y, CELL_SIZE * grid_size, CELL_SIZE * grid_size);
            int snake_id = 0;
            for (SnakeThread snake : snakes) {
                int count = 0;
                g.setColor(colors[snake_id]);
                try {
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
                } catch (ConcurrentModificationException ignored) {

                }
            }
        }
    }
}
