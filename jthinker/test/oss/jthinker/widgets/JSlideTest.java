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
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit-tests for JSlide class
 * 
 * @author iappel
 */
public class JSlideTest {

    private JSlide instance;
    
    public JSlideTest() {
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
     * Test of isSwitched method, of class JMouseEdge.
     */
    @Test
    public void isSwitched() {
        System.out.println("isSwitched");
        instance = new JSlide(new JLabel(),
                BorderBuilder.getInstance(BorderType.SHARP_RECT));
        assertFalse(instance.isSwitched());
        instance.setSwitched(true);
        assertTrue(instance.isSwitched());
        instance.setSwitched(false);
        assertFalse(instance.isSwitched());
    }

    /**
     * Test of switchOff method, of class JSlide.
     */
    @Test
    public void switchOff() {
        System.out.println("switchOff");
        isSwitched();
        instance.setSwitched(false);
        Border border = instance.getBorder();
        assertNotNull(border);
        assertTrue(border instanceof LineBorder);
        assertEquals(Color.BLACK, ((LineBorder)border).getLineColor());
    }

    /**
     * Test of switchOn method, of class JSlide.
     */
    @Test
    public void switchOn() {
        System.out.println("switchOn");
        isSwitched();
        instance.setSwitched(true);
        Border border = instance.getBorder();
        assertNotNull(border);
        assertTrue(border instanceof LineBorder);
        assertEquals(Color.RED, ((LineBorder)border).getLineColor());    
    }

    /**
     * Test of add method, of class JSlide.
     */
    @Test
    public void add() {
        System.out.println("add");
        JComponent comp1 = new JButton("foo"),
                comp2 = new JLabel("bar");
        JSlide inst = new JSlide(comp1, 
                BorderBuilder.getInstance(BorderType.ELLIPSE));
        assertEquals(1, inst.getComponentCount());
        assertEquals(comp1, inst.getComponent(0));
        inst.add(comp2);
        assertEquals(1, inst.getComponentCount());
        assertEquals(comp2, inst.getComponent(0));
    }

    /**
     * Test of remove method, of class JSlide.
     */
    @Test
    public void remove() {
        System.out.println("remove");
        JComponent comp1 = new JButton("foo"),
                comp2 = new JLabel("bar");
        JSlide inst = new JSlide(comp1,
                BorderBuilder.getInstance(BorderType.ELLIPSE));
        assertEquals(1, inst.getComponentCount());
        assertEquals(comp1, inst.getComponent(0));
        inst.remove(comp2);
        assertEquals(1, inst.getComponentCount());
        assertEquals(comp1, inst.getComponent(0));    
        inst.remove(comp1);
        assertEquals(0, inst.getComponentCount());
    }
}