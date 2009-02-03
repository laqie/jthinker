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

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

/**
 * Connection between a {@see JNode} and {@see JEdge}.
 * 
 * @author iappel
 */
public class JLeg extends AbstractEdge<JEdge> {
    /**
     * Creates a new instance of JLeg that connects given node and edge.
     * 
     * @param node node to serve as a beginning of the leg
     * @param edge edge to serve as leg's end
     * @param host manager of deletions of the leg.
     */
    public JLeg(JNode node, JEdge edge, JEdgeHost host) {
        super(node, edge, host, false);
    }

    /** {@inheritDoc} */
    protected JPopupMenu createPopupMenu(final JEdgeHost host) {
        JPopupMenu menu = new JPopupMenu();
        final JLeg instance = this;
        menu.add(new AbstractAction("Delete") {
            public void actionPerformed(ActionEvent e) {
                host.deleteJLeg(instance);
            }
        });
        return menu;
    }

    @Override
    /** {@inheritDoc} */
    protected void draw(Graphics g, int x, int y1, int y2) {
        double fullLength = Math.hypot(x - 10, y2 - y1);
        double dashedLength = fullLength - 10;
        int dashCount = (int)dashedLength / 15;
        int mxy = Math.max(y1, y2);
        
        if (dashCount == 0) {
            g.drawLine(5, y1, x-5, y2);
            return;
        }

        double xCoef     = (x - 10) / fullLength,
               yCoef     = (y2 - y1) / fullLength,
               dashSpace = dashedLength / dashCount,
               dashLen   = dashSpace * 3 / 5;

        for (int i=0;i<=dashCount;i++) {
            double startLen = i*dashSpace;
            double endLen = startLen + dashLen;
            
            int startX = (int)(startLen*xCoef);
            int startY = (int)(startLen * yCoef);
            int endX = (i == dashCount) ? (x-10) :
                       (int)(endLen*xCoef);
            int endY = (i == dashCount) ? y2 - y1 :
                       (int)(endLen*yCoef);
            g.drawLine(startX+5, startY+y1, endX+5, endY+y1);
        }
    }
}
