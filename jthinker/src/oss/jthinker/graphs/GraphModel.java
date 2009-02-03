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

import java.util.Collection;
import oss.jthinker.util.UPair;

/**
 * Data model of the graph.
 * 
 * @author iappel
 * @param nodeT type of node's descriptive information
 * @param edgeT type of edge's descriptive information
 */
public interface GraphModel<nodeT, edgeT> extends NodeBundle<nodeT> {
    /**
     * Returns number of nodes in modelled graph.
     * 
     * @return number of nodes in modelled graph.
     */
    int nodeCount();
    
    /**
     * Returns number of edges in modelled graph.
     * 
     * @return number of edges in modelled graph.
     */
    int edgeCount();
    
    /**
     * Returns collection of all graph's nodes.
     * 
     * @return collection of all graph's nodes.
     */
    Collection<nodeT> getNodes();

    /**
     * Returns collection of all graph's edges.
     * 
     * @return collection of all graph's edges.
     */    
    Collection<edgeT> getEdges();

    /**
     * Checks that node is included in this model.
     * 
     * @param node node to check
     * @return true if node is included in the model
     */
    boolean isNodeModelled(nodeT node);

    /**
     * Checks that edge is included in this model.
     * 
     * @param edge node to check
     * @return true if edge is included in the model
     */    
    boolean isEdgeModelled(edgeT edge);
    
    /**
     * Checks are two nodes connected.
     * 
     * @param node1 first node number
     * @param node2 second node number
     * @return true if there's an edge that connects two nodes.
     */
    edgeT connection(nodeT node1, nodeT node2);

    /**
     * Returns edge's endpoint nodes.
     * 
     * @param edge edge, endpoints of which are needed
     * @return edge's endpoints as UPair
     */
    UPair<nodeT> endpoints(edgeT edge);
}
