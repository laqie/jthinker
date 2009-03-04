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
import java.util.LinkedList;
import oss.jthinker.widgets.GeometryUtils;

/**
 * Overlaps monitoring container.
 * 
 * @author iappel
 */
public class OverlapMonitor extends LinkedList<Rectangle> {
    /**
     * Creates a new instance of OverlapMonitor.
     */
    public OverlapMonitor() {
    }

    /**
     * Checks, does this rectangle overlaps some contained rectangle.
     * 
     * @param rect rectangle to check
     * @return true if rect overlaps some of the contained rectangles
     * and false otherwise
     */
    public synchronized boolean overlapsSomething(Rectangle rect) {
        for (Rectangle r : this) {
            if (r.intersects(rect)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a place for a new node.
     * 
     * @param areaSize size of the area to seek
     * @param size size of the node to place
     * @param rects rectangles, which should be above the resulting node
     * @return center point for a new node
     */
    public Point newNodePoint(Dimension areaSize,
                              Dimension size,
                              Rectangle... rects) {
        int deltaX = (areaSize.width - size.width) / 10;
        int deltaY = (areaSize.height - size.height) / 10;
        
        Rectangle rect = new Rectangle();
        rect.width = size.width;
        rect.height = size.height;
        
        for (int i=0;i<10;i++) {
            rect.y = deltaY * i;
            boolean flag = false;
            for (Rectangle r : rects) {
                if (rect.y - r.y < deltaX) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                continue;
            }
            for (int j=0;j<10;j++) {
                rect.x = deltaX * j;
                if (!overlapsSomething(rect)) {
                    return GeometryUtils.computeCenterPoint(rect);
                }
            }
        }
        return null;
    }
}
