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

import java.awt.Point;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import oss.jthinker.util.QuadripoleTrigger;

import static org.junit.Assert.*;

/**
 * Unit-tests for JNodeAdjuster class.
 * 
 * @author iappel
 */
public class JNodeAdjusterTest {
    private JNodeHost stub;
    
    public JNodeAdjusterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        stub = new JNodeHost_Mock();
    }

    @After
    public void tearDown() {
    }

    /**
     * Tests correct calculation of line between two elliptic-border nodes
     */
    @Test
    public void twoEllipses() {
        System.out.println("twoEllipses");
        JNodeSpec ellipseSpec = 
            new JNodeSpec(BorderType.ELLIPSE, true, "        ", new Point(100, 100));

        JNode nodeA = new JNode(stub, ellipseSpec);
        JNode nodeB = new JNode(stub, ellipseSpec);

        QuadripoleTrigger<Point> trigger = JNodeAdjuster.makeTrigger(nodeA, nodeB);

        ComponentTrigger checker = new ComponentTrigger(nodeA);
        
        assertEquals(GeometryUtils.computeCenterPoint(nodeA),
                trigger.getLeftInput().getState());
        
        nodeA.setLocation(100, 100);
        nodeB.setLocation(500, 100);
        try {
            Thread.sleep(500);
        } catch (Throwable t) {
        }
        Point p1 = GeometryUtils.computeCenterPoint(nodeA);
        assertEquals(p1, checker.getState());
        assertEquals(p1, trigger.getLeftInput().getState());
        p1.translate(nodeA.getWidth() / 2, 0);
        System.out.println(nodeA.getBounds());
        System.out.println(GeometryUtils.computeCenterPoint(nodeB));
        assertEquals(p1, trigger.getLeftOutput().getState());
    }

    /**
     * Tests correct calculation of line between ellipse and rectangle.
     */
    @Test
    public void ellipseAndRectangle() {
        System.out.println("ellipseAndRectangle");
        JNodeSpec ellipseSpec = new JNodeSpec(BorderType.ELLIPSE,
                true, "        ", new Point(100, 100));
        JNodeSpec rectSpec = new JNodeSpec(BorderType.ROUND_RECT, true,
                "foobar", new Point(200, 300));
        
        JNode nodeA = new JNode(stub, ellipseSpec);
        JNode nodeB = new JNode(stub, rectSpec);

        QuadripoleTrigger<Point> trigger = JNodeAdjuster.makeTrigger(nodeA, nodeB);

        ComponentTrigger checker = new ComponentTrigger(nodeA);
        
        assertEquals(GeometryUtils.computeCenterPoint(nodeA),
                trigger.getLeftInput().getState());
        
        nodeA.setLocation(100, 100);
        nodeB.setLocation(500, 110);
        try {
            Thread.sleep(500);
        } catch (Throwable t) {
        }
        Point p1 = GeometryUtils.computeCenterPoint(nodeA);
        assertEquals(p1, checker.getState());
        assertEquals(p1, trigger.getLeftInput().getState());
        p1.translate(nodeA.getWidth() / 2, 0);
        assertEquals(p1, trigger.getLeftOutput().getState());
        p1 = GeometryUtils.computeCenterPoint(nodeB);
        p1.translate(- nodeB.getWidth() / 2, 0);
        System.out.println(nodeB.getBounds());
        System.out.println(GeometryUtils.computeCenterPoint(nodeA));

        assertEquals(p1, trigger.getRightOutput().getState());
    }
    
    /**
     * Tests correct calculation of line between ellipse and rectangle.
     */
    @Test
    public void twoRectangles() {
        System.out.println("twoRectangles");
        JNodeSpec rectSpec = new JNodeSpec(BorderType.ROUND_RECT,
                true, "        ", new Point(100, 100));
        JNode nodeA = new JNode(stub, rectSpec);
        JNode nodeB = new JNode(stub, rectSpec);
        
        assertNotSame(0, nodeA.getWidth());
        
        nodeA.setLocation(100, 100);
        nodeB.setLocation(500, 100);
        
        QuadripoleTrigger<Point> trigger = JNodeAdjuster.makeTrigger(nodeA, nodeB);
        
        Point p1 = trigger.getLeftInput().getState();
        Point p2 = GeometryUtils.computeCenterPoint(nodeA);
        assertEquals(p1, p2);
        
        p1 = trigger.getRightInput().getState();
        p2 = GeometryUtils.computeCenterPoint(nodeB);
        assertEquals(p1, p2);
        
        p1 = trigger.getLeftOutput().getState();
        p2 = trigger.getLeftInput().getState();
        p2.translate(nodeA.getWidth() / 2, 0);
        assertEquals(p1, p2);
        
        p1 = trigger.getRightOutput().getState();
        p2 = trigger.getRightInput().getState();
        p2.translate(- nodeB.getWidth() / 2, 0);
        assertEquals(p1, p2);
    }
}