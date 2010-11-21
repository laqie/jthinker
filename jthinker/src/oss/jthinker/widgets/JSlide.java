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
import oss.jthinker.util.Switch;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Simple decorator that makes arbitrary widget draggable around
 * it's container. Also allows draw two frames (one when mouse cursor
 * is inside component and the other when cursor is out).
 * 
 * @author iappel
 */
public class JSlide extends JPanel implements Switch {
    private final JSlide slideInstance = this;
    private final Border activeBorder,  inactiveBorder;
    private Color background;
    private boolean switched;

    /**
     * Creates a new instance of slidable container. 
     * 
     * @param internal component to contain.
     * @param builder source of active/inactive borders to wrap around the
     * component.
     */    
    public JSlide(JComponent internal, BorderBuilder builder) {
        this(internal, builder, WindowUtils.getDefaultBackground());
    }
    
    /**
     * Creates a new instance of slidable container. 
     * 
     * @param internal component to contain.
     * @param builder source of active/inactive borders to wrap around the
     * component.
     * @param background background color for slide
     */
    public JSlide(JComponent internal, BorderBuilder builder, Color background) {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                adjustCenterPoint(e.getPoint());
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setSwitched(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setSwitched(false);
            }
        });
        
        setVisible(true);
        setLayout(null);
        
        activeBorder = builder.createBorder(Color.RED);
        inactiveBorder = builder.createBorder(WindowUtils.getDefaultForeground());
        setBorder(inactiveBorder);
        add(internal);
        setColor(background);
    }

    /**
     * Creates a new instance of slidable container.
     * 
     * @param slideSpec slide construction specification
     */
    public JSlide(JSlideSpec slideSpec) {
        this(slideSpec.getComponent(), slideSpec.getBorderBuilder(),
                slideSpec.getBackground());
    }
    
    /**
     * Checks either mouse pointer is over the slide or not.
     * @return true if mouse pointer is over the slide and false otherwise.
     */
    public synchronized boolean isSwitched() {
        return switched;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component add(Component comp) {
        comp.setBackground(background);
        comp.setSize(comp.getPreferredSize());
        removeAll();
        super.add(comp);
        setSize(getPreferredSize());
        Insets iss = getBorder().getBorderInsets(this);
        comp.setLocation(iss.left + 5, iss.top + 5);
        return comp;
    }
    
    private void adjustCenterPoint(Point p) {
        WindowUtils.adjustCenterPoint(slideInstance, p);
    }
    
    @Override
    /**
     * {@inheritDoc}
     */
    public Dimension getPreferredSize() {
        Dimension d = this.getComponent(0).getPreferredSize();
        if (getBorder() != null) {
            Insets iss = getBorder().getBorderInsets(this);
            d.width += iss.left + iss.right;
            d.height += iss.top + iss.bottom;
        }
        d.width += 10;
        d.height += 10;
        return d;
    }

    public void setSwitched(boolean switching) {
        switched = switching;        
        setBorder(switched ? activeBorder : inactiveBorder);
    }

    /**
     * Sets color to be used as a background.
     * 
     * @param color to be used as a background.
     */
    public void setColor(Color color) {
        this.setBackground(color);
        getComponent(0).setBackground(color);
        background = color;
    }
    
    /**
     * Returns node's background color.
     * 
     * @return node's background color.
     */
    public Color getColor() {
        return background;
    }
}