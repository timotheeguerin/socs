
package assig3.question2.graphdrawer;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * This class represents the drawing features of an edge in the graph.
 *
 * @author Amjad Almahairi
 */

public class Edge {
    private Vertex v1,v2;
    private Color drawColor = Color.LIGHT_GRAY;

    /**
     * Constructor
     *
     * @param v1 first vertex
     * @param v2 second vertex
     */

    public Edge(Vertex v1, Vertex v2){
        this.v1 = v1;
        this.v2 = v2;
    }

    /**
     * Getters
     */

    public Vertex getV1() {
        return v1;
    }

    public Vertex getV2() {
        return v2;
    }

    /**
     * Setters
     */

    public void setDrawColor(Color drawColor) {
        this.drawColor = drawColor;
    }

    /**
     * Draws the edge on the Graphics2D object
     */

    void draw(Graphics2D g) {
        g.setColor(drawColor);
        g.drawLine(v1.getX(), v1.getY(), v2.getX(), v2.getY());
    }
}
