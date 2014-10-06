import com.sun.deploy.util.StringUtils;

import java.util.List;

/**
 * Cryptography methods
 * Created by tim on 14-10-03.
 */
public class Cryptography
{
    private static final int GRID_SIZE = 4;
    private static final int[] BLOCK_KEYS = { 4, 3, 2, 1 };

    public static String gridTransposition(List<String> messages)
    {
        String message = StringUtils.join(messages, "").replaceAll(" ", "").toLowerCase();
        char[][] grid = new char[(int) Math.ceil((float) message.length() / GRID_SIZE)][GRID_SIZE];
        for (int i = 0; i < message.length(); i++)
        {
            grid[((int) Math.floor((float) i / GRID_SIZE))][i % GRID_SIZE] = message.charAt(i);
        }
        printGrid(grid);

        grid = transpose(grid);
        printGrid(grid);
        for (int i = 0; i < grid.length; i++)
        {
            for (int j = 0; j < grid[0].length; j++)
            {
                int shift = BLOCK_KEYS[(i * grid[0].length + j) % BLOCK_KEYS.length];
                grid[i][j] += shift;
                if (grid[i][j] > 'z')
                    grid[i][j] -= 26;
            }
        }
        printGrid(grid);
        String output = "";
        for (char[] row : grid)
        {
            output += new String(row);
        }
        System.out.println(output);
        return output;
    }

    public static double hash(List<String> messages)
    {
        double sum = 0;
        for (String message : messages)
        {
            int messageSum = 0;
            for (int i = 0; i < message.length(); i++)
            {
                messageSum += message.charAt(i);
            }
            sum += Math.pow(messageSum, message.length());
        }
        return sum;
    }

    public static double hash2(List<String> messages)
    {
        String message = StringUtils.join(messages, "");

        int messageSum = 0;
        for (int i = 0; i < message.length(); i++)
        {
            messageSum += message.charAt(i);
        }
        return Math.pow(messageSum, message.length());
    }

    private static char[][] transpose(char[][] grid)
    {
        char[][] newGrid = new char[grid[0].length][grid.length];
        for (int i = 0; i < grid.length; i++)
        {
            for (int j = 0; j < grid[0].length; j++)
            {
                newGrid[j][i] = grid[i][j];
            }
        }
        return newGrid;
    }

    private static void printGrid(char[][] grid)
    {
        for (char[] row : grid)
        {
            for (char cell : row)
            {
                System.out.printf("%s ", cell);
            }
            System.out.println();
        }
        System.out.println("------------------------");
    }
}
