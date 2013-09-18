package assig2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.Map.Entry;

public class BruteForceSolver
{
	private List<Pair> disjunctions;
	boolean[] values;

	private int numV;

	public BruteForceSolver(List<Pair> disjunctions, int numV)
	{
		this.disjunctions = disjunctions;
		this.numV = numV;
		values = new boolean[numV];
		Arrays.fill(values, true);
	}

	// Solve the brute force
	public boolean solve()
	{

		do
		{
			if (testCurrentValues())
			{
				return true;
			}
		} while (incrementArray());
		return false;
	}

	// Generate the next value of the array(Counting in binary: true => 0, false
	// = 1)
	public boolean incrementArray()
	{

		if (values[0] == true)
		{
			values[0] = false;
			return true;
		} else
		{
			for (int i = 0; i < values.length; i++)
			{
				if (values[i] == true)
				{
					values[i] = false;
					return true;
				} else
				{
					values[i] = true;
				}
			}
		}
		return false;

	}

	// Test the current value store in the values array
	public boolean testCurrentValues()
	{
		// Check if each pair is working
		for (Pair pair : disjunctions)
		{
			boolean a = getValue(pair.a);
			boolean b = getValue(pair.b);
			if (!(a || b))
			{
				return false;
			}
		}
		return true;
	}

	// Get the generated variable value
	public boolean getValue(int var)
	{
		// If var is negative then
		// get the value at position -var -1 and return not(value)
		// else return the value at position var -1
		if (var < 0)
		{
			return !values[-(var + 1)];
		} else
		{
			return values[var - 1];
		}
	}

	/**
	 * @return the disjunctions
	 */
	public List<Pair> getDisjunctions()
	{
		return disjunctions;
	}

	/**
	 * @param disjunctions
	 *            the disjunctions to set
	 */
	public void setDisjunctions(List<Pair> disjunctions)
	{
		this.disjunctions = disjunctions;
	}

	/**
	 * @return the numV
	 */
	public int getNumV()
	{
		return numV;
	}

	/**
	 * @param numV
	 *            the numV to set
	 */
	public void setNumV(int numV)
	{
		this.numV = numV;
	}
}
