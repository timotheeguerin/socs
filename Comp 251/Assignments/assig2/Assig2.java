package assig2;

import java.io.*;
import java.util.*;

// Framework for assignment 2
public class Assig2
{
	// Reads input from stdin or a specified file. Accepts an optional
	// command-line argument,
	// a flag to indicate whether to use a simple brute-force solution
	// or not.
	public static void main(String[] args)
	{
		Scanner intScan;
		boolean bruteForce = true;

		Scanner inp = new Scanner(System.in);

		for (int i = 0; i < args.length; i++)
		{
			if (args[i].equals("-nobrute"))
			{
				bruteForce = false; // don't use brute force, use clever
									// algorithm
			} else
			{
				try
				{
					inp = new Scanner(new File(args[i]));
				} catch (FileNotFoundException e)
				{
					System.out.println("File not found: " + args[i]);
					System.exit(1);
				}
			}
		}
		intScan = new Scanner(inp.nextLine());

		// first read in number of vars involved
		int numV = intScan.nextInt();

		List<Pair> disjunctions = new ArrayList<Pair>();

		// then read in each disjunction
		while (inp.hasNextLine())
		{
			String s = inp.nextLine();
			intScan = new Scanner(s);

			// read in a disjunction: (a \/ b) represented by 2 integers
			// separated by whitespace
			// each variable is represented by a number in the range 1..numV, or
			// -numV..-1
			// (negative numbers represent logically negated values)
			Pair pair = new Pair();
			pair.a = intScan.nextInt();
			pair.b = intScan.nextInt();
			disjunctions.add(pair);

		}
		boolean solve;
		if (bruteForce)
		{
			BruteForceSolver bruteSolver  = new BruteForceSolver(disjunctions, numV);
			solve = bruteSolver.solve();
		} else
		{
			SmartSolver smartSolver = new SmartSolver(disjunctions, numV);
			solve = smartSolver.solve();
		}
		
		if(solve)
		{
			System.out.println("YES");
		}
		else
		{
			System.out.println("NO");
		}
	}
	



}
