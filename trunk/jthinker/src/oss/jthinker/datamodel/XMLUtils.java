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
package oss.jthinker.datamodel;

import java.awt.Color;
import java.awt.Point;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Pack of XML utility functions for saving and restoring diagrams.
 * 
 * @author iappel
 */
public class XMLUtils {
    public static Element toXML(Point p, Document document) {
        Element result = document.createElement("center");
        result.setAttribute("x", Integer.toString(p.x));
        result.setAttribute("y", Integer.toString(p.y));
        return result;
    }

    public static Point toPoint(Node n) {
        if (!n.getNodeName().equals("center")) {
            throw new IllegalArgumentException(n.getNodeName());
        }
        NamedNodeMap map = n.getAttributes();
        int x = Integer.parseInt(map.getNamedItem("x").getNodeValue());
        int y = Integer.parseInt(map.getNamedItem("y").getNodeValue());
        return new Point(x, y);
    }

    public static Element toXML(Color c, Document document) {
        Element result = document.createElement("color");
        
        String name;
        if (c.equals(Color.WHITE)) {
            name = "white";
        } else if (c.equals(Color.CYAN)) {
            name = "cyan";
        } else if (c.equals(Color.YELLOW)) {
            name = "yellow";
        } else if (c.equals(Color.PINK)) {
            name = "pink";
        } else if (c.equals(Color.GREEN)) {
            name = "green";
        } else {
            name = "white";
        }
        
        result.setAttribute("name", name);
        
        return result;
    }

    public static Color toColor(Node n) {
        if (!n.getNodeName().equals("color")) {
            throw new IllegalArgumentException(n.getNodeName());
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
}
