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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * A directed connection between two {@link JNode}s.
 * 
 * @author iappel
 */
public class JEdge extends AbstractEdge<JNode> {
    private final boolean _conflictsAllowed;
    private JMenuItem _conflictOn, _conflictOff;
    
    /**
     * Creates a new instance of JEdge that connects two given {@link JNode}s
     * and is managed by given {@link JEdgeHost}.
     * 
     * @param nodeA start of edge.
     * @param nodeB end of edge.
     * @param host manager of deletions and reversals of the edge.
     */
    public JEdge(JNode nodeA, JNode nodeB, JEdgeHost host) {
        super(nodeA, nodeB, host, true);
        nodeB.watch(this);
        _conflictsAllowed = host.allowsConflict();
    }

    /** {@inheritDoc} */
    protected JPopupMenu createPopupMenu(final JEdgeHost host) {
        JPopupMenu menu = new JPopupMenu();
        final JEdge instance = this;
        menu.add(new AbstractAction("Reverse") {
            public void actionPerformed(ActionEvent e) {
                host.reverseJEdge(instance);
            }
        });
        
        menu.add(new AbstractAction("Delete") {
            public void actionPerformed(ActionEvent e) {
                host.deleteJEdge(instance);
            }
        });

        if (host.allowsConflict()) {
            _conflictOn = new JMenuItem(new AbstractAction("Conflict") {
                public void actionPerformed(ActionEvent e) {
                    setConflict(true);
                }
            });
            
            _conflictOff = new JMenuItem(new AbstractAction("Not a conflict") {
                public void actionPerformed(ActionEvent e) {
                    setConflict(false);
                }
            });
            
            menu.addSeparator();
            menu.add(_conflictOn);
            menu.add(_conflictOff);
        }
        
        return menu;
    }

    /** {@inheritDoc} */
    @Override
    public JPopupMenu getPopupMenu() {
        JPopupMenu menu = super.getPopupMenu();

        boolean cb = super.isConflict();
        if (_conflictsAllowed) {
            _conflictOff.setVisible(cb);
            _conflictOn.setVisible(!cb);
        }
        
        return menu;
    }
}
