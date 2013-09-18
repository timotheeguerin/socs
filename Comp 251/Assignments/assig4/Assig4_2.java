package assig4;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Assig4_2
{
	private static final int LAMBDA = 10;

	private static Map<String, Integer> groups = new HashMap<String, Integer>();

	public static void main(String[] args)
	{
		String arg1 = "a b    ";
		String arg2 = "  a    b    ";

		String str1 = removeBlank(arg1);
		String str2 = removeBlank(arg2);
		System.out.println("'" + str1 + "', '" + str2 + "'");

		int d = calculate(str1, str2);
		System.out.println("Distance: ");
		System.out.println("'" + arg1 + "', '" + arg2 + "' = " + d);

	}

	// Remove unnecessary blank space
	public static String removeBlank(String str)
	{
		// Replace mutltiple by one
		String tmp = str.replaceAll(" +", " ");

		// remove first and last
		if (tmp.startsWith(" "))
		{
			tmp = tmp.substring(1);
		}
		if (tmp.endsWith(" "))
		{
			tmp = tmp.substring(0, tmp.length() - 1);
		}
		return tmp;
	}

	// Calculate tge cost between the two string
	public static int calculate(CharSequence str1, CharSequence str2)
	{

		setupGroup();

		// init
		int[][] distance = new int[str1.length() + 1][str2.length() + 1];

		for (int i = 0; i <= str1.length(); i++)
		{
			distance[i][0] = i * LAMBDA;
		}
		for (int j = 1; j <= str2.length(); j++)
		{
			distance[0][j] = j * LAMBDA;
		}
		// algotihm
		for (int i = 1; i <= str1.length(); i++)
		{
			for (int j = 1; j <= str2.length(); j++)
			{
				int a = distance[i - 1][j] + LAMBDA;
				int b = distance[i][j - 1] + LAMBDA;
				int c = distance[i - 1][j - 1] + cost(str1.charAt(i - 1), str2.charAt(j - 1));
				distance[i][j] = minimum(a, b, c);
			}
		}

		return distance[str1.length()][str2.length()];
	}

	// Cost between two char
	public static int cost(char ch1, char ch2)
	{
		return (ch1 == ch2) ? 0 : getMinimumGroup(ch1, ch2);
	}

	public static int getMinimumGroup(char ch1, char ch2)
	{
		int min = 8;
		for (Entry<String, Integer> group : groups.entrySet())
		{
			String str = group.getKey();
			if (str.indexOf(ch1) != -1 && str.indexOf(ch2) != -1)
			{
				System.out.println("Contained in " + str);
				if (group.getValue() < min)
				{
					min = group.getValue();
				}
			}
		}
		return min;
	}

	public static void setupGroup()
	{
		groups.clear();
		groups.put("o0", 2);
		groups.put("pq", 2);
		groups.put("db", 2);
		groups.put("69g", 2);
		groups.put("uvw", 2);
		groups.put("xy", 2);
		groups.put("mnh", 2);
		groups.put("ij", 2);
		groups.put("s5", 2);
		groups.put("z2", 2);
		groups.put("1l", 2);
		groups.put("6893", 3);
		groups.put("2zs5", 3);
		groups.put("bdgopq", 3);
		groups.put("ijl1tf7", 4);
		groups.put("abcdegopq0689", 4);
		groups.put("uvwxyk", 5);
		groups.put("rmnh", 5);
		groups.put(".,-", 6);
		groups.put("49", 6);

	}

	private static int minimum(int a, int b, int c)
	{
		return Math.min(Math.min(a, b), c);
	}
}
