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
import java.awt.Point;

/**
 * A {@link JNode} construction specification.
 * 
 * @author iappel
 */
public class JNodeSpec extends JSlideSpec {
    private final boolean editable;
    private final String content, comment;

    /**
     * Creates a new JNodeSpec instance.
     * 
     * @param borderType type of border around the node
     * @param editable defines, is node's content editable or not
     * @param content string content of the node
     * @param center desired center point for the slide
     */
    public JNodeSpec(BorderType borderType, boolean editable, String content,
            Point center) {
        this(borderType, editable, content, center, Color.WHITE, "");
    }
    
    
    /**
     * Creates a new JNodeSpec instance.
     * 
     * @param borderType type of border around the node
     * @param editable defines, is node's content editable or not
     * @param content string content of the node
     * @param center desired center point for the slide
     * @param color background color for the node
     * @param comment tooltip comment
     */
    public JNodeSpec(BorderType borderType, boolean editable, String content,
            Point center, Color color, String comment) {
        super(center, borderType, color);
        this.editable = editable;
        this.content = content == null ? "" : content;
        this.comment = comment == null ? "" : comment;
    }

    /**
     * Returns desired content for the node.
     * 
     * @return desired content for the node.
     */
    public String getContent() {
        return content;
    }

    public String getComment() {
        return comment;
    }
    
    /**
     * Returns, should node be editable or not.
     * 
     * @return desired modifyability of the node.
     */
    public boolean isEditable() {
        return editable;
    }
    
    /**
     * Returns a component to put into the node.
     * 
     * @return a component to put into the node.
     */
    protected JLabelBundle createComponent() {
        return new JLabelBundle(content);
    }

    /**
     * Creates a new JNodeSpec with the same border type and editability,
     * but with other content and placement.
     * 
     * @param otherContent content for new node
     * @param otherCenter place for new node
     * @return newly cloned JNodeSpec instance
     */
    public JNodeSpec clone(String otherContent, Point otherCenter) {
        BorderType borderType = super.getBorderType();
        return new JNodeSpec(borderType, editable, otherContent, otherCenter);
    }
    
    /**
     * Creates a new JNodeSpec with the same border type and editability,
     * but with other content and placement.
     * 
     * @param otherContent content for new node
     * @param otherCenter place for new node
     * @param otherColor color for new node
     * @param otherComment comment for new node
     * @return newly cloned JNodeSpec instance
     */
    public JNodeSpec clone(String otherContent, Point otherCenter, Color otherColor, String otherComment) {
        BorderType borderType = super.getBorderType();
        return new JNodeSpec(borderType, editable, otherContent, otherCenter, otherColor, otherComment);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        BorderType borderType = super.getBorderType();
        sb.append(borderType.name() + "." + borderType.ordinal() + ".");
        sb.append(editable + ".");
        Point p = getSlideCenter();
        sb.append(p.x + "." + p.y + " $ ");
        sb.append(content);
        sb.append(" $ " + super.getBackground());
        sb.append(" $ " + comment);
        return sb.toString();
    }

    @Override
    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        if (obj instanceof JNodeSpec) {
            JNodeSpec nspec = (JNodeSpec)obj;
            return nspec.editable == editable &&
                    nspec.content.equals(content) &&
                    nspec.comment.equals(comment) &&
                    super.equals(obj);
        } else {
            return super.equals(obj);
        }
    }

    @Override
    /** {@inheritDoc} */
    public int hashCode() {
        return super.hashCode() + content.hashCode() + comment.hashCode();
    }
}