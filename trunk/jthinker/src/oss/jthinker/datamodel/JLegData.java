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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import oss.jthinker.util.XMLStored;

/**
 * Saveable presentation of a leg;
 * 
 * @author iappel
 */
public class JLegData implements XMLStored {
    public final int idxA, idxZ;

    /**
     * Loads specification from XML data.
     * 
     * @param data XML node that contains description of edge.
     */
    public JLegData(Node data) {
        if (!data.getNodeName().equals("leg")) {
            throw new IllegalArgumentException(data.getNodeName());
        }
        NamedNodeMap map = data.getAttributes();
        idxA = Integer.parseInt(map.getNamedItem("start").getNodeValue());
        idxZ = Integer.parseInt(map.getNamedItem("end").getNodeValue());
    }
    
    /**
     * Creates a new JLegSpec instance.
     * 
     * @param idxA index of leg's start node
     * @param idxZ index of leg's end edge
     */
    public JLegData(int idxA, int idxZ) {
        this.idxA = idxA;
        this.idxZ = idxZ;
    }

    @Override
    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        if (obj instanceof JLegData) {
            JLegData legSpec = (JLegData)obj;
            return (legSpec.idxA == idxA) && (legSpec.idxZ == idxZ);
        } else {
            return super.equals(obj);
        }
    }

    @Override
    /** {@inheritDoc} */
    public int hashCode() {
        return idxA + 42 * idxZ;
    }

    /** {@inheritDoc} */
    public Element saveToXML(Document document) {
        Element result = document.createElement("leg");
        result.setAttribute("start",    Integer.toString(idxA));
        result.setAttribute("end",      Integer.toString(idxZ));
        return result;
    }
}
