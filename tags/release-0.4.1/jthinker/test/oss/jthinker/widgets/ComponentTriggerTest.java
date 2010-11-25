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

import java.awt.Component;
import java.awt.Point;
import javax.swing.JLabel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import oss.jthinker.util.TriggerChangeCollector;
import static org.junit.Assert.*;

/**
 * Unit-tests for ComponentTrigger class.
 * 
 * @author iappel
 */
public class ComponentTriggerTest {

    public ComponentTriggerTest() {
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
     * Test of updateLocation method, of class ComponentTrigger.
     */
    @Test
    public void updateLocation() {
        Component compo = new JLabel("foobar");
        assertEquals(0, compo.getComponentListeners().length);        
        ComponentTrigger loc = new ComponentTrigger(compo);
        loc.getState();
        assertEquals(1, compo.getComponentListeners().length);
        compo.setBounds(100, 100, 100, 100);

        loc.updateLocation();
        
        assertEquals(new Point(150, 150), loc.getState());
    }

    /**
     * Test on attaching trigger to component.
     */
    @Test
    public void attaching() {
        Component compo = new JLabel("foobar");
        assertEquals(0, compo.getComponentListeners().length);        
        ComponentTrigger loc = new ComponentTrigger(compo);
        assertEquals(1, compo.getComponentListeners().length);
        compo.setBounds(100, 100, 100, 100);

        try {
            Thread.sleep(500);
        } catch (Throwable t) {
            fail(t.toString());
        }
        
        assertEquals(new Point(150, 150), loc.getState());
    }

    /**
     * Test of constructor initializations.
     */
    @Test
    public void creation() {
        Component compo = new JLabel("foobar");
        compo.setBounds(100, 100, 100, 100);
        ComponentTrigger loc = new ComponentTrigger(compo);
        assertEquals(new Point(150, 150), loc.getState());
    }

    /**
     * Checks that peer component move correctly generates state update.
     */
    @Test
    public void consumeContent() {
        TriggerChangeCollector<Point> list = new TriggerChangeCollector<Point>();
        Component compo = new JLabel("foobar");
        ComponentTrigger loc = new ComponentTrigger(compo);
        loc.addStateConsumer(list);
        compo.setBounds(100, 100, 100, 100);
        try {
            Thread.sleep(500);
        } catch (Throwable t) {
            fail(t.toString());
        }
        assertEquals(1, list.size());
        assertEquals(new Point(150, 150), list.get(0));
    }
    
    /**
     * Checks the inability to make ComponentTrigger with
     * null argument in constructor.
     */
    @Test
    public void nullArgumentInConstructor() {
        try {
            ComponentTrigger ct = new ComponentTrigger(null);
            fail("IllegalArgumentException must be thrown");
        } catch (IllegalArgumentException e) {
            
        }
    }

    /**
     * Checks that peer component move correctly updates
     * trigger state.
     */
    @Test
    public void componentMove() {
        JLabel label = new JLabel("foobar");
        ComponentTrigger ct = new ComponentTrigger(label);
        try {
            Thread.sleep(500);
        } catch (Throwable t) {
        }
        assertEquals(GeometryUtils.computeCenterPoint(label), ct.getState());
        label.setLocation(100, 100);
        try {
            Thread.sleep(500);
        } catch (Throwable t) {
        }        
        assertEquals(GeometryUtils.computeCenterPoint(label), ct.getState());
    }
}