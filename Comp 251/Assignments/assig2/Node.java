package assig2;

public class Node
{

	final int value;
	boolean visited = false; // used for Kosaraju's algorithm and Edmonds's

	// Targan algorithm variables
	int lowlink = -1;
	int index = -1;

	public Node(final int value)
	{
		this.value = value;
	}
}