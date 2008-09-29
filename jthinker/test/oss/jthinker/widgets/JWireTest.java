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
import javax.swing.JButton;
import javax.swing.JLabel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import oss.jthinker.util.Trigger;
import static org.junit.Assert.*;

/**
 * Unit-tests for JWire class.
 * 
 * @author iappel
 */
public class JWireTest {

    public JWireTest() {
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
     * Test of initialization of instances.
     */
    @Test
    public void testInit() {
        System.out.println("testInit");
        Trigger<Point> p1 = new Trigger<Point>(new Point(50, 50)),
                p2 = new Trigger<Point>(new Point(150, 150));

        JWire edge = new JWire(p1, p2, false);
        assertEquals(new Point(50, 50), edge.getEndA());
        assertEquals(new Point(150, 150), edge.getEndZ());
    }

    /**
     * Test of correct response to related components' state
     * changes.
     */
    @Test
    public void stateChangedComponent() {
        System.out.println("stateChanged");
        Component c1 = new JLabel(),
                c2 = new JButton();

        JWire edge = new JWire(new ComponentTrigger(c1), 
                new ComponentTrigger(c2), false);
        c1.setBounds(0, 0, 100, 100);
        c2.setBounds(100, 100, 100, 100);

        try {
            Thread.sleep(500);
        } catch (Throwable t) {
            fail(t.toString());
        }
        
        assertEquals(new Point(150, 150), edge.getEndZ());
        assertEquals(new Point(50, 50), edge.getEndA());
    }
}
