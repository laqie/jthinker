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

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * Component for presenting groups of nodes. Implemented as a
 * titled border that surrounds associated nodes and is resized
 * as the containees move.
 * 
 * @author iappel
 */
public class JNodeGroup extends JComponent {
    private final TitledBorder border;
    private final Set<JComponent> content;
    private final JBackground holder;
    
    /**
     * Creates a new JNodeGroup instance.
     * 
     * @param title initial title for the group
     * @param holder background, on which group is being painted
     */
    public JNodeGroup(String title, JBackground holder) {
        this.border = new TitledBorder(new EtchedBorder(Color.BLACK, Color.WHITE), title);
        setBorder(border);
        content = new HashSet<JComponent>();
        this.holder = holder;
        holder.addBackground(this);
        updatePresentation();
    }
    
    /**
     * Updates group's title.
     * 
     * @param title new title for the group
     */
    public void setTitle(String title) {
        border.setTitle(title);
        updatePresentation();
    }
    
    /**
     * Recalculates bounds and request a repaint.
     */
    public void updatePresentation() {
        if (content.isEmpty()) {
            setVisible(false);
        } else {
            int up, down, left, right;
            Iterator<JComponent> iterator = content.iterator();
            Rectangle rect = iterator.next().getBounds();
            left = rect.x;
            right = rect.x + rect.width;
            up = rect.y;
            down = rect.y + rect.height;
            while (iterator.hasNext()) {
                iterator.next().getBounds(rect);
                left = Math.min(left, rect.x);
                right = Math.max(right, rect.x + rect.width);
                up = Math.min(up, rect.y);
                down = Math.max(down, rect.y + rect.height);
            }
            setBounds(left-30, up-30, right-left+60, down-up+60);
            setVisible(true);
        }
        holder.repaintBackground();
    }
    
    /**
     * Adds a component to the group.
     * 
     * @param component component to add
     */    
    public void addContent(JComponent component) {
        content.add(component);
        updatePresentation();
    }

    /**
     * Removes a component from the group.
     * 
     * @param component component to remove
     */    
    public void removeContent(JComponent component) {
        content.remove(component);
        updatePresentation();
    }

    /**
     * Returns true if group has at least one associated component.
     * 
     * @return true if group has at least one associated component and
     * false otherwise.
     */    
    public boolean hasContent() {
        return !content.isEmpty();
    }
    
    /**
     * Returns count of associated components.
     * 
     * @return true if group has at least one associated component and
     * false otherwise.
     */    
    public int contentSize() {
        return content.size();
    }
    
    /**
     * Clears content and removes itself from the background.
     */
    public void destroy() {
        content.clear();
        holder.removeBackground(this);
    }

    /**
     * Returns the associated components as a collection.
     * 
     * @return associated components as a collection.
     */
    public Collection<JComponent> getContent() {
        Collection<JComponent> result = new HashSet<JComponent>();
        for (JComponent item : content) {
            result.add(item);
        }
        return result;
    }
}