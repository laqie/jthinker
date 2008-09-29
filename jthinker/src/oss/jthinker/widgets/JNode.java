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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

/**
 * {@see JSlide} instance that contains a {@see JLabelBundle} text and
 * maybe linked to other JNodes with {@see JEdge}s.
 * 
 * @author iappel
 */
public class JNode extends JSlide {
    private final HashSet<JComponent> peers = new HashSet<JComponent>();
    private final JNodeHost host;
    private final JNodeSpec spec;

    private void initListeners() {
        final JNode instance = this;
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    host.endLinking(instance);
                }
            }
        });

        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                host.dispatchJNodeMove();
            }
        });
    }

    private JPopupMenu createPopupMenu(boolean editable) {
        JPopupMenu menu = new JPopupMenu();
        final JNode instance = this;

        menu.add(new AbstractAction("Link") {

            public void actionPerformed(ActionEvent e) {
                host.startLinking(instance);
            }
        });

        if (editable) {
            menu.add(new AbstractAction("Edit") {

                public void actionPerformed(ActionEvent e) {
                    host.editJNodeContent(instance);
                }
            });
        }

        menu.add(new AbstractAction("Delete") {

            public void actionPerformed(ActionEvent e) {
                host.deleteJNode(instance);
            }
        });

        return menu;
    }

    /**
     * Creates a new JNode instance.
     * 
     * @param nodeHost object manager that should host this node
     * @param spec description of node's interior and content
     */
    public JNode(JNodeHost nodeHost, JNodeSpec spec) {
        super(spec);
        setComponentPopupMenu(createPopupMenu(spec.isEditable()));
        host = nodeHost;
        initListeners();
        this.spec = spec;
        setLocation(spec.getSlideCenter());
    }

    /**
     * Adds a peer to the collection of watched components.
     * 
     * @param compo peer to add
     */
    public void watch(JComponent compo) {
        peers.add(compo);
    }

    /**
     * Adds a peer to the collection of watched components.
     * 
     * @param compo peer to add
     * @return true is collection of connections was changed.
     */
    public boolean unwatch(JComponent compo) {
        return peers.add(compo);
    }

    /**
     * Returns a collection of peer components, that this node is attached to.
     * 
     * @return a collection of peers, that this node is attached to.
     */    
    public Set<JComponent> getPeers() {
        HashSet<JComponent> result = new HashSet<JComponent>();
        result.addAll(peers);
        return result;
    }
    
    @Override
    /** {@inheritDoc} */
    public void paint(Graphics g) {
        super.paint(g);
        for (JComponent c : peers) {
            Graphics peerG = g.create();
            peerG.translate(c.getX() - getX(), c.getY() - getY());
            c.paint(peerG);
        }
    }

    public JNodeSpec getNodeSpec() {
        return spec.clone(spec.getContent(), GeometryUtils.computeCenterPoint(this));
    }
}
