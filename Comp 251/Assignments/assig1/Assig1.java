package assig1;

import java.awt.image.*;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.*;

// Template for assignment 1
public class Assig1
{

	public static void main(String[] args)
	{
		try
		{

			// arg 0 is the input image name
			BufferedImage img = ImageIO.read(new File(args[0]));

			// arg 1 is the min radius
			int minr = Integer.parseInt(args[1]);
			// arg 2 is the max radius
			int maxr = Integer.parseInt(args[2]);

			// if present, arg 3 is the max width we consider
			int w = (args.length > 3) ? Integer.parseInt(args[3]) : img.getWidth();
			// if present, arg 4 is the max height we consider
			int h = (args.length > 4) ? Integer.parseInt(args[4]) : img.getHeight();

			// you can look at pixel values with this API call:
			int c = img.getRGB(0, 0); // get RGB value of pixel at (0,0)

			long start = System.currentTimeMillis();
			// Call the algorithm and get the circle array.
			List<int[]> circles = detectAllCircle(minr, maxr, img, h, w);
			long end = System.currentTimeMillis();

			System.out.println("Time: " + (end - start));
			// you can write out pixels with the setRGB() API. However,
			// what you get will depend on the colour model, so here
			// we use a Graphics2D object.

			// graphical output
			Graphics2D g2 = img.createGraphics();
			// use red
			g2.setColor(Color.RED);

			// Print all the circles
			for (int circle[] : circles)
			{
				drawCircle(circle[0], circle[1], circle[2], img, g2);
			}

			// write out the image
			File outputfile = new File("outputimage.png");
			ImageIO.write(img, "png", outputfile);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	static List<int[]> detectAllCircle(int minr, int maxr, BufferedImage img, int imgHeight, int imgWidth)
	{
		// Create the array containing the circles.
		List<int[]> circles = new ArrayList<int[]>();

		for (int i = minr; i < imgWidth - minr; i++)
		{
			for (int j = minr; j < imgHeight - minr; j++)
			{
				for (int r = minr; r <= maxr; r++)
				{
					if (!(i - r <= 0 || i + r >= imgWidth - 1 || j - r <= 0 || j + r >= imgHeight - 1))
					{
						int color = img.getRGB(i + r, j);
						boolean result = detectCircle(i, j, r, color, img);
						if (result)
						{
							if (!(intersect(i, j, r + 1, color, img) || intersect(i, j, r - 1, color, img)))
							{
								// Store the circle in the array
								int[] circle = new int[3];
								circle[0] = i; // a[0]: x
								circle[1] = j; // a[1]: y
								circle[2] = r; // a[2]: radius

								circles.add(circle);
							}

						}
					}

				}
			}
		}

		return circles;
	}

	// Detect if there is a circle of radius r and center (x, y)
	static boolean detectCircle(int cx, int cy, int r, int color, BufferedImage img)
	{

		// Check cardinal point
		if (img.getRGB(cx, cy + r) != color)
			return false;
		if (img.getRGB(cx, cy - r) != color)
			return false;
		if (img.getRGB(cx + r, cy) != color)
			return false;
		if (img.getRGB(cx - r, cy) != color)
			return false;

		// check point after
		if (img.getRGB(cx, cy + r + 1) == color)
			return false;
		if (img.getRGB(cx, cy - r - 1) == color)
			return false;
		if (img.getRGB(cx + r + 1, cy) == color)
			return false;
		if (img.getRGB(cx - r - 1, cy) == color)
			return false;

		// Check point before
		if (img.getRGB(cx, cy + r - 1) == color)
			return false;
		if (img.getRGB(cx, cy - r + 1) == color)
			return false;
		if (img.getRGB(cx + r - 1, cy) == color)
			return false;
		if (img.getRGB(cx - r + 1, cy) == color)
			return false;

		int f = 1 - r;
		int ddF_x = 1;
		int ddF_y = -2 * r;
		int x = 0;
		int y = r;

		while (x < y)
		{
			if (f >= 0)
			{
				y--;
				ddF_y += 2;
				f += ddF_y;
			}
			x++;
			ddF_x += 2;
			f += ddF_x;

			// Check for the symmetry point
			if (img.getRGB(cx + x, cy + y) != color)
				return false;
			if (img.getRGB(cx - x, cy + y) != color)
				return false;
			if (img.getRGB(cx + x, cy - y) != color)
				return false;
			if (img.getRGB(cx - x, cy - y) != color)
				return false;
			if (img.getRGB(cx + y, cy + x) != color)
				return false;
			if (img.getRGB(cx - y, cy + x) != color)
				return false;
			if (img.getRGB(cx + y, cy - x) != color)
				return false;
			if (img.getRGB(cx - y, cy - x) != color)
				return false;

		}

		return true;

	}

	static boolean intersect(int cx, int cy, int r, int color, BufferedImage img)
	{
		int f = 1 - r;
		int ddF_x = 1;
		int ddF_y = -2 * r;
		int x = 0;
		int y = r;

		while (x < y)
		{
			if (f >= 0)
			{
				y--;
				ddF_y += 2;
				f += ddF_y;
			}
			x++;
			ddF_x += 2;
			f += ddF_x;

			// Check for the symetry point
			if (img.getRGB(cx + x, cy + y) == color)
				return true;
			if (img.getRGB(cx - x, cy + y) == color)
				return true;
			if (img.getRGB(cx + x, cy - y) == color)
				return true;
			if (img.getRGB(cx - x, cy - y) == color)
				return true;
			if (img.getRGB(cx + y, cy + x) == color)
				return true;
			if (img.getRGB(cx - y, cy + x) == color)
				return true;
			if (img.getRGB(cx + y, cy - x) == color)
				return true;
			if (img.getRGB(cx - y, cy - x) == color)
				return true;

		}

		return false;

	}

	// Bresenham's algorithm to draw a circle
	// requires circle center and radius, as well as the
	// image and Graphics2D object with drawing colour already set.
	static void drawCircle(int cx, int cy, int r, BufferedImage img, Graphics2D g)
	{
		int f = 1 - r;
		int ddF_x = 1;
		int ddF_y = -2 * r;
		int x = 0;
		int y = r;

		// draw cardinal points
		g.drawLine(cx, cy + r, cx, cy + r);
		g.drawLine(cx, cy - r, cx, cy - r);
		g.drawLine(cx + r, cy, cx + r, cy);
		g.drawLine(cx - r, cy, cx - r, cy);

		// draw 1/8 of the circle, taking advantage of symmetry
		while (x < y)
		{
			if (f >= 0)
			{
				y--;
				ddF_y += 2;
				f += ddF_y;
			}
			x++;
			ddF_x += 2;
			f += ddF_x;

			g.drawLine(cx + x, cy + y, cx + x, cy + y);
			g.drawLine(cx - x, cy + y, cx - x, cy + y);
			g.drawLine(cx + x, cy - y, cx + x, cy - y);
			g.drawLine(cx - x, cy - y, cx - x, cy - y);
			g.drawLine(cx + y, cy + x, cx + y, cy + x);
			g.drawLine(cx - y, cy + x, cx - y, cy + x);
			g.drawLine(cx + y, cy - x, cx + y, cy - x);
			g.drawLine(cx - y, cy - x, cx - y, cy - x);
		}
	}
}
