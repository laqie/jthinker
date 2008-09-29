package oss.jthinker.widgets;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import oss.jthinker.util.Switch;

/**
 * A directed connection between two {@link JNode}s.
 * 
 * @author iappel
 */
public class JEdge extends JWire implements Switch {
    private final JNode peerA,  peerZ;
    private boolean switchState;
    private final JPopupMenu popupMenu;
    private final JEdgeHost edgeHost;

    /**
     * Creates a new instance of JEdge that connects two given {@link JNode}s
     * and is managed by given {@link JEdgeHost}.
     * 
     * @param nodeA start of edge.
     * @param nodeB end of edge.
     * @param host manager of deletions and reversals of the edge.
     */
    public JEdge(JNode nodeA, JNode nodeB, JEdgeHost host) {
        super(JNodeAdjuster.makeTrigger(nodeA, nodeB), true);
        peerA = nodeA;
        peerZ = nodeB;
        switchState = false;
        popupMenu = createPopupMenu();
        edgeHost = host;
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        final JEdge instance = this;
        menu.add(new AbstractAction("Reverse") {
            public void actionPerformed(ActionEvent e) {
                edgeHost.reverseJEdge(instance);
            }
        });
        
        menu.add(new AbstractAction("Delete") {
            public void actionPerformed(ActionEvent e) {
                edgeHost.deleteJEdge(instance);
            }
        });
        return menu;
    }

    /** {@inheritDoc} */
    public boolean isSwitched() {
        return switchState;
    }

    /**
     * Toggles inactive color of the edge.
     */
    public void switchOff() {
        if (switchState) {
            setForeground(Color.BLACK);
            repaint();
            switchState = false;
        }
    }

    /**
     * Toggles active color of the edge.
     */    
    public void switchOn() {
        if (!switchState) {
            setForeground(Color.BLUE);
            repaint();
            switchState = true;
        }
    }

    /**
     * Checks, where mouse pointer is and switches component accordingly.
     * Mouse pointer's near location is treated as one being not further
     * than five pixels away from the line.
     * 
     * @param p location of the mouse pointer
     */    
    public void switchOnMouseMove(Point p) {
        if (distanceToPoint(p) < 5.0) {
            switchOn();
        } else {
            switchOff();
        }
    }
    
    /**
     * Returns component's popup menu.
     * @return component's popup menu.
     */
    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    /**
     * Returns component's starting connection.
     * @return component's starting connection.
     */
    public JNode getPeerA() {
        return peerA;
    }
    
    /**
     * Returns component's ending connection.
     * @return component's ending connection.
     */
    public JNode getPeerZ() {
        return peerZ;
    }

    public void setSwitched(boolean switching) {
        setForeground(switching ? Color.BLUE : Color.BLACK);
    }
}
