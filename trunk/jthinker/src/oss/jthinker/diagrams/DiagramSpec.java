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

import java.util.LinkedList;
import oss.jthinker.tocmodel.DiagramType;
import java.util.List;
import oss.jthinker.widgets.JNodeSpec;

/**
 * Container for packaging all information on diagram's structure
 * and data.
 * 
 * @author iappel
 */
public class DiagramSpec {
    public final List<JEdgeSpec> edgeSpecs;
    public final List<JNodeSpec> nodeSpecs;
    public final List<JLegSpec> legSpecs;
    public final DiagramType type;

    private DiagramSpec(List<JNodeSpec> nodeSpecs, List<JEdgeSpec> edgeSpecs,
            List<JLegSpec> legSpecs, DiagramType type) {
        this.edgeSpecs = edgeSpecs != null ? edgeSpecs :
            new LinkedList<JEdgeSpec>();
        this.nodeSpecs = nodeSpecs != null ? nodeSpecs :
            new LinkedList<JNodeSpec>();
        this.legSpecs = legSpecs != null ? legSpecs :
            new LinkedList<JLegSpec>();
        this.type = type;
    }

    /**
     * Creates a new DiagramSpec instance. Since the only {@see DiagramType}
     * that allows use of {@see JLeg}s is transition tree, field is omitted.
     * 
     * @param nodeSpecs list of diagram's nodes
     * @param edgeSpecs list of diagram's edges
     * @param legSpecs list of diagram's legs
     */
    public DiagramSpec(List<JNodeSpec> nodeSpecs, List<JEdgeSpec> edgeSpecs,
            List<JLegSpec> legSpecs) {
        this(nodeSpecs, edgeSpecs, legSpecs, DiagramType.TRANSITION_TREE);
    }

    /**
     * Creates a new DiagramSpec instance. Since the only {@see DiagramType}
     * that allows use of {@see JLeg}s is transition tree and there's a
     * separate constructor for that, this constructor doesn't allow setting
     * of legs.
     * 
     * @param nodeSpecs list of diagram's nodes
     * @param edgeSpecs list of diagram's edges
     * @param type type of the diagram
     */
    public DiagramSpec(List<JNodeSpec> nodeSpecs, List<JEdgeSpec> edgeSpecs,
            DiagramType type) {
        this(nodeSpecs, edgeSpecs, null, type);
    }
}
