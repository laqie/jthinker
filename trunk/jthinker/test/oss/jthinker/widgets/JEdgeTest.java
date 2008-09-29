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
import static org.junit.Assert.*;

/**
 * Unit-tests for JEdge class.
 * 
 * @author iappel
 */
public class JEdgeTest {
    public JEdgeTest() {
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
     * Test on correct creation of JEdge.
     */
    @Test
    public void initTest() {
        System.out.println("initTest");
        
        JNodeSpec rectSpec = new JNodeSpec(BorderType.ROUND_RECT,
                true, "        ", new Point(100, 100));
        
        JNodeHost stub = new JNodeHost() {
            public void deleteJNode(JNode node) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void dispatchJNodeMove() {
            }

            public void editJNodeContent(JNode node) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void endLinking(JNode end) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void startLinking(JNode start) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        
        JNode nodeA = new JNode(stub, rectSpec);
        JNode nodeB = new JNode(stub, rectSpec);
        
        assertNotSame(0, nodeA.getWidth());
        assertNotSame(0, nodeB.getWidth());
        
        nodeA.setLocation(100, 100);
        nodeB.setLocation(500, 100);

        JEdge edge = new JEdge(nodeA, nodeB, null);

        Point p1 = edge.getEndA();
        Point p2 = GeometryUtils.computeCenterPoint(nodeA);
        
        assertNotSame(p1, p2);
        p2.translate(nodeA.getWidth() / 2, 0);
        assertEquals(p1, p2);

        p1 = edge.getEndZ();
        p2 = GeometryUtils.computeCenterPoint(nodeB);
        assertNotSame(p1, p2);
        p2.translate(-nodeA.getWidth() / 2, 0);
        assertEquals(p1, p2);        
        
        nodeA.setLocation(0, 100);
        try { Thread.sleep(500); } catch (Throwable t) {}
        p1 = edge.getEndA();
        p2 = GeometryUtils.computeCenterPoint(nodeA);
        
        assertNotSame(p1, p2);
        p2.translate(nodeA.getWidth() / 2, 0);
        assertEquals(p1, p2);
        
        p1 = edge.getEndZ();
        p2 = GeometryUtils.computeCenterPoint(nodeB);
        assertNotSame(p1, p2);
        p2.translate(-nodeA.getWidth() / 2, 0);
        assertEquals(p1, p2);
    }
}