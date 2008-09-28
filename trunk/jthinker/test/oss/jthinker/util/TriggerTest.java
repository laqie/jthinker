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
 * Pack of tests for {@see Trigger}
 * 
 * @author iappel
 */
public class TriggerTest {
    private Trigger<String> instance1, instance2;
    private TriggerChangeCollector<String> list;
    
    public TriggerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance1 = new Trigger<String>("foobar");
        instance2 = new Trigger<String>();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getState method, of class Trigger.
     */
    @Test
    public void getState() {
        System.out.println("getState");
        assertEquals("foobar", instance1.getState());
        try {
            instance2.getState();
            fail("IllegalStateException must be thrown");
        } catch (IllegalStateException e) {
            
        }
    }

    /**
     * Test of setState method, of class Trigger.
     */
    @Test
    public void setState() {
        System.out.println("setState");
        try {
            instance1.setState(null);
            fail("IllegalArgumentException must be thrown");
        } catch (IllegalArgumentException e) {
            
        }
        
        instance1.setState("barfoo");
        assertEquals("barfoo", instance1.getState());
    }

    /**
     * Test of unsetState method, of class Trigger.
     */
    @Test
    public void unsetLocation() {
        System.out.println("unsetLocation");
        instance1.unsetState();
        try {
            instance1.getState();
            fail("IllegalStateException must be thrown");
        } catch (IllegalStateException i) {
        }
    }
    
    /**
     * Test of addStateConsumer method, of class Trigger.
     */
    @Test
    public void addStateConsumer() {
        System.out.println("addStateConsumer");
        try {
            instance1.addStateConsumer(null);
            fail("IllegalArgumentException must be thrown");
        } catch (IllegalArgumentException i) {
            
        }
        
        TriggerChangeCollector<String> cons = new TriggerChangeCollector<String>();
        
        instance1.setState("foo");
        instance1.addStateConsumer(cons);
        instance1.setState("bar");
        assertEquals(1, cons.size());
        assertEquals("bar", cons.get(0));
    }

    /**
     * Test of removeStateConsumer method, of class Trigger.
     */
    @Test
    public void removeStateConsumer() {
        System.out.println("removeStateConsumer");
        instance1.removeStateConsumer(null);
        
        TriggerChangeCollector<String> cons =
                new TriggerChangeCollector<String>();
        
        instance1.setState("foo");
        instance1.addStateConsumer(cons);
        assertEquals(1, instance1.getConsumerCount());
        instance1.setState("bar");
        assertEquals(1, cons.size());
        instance1.removeStateConsumer(cons);
        assertEquals(1, cons.size());
        assertEquals(0, instance1.getConsumerCount());
        instance1.setState("baz");
        assertEquals(1, cons.size());
    }

    /**
     * Test of fireUpdate method, of class Trigger.
     */
    @Test
    public void fireUpdate() {
        System.out.println("fireUpdate");
        TriggerChangeCollector<String> cons = new TriggerChangeCollector<String>();
        instance1.setState("foo");
        instance1.addStateConsumer(cons);
        for (int i=0;i<5;i++) {
            instance1.fireUpdate();
        }
        assertEquals(5, cons.size());
    }

    /**
     * Test of getConsumerCount method, of class Trigger.
     */
    @Test
    public void getConsumerCount() {
        System.out.println("getConsumerCount");
        instance1.addStateConsumer(new TriggerChangeCollector<String>());
        instance1.addStateConsumer(new TriggerChangeCollector<String>());
        instance1.addStateConsumer(new TriggerChangeCollector<String>());
        assertEquals(3, instance1.getConsumerCount());
    }

    /**
     * Test of switchOff method, of class Trigger.
     */
    @Test
    public void switchOff() {
        System.out.println("disable");
        list = new TriggerChangeCollector<String>();
        instance1.addStateConsumer(list);
        instance1.setState("foobaz");
        assertEquals(1, list.size());
        instance1.setSwitched(false);
        instance1.setState("bazfoo");
        assertEquals(1, list.size());
    }

    /**
     * Test of switchOn method, of class Trigger.
     */
    @Test
    public void switchOn() {
        System.out.println("enable");
                
        switchOff();
        list.clear();
        instance1.setSwitched(true);
        instance1.setState("foobar");
        assertEquals(1, list.size());
        
    }

    /**
     * Test of isSwitched method, of class Trigger.
     */
    @Test
    public void isSwitched() {
        System.out.println("isEnabled");
        instance1.setSwitched(true);
        assertTrue(instance1.isSwitched());
        instance1.setSwitched(false);
        assertFalse(instance1.isSwitched());
        
    }
}