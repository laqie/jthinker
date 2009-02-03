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

package oss.jthinker.graphs;

/**
 * Engine for graph optimizations.
 * 
 * @author iappel
 * @param T type of node data
 */
public class GraphEngine<T> {
    private final NodeBundle<T> data;
    private OrderingLevel level;

    /**
     * Initializes a new GraphEngine instance.
     * 
     * @param data bundle of nodes to service
     * @param level engine's level of optimization
     */
    public GraphEngine(NodeBundle<T> data, OrderingLevel level) {
        this.data = data;
        this.level = level;
    }

    /**
     * Recalculates positions of bundle's nodes after some of the nodes
     * got moved.
     * 
     * @param node node that moved
     */
    public synchronized void updatePosition(T node) {
        if (level == OrderingLevel.OFF) {
            return;
        }
        
        OverlapResolver<T> resolver = 
                new OverlapResolver<T>(data.getAllNodes(), data.getMapping());
        resolver.fix(node);
        resolver.resolve();
    }

    /**
     * Returns current layout policy.
     * 
     * @return current layout policy.
     */
    public OrderingLevel getLevel() {
        return level;
    }

    /**
     * Sets current layout policy.
     * 
     * @param level layout policy to set.
     */    
    public void setLevel(OrderingLevel level) {
        this.level = level;
    }
}
