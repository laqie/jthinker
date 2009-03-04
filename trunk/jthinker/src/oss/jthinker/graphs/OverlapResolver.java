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
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import oss.jthinker.util.Mapping;

/**
 * Implementation of overlap-prevention algorithm.
 * 
 * @author iappel
 * @param T node data type
 */
public class OverlapResolver<T> extends OverlapMonitor {
    private final Mapping<? super T, Rectangle, ?> mapper;
    private final Set<T> rest;

    /**
     * Creates a new instance of OverlapResolver.
     * 
     * @param nodeData collection of nodes to arrange
     * @param mapper node object/location converter
     */
    public OverlapResolver(Collection<T> nodeData,
                           Mapping<? super T, Rectangle, ?> mapper) {
        this.mapper = mapper;
        rest  = new HashSet<T>();
        rest.addAll(nodeData);
    }

    /**
     * Marks node as fixed, it should not be moved during optimization.
     * 
     * @param data node to mark
     */
    public void fix(T data) {
        rest.remove(data);
        add(mapper.fetch(data));
    }

    public void fixEverything() {
        for (T item : rest) {
            add(mapper.fetch(item));
        }
        rest.clear();
    }
    
    /**
     * Rearranges nodes to prevent overlaps
     */
    public synchronized void resolve() {
        for (T item : rest) {
            Rectangle rect = mapper.fetch(item);
            if (overlapsSomething(rect)) {
                resolvePosition(item);
            } else {
                add(rect);
            }
        }
    }

    private static final int SHIFT_STEP = 5;

    private void resolvePosition(T item) {
        final Rectangle start = mapper.fetch(item);
        final Rectangle result = start.getBounds();
        final int minimumX = - start.x / SHIFT_STEP;
        final int minimumY = - start.y / SHIFT_STEP;
        final int single[] = new int[1];
        final int paired[] = new int[2];
        int ys[];
        
        for (int sum = 1;;sum++) {
            for (int x = Math.max(-sum, minimumX); x <= sum; x++) {
                result.x = start.x + x * SHIFT_STEP;
                int ty = Math.abs(x) - sum;
                if (ty == 0) {
                    ys = single;
                    ys[0] = 0;
                } else if (ty < minimumY) {
                    ys = single;
                    ys[0] = -ty;
                } else {
                    ys = paired;
                    ys[0] = ty;
                    ys[1] = -ty;
                }
                
                for (int y : ys) {
                    result.y = start.y + SHIFT_STEP*y;
                    if (!overlapsSomething(result)) {
                        mapper.assign(item, result);
                        add(result);
                        return;
                    }
                }
            }
        }
    }
        
    public Point newNodePoint(Dimension areaSize,
                              Dimension size,
                              Collection<T> nodes) {
        Rectangle[] rects = new Rectangle[nodes.size()];
        Iterator<T> iter = nodes.iterator();
        for (int i=0;i<nodes.size();i++) {
            rects[i] = mapper.fetch(iter.next());
        }
        return super.newNodePoint(areaSize, size, rects);
    }
}
