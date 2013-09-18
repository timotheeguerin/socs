
package assig3.question2.graphdrawer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.JPanel;

/**
 * This class represensts the drawing panel that is used to draw the graph on.
 *
 * @author Amjad Almahairi
 */

public class DrawingPanel extends JPanel {
    private Graph graph;

    /**
     * Constructor
     */

    public DrawingPanel() {
        this.setLayout(null);
        this.setBackground(Color.white);
    }

    /**
     * Sets the graph to be drawn
     */

    public void setGraph(Graph graph) {
        this.graph = graph;
        repaint();
    }

    /**
     * This function is the function that is called whenever the panel is
     * rendered
     *
     * @param tg graphics object that will use to draw graph on
     */

    public void paint(Graphics tg) {
        super.paint(tg);
        Graphics2D g = (Graphics2D) tg;
        if(graph != null)
            graph.draw(g);
    }

}

