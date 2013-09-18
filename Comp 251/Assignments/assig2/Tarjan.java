package assig2;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Tarjan
{

	private int index = 0;
	private Stack<Node> stack = new Stack<Node>();
	private ArrayList<ArrayList<Node>> SCC = new ArrayList<ArrayList<Node>>();
	private ConnectionGraph graph;

	public Tarjan(ConnectionGraph graph)
	{
		this.graph = graph;
	}

	public static ArrayList<ArrayList<Node>> getSCC(ConnectionGraph graph)
	{
		Tarjan tarjan = new Tarjan(graph);
		return tarjan.calculateSCC();

	}

	public ArrayList<ArrayList<Node>> calculateSCC()
	{

		index = 0;
		SCC.clear();
		stack.clear();

		if (graph != null)
		{
			List<Node> nodeList = new ArrayList<Node>(graph.getKeySet());

			for (Node node : nodeList)
			{
				if (node.index == -1)
				{
					tarjan(node, graph);
				}
			}
		}
		return SCC;
	}

	private void tarjan(Node v, ConnectionGraph list)
	{
		v.index = index;
		v.lowlink = index;
		index++;
		stack.push(v);

		if (list.getLinkedEdge(v) != null)
		{
			for (Node n : list.getLinkedEdge(v))
			{
				if (n.index == -1)
				{
					tarjan(n, list);
					v.lowlink = Math.min(v.lowlink, n.lowlink);
				} else if (stack.contains(n))
				{
					v.lowlink = Math.min(v.lowlink, n.index);
				}
			}
		}
		if (v.lowlink == v.index)
		{
			Node n;
			ArrayList<Node> component = new ArrayList<Node>();
			do
			{
				n = stack.pop();
				component.add(n);
			} while (n != v);
			SCC.add(component);
		}
	}
}