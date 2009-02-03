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

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import oss.jthinker.graphs.GraphEngine;
import oss.jthinker.graphs.OrderingLevel;
import oss.jthinker.tocmodel.DiagramType;
import oss.jthinker.widgets.BorderType;
import oss.jthinker.widgets.GroupHandler;
import oss.jthinker.widgets.JEdge;
import oss.jthinker.widgets.JEdgeHost;
import oss.jthinker.widgets.JLeg;
import oss.jthinker.widgets.JNode;
import oss.jthinker.widgets.JNodeHost;
import oss.jthinker.widgets.JNodeSpec;

/**
 * {@link JEdgeHost} and {@link JNodeHost} implementation on top of the
 * {@link ComponentHolder}.
 * 
 * @author iappel
 */
public class ComponentManager extends ComponentHolder 
        implements JEdgeHost, JNodeHost {
    private final GroupHandler groupHandler;
    private final GraphEngine<JNode> engine;
    
    /**
     * Creates a new component manager for given diagram's view and type.
     * 
     * @param view visual renderer of the diagram.
     * @param type type of the diagram
     */
    public ComponentManager(DiagramView view, DiagramType type) {
        super(view, type);
        groupHandler = new GroupHandler(view);
        engine = new GraphEngine<JNode>(this, OrderingLevel.SUPPRESS_OVERLAP);
    }

    /** {@inheritDoc} */
    public void deleteJEdge(JEdge edge) {
        remove(edge);
    }

    /** {@inheritDoc} */
    public void deleteJLeg(JLeg leg) {
        remove(leg);
    }
    
    /** {@inheritDoc} */
    public void reverseJEdge(JEdge edge) {
        if (!contains(edge)) {
            throw new IllegalArgumentException(edge.toString());
        }
        JNode nodeA = edge.getPeerA();
        JNode nodeZ = edge.getPeerZ();
        remove(edge);
        add(new JEdge(nodeZ, nodeA, this));
    }

    /** {@inheritDoc} */
    public void deleteJNode(JNode node) {
        remove(node);
        groupHandler.ungroup(node);
    }

    /** {@inheritDoc} */
    public void dispatchJNodeMove(JNode node) {
        Point place = node.getLocation();
        if (place.x < 0 || place.y < 0) {
            place.x = Math.max(place.x, 0);
            place.y = Math.max(place.y, 0);
            node.setLocation(place);
        }
        _view.dispatchMove();
        groupHandler.updatePosition(node);
        engine.updatePosition(node);
    }
    
    /** {@inheritDoc} */
    public void editJNodeContent(JNode node) {
        _view.editNode(node);
    }

    private JNode linkPeer = null;
    /** {@inheritDoc} */
    public void endLinking(JNode end) {
        if (linkPeer == null) {
            return;
        }
        _view.disableMouseEdge(linkPeer);
        JEdge edge = new JEdge(linkPeer, end, this);
        add(edge);
        linkPeer = null;
    }
    
    /** {@inheritDoc} */
    public void startLinking(JNode start) {
        if (linkPeer != null) {
            throw new IllegalStateException("Linking already started");
        }
        linkPeer = start;
        _view.enableMouseEdge(start);
    }

    /** {@inheritDoc} */
    public void endLinking(JEdge end) {
        if (linkPeer == null) {
            return;
        }
        if (linkPeer.getBorderType() != BorderType.HEXAGON) {
            return;
        }
        if (_type != DiagramType.TRANSITION_TREE) {
            return;
        }
        _view.disableMouseEdge(linkPeer);
        JLeg leg = new JLeg(linkPeer, end, this);
        add(leg);
        linkPeer = null;
    }
    
    /** 
     * Restores a diagram from given specification
     * 
     * @param spec specification to apply
     */
    public void setDiagramSpec(DiagramSpec spec) {
        List<JNode> nodes = new LinkedList<JNode>();
        List<JEdge> edges = new LinkedList<JEdge>();
        
        for (JNodeSpec nodeSpec : spec.nodeSpecs) {
            JNode node = new JNode(this, nodeSpec);
            add(node);
            nodes.add(node);
        }
        for (JEdgeSpec edgeSpec : spec.edgeSpecs) {
            int a = edgeSpec.idxA, z = edgeSpec.idxZ;
            JEdge edge = new JEdge(nodes.get(a), nodes.get(z), this);
            edge.setConflict(edgeSpec.conflict);
            edges.add(edge);
            add(edge);
        }
        for (JLegSpec legSpec : spec.legSpecs) {
            int a = legSpec.idxA, z = legSpec.idxZ;
            JLeg leg = new JLeg(nodes.get(a), edges.get(z), this);
            add(leg);
        }
    }

    /** {@inheritDoc} */
    public GroupHandler getGroupHandler() {
        return groupHandler;
    }

    /**
     * Returns diagram layout engine.
     * 
     * @return diagram layout engine
     */
    public GraphEngine<JNode> getGraphEngine() {
        return engine;
    }
}
