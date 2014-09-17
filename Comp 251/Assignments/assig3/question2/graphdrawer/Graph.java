
package assig3.question2.graphdrawer;

import java.awt.Graphics2D;

/**
 * This class represents the graph that is drawn on the GUI
 *
 * @author Amjad Almahairi
 */

public class Graph {
    private Vertex[] vertices;
    private Edge[] edges;

    /**
     * Constructor
     *
     * @param vertices array of vertices of the graph
     * @param edges array of edges of the graph
     */

    public Graph(Vertex[] vertices, Edge[] edges) {
         this.vertices = vertices;
         this.edges = edges;
    }

    /**
     * Draws the graph on the Graphics2D object
     */

    void draw(Graphics2D g) {
        for (int i = 0; i < edges.length; i++) {
            Edge edge = edges[i];
            edge.draw(g);
        }
        for (int i = 0; i < vertices.length; i++) {
            Vertex vertex = vertices[i];
            vertex.draw(g);
        }
    }
}
