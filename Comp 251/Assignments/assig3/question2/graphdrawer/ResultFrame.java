
package assig3.question2.graphdrawer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.Toolkit;

/**
 * This class represents the GUI window that contains the drawing panel
 *
 * @author Amjad Almahairi
 */
public class ResultFrame extends JFrame {

    private DrawingPanel drawingPnl = new DrawingPanel();
    BorderLayout layout = new BorderLayout();
    private Dimension size;

    /**
     * Constructor
     *
     * @param d size of the GUI window
     */
    public ResultFrame(Dimension d) {
        size = d;
        try {
            init();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void init() throws Exception {
        getContentPane().setLayout(layout);
        this.setTitle("Graph GUI");
        this.setSize(size);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(drawingPnl, BorderLayout.CENTER);

        // initial positioning for the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - size.width) / 2,
                (screenSize.height - size.height) / 2);
    }

    /**
     * Sets the graph to be drawn
     *
     * @param graph
     */

    public void setGraph(Graph graph) {
        drawingPnl.setGraph(graph);
    }

    /**
     * Gives the drawing panel (white area) size
     *
     * @return actual drawing panel size
     */

    public Dimension getWindowSize() {
        return drawingPnl.getSize();
    }
}
