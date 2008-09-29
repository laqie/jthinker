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

import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import oss.jthinker.util.QuadripoleTrigger;
import oss.jthinker.util.Trigger;
import oss.jthinker.util.TriggerEvent;
import oss.jthinker.util.TriggerListener;

/**
 * A {@link JLine} that is wired between two UI widgets. Placement
 * of line is updated when some of the wired objects are moved or resized.
 * 
 * @author iappel
 */
public class JWire extends JLine implements TriggerListener<Point> {
    private static final Logger logger = 
            Logger.getLogger(JWire.class.getName());
    private final Trigger<Point> endAsrc, endZsrc;

    /**
     * Creates a new JWire with two {@link Trigger}s as sources of
     * position. Uses {@link ComponentTrigger} to hold information on
     * position of components.
     * 
     * @param endA trigger that holds coordinates of one end of the line
     * @param endZ trigger that holds coordinates of the other end of the line
     * @param arrow should arrowhead be drawn or not
     */
    public JWire(Trigger<Point> endA, Trigger<Point> endZ, boolean arrow) {
        super(endA.getState(), endZ.getState(), arrow);
        endAsrc = endA;
        endZsrc = endZ;
        endAsrc.addStateConsumer(this);
        endZsrc.addStateConsumer(this);
    }

    /**
     * Creates a new JWire with two {@link Trigger}s from
     * a {@see QuadripoleTrigger} as sources of position.
     * 
     * @param dualTrigger QuadripoleTrigger to retrieve two triggers
     * @param arrow specifies, should arrowhead be drawn or not
     */
    public JWire(QuadripoleTrigger<Point> dualTrigger, boolean arrow) {
        this(dualTrigger.getLeftOutput(), dualTrigger.getRightOutput(), arrow);
    }

    /**
     * Updates position of the line.
     * 
     * @param e event of change on one of the connected components
     */
    public void stateChanged(TriggerEvent<? extends Point> e) {
        if (e.getSource() == endAsrc) {
            setEndA(e.getState());
        } else if (e.getSource() == endZsrc) {
            setEndZ(e.getState());
        } else {
            logger.log(Level.WARNING,
                    "Source of event {0} is not wired by the JWire", e);
        }
    }
}
