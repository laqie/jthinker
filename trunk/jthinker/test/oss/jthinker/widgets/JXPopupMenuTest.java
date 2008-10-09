package oss.jthinker.widgets;

import oss.jthinker.widgets.JXPopupMenu;
import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit-test of JXPopupMenu class.
 * 
 * @author iappel
 */
public class JXPopupMenuTest {

    public JXPopupMenuTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getLastDisplayLocation() method.
     */
    @Test
    public void getLastDisplayLocation() {
        JXPopupMenu menu = new JXPopupMenu();
        Window w = new JFrame();
        Component invoker = new JPanel();
        w.add(invoker);
        w.setVisible(true);
        menu.show(invoker, 100, 200);
        menu.setVisible(false);
        Point p = menu.getLastDisplayLocation();
        assertNotNull(p);
        assertEquals(100, p.x);
        assertEquals(200, p.y);
    }
}