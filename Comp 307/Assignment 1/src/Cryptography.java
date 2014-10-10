import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Cryptography methods
 * Created by tim on 14-10-03.
 */
public class Cryptography
{
    private static final int GRID_SIZE = 4;
    private static final int[] BLOCK_KEYS = { 4, 3, 2, 1 };

    private static final int PRIVATE_W = 9;
    private static final int PRIVATE_W_REVERSE = 39;
    private static final int PRIVATE_U = 50;
    /**
     * Encrypt the messages by transposing then shifting with a 4 block key
     *
     * @param messages messages to encrypt
     * @return encrypted string
     */
    public static String gridTransposition(List<String> messages)
    {
        //Join the messages
        String message = StringUtils.join(messages, ",").toLowerCase();

        //Create the grid
        char[][] grid = new char[(int) Math.ceil((float) message.length() / GRID_SIZE)][GRID_SIZE];
        for (int i = 0; i < message.length(); i++)
        {
            grid[((int) Math.floor((float) i / GRID_SIZE))][i % GRID_SIZE] = message.charAt(i);
        }
        printGrid(grid);

        //Transpose
        grid = transpose(grid);
        printGrid(grid);

        //Shift 4-key block
        for (int i = 0; i < grid.length; i++)
        {
            for (int j = 0; j < grid[0].length; j++)
            {
                if (grid[i][j] >= 'a' && grid[i][j] <= 'z')
                {
                    int shift = BLOCK_KEYS[(i * grid[0].length + j) % BLOCK_KEYS.length];
                    grid[i][j] += shift;
                    if (grid[i][j] > 'z')
                        grid[i][j] -= 26;
                }
            }
        }
        printGrid(grid);
        String output = "";
        for (char[] row : grid)
        {
            output += new String(row);
        }
        return output;
    }

    /**
     * Hash message separately
     *
     * @param messages messages to hash
     * @return hash
     */
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

    /**
     * Hash message together
     *
     * @param messages messages to hash
     * @return hash
     */
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

    public static Integer[] privatePublicEncrypt(List<String> messages)
    {
        String message = StringUtils.join(messages, ",").toLowerCase();
        Integer[] output = new Integer[message.length()];
        for (int i = 0; i < message.length(); i++)
        {
            if (message.charAt(i) >= 'a' && message.charAt(i) <= 'z')
            {
                output[i] = (((message.charAt(i) - 'a') * PRIVATE_W) % PRIVATE_U);
            }
            else if (message.charAt(i) == ' ')
            {
                output[i] = -1; // -1 For space
            }
            else
            {
                output[i] = null; //NUll for message separator
            }
        }
        return output;
    }

    public static String privatePublicDecrypt(Integer[] array)
    {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < array.length; i++)
        {
            if (array[i] == null)
            {
                output.append(","); //NUll for message separator
            }
            else if (array[i] >= 0)
            {
                output.append((char) (((array[i] * PRIVATE_W_REVERSE) % PRIVATE_U) + 'a'));
            }
            else if (array[i] == -1)
            {
                output.append(" ");
            }
        }
        return output.toString();
    }

    /**
     * Helper function that transpose the given grid
     *
     * @param grid grid to transpose
     * @return transposed grid
     */
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

    /**
     * Helper function
     *
     * @param grid grid to print
     */
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
