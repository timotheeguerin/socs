package assig3.question2;

import java.awt.Dimension;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import assig3.question2.graphdrawer.Edge;
import assig3.question2.graphdrawer.Graph;
import assig3.question2.graphdrawer.ResultFrame;
import assig3.question2.graphdrawer.Vertex;

public class MainClass
{

	/**
	 * Application entry point
	 * 
	 */
	public static void main(String[] args)
	{
		Dimension dim = new Dimension(800, 600);
		ResultFrame resultFrame = new ResultFrame(dim);
		resultFrame.validate();
		resultFrame.setVisible(true);

		Dimension window = resultFrame.getWindowSize();

		for (int i = 10000; i <= 10000; i++)
		{
			Graph g = generateGraph(700, 500, i, System.currentTimeMillis());
			resultFrame.setGraph(g);
		}

	}

	static Graph generateGraph(int width, int height, int n, long l)
	{
		Vertex[] vertices = new Vertex[n];
		Random rand = new Random(l);

		for (int i = 0; i < n; i++)
		{
			int x = rand.nextInt(width);
			int y = rand.nextInt(height);
			vertices[i] = new Vertex(x, y);
		}

		// random edges
		List<Edge> edges = new ArrayList<Edge>();

		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
			{
				if (i != j)
				{

					Edge edge = new Edge(vertices[i], vertices[j]);
					if (!intersect(edge, edges))
					{
						edges.add(edge);
					}
				}
			}
		}

		Graph g = new Graph(vertices, edges.toArray(new Edge[edges.size()]));
		System.out.println(edges.size());

		return g;
	}

	static boolean intersect(Edge edge, List<Edge> edges)
	{
		for (Edge e : edges)
		{
			boolean in = intersect(edge, e);
			if (in)
			{
				return true;
			}
		}
		return false;
	}

	static boolean intersect(Edge edge1, Edge edge2)
	{
		// If exactly one extremity point intersect
		if (edge1.getV1().equals(edge2.getV1()) ^ edge1.getV2().equals(edge2.getV2())
				^ edge1.getV1().equals(edge2.getV2()) ^ edge1.getV2().equals(edge2.getV1()))
		{
			return false;
		}

		// Create two lines from the edges and check if they intersect
		Line2D line1 = new Line2D.Double(edge1.getV1().getX(), edge1.getV1().getY(), edge1.getV2().getX(), edge1
				.getV2().getY());
		Line2D line2 = new Line2D.Double(edge2.getV1().getX(), edge2.getV1().getY(), edge2.getV2().getX(), edge2
				.getV2().getY());

		return line1.intersectsLine(line2);

	}

}
