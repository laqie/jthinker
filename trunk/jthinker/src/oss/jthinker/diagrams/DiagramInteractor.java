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

import java.awt.Color;
import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JOptionPane;
import oss.jthinker.graphs.GraphEngine;
import static oss.jthinker.tocmodel.NodeType.*;
import oss.jthinker.util.Pair;
import oss.jthinker.widgets.JEdge;
import oss.jthinker.widgets.JNode;
import oss.jthinker.widgets.JNodeSpec;


/**
 * Handles various typical diagram interactions.
 * 
 * @author iappel
 */
public class DiagramInteractor {
    private final DiagramView _view;
    private final GraphEngine<JNode> _engine;
    private final LinkController _manager;
    
    private InteractorActionFactory _factory;
    
    /**
     * Creates a new DiagramInteractor instance.
     * 
     * @param view served diagram view
     */
    public DiagramInteractor(DiagramView view) {
        _view = view;
        _engine = view.getGraphEngine();
        _manager = view.getLinkController();
    }

    /**
     * Adds a new undesired effect to the diagram.
     */
    public void addUndesiredEffect() {
        Point p = _engine.newNodePoint();
        if (p == null) {
            throw new NullPointerException(_view.getClass().toString());
        }
        JNodeSpec spec = NodeSpecHolder.clone(STATEMENT, "Type UDE here", p);
        JNode node = _manager.add(spec);
        node.setColor(Color.PINK);
        _view.editNode(node);
    }
    
    /**
     * Picks two unreasoned items on the diagram and suggests
     * a common reason for them.
     */
    public void suggestReasons() {
        Collection<JNode> terms = _manager.getRandomSources(2);
        if (terms == null) {
            terms = _manager.getRandomNodes(2);
            if (terms == null) {
                return;
            }
        }
        Point p = _engine.newNodePoint(terms);
        if (p == null) {
            throw new NullPointerException();
        }
        JNodeSpec spec = NodeSpecHolder.clone(STATEMENT, "Common reason", p);
        JNode node = _manager.add(spec);
        Iterator<JNode> iter = terms.iterator();
        JEdge edge1 = _manager.add(node, iter.next());
        JEdge edge2 = _manager.add(node, iter.next());
        int option = JOptionPane.showOptionDialog(null,
                "Is there a common reason?",
                "Suggest common reason",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (option == JOptionPane.YES_OPTION) {
            _view.editNode(node);
            return;
        } else {
            _manager.remove(edge1);
        }
        option = JOptionPane.showOptionDialog(null,
                "Can you tell a reason?",
                "Suggest a reason",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (option == JOptionPane.YES_OPTION) {
            _view.editNode(node);
        } else {
            _manager.remove(edge1);
            _manager.remove(node);
        }
    }

    /**
     * Picks two unreasoned items on the diagram and suggests
     * a common reason for them.
     */
    public void suggestEllipse() {
        Pair<JNode, ? extends Collection<JNode>> entry =
                _manager.getRandomIncomings(2);
        
        if (entry == null) {
            return;
        }
        
        JNode target = entry.first;
        
        Iterator<JNode> iter = entry.second.iterator();
        
        JNode sourceA = iter.next();
        JNode sourceB = iter.next();
        
        JEdge edgeA = _manager.connection(sourceA, target);
        JEdge edgeB = _manager.connection(sourceB, target);
        
        edgeA.setVisible(false);
        edgeB.setVisible(false);
        
        Point p = _engine.centerPoint(target, sourceA, sourceB);
        JNodeSpec spec = NodeSpecHolder.cloneEllipse(p);
        
        JNode ellipseNode = _manager.add(spec);
        JEdge edgeX = _manager.add(sourceA, ellipseNode);
        JEdge edgeY = _manager.add(sourceB, ellipseNode);
        JEdge edgeZ = _manager.add(ellipseNode, target);
        
        int option = JOptionPane.showOptionDialog(null,
                "Is it correct?",
                "Suggest ellipse",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        
        if (option == JOptionPane.YES_OPTION) {
            _manager.remove(edgeA, edgeB);
        } else {
            _manager.remove(ellipseNode);
            _manager.remove(edgeX, edgeY, edgeZ);
            edgeA.setVisible(true);
            edgeB.setVisible(true);
        }
    }
    
    /**
     * Returns the interactor's {@see InteractorActionFactory}.
     * 
     * @return interactor's {@see InteractorActionFactory}.
     */
    public InteractorActionFactory getActionFactory() {
        if (_factory == null) {
            _factory = new InteractorActionFactory(this);
        }
        return _factory;
    }
}
