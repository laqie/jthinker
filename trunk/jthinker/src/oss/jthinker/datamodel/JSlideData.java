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

package oss.jthinker.datamodel;

import oss.jthinker.swingutils.WindowUtils;
import java.awt.Color;
import java.awt.Point;
import javax.swing.JComponent;
import oss.jthinker.widgets.BorderBuilder;
import oss.jthinker.widgets.JSlide;

/**
 * A {@see JSlide} construction specification.
 * 
 * @author iappel
 */
public abstract class JSlideData {
    private final Point slideCenter;
    private final BorderType borderType;
    private final Color background;
    private JComponent component = null;
    
    /**
     * Creates a new JSlideSpec instance.
     * 
     * @param center desired center coordinate for the slide
     * @param border desired border type for the slide
     * @param color background color for slide
     */
    protected JSlideData(Point center, BorderType border, Color color) {
        slideCenter = center;
        borderType = border;
        background = color == null ? WindowUtils.getDefaultBackground() : color;
    }
    
    /**
     * Returns component to be used in slide.
     * 
     * @return component to be used in slide.
     */
    protected abstract JComponent createComponent();

    /**
     * Returns component to be used in slide.
     * 
     * @return component to be used in slide.
     */    
    public final JComponent getComponent() {
        if (component == null) {
            component = createComponent();
        }
        return component;
    }
    
    /**
     * Returns border builder to be used to construct the node component.
     * 
     * @return border builder to be used to construct the node component.
     */
    public BorderBuilder getBorderBuilder() {
        return BorderBuilder.getInstance(borderType);
    }

    /**
     * Returns desired center coordinate for the slide.
     * 
     * @return desired center coordinate for the slide
     */
    public Point getSlideCenter() {
        return slideCenter;
    }
    
    /**
     * Returns desired border type for the node.
     * 
     * @return desired border type for the node.
     */
    public BorderType getBorderType() {
        return borderType;
    }

    @Override
    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        if (obj instanceof JSlideData) {
            JSlideData slideSpec = (JSlideData)obj;
            return slideSpec.borderType.equals(borderType) &&
                   slideSpec.background.equals(background) &&
                   slideSpec.slideCenter.equals(slideCenter);
        } else {
            return super.equals(obj);
        }
    }

    @Override
    /** {@inheritDoc} */
    public int hashCode() {
        return borderType.hashCode() + slideCenter.hashCode() +
                background.hashCode();
    }

    /**
     * Returns background color for the node.
     * 
     * @return background color for the node.
     */
    public Color getBackground() {
        return background;
    }
}