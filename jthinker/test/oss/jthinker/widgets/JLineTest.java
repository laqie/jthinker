/*
 * Copyright (c) 2008, Ivan Appel <ivan.appel@gmail.com>
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution. 
 *
 * Neither the name of Ivan Appel nor the names of any other jThinker
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package oss.jthinker.widgets;

import java.awt.Color;
import java.awt.Point;
import javax.swing.JPanel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit-tests for JLine class.
 * 
 * @author iappel
 */
public class JLineTest {

    protected JPanel panel;
    protected JLine line;

    public JLineTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        panel = new JPanel();
        panel.setBounds(100, 100, 300, 300);

        line = new JLine(new Point(100, 100), new Point(200, 200), false);
        panel.add(line);
    }

    @After
    public void tearDown() {
    }

    public static void sleep() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException t) {

        }
    }

    /**
     * Test of setEndA method, of class JLine.
     */
    @Test
    public void setEndA() {
        System.out.println("setEndA");

        Point p = new Point(100, 100);

        line.setEndA(p);

        assertEquals(p.x - 5, line.getX());
        assertEquals(p.y - 5, line.getY());

        p = new Point(250, 250);

        line.setEndA(p);

        assertEquals(p.x - 200 + 10, line.getWidth());
        assertEquals(p.y - 200 + 10, line.getHeight());
    }

    /**
     * Test of setEndZ method, of class JLine.
     */
    @Test
    public void setEndZ() {
        System.out.println("setEndZ");

        Point p = new Point(50, 50);

        line.setEndZ(p);

        assertEquals(p.x - 5, line.getX());
        assertEquals(p.y - 5, line.getY());

        p = new Point(200, 200);

        line.setEndZ(p);

        assertEquals(p.x - 100 + 10, line.getWidth());
        assertEquals(p.y - 100 + 10, line.getHeight());
    }

    /**
     * Test of getEndA method, of class JLine.
     */
    @Test
    public void getEndA() {
        System.out.println("getEndA");

        assertEquals(new Point(100, 100), line.getEndA());

        Point p = new Point(0, 300);
        line.setEndA(p);

        assertEquals(p, line.getEndA());
    }

    /**
     * Test of getEndZ method, of class JLine.
     */
    @Test
    public void getEndZ() {
        System.out.println("getEndZ");

        assertEquals(new Point(200, 200), line.getEndZ());

        Point p = new Point(0, 300);
        line.setEndZ(p);

        assertEquals(p, line.getEndZ());
    }

    /**
     * Test of setEnds method, of class JLine.
     */
    @Test
    public void setEnds() {
        System.out.println("setEnds");
        Point a = new Point(100, 100);
        Point z = new Point(200, 100);
        line.setEnds(a, z);

        assertEquals(a, line.getEndA());
        assertEquals(z, line.getEndZ());
    }

    /**
     * Test of distanceToInnerPoint method, of class JLine.
     */
    @Test
    public void distanceToInnerPoint() {
        System.out.println("distanceToInnerPoint");
        
        double d = line.distanceToPoint(new Point(0, 100));
        assertTrue(Math.abs(d - Math.sqrt(5000)) < 0.0001);
    }

    /**
     * Test of distanceToPoint method, of class JLine.
     */
    @Test
    public void distanceToPoint() {
        System.out.println("distanceToPoint");
        
        double d = line.distanceToPoint(new Point(100, 200));
        assertTrue(Math.abs(d - Math.sqrt(5000)) < 0.0001);
    }

    /**
     * Test of correct initial color.
     */
    @Test
    public void colorTest() {
        System.out.println("colorTest");
        
        assertEquals(WindowUtils.getDefaultForeground(), line.getForeground());
    }
}
