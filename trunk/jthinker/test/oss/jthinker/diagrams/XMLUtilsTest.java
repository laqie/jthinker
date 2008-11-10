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

package oss.jthinker.diagrams;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import oss.jthinker.tocmodel.DiagramType;
import oss.jthinker.widgets.BorderType;
import oss.jthinker.widgets.JNodeSpec;
import static org.junit.Assert.*;

/**
 * Unit-tests for XMLUtils class.
 * 
 * @author iappel
 */
public class XMLUtilsTest {

    public XMLUtilsTest() {
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
     * Test of edgesToXML method, of class XMLUtils.
     */
    @Test
    public void pointToXML() {
        System.out.println("pointToXML");
        Point p = new Point(100, 200);
        assertEquals("<center x = \"100\" y = \"200\" />", XMLUtils.toXML(p));
    }

    /**
     * Test of working with non-ASCII characters.
     */
    @Test
    public void russianTest() {
        System.out.println("russianText");
        JNodeSpec nodeSpec = new JNodeSpec(BorderType.ELLIPSE, false,
                "Пример текста на русском", new Point(0,0), Color.WHITE, "");
        DiagramSpec diaSpec = new DiagramSpec(DiagramType.CONFLICT_RESOLUTION);
        diaSpec.nodeSpecs.add(nodeSpec);
        try {
            XMLUtils.save(new File("c:\\russian.xml"), diaSpec);
        } catch (Throwable t) {
            fail("No exceptions allowed");
        }
        DiagramSpec spec;
        try {
           spec = XMLUtils.load(new File("c:\\russian.xml"));
        } catch (Throwable t) {
            fail("No exception allowed");
            return;
        }
        assertEquals(diaSpec.edgeSpecs, spec.edgeSpecs);
        assertEquals(diaSpec.nodeSpecs, spec.nodeSpecs);
        assertEquals(diaSpec.legSpecs, spec.legSpecs);
        assertEquals(diaSpec.type, spec.type);
        assertTrue(diaSpec.equals(spec));
        assertEquals(diaSpec, spec);
    }

    /**
     * Test on whitespaces at the beginning of the file.
     */
    @Test
    public void whitespaceTest() {
        System.out.println("whitespaceTest");
        String content = "\t\n   <?xml version=\"1.0\"?><diagram> </diagram>";
        try {
            DiagramSpec spec = XMLUtils.parse(content);
        } catch (NullPointerException npe) {
            
        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.toString());
        }
        
        content = "\t\n   <?xml version=\"1.0\"?><diagram type=\"CURRENT_REALITY_TREE\"> </diagram>";
        try {
            DiagramSpec spec = XMLUtils.parse(content);
            assertEquals(0, spec.nodeSpecs.size());
            assertEquals(DiagramType.CURRENT_REALITY_TREE, spec.type);
        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.toString());
        }
    }
    
    /**
     * Test of treating color and comment parametres.
     */
    @Test
    public void colorCommentsTest() {
        System.out.println("russianText");
        JNodeSpec nodeSpec = new JNodeSpec(BorderType.ELLIPSE, false,
                "sample", new Point(0,0), Color.PINK, "sample");
        DiagramSpec diaSpec = new DiagramSpec(DiagramType.CONFLICT_RESOLUTION);
        diaSpec.nodeSpecs.add(nodeSpec);
        try {
            XMLUtils.save(new File("c:\\sample.xml"), diaSpec);
        } catch (Throwable t) {
            fail("No exceptions allowed");
        }
        DiagramSpec spec;
        try {
           spec = XMLUtils.load(new File("c:\\sample.xml"));
        } catch (Throwable t) {
            fail("No exception allowed");
            return;
        }
        assertEquals(diaSpec.edgeSpecs, spec.edgeSpecs);
        assertEquals(diaSpec.nodeSpecs, spec.nodeSpecs);
        assertEquals(diaSpec.legSpecs, spec.legSpecs);
        assertEquals(diaSpec.type, spec.type);
        assertTrue(diaSpec.equals(spec));
        assertEquals(diaSpec, spec);
    }    
}