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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit-tests for JNodeSpec class.
 * 
 * @author iappel
 */
public class JNodeSpecTest {

    public JNodeSpecTest() {
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
     * Tests how equals() method treats border type and content text.
     */
    @Test
    public void equalityTest() {
        System.out.println("equalityTest");
        JNodeSpec spec = new JNodeSpec(BorderType.ELLIPSE, true, "sample",
                new Point(0,0), Color.WHITE, "sample");
        JNodeSpec spec2 = new JNodeSpec(BorderType.ROUND_RECT, true, "foobar",
                new Point(0,0), Color.WHITE, "sample");
        assertNotSame(spec, spec2);
    }

    /**
     * Tests how equals() method treats color setting and comment text.
     */
    @Test
    public void equalityTest2() {
        System.out.println("equalityTest2");
        JNodeSpec spec = new JNodeSpec(BorderType.ROUND_RECT, true, "sample",
                new Point(0,0), Color.WHITE, "sample");
        JNodeSpec spec2 = new JNodeSpec(BorderType.ROUND_RECT, true, "sample",
                new Point(0,0), Color.PINK, "sample");
        assertFalse(spec.equals(spec2));
        spec2 = new JNodeSpec(BorderType.ROUND_RECT, true, "sample",
                new Point(0,0), Color.WHITE, "foobar");
        assertFalse(spec.equals(spec2));
        spec2 = new JNodeSpec(BorderType.ROUND_RECT, true, "sample",
                new Point(0,0), Color.WHITE, "sample");
        assertEquals(spec, spec2);
    }    
}