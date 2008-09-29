package oss.jthinker.widgets;

import java.awt.Component;
import java.awt.Point;
import javax.swing.JPopupMenu;

/**
 * {@link JPopupMenu} that stores the place of the last time being showed.
 * @author iappel
 */
public class JXPopupMenu extends JPopupMenu {
    private Point lastDisplayLocation;
    
    @Override
    /** {@inheritDoc} */
    public void show(Component invoker, int x, int y) {
        super.show(invoker, x, y);
        lastDisplayLocation = new Point(x, y);
    }

    /**
     * Returns last display location.
     * 
     * @return last display location.
     */
    public Point getLastDisplayLocation() {
        return lastDisplayLocation;
    }
}
