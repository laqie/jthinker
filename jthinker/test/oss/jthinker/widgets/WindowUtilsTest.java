package oss.jthinker.widgets;

import java.awt.Component;
import java.awt.Point;
import javax.swing.JLabel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit-tests for WindowUtils class
 * 
 * @author iappel
 */
public class WindowUtilsTest {

    public WindowUtilsTest() {
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
     * Similar to {@link setCenterPoint(Component, Point)} but operates
     * with component's own point of origin instead of parent's.
     * 
     * @param c component to move
     * @param p
     */
    public static void adjustCenterPoint(Component c, Point p) {
        WindowUtils.adjustCenterPoint(c, p);
        Point l = c.getLocation();
        l.translate(p.x - c.getWidth() / 2, p.y - c.getHeight() / 2);
        c.setLocation(l);
    }
    
    /**
     * Test of computeCenterPoint method, of class WindowUtils.
     */
    @Test
    public void computeCenterPoint() {
        System.out.println("computeCenterPoint");
        Component c = new JLabel("foobar");
        c.setBounds(100, 100, 150, 150);
        Point expect = new Point(175, 175);
        assertEquals(expect, WindowUtils.computeCenterPoint(c));
    }

    /**
     * Test of setCenterPoint method, of class WindowUtils.
     */
    @Test
    public void setCenterPoint() {
        System.out.println("setCenterPoint");
        Component c = new JLabel("foobar");
        c.setSize(150, 100);
        Point expect = new Point(75, 100);
        WindowUtils.setCenterPoint(c, new Point(150, 150));
        assertEquals(expect, c.getLocation());
    }

    /**
     * Test of adjustCenterPoint method, of class WindowUtils.
     */
    @Test
    public void adjustCenterPoint() {
        System.out.println("adjustCenterPoint");
        Component c = new JLabel("foobar");
        c.setBounds(100, 100, 100, 100);
        WindowUtils.adjustCenterPoint(c, new Point(60, 70));
        assertEquals(new Point(110, 120), c.getLocation());
    }
}