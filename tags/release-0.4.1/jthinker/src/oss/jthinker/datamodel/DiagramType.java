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

package oss.jthinker.datamodel;

import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import static oss.jthinker.datamodel.NodeType.*;

/**
 * List of the valid thinking processes diagrams
 * 
 *  * @author iappel
 */
public enum DiagramType {
    CURRENT_REALITY_TREE("Current Reality Tree", STATEMENT, ELLIPSE),
    CONFLICT_RESOLUTION("Conflict Resolution", true, STATEMENT),
    FUTURE_REALITY_TREE("Future Reality Tree", STATEMENT, ELLIPSE, TASK),
    TRANSITION_TREE("Prerequisite Tree", STATEMENT, ELLIPSE, TASK, OBSTACLE),
    TRANSFORM_PLAN("Transform Tree", STATEMENT, ELLIPSE),
    FREEFORM_DIAGRAM("Free-form Diagram", true, NodeType.values());

    private final String title;
    private final boolean conflictAllowed;
    private final List<NodeType> allowedNodes;

    private DiagramType(String title, NodeType... nodes) {
        this(title, false, nodes);
    }

    private DiagramType(String title, boolean conflictAllowed, NodeType... nodes) {
        this.title = title;
        this.conflictAllowed = conflictAllowed;
        //
        List<NodeType> nodeTemp = new ArrayList<NodeType>();
        nodeTemp.addAll(Arrays.asList(nodes));
        allowedNodes = Collections.unmodifiableList(nodeTemp);
    }

    public String getTitle() {
        return title;
    }

    public List<NodeType> getAllowedNodeTypes() {
        return allowedNodes;
    }

    public boolean isConflictAllowed() {
        return conflictAllowed;
    }

    private static <T> List<T> wrap(T... values) {
        List<T> result = new ArrayList<T>();
        result.addAll(Arrays.asList(values));
        return Collections.unmodifiableList(result);
    }
}
