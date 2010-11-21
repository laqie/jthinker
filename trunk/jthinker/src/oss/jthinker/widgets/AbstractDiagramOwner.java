/*
 * Copyright (c) 2010, Ivan Appel <ivan.appel@gmail.com>
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

package oss.jthinker.widgets;

import oss.jthinker.graphs.AbstractGraphModel;

/**
 *
 * @author vanya
 */
public abstract class AbstractDiagramOwner
extends AbstractGraphModel<JNode, JEdge>
implements JNodeCallback, JEdgeCallback {
    /**
     * Enables or disables numbering of nodes.
     *
     * @param state should numbering be enabled or disabled
     */
    abstract public void enableNodeNumbering(boolean state);


    /**
     * Creates a new node and adds it to the manager and to the managed
     * view.
     *
     * @param nodeSpec specification of the node to create
     * @return newly created node
     */
    abstract public JNode add(JNodeSpec nodeSpec);

    /**
     * Removes several nodes from the manager and from the managed view.
     *
     * @param nodes nodes to remove
     */
    abstract public void remove(JNode... nodes);

    /**
     * Selects a peer for linking with the node, that was chosen
     * via {@link startLinking(JNode)} method.
     * @param end node that should be the end of the new edge
     */
    abstract public void onLinkingDone(JEdge end);

    /**
     * Creates an edge and adds it to this manager and to the managed view.
     *
     * @param nodeA edge's start node
     * @param nodeZ edge's end node
     * @return newly create edge
     */
    abstract public JEdge add(JNode nodeA, JNode nodeZ);

    /**
     * Removes several edges from the manager and from the managed view.
     *
     * @param edges edges to remove
     */
    abstract public void remove(JEdge... edges);
}
