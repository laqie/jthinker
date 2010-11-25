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

package oss.jthinker.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit-test for TriggerEvent class
 * 
 * @author iappel
 */
public class TriggerEventTest {

    public TriggerEventTest() {
    }

    private TriggerEvent<String> event1, event2, event3;
    private Trigger<String> source;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        source = new Trigger<String>("foobar");
        event1 = new TriggerEvent<String>(source);
        event2 = new TriggerEvent<String>(source, "bazbaz");
        event3 = new TriggerEvent<String>(source, null);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getState method, of class TriggerEvent.
     */
    @Test
    public void getState() {
        System.out.println("getState");
        assertEquals("foobar", event1.getState());
        assertEquals("bazbaz", event2.getState());
        assertEquals("foobar", event3.getState());
    }

    /**
     * Test of getSource method, of class TriggerEvent.
     */
    @Test
    public void getSource() {
        System.out.println("getSource");
        assertEquals(source, event1.getSource());
        assertEquals(source, event2.getSource());
        assertEquals(source, event3.getSource());
    }

    /**
     * Test of checks in constructor.
     */
    @Test
    public void crashTest() {
        System.out.println("crashTest");
        try {
            event1 = new TriggerEvent<String>(null);
            fail ("Illegal argument exception must be thrown");
        } catch (IllegalArgumentException e) {
            
        }
        
        try {
            event2 = new TriggerEvent<String>(null, "foobar");
            fail ("Illegal argument exception must be thrown");
        } catch (IllegalArgumentException e) {
            
        }
    }
}