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

package oss.jthinker.graphs;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit-tests for XMLUtils class.
 *
 * @author iappel
 */
public class OverlapMonitorTest {

    public OverlapMonitorTest() {
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
     * Test of newNodePoint method, of class OverlapMonitor.
     */
    @Test
    public void newNodePoint() {
        System.out.println("newNodePoint");
        OverlapMonitor instance = new OverlapMonitor();
        
        Dimension size = new Dimension(50,50);
        
        Point point = instance.newNodePoint(100, size);
        assertEquals(new Point(25,25), point);

        instance.add(new Rectangle(0,0,50,50));
        point = instance.newNodePoint(100, size);
        assertEquals(new Point(75,25), point);
        
        instance.add(new Rectangle(50,0,50,50));
        point = instance.newNodePoint(100, size);
        assertEquals(new Point(25, 75), point);
    }

    /**
     * Test of overlapsSomething method, of class OverlapMonitor.
     */
    @Test
    public void overlapsSomething() {
        System.out.println("overlapsSomething");
        OverlapMonitor instance = new OverlapMonitor();
        
        Rectangle rect = new Rectangle(0,0,50,50);
        assertFalse(instance.overlapsSomething(rect));
        instance.add(rect);
        assertTrue(instance.overlapsSomething(rect));
        
        rect = new Rectangle(50,0,50,50);
        assertFalse(instance.overlapsSomething(rect));
        instance.add(rect);
        assertTrue(instance.overlapsSomething(rect));
        
        rect = new Rectangle(25,0,50,50);
        assertTrue(instance.overlapsSomething(rect));
        
        rect = new Rectangle(0,50,50,50);
        assertFalse(instance.overlapsSomething(rect));
        instance.add(rect);
        assertTrue(instance.overlapsSomething(rect));

    }
}