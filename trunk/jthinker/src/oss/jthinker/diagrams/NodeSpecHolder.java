/*
 * Copyright (c) 2009, Ivan Appel <ivan.appel@gmail.com>
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

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import oss.jthinker.tocmodel.NodeType;
import oss.jthinker.widgets.BorderType;
import oss.jthinker.widgets.JNodeSpec;
import static oss.jthinker.tocmodel.NodeType.*;
import static oss.jthinker.widgets.BorderType.*;

/**
 * Holder of prototype nodespecs.
 * 
 * @author iappel
 */
public class NodeSpecHolder {
    private final Map<NodeType, JNodeSpec> typeToSpecMap =
            new HashMap<NodeType, JNodeSpec>();
    private final HashMap<NodeType, BorderType> typeToBorderMap =
            new HashMap<NodeType, BorderType>();

    private static NodeSpecHolder instance;    
    
    private NodeSpecHolder() {
        typeToBorderMap.put(NodeType.ELLIPSE, BorderType.ELLIPSE);
        typeToBorderMap.put(STATEMENT, ROUND_RECT);
        typeToBorderMap.put(TASK, SHARP_RECT);
        typeToBorderMap.put(OBSTACLE, HEXAGON);
        
        for (NodeType nt : NodeType.values()) {
            BorderType bt = typeToBorderMap.get(nt);
            if (bt == null) {
                throw new RuntimeException(nt.name());
            }
            JNodeSpec protoSpec = new JNodeSpec(bt, nt!=NodeType.ELLIPSE, "", null);
            typeToSpecMap.put(nt, protoSpec);
        }
    }

    protected static NodeSpecHolder getInstance() {
        if (instance == null) {
            instance = new NodeSpecHolder();
        }
        return instance;
    }
    
    /**
     * Returns a prototype spec of a given node type.
     * 
     * @param nodeType node type
     * @return prototype spec
     */
    public static JNodeSpec getSpec(NodeType nodeType) {
        return getInstance().typeToSpecMap.get(nodeType);
    }
    
    /**
     * Creates a prototype-based node spec.
     * 
     * @param nodeType node type
     * @param content textual content
     * @param p center point
     * @return new prototype-based node spec
     */
    public static JNodeSpec clone(NodeType nodeType, String content, Point p) {
        return getSpec(nodeType).clone(content, p);
    }
    
    /**
     * Creates a prototype-based node spec.
     * 
     * @param p center point
     * @return new prototype-based node spec
     */
    public static JNodeSpec cloneEllipse(Point p) {
        JNodeSpec proto = getSpec(NodeType.ELLIPSE);
        return proto.clone("        ", p);
    }
}
