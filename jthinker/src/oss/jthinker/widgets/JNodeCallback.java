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

package oss.jthinker.widgets;

/**
 * Interface of a {@link JNode} hoster that manages some of the linking
 * and moving issues.
 * 
 * @author iappel
 */
public interface JNodeCallback {
    /**
     * Should be called when some of hosted {@link JNode}s were moved to
     * do some after-move actions.
     * 
     * @param node node that recently moved
     */    
    void onNodeMoved(JNode node);
    
    /**
     * Starts selecting peer to link with given node.
     * @param start node that will be a start for edge after linking
     */
    void onLinkingStarted(JNode start);
    
    /**
     * Selects a peer for linking with the node, that was chosen
     * via {@link startLinking(JNode)} method.
     * @param end node that should be the end of the new edge
     */
    void onLinkingDone(JNode end);
    
    /**
     * Deletes given node.
     * @param node node to delete
     */
    void delete(JNode node);
    
    /**
     * Starts editing of selected node.
     * @param node node to edit
     */
    void editContent(JNode node);

    /**
     * Returns a numeric index of the node.
     * 
     * @param node node to seek
     * @return node's numeric index
     */
    int issueIndex(JNode node);
    
    /**
     * Returns a node-group associations holder.
     * 
     * @return grouping associations holder.
     */
    GroupHandler getGroupHandler();

    /**
     * Starts editing of selected node.
     *
     * @param node node, for which editor should be displayed
     */
    void startEditing(JNode node);
}
