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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import oss.jthinker.graphs.OrderingLevel;
import oss.jthinker.util.XMLStored;

/**
 * Simple value holder for diagram's options.
 * 
 * @author iappel
 */
public class DiagramOptionSpec implements XMLStored {
    public boolean numbering;
    public OrderingLevel orderingLevel;

    /**
     * Creates a new instance of DiagramOptionSpec with all values
     * set to default.
     */
    public DiagramOptionSpec() {
        numbering = true;
        orderingLevel = OrderingLevel.SUPPRESS_OVERLAP;
    }

    /**
     * Creates a new instance of DiagramOptionSpec and loads all the data
     * from the provided XML document node.
     * 
     * @param data XML node
     */
    public DiagramOptionSpec(Node data) {
        NodeList nodeList = data.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (!nodeList.item(i).hasAttributes()) {
                continue;
            }
            NamedNodeMap attrs = nodeList.item(i).getAttributes();
            String name = attrs.getNamedItem("name").getNodeValue();
            String value = attrs.getNamedItem("value").getNodeValue();
            if (name.equals("numbering")) {
                numbering = Boolean.parseBoolean(value);
            } else if (name.equals("ordering-level")) {
                orderingLevel = OrderingLevel.valueOf(value);
            }
        }
    }

    @Override
    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        if (obj instanceof DiagramOptionSpec) {
            DiagramOptionSpec spec = (DiagramOptionSpec)obj;
            
            return spec.numbering == numbering &&
                   spec.orderingLevel.equals(orderingLevel);
        }
        return super.equals(obj);
    }

    @Override
    /** {@inheritDoc} */
    public int hashCode() {
        return (numbering ? 42 : -11) * (2 * orderingLevel.hashCode() + 1);
    }
    
    /**
     * Copies values from some other DiagramOptionSpec instance.
     * 
     * @param spec specification to get values from.
     */
    public void fill(DiagramOptionSpec spec) {
        if (spec == null) {
            return;
        }
        numbering = spec.numbering;
        orderingLevel = spec.orderingLevel;
    }

    /** {@inheritDoc} */
    public Element saveToXML(Document document) {
        Element section = document.createElement("options");
        Element option = document.createElement("option");
        option.setAttribute("name", "numbering");
        option.setAttribute("value", Boolean.toString(numbering));
        section.appendChild(option);
        option = document.createElement("option");
        option.setAttribute("name", "ordering-level");
        option.setAttribute("value", orderingLevel.toString());
        section.appendChild(option);
        return section;
    }
}
