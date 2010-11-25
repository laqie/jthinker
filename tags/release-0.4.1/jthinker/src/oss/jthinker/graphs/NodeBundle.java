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

package oss.jthinker.graphs;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collection;
import oss.jthinker.util.Mapping;

/**
 * Something that contains several nodes.
 * 
 * @author iappel
 * @param nodeT type of the nodes
 */
public interface NodeBundle<nodeT> {
    /**
     * Returns collection of all graph's nodes.
     *
     * @return collection of all graph's nodes.
     */
    Collection<nodeT> getAllNodes();

    /**
     * Returns number of nodes in modelled graph.
     *
     * @return number of nodes in modelled graph.
     */
    int nodeCount();

    /**
     * Returns mapper of nodeT info to Rectangle
     * 
     * @return mapper of nodeT info to Rectangle
     */
    Mapping<? super nodeT, Rectangle, ?> getMapping();
    
    /**
     * Size of the area in which graph resides.
     * 
     * @return size of the area
     */
    Dimension getAreaSize();
    
    /**
     * Returns collection of nodes, from which given node can be reached
     * directly.
     * 
     * @param target a node to check
     * @return collection of nodes, from which given node can be reached.
     */
    Collection<nodeT> getIncomeNodes(nodeT target);

    /**
     * Returns collection of nodes, that can be reached directly from 
     * given node.
     * 
     * @param source a node to check
     * @return collection of nodes, from which given node can be reached.
     */    
    Collection<nodeT> getOutcomeNodes(nodeT source);
}
