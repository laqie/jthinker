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

import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import oss.jthinker.util.Mapping;

/**
 * Implementation of overlap-prevention algorithm.
 * 
 * @author iappel
 * @param T node data type
 */
public class OverlapResolver<T> {
    private final Mapping<? super T, Rectangle, ?> mapper;
    private final Set<T> fixed, moved, rest;

    /**
     * Creates a new instance of OverlapResolver.
     * 
     * @param nodeData collection of nodes to arrange
     * @param mapper node object/location converter
     */
    public OverlapResolver(Collection<T> nodeData,
                           Mapping<? super T, Rectangle, ?> mapper) {
        this.mapper = mapper;
        fixed = new HashSet<T>();
        moved = new HashSet<T>();
        rest  = new HashSet<T>();
        rest.addAll(nodeData);
    }

    /**
     * Marks node as fixed, it should not be moved during optimization.
     * 
     * @param data node to mark
     */
    public void fix(T data) {
        fixed.add(data);
        moved.add(data);
        rest.remove(data);
    }

    /**
     * Rearranges nodes to prevent overlaps
     */
    public synchronized void resolve() {
        while (!moved.isEmpty()) {
            T item = moved.iterator().next();
            moved.remove(item);
            for (T jtem : rest) {
                if (overlaps(item, jtem)) {
                    resolvePosition(jtem);
                }
            }
            for (T jtem : fixed) {
                rest.remove(jtem);
            }
        }
    }
    
    private boolean overlaps(T nodeA, T nodeB) {
        Rectangle rectA = mapper.fetch(nodeA);
        Rectangle rectB = mapper.fetch(nodeB);
        
        return rectA.intersects(rectB);
    }
    
    private boolean validPosition(Rectangle rect) {
        for (T i: fixed) {
            if (mapper.fetch(i).intersects(rect)) {
                return false;
            }
        }
        return true;
    }

    private static final int SHIFT_STEP = 5;
    
    private void resolvePosition(T item) {
        Rectangle start = mapper.fetch(item);
        
        Rectangle result = start.getBounds();
        
        for (int sum = 1;;sum++) {
            boolean flag = false;
            for (int x = Math.max(-sum, -start.x / SHIFT_STEP); x < sum; x++) {
                result.x = start.x + SHIFT_STEP * x;
                if (Math.abs(sum) == Math.abs(x)) {
                    result.y = start.y;
                    if (validPosition(result)) {
                        flag = true;
                        break;
                    }
                } else {
                    int y = sum - Math.abs(x);
                    result.y = start.y + SHIFT_STEP * y;
                    if (validPosition(result)) {
                        flag = true;
                        break;
                    }
                    y = -Math.min(y, start.y/SHIFT_STEP);
                    result.y = start.y + SHIFT_STEP*y;
                    if (validPosition(result)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                break;
            }
        }
        mapper.assign(item, result);
        fixed.add(item);
        moved.add(item);
    }
}
