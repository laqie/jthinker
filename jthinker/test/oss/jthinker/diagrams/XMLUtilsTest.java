/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oss.jthinker.diagrams;

import java.awt.Point;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author 1
 */
public class XMLUtilsTest {

    public XMLUtilsTest() {
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
     * Test of edgesToXML method, of class XMLUtils.
     */
    @Test
    public void pointToXML() {
        System.out.println("pointToXML");
        Point p = new Point(100, 200);
        assertEquals("<center x = \"100\" y = \"200\" />", XMLUtils.toXML(p));
    }

}