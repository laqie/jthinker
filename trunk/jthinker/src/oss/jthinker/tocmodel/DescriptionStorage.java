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

import java.util.HashMap;
import java.util.logging.Logger;

import static oss.jthinker.tocmodel.DiagramType.*;
import static oss.jthinker.tocmodel.LinkType.*;
import static oss.jthinker.tocmodel.NodeType.*;

/**
 * Registry of TOC Thinking processes diagram descriptions.
 * 
 * @author iappel
 */
public class DescriptionStorage {
    private final Logger logger = Logger.getLogger(DescriptionStorage.class.getName());

    private static HashMap<DiagramType, DiagramDescription> descriptionMap;

    static {
        descriptionMap = new HashMap<DiagramType, DiagramDescription>();
        DescriptionImpl common = new DescriptionImpl(null, "common");
        common.add(STATEMENT);
        common.add(CONSEQUENCE);
        
        DescriptionImpl currentReality = new DescriptionImpl(
                CURRENT_REALITY_TREE, "Current Reality Tree", common);
        currentReality.add(ELLIPSE);
        register(currentReality);
        
        DescriptionImpl conflictResolution = new DescriptionImpl(
                CONFLICT_RESOLUTION, "Conflict Resolution", common);
        conflictResolution.add(CONFLICT);
        register(conflictResolution);
        
        DescriptionImpl futureReality = new DescriptionImpl(
                FUTURE_REALITY_TREE, "Future Reality Tree", currentReality);
        futureReality.add(TASK);
        register(futureReality);
        
        DescriptionImpl transitionTree = new DescriptionImpl(
                TRANSITION_TREE, "Prerequsite Tree", futureReality);
        transitionTree.add(OBSTACLE);
        transitionTree.add(AFFECTABLE);
        register(transitionTree);
        
        DescriptionImpl transformPlan = new DescriptionImpl(
                TRANSFORM_PLAN, "Transform Tree", currentReality);
        register(transformPlan);
    }
    
    /**
     * Returns a {@link DiagramDescription} for given {@link DiagramType}
     * 
     * @param dType diagram type-key
     * @return description of selected diagram
     */
    public static DiagramDescription getEntity(DiagramType dType) {
        return descriptionMap.get(dType);
    }
    
    private static void register(DiagramDescription desc) {
        descriptionMap.put(desc.getType(), desc);
    }
}
