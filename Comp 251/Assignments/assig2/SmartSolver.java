package assig2;

import java.util.ArrayList;
import java.util.List;

public class SmartSolver
{
	private List<Pair> disjunctions;

	private ConnectionGraph graph = new ConnectionGraph();

	private int numV;

	public SmartSolver(List<Pair> disjunctions, int numV)
	{
		this.setDisjunctions(disjunctions);
		this.numV = numV;

		// Generate the implication graph
		generateGraph();
	}

	// Solve using the smart algorithm
	public boolean solve()
	{
		List<ArrayList<Node>> result = Tarjan.getSCC(graph);

		for (List<Node> list : result)
		{
			for (int i = 1; i <= numV; i++)
			{
				if (contains(list, i) && contains(list, -i))
				{
					return false;
				}
			}
		}
		return true;
	}

	// Check if a node list contains the given value
	private boolean contains(List<Node> list, int val)
	{
		for (Node node : list)
		{
			if (node.value == val)
			{
				return true;
			}
		}
		return false;
	}

	private void generateGraph()
	{
		// Add all the disjunctions
		for (Pair pair : getDisjunctions())
		{
			// Add -b => a
			graph.addDisjunction(-pair.b, pair.a);
			// Add -a => b
			graph.addDisjunction(-pair.a, pair.b);
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
