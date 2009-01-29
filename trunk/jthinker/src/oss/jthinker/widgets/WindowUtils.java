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

package oss.jthinker.widgets;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.util.Vector;

/**
 * Various component-related helper functions.
 * 
 * @author iappel
 */
public class WindowUtils {
    /**
     * Calculates location of component's center (in coordinates of
     * component's parent)
     * 
     * @param c component to calculate
     * @return component center location
     */
    public static Point computeCenterPoint(Component c) {
        return GeometryUtils.computeCenterPoint(c.getBounds());
    }

    /**
     * Finds, what window does this component is displayed in.
     * 
     * @param c component to seek
     * @return window, which this component belongs to
     */
    public static Window findWindow(Component c) {
        if (c == null) {
            return null;
        } else if (c instanceof Window) {
            return (Window)c;
        } else {
            return findWindow(c.getParent());
        }
    }

    /**
     * Finds, what window does this component is displayed in.
     * 
     * @param c component to seek
     * @return window, which this component belongs to
     */
    public static Frame findFrame(Component c) {
        return (Frame)findWindow(c);
    }
    
    /**
     * Calculates containment sequence, starting with given
     * component.
     * 
     * @param c component to start containment sequence.
     * @return containment sequence.
     */
    public static Vector<Container> computeContainment(Component c) {
        Vector<Container> result = new Vector<Container>();
        Container container = c.getParent();
        
        do {
            result.add(container);
            container = container.getParent();
        } while ((container != null) && !(container instanceof Window));
        if (container != null) {
            result.add(container);
        }
        
        return result;
    }

    /**
     * 
     * @param c
     * @return
     */
    public static Point computeTotalContainmentShift(Component c) {
        Point result = new Point(0,0);
        for (Container container : computeContainment(c)) {
            result.translate(container.getX(), container.getY());
        }
        return result;
    }
    
    public static Point computeAbsoluteCenterPoint(Component c) {
        Point p = computeCenterPoint(c);
        Point p2 = computeTotalContainmentShift(c);
        return new Point(p.x+p2.x, p.y+p2.y);
    }
    
    /**
     * Sets component's center point to provided coordinate.
     * General contract is that {@link computeCenterPoint(Component)} call
     * right after setting center must return the very same coordinate as
     * one that was set.
     * 
     * @param c component to move
     * @param p point to set as center (in parent's coordinates)
     */
    public static void setCenterPoint(Component c, Point p) {
        int dw = c.getWidth() / 2;
        int dh = c.getHeight() / 2;
        c.setLocation(p.x - dw, p.y - dh);
    }
    
    /**
     * Similar to {@link setCenterPoint(Component, Point)} but operates
     * with component's own point of origin instead of parent's.
     * 
     * @param c component to move
     * @param p
     */
    public static void adjustCenterPoint(Component c, Point p) {
        Point l = c.getLocation();
        l.translate(p.x - c.getWidth() / 2, p.y - c.getHeight() / 2);
        c.setLocation(l);
    }    
}
