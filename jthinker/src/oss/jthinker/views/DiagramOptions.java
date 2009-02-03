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

package oss.jthinker.views;

import oss.jthinker.diagrams.DiagramOptionSpec;
import oss.jthinker.graphs.OrderingLevel;

/**
 * Value holder that mirrors {@link DiagramOptionSpec} and maintains
 * propagation of options into the diagram.
 * 
 * @author iappel
 */
public class DiagramOptions {
    private boolean numberNodes;
    private OrderingLevel orderLevel;
    
    private final DiagramPane pane;

    /**
     * Creates a new instance of DiagramOptions.
     * 
     * @param diagram diagram to forward option changes to
     * @param spec initial option specification
     */
    public DiagramOptions(DiagramPane diagram, DiagramOptionSpec spec) {
        numberNodes = spec.numbering;
        orderLevel = spec.orderingLevel;
        pane = diagram;
    }

    /**
     * Returns true if numbering of the nodes is enabled and
     * false otherwise.
     * 
     * @return true if numbering of the nodes is enabled and
     * false otherwise.
     */
    public boolean isNumberingEnabled() {
        return numberNodes;
    }
    
    /**
     * Inverts 'numbering' options.
     * 
     * @return new value of the option
     */
    public boolean invertNumbering() {
        numberNodes = !numberNodes;
        pane.getLinkController().enableNodeNumbering(numberNodes);
        return numberNodes;
    }
    
    /**
     * Converts object to {@link DiagramOptionSpec}.
     * 
     * @return new {@link DiagramOptionSpec} that contains all
     * options from this object.
     */
    public DiagramOptionSpec getSpec() {
        DiagramOptionSpec result = new DiagramOptionSpec();
        result.numbering = numberNodes;
        result.orderingLevel = orderLevel;
        return result;
    }

    /**
     * Sets preferred auto-layout policy for this diagram.
     * 
     * @param level layout policy.
     */
    public void setOrderingLevel(OrderingLevel level) {
        orderLevel = level;
        pane.getLinkController().getGraphEngine().setLevel(level);
    }

    /**
     * Returns preferred auto-layout policy for this diagram.
     * 
     * @return layout policy.
     */
    public OrderingLevel getOrderingLevel() {
        return orderLevel;
    }    
}
