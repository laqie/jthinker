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

package oss.jthinker.widgets;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * JPanel-based implementation of JBackground.
 * 
 * @author iappel
 */
public class JBackgroundPane extends JPanel implements JBackground {
    private final Set<JComponent> backgroundComponents =
            new HashSet<JComponent>();

    /** {@inheritDoc} */
    public void addBackground(JComponent entry) {
        if (backgroundComponents.add(entry)) {
            repaintBackground();
        }
    }

    /** {@inheritDoc} */
    public void removeBackground(JComponent entry) {
        if (backgroundComponents.remove(entry)) {
            repaintBackground();
        }
    }

    /** {@inheritDoc} */
    public void repaintBackground() {
        repaint();
    }
    
    @Override
    /** {@inheritDoc} */
    public void paint(Graphics g) {
        super.paintComponent(g);
        
        for (JComponent entry : backgroundComponents) {
            if (entry.isVisible()) {
                Graphics gg = g.create();
                gg.translate(entry.getX(), entry.getY());
                entry.paint(gg);
            }
        }
        
        super.paintChildren(g);
    }
}
