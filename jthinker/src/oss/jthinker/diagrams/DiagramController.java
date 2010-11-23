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

import oss.jthinker.datamodel.DiagramData;
import oss.jthinker.datamodel.DiagramOptionData;
import oss.jthinker.datamodel.JLegData;
import oss.jthinker.datamodel.JEdgeData;
import oss.jthinker.datamodel.DiagramType;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import oss.jthinker.graphs.GraphEngine;
import oss.jthinker.graphs.OrderingLevel;
import oss.jthinker.widgets.GroupHandler;
import oss.jthinker.widgets.JEdge;
import oss.jthinker.widgets.JLeg;
import oss.jthinker.widgets.JNode;
import oss.jthinker.widgets.JNodeEditor;
import oss.jthinker.datamodel.JNodeData;
import oss.jthinker.widgets.WidgetFactory;

/**
 * {@link JEdgeHost} and {@link JNodeHost} implementation on top of the
 * {@link ComponentHolder}.
 * 
 * @author iappel
 */
public class DiagramController extends ComponentContainer {
    private final GroupHandler _groupHandler;
    private final GraphEngine<JNode> _graphEngine;
    private final JNodeEditor.EditorContainer _editorContainer;
    private final WidgetFactory _widgetFactory;
    
    /**
     * Creates a new component manager for given diagram's view and type.
     * 
     * @param view visual renderer of the diagram.
     * @param type type of the diagram
     */
    public DiagramController(DiagramView view, DiagramType type) {
        super(view, type);
        _groupHandler = new GroupHandler(view, this);
        _graphEngine = new GraphEngine<JNode>(this, OrderingLevel.SUPPRESS_OVERLAP);
        _editorContainer = view.getEditorContainer();
        _widgetFactory = new WidgetFactory(this);
    }

    /** {@inheritDoc} */
    public void delete(JEdge edge) {
        remove(edge);
    }

    /** {@inheritDoc} */
    public void delete(JLeg leg) {
        remove(leg);
    }
    
    /** {@inheritDoc} */
    public void reverse(JEdge edge) {
        if (!contains(edge)) {
            throw new IllegalArgumentException(edge.toString());
        }
        JNode nodeA = edge.getPeerA();
        JNode nodeZ = edge.getPeerZ();
        remove(edge);
        add(_widgetFactory.produceEdge(nodeZ, nodeA));
    }

    /** {@inheritDoc} */
    public void delete(JNode node) {
        remove(node);
        _groupHandler.ungroup(node);
    }

    /** {@inheritDoc} */
    public void onNodeMoved(JNode node) {
        Point place = node.getLocation();
        if (place.x < 0 || place.y < 0) {
            place.x = Math.max(place.x, 0);
            place.y = Math.max(place.y, 0);
            node.setLocation(place);
        }
        _view.dispatchMove();
        _groupHandler.updatePosition(node);
        _graphEngine.updatePosition(node);
    }
    
    /** {@inheritDoc} */
    public void editContent(JNode node) {
        _view.editNode(node);
    }

    private JNode linkPeer = null;
    /** {@inheritDoc} */
    public void onLinkingDone(JNode end) {
        if (linkPeer == null) {
            return;
        }
        _view.disableMouseEdge(linkPeer);
        JEdge edge = _widgetFactory.produceEdge(linkPeer, end);
        add(edge);
        linkPeer = null;
    }
    
    /** {@inheritDoc} */
    public void onLinkingStarted(JNode start) {
        if (linkPeer != null) {
            throw new IllegalStateException("Linking already started");
        }
        linkPeer = start;
        _view.enableMouseEdge(start);
    }

    /** {@inheritDoc} */
    public void onLinkingDone(JEdge end) {
        if (linkPeer == null) {
            return;
        }
        _view.disableMouseEdge(linkPeer);
        JLeg leg = _widgetFactory.produceLeg(linkPeer, end);
        add(leg);
        linkPeer = null;
    }
    
    /** 
     * Restores a diagram from given specification
     * 
     * @param spec specification to apply
     */
    public void setDiagramSpec(DiagramData spec) {
        List<JNode> nodes = new LinkedList<JNode>();
        List<JEdge> edges = new LinkedList<JEdge>();
        
        for (JNodeData nodeSpec : spec.getNodeData()) {
            JNode node = _widgetFactory.produceNode(nodeSpec);
            add(node);
            nodes.add(node);
        }
        for (JEdgeData edgeSpec : spec.getEdgeData()) {
            int a = edgeSpec.idxA, z = edgeSpec.idxZ;
            JEdge edge = _widgetFactory.produceEdge(nodes.get(a), nodes.get(z));
            edge.setConflict(edgeSpec.conflict);
            edges.add(edge);
            add(edge);
        }
        for (JLegData legSpec : spec.getLegData()) {
            int a = legSpec.idxA, z = legSpec.idxZ;
            JLeg leg = _widgetFactory.produceLeg(nodes.get(a), edges.get(z));
            add(leg);
        }
    }

    /** {@inheritDoc} */
    public GroupHandler getGroupHandler() {
        return _groupHandler;
    }

    /**
     * Returns diagram layout engine.
     * 
     * @return diagram layout engine
     */
    public GraphEngine<JNode> getGraphEngine() {
        return _graphEngine;
    }

    /** {@inheritDoc} */
    public JEdge add(JNode nodeA, JNode nodeZ) {
        JEdge edge = _widgetFactory.produceEdge(nodeA, nodeZ);
        add(edge);
        return edge;
    }

    /** {@inheritDoc} */
    public JNode add(JNodeData nodeSpec) {
        JNode node = _widgetFactory.produceNode(nodeSpec);
        add(node);
        return node;
    }

    /** {@inheritDoc} */
    public void remove(JNode... nodes) {
        for (JNode node : nodes) {
            super.remove(node);
        }
    }
 
    /** {@inheritDoc} */   
    public void startEditing(JNode node) {
        JNodeEditor.startEditing(node, _editorContainer);
    }

    public WidgetFactory getWidgetFactory() {
        return _widgetFactory;
    }

    public DiagramOptionData getOptions() {
        return _view.getDiagramOptionsData();
    }
}
