package assig2;

import java.util.HashSet;
import java.util.Random;

// program to generate 2SAT instances for testing
public class GenerateTests
{

	// expects 2 arguments: number of variables, and number of disjunctions
	// an optional third argument specified the random seed for reproducibility.
	public static void main(String[] args)
	{
		try
		{
			HashSet<Disjunc> h = new HashSet<Disjunc>(); // for tracking
															// duplicates
			int numV = Integer.parseInt(args[0]);
			int numD = Integer.parseInt(args[1]);

			Random r = new Random();
			if (args.length > 2)
				r.setSeed(Long.parseLong(args[2]));

			System.out.println(numV); // print out number of vars used
			int i = 0;
			while (i < numD)
			{ // repeat until we generate the required number of disjunctions
				int a = r.nextInt(numV) + 1;
				int b = (r.nextInt(numV - 1) + a) % numV + 1; // select
																// something
																// which isn't
																// the same as a

				// perhaps negate one or both
				if (r.nextBoolean())
					a = -a;
				if (r.nextBoolean())
					b = -b;

				// now verify we haven't created that disjunction so far, and if
				// so emit it. Otherwise, throw it away and keep trying.
				Disjunc d = new Disjunc(a, b);
				if (!h.contains(d))
				{
					h.add(d);
					i++;
					System.out.println(a + " " + b);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

// helper class to represent a single disjunction
class Disjunc
{
	int a, b; // vars

	public Disjunc(int x, int y)
	{
		a = x;
		b = y;
	}

	// override equals and hashcode so identical disjunctions are recognized
	// as identical despite potentially being different objects
	public boolean equals(Object o)
	{
		if (o instanceof Disjunc && ((Disjunc) o).a == a && ((Disjunc) o).b == b)
			return true;
		return false;
	}

	// not the clever-est hashing, but sufficient
	public int hashCode()
	{
		return a ^ b;
	}
}
