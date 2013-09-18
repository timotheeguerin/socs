package assig4;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

// Template for assignment 4
public class Assig4_1
{
	public static final int IMAGESIZE = 1024;
	private static final int MAX_STACK = 5;

	public static void main(String[] args)
	{
		try
		{
			// first arg is the t value
			double t = Double.parseDouble(args[0]);

			// parse 1 or 2 control points if specified
			DPoint cp1 = DPoint.parsePoint(args[1]);
			DPoint cp2 = null;
			if (args.length > 2)
				cp2 = DPoint.parsePoint(args[2]);

			// and create our two endpoints. This is just for convenience.
			DPoint cp0 = new DPoint(0.0, 0.5);
			DPoint cpN = new DPoint(1.0, 0.5);

			List<DPoint> list = new ArrayList<DPoint>();
			list.add(cp0);
			if (cp2 == null)
			{
				quadratic(cp0, cp1, cpN, list, 0, t);
			} else
			{
				cubic(cp0, cp1, cp2, cpN, list, 0, t);
			}
			// create an image for output. We don't need colour, so greyscale or
			// even binary is ok.
			BufferedImage img = new BufferedImage(IMAGESIZE, IMAGESIZE, BufferedImage.TYPE_BYTE_GRAY);

			// graphical output for drawing
			Graphics2D g = img.createGraphics();
			System.out.println("-----------------------------------------");
			g.setColor(Color.WHITE);

			list.add(cpN);
			display(list, g);
			// your code would go here!

			// write out the image
			File outputfile = new File("outputimage.png");
			ImageIO.write(img, "png", outputfile);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// Quadratic
	public static void quadratic(DPoint left, DPoint center, DPoint right, List<DPoint> list, int stack, double t)
	{
		if (stack >= MAX_STACK)
		{
			return;
		}
		stack++;
		// Calucate points
		DPoint leftMiddle = getMiddle(left, center, t);
		DPoint rightMiddle = getMiddle(center, right, t);
		DPoint middle = getMiddle(leftMiddle, rightMiddle, t);

		// Recursion
		quadratic(left, leftMiddle, middle, list, stack, t);
		list.add(middle);
		quadratic(middle, rightMiddle, right, list, stack, t);

	}

	// Cubic algorithm
	public static void cubic(DPoint left, DPoint center1, DPoint center2, DPoint right, List<DPoint> list, int stack,
			double t)
	{
		if (stack >= MAX_STACK)
		{
			return;
		}
		stack++;
		// Calculate points
		DPoint center = getMiddle(center1, center2, t);
		DPoint leftCenter = getMiddle(left, center1, t);
		DPoint rightCenter = getMiddle(center2, right, t);

		DPoint leftMiddle = getMiddle(leftCenter, center, t);
		DPoint rightMiddle = getMiddle(center, rightCenter, t);
		DPoint middle = getMiddle(leftMiddle, rightMiddle, t);
		// Recursion
		cubic(left, leftCenter, leftMiddle, middle, list, stack, t);
		list.add(middle);
		cubic(middle, rightMiddle, rightCenter, right, list, stack, t);

	}

	// Display a list
	public static void display(List<DPoint> list, Graphics2D g)
	{

		for (int i = 0; i < list.size() - 1; i++)
		{
			DPoint point1 = list.get(i);
			double x1 = point1.x * IMAGESIZE;
			double y1 = point1.y * IMAGESIZE;

			DPoint point2 = list.get(i + 1);
			double x2 = point2.x * IMAGESIZE;
			double y2 = point2.y * IMAGESIZE;

			g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);

		}

	}

	// Get the middle point depending on t
	public static DPoint getMiddle(DPoint point1, DPoint point2, double t)
	{
		DPoint middle = new DPoint();
		middle.x = point1.x * (1 - t) + point2.x * t;
		middle.y = point1.y * (1 - t) + point2.y * t;
		return middle;
	}
}

// utility class for double-based control points
class DPoint
{
	double x, y;

	public DPoint()
	{
		// TODO Auto-generated constructor stub
	}

	public DPoint(double dx, double dy)
	{
		x = dx;
		y = dy;
	}

	// static method to parse a pair of coords
	public static DPoint parsePoint(String s) throws NumberFormatException
	{
		int i = s.indexOf(',');
		return new DPoint(Double.parseDouble(s.substring(0, i)), Double.parseDouble(s.substring(i + 1)));
	}
}

class Tree
{
	DPoint point;
	Tree left, right;
}