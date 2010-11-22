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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import oss.jthinker.swingutils.SwingMapping;
import oss.jthinker.util.GappedArray;
import oss.jthinker.util.Mapping;
import oss.jthinker.util.Pair;
import oss.jthinker.widgets.JLink;
import oss.jthinker.widgets.AbstractDiagramOwner;
import oss.jthinker.widgets.JNode;
import oss.jthinker.widgets.JEdge;
import oss.jthinker.widgets.JLeg;
import oss.jthinker.widgets.JNodeSpec;

/**
 * Container for sorted holding of components.
 *
 * @author iappel
 */
public abstract class ComponentHolder extends AbstractDiagramOwner {
    private final GappedArray<JNode> _nodes = new GappedArray<JNode>();
    private final GappedArray<JEdge> _edges = new GappedArray<JEdge>();
    private final GappedArray<JLeg> _legs = new GappedArray<JLeg>();
    protected final DiagramView _view;
    protected final DiagramType _type;
    protected boolean numberingEnabled;
    
    /**
     * Creates a new ComponentHolder instance.
     * 
     * @param view {@link DiagramView} that this holder serves.
     * @param type type of diagram
     */
    public ComponentHolder(DiagramView view, DiagramType type) {
        _view = view;
        _type = type;
    }

    /**
     * Adds a node to container.
     * 
     * @param node node to add
     */
    public void add(JNode node) {
        _nodes.add(node);
        _view.add(node);
        node.enableNumbering(numberingEnabled);
    }
    
    /**
     * Removes a node from container. Also removes all connected edges
     * and legs.
     
     * @param node node to remove
     */
    public void remove(JNode node) {
        _nodes.remove(node);
        for (JEdge edge : getEdges(node)) {
            remove(edge);
        }
        for (JLeg leg : getLegs(node)) {
            remove(leg);
        }
        _view.remove(node);
    }

    /**
     * Adds an edge to container.
     * 
     * @param edge edge to add
     * @throw IllegalArgumentException when some of edge's peer nodes
     * are not contained within the holder.
     */
    public void add(JEdge edge) {
        boolean ca = _nodes.contains(edge.getPeerA()),
                cz = _nodes.contains(edge.getPeerZ());
        if (ca && cz) {
            _edges.add(edge);
            _view.add(edge);
        } else {
            String msg = "Nodes for edge must be already contained";
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Adds a leg to container.
     * 
     * @param leg leg to add
     * @throw IllegalArgumentException when some of leg's peer components
     * are not contained within the holder.
     */    
    public void add(JLeg leg) {
        boolean ca = _nodes.contains(leg.getPeerA()),
                cz = _edges.contains(leg.getPeerZ());
        if (ca && cz) {
            _legs.add(leg);
            _view.add(leg);
        } else {
            String msg = "Ends for leg must be already contained";
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Removes an edge from container. Also removes all connected nodes
     * and legs.
     
     * @param edge edge to remove
     */
    public void remove(JEdge edge) {
        edge.getPeerA().unwatch(edge);
        edge.getPeerZ().unwatch(edge);
        for (JLeg leg : getLegs(edge)) {
            remove(leg);
        }
        _edges.remove(edge);
        _view.remove(edge);
    }

    /**
     * Removes a leg from container. Also removes all connected edges
     * and nodes.
     
     * @param leg leg to remove
     */
    public void remove(JLeg leg) {
        leg.getPeerA().unwatch(leg);
        _legs.remove(leg);
        _view.remove(leg);
    }

    /**
     * Calculates a list of the edges that are connected to
     * a node.
     * 
     * @param node
     * @return list of the connected edges.
     */
    public Set<JEdge> getEdges(JNode node) {
        HashSet<JEdge> result = new HashSet<JEdge>();
        for (JEdge e : _edges) {
            if (e.getPeerA().equals(node) || e.getPeerZ().equals(node)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Calculates a list of the legs that are connected to a node.
     * 
     * @param node node to check
     * @return list of the connected legs
     */
    public Set<JLeg> getLegs(JNode node) {
        HashSet<JLeg> result = new HashSet<JLeg>();
        for (JLeg l : _legs) {
            if (l.getPeerA().equals(node)) {
                result.add(l);
            }
        }
        return result;
    }

    /**
     * Calculates a list of the legs that are connected to edge.
     * 
     * @param edge edge to check
     * @return list of the connected legs
     */
    public Set<JLeg> getLegs(JEdge edge) {
        HashSet<JLeg> result = new HashSet<JLeg>();
        for (JLeg l : _legs) {
            if (l.getPeerZ().equals(edge)) {
                result.add(l);
            }
        }
        return result;
    }    

    private JEdgeSpec getEdgeSpec(JEdge edge) {
        if (_edges.locate(edge) == -1) {
            throw new IllegalArgumentException("edge must be held by holder");
        }
        int idxA = _nodes.locate(edge.getPeerA());
        int idxZ = _nodes.locate(edge.getPeerZ());
        return new JEdgeSpec(idxA, idxZ, edge.isConflict());
    }
    
    private JLegSpec getLegSpec(JLeg leg) {
        if (_legs.locate(leg) == -1) {
            throw new IllegalArgumentException("leg must be held by holder");
        }
        int idxA = _nodes.locate(leg.getPeerA());
        int idxZ = _edges.locate(leg.getPeerZ());
        return new JLegSpec(idxA, idxZ);
    }
    
    private List<JEdgeSpec> saveEdgeSpecs() {
        _nodes.relax();
        List<JEdgeSpec> result = new ArrayList<JEdgeSpec>();
        for (JEdge edge : _edges) {
            result.add(getEdgeSpec(edge));
        }
        return result;
    }

    private List<JLegSpec> saveLegSpecs() {
        _nodes.relax();
        List<JLegSpec> result = new ArrayList<JLegSpec>();
        for (JLeg edge : _legs) {
            result.add(getLegSpec(edge));
        }
        return result;
    }
    
    private List<JNodeSpec> saveNodeSpecs() {
        _nodes.relax();
        ArrayList<JNodeSpec> list = new ArrayList<JNodeSpec>();
        for (JNode node : _nodes) {
            list.add(node.getNodeSpec());
        }
        return list;
    }
    
    /**
     * Calculates a diagram-making specification for the current content
     * of the holder.
     * 
     * @return diagram-making specification
     */
    public DiagramSpec getDiagramSpec() {
        _nodes.relax();
        _edges.relax();
        List<JNodeSpec> nodes = saveNodeSpecs();
        List<JEdgeSpec> edges = saveEdgeSpecs();
        List<JLegSpec> legs = saveLegSpecs();
        return new DiagramSpec(nodes, edges, legs, _type);
    }

    /**
     * Checks, does holder contain an edge or not.
     * 
     * @param edge edge to check
     * @return true if holder contains the edge and false otherwise
     */
    public boolean contains(JEdge edge) {
        return _edges.contains(edge);
    }

    /**
     * Returns the list of all holder's notes.
     * 
     * @return list of all holder's nodes
     */
    public Vector<JNode> getAllNodes() {
        return _nodes.getContent();
    }
    
    /**
     * Returns the list of all holder's wires - edges and legs.
     * 
     * @return list of all holder's wires
     */
    public Vector<JLink> getAllWires() {
        Vector<JLink> ret = new Vector<JLink>();
        ret.addAll(_edges.getContent());
        ret.addAll(_legs.getContent());
        return ret;
    }
    
    /**
     * Returns a numeric index of the node.
     * 
     * @param node node to seek
     * @return node's numeric index
     */
    public int issueIndex(JNode node) {
        return _nodes.locate(node);
    }
    
    /**
     * Enables or disables numbering of nodes.
     * 
     * @param bool should numbering be enabled or disabled
     */
    @Override
    public void enableNodeNumbering(boolean bool) {
        numberingEnabled = bool;
        for (JNode node : this.getAllNodes()) {
            node.enableNumbering(bool);
        }
    }
    
    /**
     * Returns true is diagram allows conflict-shaped arrows
     * and false otherwise.
     * 
     * @return true is diagram allows conflict-shaped arrows
     * and false otherwise.
     */
    public boolean allowsConflict() {
        return _type.isConflictAllowed();
    }

    /** {@inheritDoc} */
    public int nodeCount() {
        return getAllNodes().size();
    }

    /** {@inheritDoc} */
    public Mapping<? super JNode, Rectangle, ?> getMapping() {
        return new SwingMapping();
    }

    /** {@inheritDoc} */
    public Dimension getAreaSize() {
        return _view.getAreaSize();
    }

    /** {@inheritDoc} */    
    public Collection<JNode> getIncomeNodes(JNode target) {
        Collection<JNode> result = new LinkedList<JNode>();
        for (JEdge edge : _edges) {
            if (edge.getPeerZ() == target) {
                result.add(edge.getPeerA());
            }
        }
        return result;
    }

    /** {@inheritDoc} */    
    public Collection<JNode> getOutcomeNodes(JNode source) {
        Collection<JNode> result = new LinkedList<JNode>();
        for (JEdge edge : _edges) {
            if (edge.getPeerA() == source) {
                result.add(edge.getPeerZ());
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    public Pair<JNode, JNode> endpoints(JEdge edge) {
        return new Pair(edge.getPeerA(), edge.getPeerZ());
    }

    /** {@inheritDoc} */
    public Collection<JEdge> getAllEdges() {
        return _edges.getContent();
    }
}
