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
import javax.swing.JFrame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit-tests for JNode class.
 * 
 * @author iappel
 */
public class JNodeTest {

    public JNodeTest() {
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
     * Tests of background color-related actions.
     */
    @Test
    public void backgroundColorTest() {
        JNodeSpec spec = new JNodeSpec(BorderType.ROUND_RECT, true, "sample",
                new Point(0,0), Color.GREEN, "sample", null);
        JNode node = new JNode(new JNodeHost_Mock(), spec);
        assertEquals(Color.GREEN, node.getColor());
        assertEquals(Color.GREEN, node.getBackground());
        assertEquals(Color.GREEN, node.getComponent(0).getBackground());
        JNodeSpec specRet = node.getNodeSpec();
        assertEquals(Color.GREEN, specRet.getBackground());
    }

    /**
     * Tests of comments-related actions.
     */
    @Test
    public void commentTest() {
        JNodeSpec spec = new JNodeSpec(BorderType.ROUND_RECT, true, "sample",
                new Point(0,0), Color.GREEN, "sample", null);
        JNode node = new JNode(new JNodeHost_Mock(), spec);
        assertEquals("sample", node.getComment());
        assertEquals("sample", node.getToolTipText());
        JNodeSpec specRet = node.getNodeSpec();
        assertEquals("sample", specRet.getComment());
    }    
    
    /**
     * Tests of hasContent() method
     */
    @Test
    public void hasContentTest() {
        JNodeSpec spec = new JNodeSpec(BorderType.ROUND_RECT, true, "sample",
                new Point(0,0), Color.WHITE, "sample", null);
        JNodeSpec spec2 = new JNodeSpec(BorderType.ROUND_RECT, true, "      ",
                new Point(0,0), Color.WHITE, "sample", null);
        
        JNodeHost_Mock stub = new JNodeHost_Mock();
        
        JNode node1 = new JNode(stub, spec);
        JNode node2 = new JNode(stub, spec2);
        
        assertTrue(node1.hasContent());
        assertFalse(node2.hasContent());
    }

    /**
     * Test for setContent() method.
     */
    @Test
    public void setContentTest() {
        JNodeSpec spec = new JNodeSpec(BorderType.ROUND_RECT, true, "      ",
                new Point(0,0), Color.WHITE, "sample", null);
        JNodeHost_Mock stub = new JNodeHost_Mock();

        JFrame frame = new JFrame();
        JNode node1 = new JNode(stub, spec);
        frame.add(node1);
        JLabelBundle label = (JLabelBundle)node1.getComponent(0);
        node1.enableNumbering(true);
        
        node1.setContent("  ");
        assertEquals("  ", label.getText());

        node1.setContent("foo");
        assertEquals("1. foo", label.getText());
    }
    
    /**
     * Test for correct resizing on enabling numbering.
     */
    @Test
    public void resizeTest() {
        JNodeSpec spec = new JNodeSpec(BorderType.ROUND_RECT, true, "foo",
                new Point(0,0), Color.WHITE, "sample", null);
        JNodeHost_Mock stub = new JNodeHost_Mock();

        JFrame frame = new JFrame();
        JNode node1 = new JNode(stub, spec);
        frame.add(node1);
        JLabelBundle label = (JLabelBundle)node1.getComponent(0);
        assertEquals("foo", label.getText());
        int before1 = node1.getWidth(),
            before2 = label.getWidth(),
            before3 = label.getPreferredSize().width;

        assertEquals(before2, before3);
        assertTrue(before1 > before2);
        node1.enableNumbering(true);
        
        assertEquals("1. foo", label.getText());
        
        assertNotSame(before3, label.getPreferredSize().width);
        
        assertEquals(label.getSize().width, label.getPreferredSize().width);
    }    
}