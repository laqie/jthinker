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

import oss.jthinker.swingutils.WindowUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import oss.jthinker.swingutils.ComponentLocationTrigger;
import oss.jthinker.swingutils.MouseLocator;
import oss.jthinker.util.IgnoreMixer;
import oss.jthinker.util.MixedQuadripoleTrigger;
import oss.jthinker.util.Mixer;
import oss.jthinker.util.QuadripoleTrigger;
import oss.jthinker.util.QuadripoleTriggerListener;
import oss.jthinker.util.Trigger;

/**
 * A connection between a {@see JNode} and something else, used as a
 * base superclass for {@see JEdge} and {@see JLeg}.
 * 
 * @author iappel
 * @param T type of the object on connection's end.
 */
public class JLink<T extends Component> extends JLine implements QuadripoleTriggerListener<Point> {
    private final JNode peerA;
    private final T peerZ;
    private boolean switchState;

    protected JLink(JNode peerA, boolean arrow) {
        super(arrow);
        this.peerA = peerA;
        peerZ = null;
    }

    protected JLink(JNode peerA, T peerZ, boolean arrow) {
        super(arrow);
        this.peerA = peerA;
        this.peerZ = peerZ;
    }

    /**
     * Returns a node, on which edge starts.
     * 
     * @return a node, on which edge starts.
     */
    public JNode getPeerA() {
        return peerA;
    }

    /**
     * Returns a component, on which edge ends.
     * 
     * @return a component, on which edge ends.
     */
    public T getPeerZ() {
        return peerZ;
    }

    /** {@inheritDoc} */
    public void setSwitched(boolean switching) {
        setForeground(switching ? Color.BLUE : WindowUtils.getDefaultForeground());
        switchState = switching;
    }
    
    /**
     * Checks, where mouse pointer is and switches component accordingly.
     * Mouse pointer's near location is treated as one being not further
     * than five pixels away from the line.
     * 
     * @param p location of the mouse pointer
     */    
    public void switchOnMouseMove(Point p) {
        setSwitched(distanceToPoint(p) < 5.0);
    }    
    
    /** {@inheritDoc} */
    public boolean isSwitched() {
        return switchState;
    }

    public void leftStateChanged(Point event) {
        setEndA(event);
    }

    public void rightStateChanged(Point event) {
        setEndZ(event);
    }

    protected void setupEvents() {
        Trigger<Point> ta = new ComponentLocationTrigger(peerA);
        Trigger<Point> tb;
        if (peerZ == null) {
            tb = MouseLocator.getInstance();
        } else {
            tb = new ComponentLocationTrigger(peerZ);
        }

        Mixer<Point> mixa = new JNodeAdjuster(peerA);
        Mixer<Point> mixb;
        if (peerZ instanceof JNode) {
            mixb = new JNodeAdjuster((JNode)peerZ);
        } else {
            mixb = new IgnoreMixer<Point>();
        }

        QuadripoleTrigger quad = new MixedQuadripoleTrigger<Point>(ta, tb, mixa, mixb);
        quad.addStateConsumer(this);
    }
}
