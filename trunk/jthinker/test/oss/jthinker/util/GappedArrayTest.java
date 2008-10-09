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

import java.util.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit-tests for GappedArrayTest class.
 * 
 * @author iappel
 */
public class GappedArrayTest {
    private GappedArray<String> testEntry;

    public GappedArrayTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        testEntry = new GappedArray<String>();
        testEntry.add("foo");
        testEntry.add("bar");
        testEntry.remove("foo");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of get method, of class GappedArrayTest.
     */
    @Test
    public void get() {
        System.out.println("get");
        assertNull(testEntry.get(0));
        assertEquals("bar", testEntry.get(1));
        try {
            testEntry.get(2);
            fail("exception must be thrown");
        } catch (IndexOutOfBoundsException a) {
        }
    }

    /**
     * Test of locate method, of class GappedArrayTest.
     */
    @Test
    public void locate() {
        System.out.println("locate");
        assertEquals(-1, testEntry.locate("foo"));
        assertEquals(1, testEntry.locate("bar"));
        assertEquals(0, testEntry.locate(null));
    }

    /**
     * Test of locateGap method, of class GappedArrayTest.
     */
    @Test
    public void locateGap() {
        System.out.println("locateGap");
        assertEquals(0, testEntry.locateGap());
        testEntry.add("baz");
        assertEquals(-1, testEntry.locateGap());
    }

    /**
     * Test of add method, of class GappedArrayTest.
     */
    @Test
    public void add() {
        System.out.println("add");
        testEntry.add("foo");
        assertEquals("foo", testEntry.get(0));
        testEntry.add("baz");
        assertEquals("bar", testEntry.get(1));
        assertEquals("baz", testEntry.get(2));
    }

    /**
     * Test of remove method, of class GappedArrayTest.
     */
    @Test
    public void remove() {
        System.out.println("remove");
        testEntry.add("foo");
        testEntry.remove("bar");
    }

    /**
     * Test of getContent method, of class GappedArrayTest.
     */
    @Test
    public void getContent() {
        System.out.println("getContent");
        testEntry.add("foo");
        testEntry.add("baz");
        Vector<String> entry = testEntry.getContent();
        assertEquals(3, entry.size());
    }

    /**
     * Test of iterator method, of class GappedArrayTest.
     */
    @Test
    public void iterator() {
        System.out.println("iterator");
        testEntry.add("baz");
        testEntry.add("foo");
        String ret = "";
        for (String s : testEntry) {
            ret += s+"-";
        }
        assertEquals("baz-bar-foo-", ret);
    }

    /**
     * Test on clearing array.
     */
    @Test
    public void clearTest() {
        System.out.println("clearTest");
        testEntry.remove("bar");
        assertEquals(0, testEntry.getContent().size());
    }
}
