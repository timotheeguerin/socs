package assig3.question2.graphdrawer;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics2D;

/**
 * This class represents the drawing features of a vertex in the graph.
 * 
 * @author Amjad Almahairi
 */

public class Vertex
{
	private int radius = 3;
	private Point center = new Point();
	private Point drawPos = new Point();
	private Color drawColor = Color.BLACK;
	private Color fillColor = Color.RED;

	/**
	 * Constructor
	 */

	public Vertex(int x, int y)
	{
		center.setLocation(x, y);
		drawPos.setLocation(x - radius, y - radius);
	}

	/**
	 * Getters
	 */

	public int getX()
	{
		return center.x;
	}

	public int getY()
	{
		return center.y;
	}

	/**
	 * Setters
	 */

	public void setDrawColor(Color drawColor)
	{
		this.drawColor = drawColor;
	}

	public void setFillColor(Color fillColor)
	{
		this.fillColor = fillColor;
	}

	public void setRadius(int radius)
	{
		this.radius = radius;
	}

	/**
	 * Draws the vertex on the Graphics2D object
	 */

	void draw(Graphics2D g)
	{
		g.setColor(fillColor);
		g.fillOval(drawPos.x, drawPos.y, radius * 2, radius * 2);
		g.setColor(drawColor);
		g.drawOval(drawPos.x, drawPos.y, radius * 2, radius * 2);
		g.setColor(drawColor);
	}

}
