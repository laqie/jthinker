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
import java.util.ArrayList;
import java.util.List;
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
     * Test of working with non-ASCII characters.
     */
    @Test
    public void russianQuotesTest() {
        System.out.println("russianText");
        JNodeSpec nodeSpec = new JNodeSpec(BorderType.ELLIPSE, false,
                "Пример \"текста\" на русском", new Point(0,0), Color.WHITE, "", null);
        DiagramSpec diaSpec = new DiagramSpec(DiagramType.CONFLICT_RESOLUTION);
        diaSpec.nodeSpecs.add(nodeSpec);
        try {
            diaSpec.save(new File("c:\\russian.xml"));
        } catch (Throwable t) {
            t.printStackTrace();
            fail("No exceptions allowed");
        }
        DiagramSpec spec;
        try {
           spec = new FileDiagramSpec(new File("c:\\russian.xml"));
        } catch (Throwable t) {
            t.printStackTrace();
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
            DiagramSpec spec = FileDiagramSpec.parse(content);
            fail("Exception must be thrown");
        } catch (IllegalArgumentException npe) {
            
        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.toString());
        }
        
        content = "\t\n   <?xml version=\"1.0\"?><diagram type=\"CURRENT_REALITY_TREE\"> </diagram>";
        try {
            DiagramSpec spec = FileDiagramSpec.parse(content);
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
                "sample", new Point(0,0), Color.PINK, "sample", null);
        DiagramSpec diaSpec = new DiagramSpec(DiagramType.CONFLICT_RESOLUTION);
        diaSpec.nodeSpecs.add(nodeSpec);
        try {
            diaSpec.save(new File("c:\\sample.xml"));
        } catch (Throwable t) {
            fail("No exceptions allowed");
        }
        DiagramSpec spec;
        try {
           spec = new FileDiagramSpec(new File("c:\\sample.xml"));
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
     * Test of treating leg info and options.
     */
    @Test
    public void legOptionTest() {
        System.out.println("legTest");
        JNodeSpec node1 = new JNodeSpec(BorderType.ELLIPSE, false,
                "sample", new Point(0,0), Color.PINK, "sample", null);
        JNodeSpec node2 = new JNodeSpec(BorderType.ELLIPSE, false,
                "sample", new Point(0,0), Color.PINK, "sample", null);
        JNodeSpec node3 = new JNodeSpec(BorderType.ELLIPSE, false,
                "sample", new Point(0,0), Color.PINK, "sample", null);
        List<JNodeSpec> nodeList = new ArrayList<JNodeSpec>(3);
        nodeList.add(node1);
        nodeList.add(node2);
        nodeList.add(node3);
        JEdgeSpec edge1 = new JEdgeSpec(1, 2, true);
        JLegSpec leg1 = new JLegSpec(0, 0);
        List<JEdgeSpec> edgeList = new ArrayList<JEdgeSpec>(1);
        edgeList.add(edge1);
        List<JLegSpec> legList = new ArrayList<JLegSpec>(1);
        legList.add(leg1);
        DiagramSpec spec = new DiagramSpec(nodeList, edgeList, legList);
        try {
            spec.save(new File("c:\\test2.xml"));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        
        DiagramSpec spec2;
        try {
            spec2 = new FileDiagramSpec(new File("c:\\test2.xml"));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        assertEquals(spec.legSpecs, spec2.legSpecs);
        assertEquals(spec.options, spec2.options);
        assertEquals(spec, spec2);
    }
    
    @Test
    public void quotationTest() {
        System.out.println("quotationTest");
        JNodeSpec node = new JNodeSpec(BorderType.ROUND_RECT, false,
                "\"sample\"", new Point(0,0), Color.PINK, "sample \"sample\"", null);
        DiagramSpec spec = new DiagramSpec(DiagramType.CONFLICT_RESOLUTION),
                spec2;
        spec.nodeSpecs.add(node);
        try {
            spec.save(new File("c:\\quote.xml"));
        } catch (Throwable t) {
            t.printStackTrace();
            fail("No exceptions allowed");
        }
        try {
            spec2 = new FileDiagramSpec(new File("c:\\quote.xml"));
        } catch (Throwable t) {
            t.printStackTrace();
            fail("No exceptions allowed");
            return;
        }
        assertEquals(spec, spec2);
    }
}