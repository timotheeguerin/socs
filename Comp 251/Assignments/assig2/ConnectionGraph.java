package assig2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConnectionGraph
{

	private Map<Node, List<Node>> graph = new HashMap<Node, List<Node>>();

	// Add a new edge
	public void addEdge(Node source, Node target)
	{
		List<Node> list;
		if (!graph.containsKey(source))
		{
			list = new ArrayList<Node>();
			graph.put(source, list);
		} else
		{
			list = graph.get(source);
		}
		list.add(target);
	}

	// Add a new disjunction
	public void addDisjunction(int a, int b)
	{
		Node an = getNode(a);
		Node bn = getNode(b);
		addEdge(an, bn);
	}

	// Get the node or create it if it dowesnt exist
	public Node getNode(int i)
	{
		for (Node node : graph.keySet())
		{
			if (node.value == i)
			{
				return node;
			}
		}
		Node node = new Node(i);
		graph.put(node, new ArrayList<Node>());

		return node;
	}

	public List<Node> getLinkedEdge(Node source)
	{
		return graph.get(source);
	}

	public Set<Node> getKeySet()
	{
		return graph.keySet();
	}
}