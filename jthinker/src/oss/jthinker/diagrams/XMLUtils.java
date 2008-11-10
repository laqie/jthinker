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

import com.sun.org.apache.xerces.internal.impl.xs.dom.DOMParser;
import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import oss.jthinker.tocmodel.DiagramType;
import oss.jthinker.widgets.BorderType;
import oss.jthinker.widgets.JNodeSpec;

/**
 * Pack of XML utility functions for saving and restoring diagrams.
 * 
 * @author iappel
 */
public class XMLUtils {
    private static final Logger logger = Logger.getAnonymousLogger();
    
    protected static String toXML(Point p) {
        StringBuilder sb = new StringBuilder();
        sb.append("<center x = \""+p.x+"\" y = \""+p.y+"\" />");
        return sb.toString();
    }

    protected static Point toPoint(Node n) throws SAXException {
        if (!n.getNodeName().equals("center")) {
            throw new SAXException();
        }
        NamedNodeMap map = n.getAttributes();
        int x = Integer.parseInt(map.getNamedItem("x").getNodeValue());
        int y = Integer.parseInt(map.getNamedItem("y").getNodeValue());
        return new Point(x,y);
    }

    protected static String toXML(Color c) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<color name=\"");
        if (c.equals(Color.WHITE)) {
            sb.append("white");
        } else if (c.equals(Color.CYAN)) {
            sb.append("cyan");
        } else if (c.equals(Color.YELLOW)) {
            sb.append("yellow");
        } else if (c.equals(Color.PINK)) {
            sb.append("pink");
        } else if (c.equals(Color.GREEN)) {
            sb.append("green");
        } else {
            sb.append("white");
        }
        sb.append("\" />");
        return sb.toString();
    }
    
    protected static Color toColor(Node n) throws SAXException {
        if (!n.getNodeName().equals("color")) {
            throw new SAXException();
        }
        NamedNodeMap map = n.getAttributes();
        String s = map.getNamedItem("name").getNodeValue();
        if (s.equals("white")) {
            return Color.WHITE;
        } else if (s.equals("green")) {
            return Color.GREEN;
        } else if (s.equals("pink")) {
            return Color.PINK;
        } else if (s.equals("cyan")) {
            return Color.CYAN;
        } else if (s.equals("yellow")) {
            return Color.YELLOW;
        } else {
            return Color.WHITE;
        }
    }
    
    protected static String toXML(JEdgeSpec edgeSpec) {
        int start = edgeSpec.idxA, end = edgeSpec.idxZ;
        StringBuilder sb = new StringBuilder();
        sb.append("<edge start = \""+start+"\" end = \""+end+"\" />\n");
        return sb.toString();
    }

    protected static JEdgeSpec toEdgeSpec(Node n) throws SAXException {
        if (!n.getNodeName().equals("edge")) {
            throw new SAXException();
        }
        NamedNodeMap map = n.getAttributes();
        int a = Integer.parseInt(map.getNamedItem("start").getNodeValue());
        int z = Integer.parseInt(map.getNamedItem("end").getNodeValue());
        return new JEdgeSpec(a, z);
    }
    
    protected static String toXML(JLegSpec legSpec) {
        int start = legSpec.idxA, end = legSpec.idxZ;
        StringBuilder sb = new StringBuilder();
        sb.append("<leg start = \""+start+"\" end = \""+end+"\"\n");
        return sb.toString();
    }
    
    protected static JLegSpec toLegSpec(Node n) throws SAXException {
        if (!n.getNodeName().equals("leg")) {
            throw new SAXException();
        }
        NamedNodeMap map = n.getAttributes();
        int a = Integer.parseInt(map.getNamedItem("start").getNodeValue());
        int z = Integer.parseInt(map.getNamedItem("end").getNodeValue());
        return new JLegSpec(a, z);
    }    
    
    protected static String toXML(JNodeSpec nodeSpec) {
        BorderType type = nodeSpec.getBorderType();
        String content = nodeSpec.getContent();
        String comment = nodeSpec.getComment();
        Color color = nodeSpec.getBackground();
        Point point = nodeSpec.getSlideCenter();
        boolean b = nodeSpec.isEditable();
        StringBuilder sb = new StringBuilder();
        sb.append("<node type = \"" + type + "\" editable = \""+b+"\">");
        sb.append(toXML(point));
        sb.append("\n<content text = \""+content+"\" />\n");
        sb.append(toXML(color));
        sb.append("\n<comment text = \""+comment+"\" />\n");
        sb.append("</node>\n");
        return sb.toString();
    }

    protected static JNodeSpec toNodeSpec(Node node) throws SAXException {
        if (!node.getNodeName().equals("node")) {
            throw new SAXException(node.getNodeName());
        }
        
        NamedNodeMap map = node.getAttributes();
        String typeStr = map.getNamedItem("type").getNodeValue();
        BorderType type = BorderType.valueOf(typeStr);
        String editStr = map.getNamedItem("editable").getNodeValue();
        boolean edit = Boolean.valueOf(editStr);
        
        NodeList children = node.getChildNodes();
        
        String content = null;
        Point center = null;
        Color color = null;
        String comment = null;
        
        for (int i=0;i<children.getLength();i++) {
            Node n = children.item(i);
            if (n.getNodeName().equals("content")) {
                content = n.getAttributes().getNamedItem("text").getNodeValue();
            } else if (n.getNodeName().equals("color")) {
                color = toColor(n);
            } else if (n.getNodeName().equals("comment")) {
                comment = n.getAttributes().getNamedItem("text").getNodeValue();
            } else if (n.getNodeName().equals("center")) {
                center = toPoint(n);
            }
        }
        
        return new JNodeSpec(type, edit, content, center, color, comment);
    }
    
    protected static String nodesToXML(List<JNodeSpec> specs) {
        StringBuilder sb = new StringBuilder();
        for (JNodeSpec spec : specs) {
            sb.append(toXML(spec));
        }
        return sb.toString();
    }

    protected static DiagramSpec toDiagramSpec(Node node) throws SAXException {
        if (!node.getNodeName().equals("diagram")) {
            throw new SAXException(node.getNodeName());
        }
        String typeStr = node.getAttributes().getNamedItem("type").getNodeValue();
        DiagramType type = DiagramType.valueOf(typeStr);
        List<JEdgeSpec> edges = new LinkedList<JEdgeSpec>();
        List<JNodeSpec> nodes = new LinkedList<JNodeSpec>();
        List<JLegSpec> legs = new LinkedList<JLegSpec>();
        
        NodeList content = node.getChildNodes();
        
        for (int i=0;i<content.getLength();i++) {
            Node n = content.item(i);
            String name = n.getNodeName();
            if (name.equals("node")) {
                nodes.add(toNodeSpec(n));
            } else if (name.equals("edge")) {
                edges.add(toEdgeSpec(n));
            } else if (name.equals("leg")) {
                legs.add(toLegSpec(n));
            }
        }
        if (type.equals(DiagramType.TRANSFORM_PLAN)) {
            return new DiagramSpec(nodes, edges, legs);
        } else {
            return new DiagramSpec(nodes, edges, type);
        }
    }
    
    protected static String edgesToXML(List<JEdgeSpec> specs) {
        StringBuilder sb = new StringBuilder();
        for (JEdgeSpec spec : specs) {
            sb.append(toXML(spec));
        }
        return sb.toString();
    }
    
    protected static String toXML(DiagramSpec spec) {
        StringBuilder sb = new StringBuilder();
        sb.append("<diagram type = \""+spec.type+"\">\n");
        sb.append(nodesToXML(spec.nodeSpecs));
        sb.append(edgesToXML(spec.edgeSpecs));
        sb.append("</diagram>");
        return sb.toString();
    }

    /**
     * Saves a diagram specification into an XML file.
     * 
     * @param f file to use
     * @param spec specification to save
     * @throws FileNotFoundException if the file exists but is a directory
     *                   rather than a regular file, does not exist but cannot
     *                   be created, or cannot be opened for any other reason
     * {@see FileOutputStream}
     */
    public static void save(File f, DiagramSpec spec)
            throws FileNotFoundException {
        PrintStream printer;
        try {
            printer = new PrintStream(f, "UTF-8");
        } catch (UnsupportedEncodingException c) {
            logger.log(Level.WARNING, "Unable to write UTF-8 file");
            printer = new PrintStream(f);
        }
        printer.print("<?xml version=\"1.0\"?>\n");
        printer.print(toXML(spec));
    }

    /**
     * Loads a diagram specification from XML file.
     * 
     * @param f XML file
     * @return diagram specification
     * @throws SAXException on parsing errors
     * @throws IOException on I/O errors of loading a file
     */
    public static DiagramSpec load(File f) throws SAXException, IOException {
        FileInputStream stream = new FileInputStream(f);
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        char[] cData = new char[(int)f.length()];
        reader.read(cData);
        
        return parse(new String(cData));
    }
    
    /**
     * Loads a diagram specification from string buffer.
     * 
     * @param rawContent string buffer
     * @return diagram specification
     * @throws SAXException on parsing errors
     * @throws IOException on I/O errors of loading a file
     */
    public static DiagramSpec parse(String rawContent) throws SAXException, IOException {
        String content = rawContent.trim();
        DOMParser parser = new DOMParser();
        Reader reader = new StringReader(content);
        InputSource source = new InputSource(reader);
        parser.parse(source);
        return toDiagramSpec(parser.getDocument().getFirstChild());
    }
}
