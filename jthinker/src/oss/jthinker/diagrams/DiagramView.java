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

import oss.jthinker.util.MutableTrigger;
import oss.jthinker.widgets.JEdge;
import oss.jthinker.widgets.JLeg;
import oss.jthinker.widgets.JNode;

/**
 * Base interface for a view for diagram.
 * 
 * @author iappel
 */
public interface DiagramView {
    /**
     * Adds a new node to view.
     * 
     * @param node node to add.
     */    
    void add(JNode node);

    /**
     * Adds a new edge to view.
     * 
     * @param edge edge to add.
     */
    void add(JEdge edge);

   /**
     * Adds a new leg to view.
     * 
     * @param leg leg to add.
     */
    void add(JLeg leg);

    
    /**
     * Remove a node from view.
     * 
     * @param node node to remove.
     */           
    void remove(JNode node);
    
    /**
     * Remove an edge from view.
     * 
     * @param edge edge to remove.
     */    
    void remove(JEdge edge);

    /**
     * Remove a leg from view.
     * 
     * @param leg leg to remove.
     */    
    void remove(JLeg leg);

    
    /**
     * Should be called when some of hosted {@link JNode}s were moved to
     * do some after-move actions.
     */        
    void dispatchMove();
    
    /**
     * Creates a mouse-driven edge with given {@link JNode} as a static
     * end.
     * 
     * @param c node to use as fixed end.
     */
    void enableMouseEdge(JNode c);
    
    /**
     * Removes a mouse-driven edge.
     * 
     * @param start node, that initially served as a start for edge
     */        
    void disableMouseEdge(JNode start);

    /**
     * Returns trigger that holds was the pane's diagram modified after
     * the last save or not.
     *
     * @return trigger that holds was the pane's diagram modified after
     * the last save or not.
     */
    MutableTrigger<Boolean> getModifiedTrigger();
    
    /**
     * Starts editing of given node.
     * 
     * @param node node to edit
     */
    void editNode(JNode node);
}
