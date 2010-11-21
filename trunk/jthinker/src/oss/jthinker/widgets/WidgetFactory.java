/*
 * Copyright (c) 2010, Ivan Appel <ivan.appel@gmail.com>
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
import javax.swing.JPopupMenu;

/**
 *
 * @author iappel
 */
public class WidgetFactory {
    private final JEdgeCallback edgeCallback;
    private final JNodeCallback nodeCallback;

    public WidgetFactory(AbstractDiagramOwner diagramOwner) {
        this(diagramOwner, diagramOwner);
    }

    public WidgetFactory(JNodeCallback nodeCallback, JEdgeCallback edgeCallback) {
        this.edgeCallback = edgeCallback;
        this.nodeCallback = nodeCallback;
    }

    public JEdge produceEdge(JNode nodeA, JNode nodeB) {
        JEdge edge = new JEdge(nodeA, nodeB);
        edge.setupEvents();
        nodeA.watch(edge);
        nodeB.watch(edge);
        return edge;
    }

    public JLeg produceLeg(JNode node, JEdge edge) {
        JLeg leg = new JLeg(node, edge);
        leg.setupEvents();
        node.watch(leg);
        return leg;
    }

    public JLink produceWire(JNode node) {
        JLink wire = new JLink(node, false);
        wire.setupEvents();
        node.watch(wire);
        return wire;
    }

    public JNode produceNode(JNodeSpec spec) {
        JNode node = new JNode(nodeCallback, spec);
        node.setComponentPopupMenu(producePopupMenu(node, spec.isEditable()));
        if (spec.getGroup() != null) {
            nodeCallback.getGroupHandler().group(spec.getGroup(), node);
        }
        return node;
    }

    private JPopupMenu producePopupMenu(final JNode node, boolean editable) {
        JPopupMenu menu = new JPopupMenu();

        menu.add(new AbstractAction("Link") {
            public void actionPerformed(ActionEvent e) {
                nodeCallback.onLinkingStarted(node);
            }
        });

        if (editable) {
            menu.add(new AbstractAction("Edit") {
                public void actionPerformed(ActionEvent e) {
                    nodeCallback.editContent(node);
                }
            });
        }

        menu.add(new AbstractAction("Delete") {
            public void actionPerformed(ActionEvent e) {
                nodeCallback.delete(node);
            }
        });

        menu.add(new AbstractAction("Group") {
            public void actionPerformed(ActionEvent e) {
                nodeCallback.getGroupHandler().selectGroup(node);
            }
        });

        return menu;
    }

    public JPopupMenu producePopupMenu(final JLeg leg) {
        JPopupMenu menu = new JPopupMenu();
        menu.add(new AbstractAction("Delete") {
            public void actionPerformed(ActionEvent e) {
                edgeCallback.delete(leg);
            }
        });
        return menu;
    }

    public JPopupMenu producePopupMenu(final JEdge edge) {
        JPopupMenu menu = new JPopupMenu();
        menu.add(new AbstractAction("Reverse") {
            public void actionPerformed(ActionEvent e) {
                edgeCallback.reverse(edge);
            }
        });

        menu.add(new AbstractAction("Delete") {
            public void actionPerformed(ActionEvent e) {
                edgeCallback.delete(edge);
            }
        });

        if (edgeCallback.allowsConflict()) {
            menu.addSeparator();

            if (edge.isConflict()) {
                menu.add(new AbstractAction("Not a conflict") {
                    public void actionPerformed(ActionEvent e) {
                        edge.setConflict(false);
                    }
                });
            } else {
                menu.add(new AbstractAction("Conflict") {
                    public void actionPerformed(ActionEvent e) {
                        edge.setConflict(true);
                    }
                });
            }
        }

        return menu;
    }

    public JPopupMenu producePopupMenu(JLink edge) {
        if (edge instanceof JLeg) {
            return producePopupMenu((JLeg)edge);
        } else if (edge instanceof JEdge) {
            return producePopupMenu((JEdge)edge);
        } else {
            throw new ClassCastException(edge.getClass().toString());
        }
    }
}
