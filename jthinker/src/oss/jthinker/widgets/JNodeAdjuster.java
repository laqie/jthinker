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
import java.awt.Point;
import java.awt.Rectangle;
import oss.jthinker.util.MixedQuadripoleTrigger;
import oss.jthinker.util.QuadripoleTrigger;
import oss.jthinker.util.Mixer;
import oss.jthinker.util.MixerInvertor;
import oss.jthinker.util.IgnoreMixer;
import oss.jthinker.util.Trigger;

/**
 * Helper class for adjusting coordinates of lines to make it align
 * properly to custom node borders.
 * 
 * @author iappel
 */
public class JNodeAdjuster {
    /**
     * Creates a new {@link QuadripoleTrigger} that recalculates positions
     * of the nodes into coordinates of line edges.
     * 
     * @param a node to be used as a beginning of the line
     * @param b node to be used as a end of the line
     * @return a new {@link QuadripoleTrigger} that correctly recalculates
     * positions
     */
    public static QuadripoleTrigger<Point> makeTrigger(JNode a, JNode b) {
        ComponentTrigger cta = new ComponentTrigger(a);
        ComponentTrigger ctb = new ComponentTrigger(b);
        Mixer<Point> mixa = makeMixer(a);
        Mixer<Point> mixb = makeMixer(b);

        MixerInvertor<Point> invmixb = new MixerInvertor<Point>(mixb);
        return new MixedQuadripoleTrigger<Point>(cta, ctb, mixa, invmixb);
    }

    /**
     * Creates a new {@link QuadripoleTrigger} that recalculates positions
     * of the nodes into coordinates of line edges.
     * 
     * @param a node to be used as a beginning of the line
     * @param b component to be used as a end of the line
     * @return a new {@link QuadripoleTrigger} that correctly recalculates
     * positions
     */    
    public static QuadripoleTrigger<Point> makeTrigger(JNode a, Component b) {
        if (b instanceof JNode) {
            return makeTrigger(a, (JNode)b);
        } else {
            return makeTrigger(a, new ComponentTrigger(b));
        }
    }

    /**
     * Creates a new {@link QuadripoleTrigger} that recalculates positions
     * of the nodes into coordinates of line edges.
     * 
     * @param a node to be used as a beginning of the line
     * @param ctb trigger to get data on end of the line
     * @return a new {@link QuadripoleTrigger} that correctly recalculates
     * positions
     */    
    public static QuadripoleTrigger<Point> makeTrigger(JNode a, Trigger<Point> ctb) {
        ComponentTrigger cta = new ComponentTrigger(a);
        
        Mixer<Point> mixa = makeMixer(a);
        Mixer<Point> mixb = new IgnoreMixer<Point>();
        
        MixerInvertor<Point> invmixb = new MixerInvertor<Point>(mixb);
        
        return new MixedQuadripoleTrigger<Point>(cta, ctb, mixa, invmixb);
    }
    
    private static abstract class ComponentMixer implements Mixer<Point> {
        private final Component component;

        public ComponentMixer(Component component) {
            this.component = component;
        }

        protected abstract Point compute(Rectangle bounds, Point outer);

        public Point mix(Point first, Point second) {
            return compute(component.getBounds(), second);
        }
    }

    private static Mixer<Point> makeMixer(JNode node) {
        switch (node.getNodeSpec().getBorderType()) {
            case ELLIPSE:
                return new ComponentMixer(node) {
                    @Override
                    protected Point compute(Rectangle bounds, Point outer) {
                        return GeometryUtils.ellipseIntersection(
                                bounds, outer);
                    }
                };
            case ROUND_RECT:
                return new ComponentMixer(node) {
                    @Override
                    protected Point compute(Rectangle bounds, Point outer) {
                        return GeometryUtils.roundRectangleIntersection(
                                bounds, outer);
                    }
                };
            case SHARP_RECT:
            case HEXAGON:
               return new ComponentMixer(node) {
                    @Override
                    protected Point compute(Rectangle bounds, Point outer) {
                        return GeometryUtils.rectangleIntersection(
                                bounds, outer);
                    }
                };
            default:
                throw new RuntimeException();
        }
    }
}
