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

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import javax.swing.JPopupMenu;
import oss.jthinker.util.Switch;

/**
 * A connection between a {@see JNode} and something else, used as a
 * base superclass for {@see JEdge} and {@see JLeg}.
 * 
 * @author iappel
 * @param T type of the object on connection's end.
 */
public abstract class AbstractEdge<T extends Component> extends JWire
    implements Switch {
    
    private final JNode peerA;
    private final T peerZ;
    private boolean switchState;
    private final JPopupMenu popupMenu;
    
    protected AbstractEdge(JNode peerA, T peerZ, JEdgeHost host, boolean arrow) {
        super(JNodeAdjuster.makeTrigger(peerA, peerZ), arrow);
        this.peerA = peerA;
        this.peerZ = peerZ;
        popupMenu = createPopupMenu(host);
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
        setForeground(switching ? Color.BLUE : Color.BLACK);
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

    /**
     * Initializes edge's popup menu.
     * 
     * @param host object manager, to which actions should be transmitted
     * @return a new popup menu for the edge.
     */
    protected abstract JPopupMenu createPopupMenu(JEdgeHost host);

    /**
     * Returns component's popup menu.
     * @return component's popup menu.
     */
    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }
}
