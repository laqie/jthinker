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

package oss.jthinker.tocmodel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Actual implementation of TOC diagram model.
 *
 * @author iappel
 */
class DescriptionImpl implements DiagramDescription {
    private final String title;
    private final List<LinkType> links;
    private final List<NodeType> nodes;
    private final DiagramType dtype;

    public DescriptionImpl(DiagramType type, String name) {
        title = name;
        links = new LinkedList<LinkType>();
        nodes = new LinkedList<NodeType>();
        dtype = type;
    }

    public DescriptionImpl(DiagramType type, String title, DiagramDescription desc) {
        this(type, title);
        links.addAll(desc.possibleLinks());
        nodes.addAll(desc.possibleNodes());
    }

    protected void add(LinkType linkKind) {
        links.add(linkKind);
    }
    
    protected void add(NodeType nodeKind) {
        nodes.add(nodeKind);
    }
    
    /** {@inheritDoc} */    
    public String getTitle() {
        return title;
    }

    /** {@inheritDoc} */
    public List<LinkType> possibleLinks() {
        return Collections.unmodifiableList(links);
    }

    /** {@inheritDoc} */
    public List<NodeType> possibleNodes() {
        return Collections.unmodifiableList(nodes);
    }

    /** {@inheritDoc} */
    public DiagramType getType() {
        return dtype;
    }
}
