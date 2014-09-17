package assig3.question1;

import java.util.*;

// Framework for assignment 3, question 1c
public class Question1
{
	public static Random r; // seeded or not

	// Starting point. Accepts 1 or 2 command-line arguments,
	// the number of tasks (n) and an optional random seed may
	// be passed in for repeatable results.
	public static void main(String[] args)
	{
		try
		{
			// arg 1 is n
			int n = Integer.parseInt(args[0]);

			if (args.length > 1)
			{
				long seed = Long.parseLong(args[1]);
				r = new Random(seed);
			} else
			{
				r = new Random();
			}
			generate(n);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	// Generate a random graph
	public static void generate(int n)
	{
		List<Integer> nodes = new ArrayList<Integer>();
		float proba = r.nextFloat();
		for (int i = 1; i < n + 1; i++)
		{
			nodes.add(i);
		}
		while (!nodes.isEmpty())
		{
			int rand = r.nextInt(nodes.size());
			int node = nodes.get(rand);
			nodes.remove(rand);
			System.out.printf("%d ", node);

			for (int to : nodes)
			{
				if (r.nextFloat() < proba)
				{
					System.out.printf("%d ", to);
				}
			}
			System.out.println();
		}
	}
}
