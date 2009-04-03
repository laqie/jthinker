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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import oss.jthinker.graphs.NodeBundle;

/**
 * Node groups management dialog window.
 * 
 * @author iappel
 */
class GroupManager extends JDialog {
    private final ArrayList<String> names;
    private final GroupHandler handler;
    private final GroupContent _content;

    /**
     * Initializes a new group management dialog window.
     * 
     * @param handler GroupHandler, data from which is to be presented
     * @param parent frame to use for setting modality and position
     */
    public GroupManager(GroupHandler handler, NodeBundle<JNode> nodes, Frame parent) {
        super(parent, "Node groups", true);
        this.handler = handler;
        names = new ArrayList<String>(handler.getGroupList());
        _content = new GroupContent(handler, nodes);
        setupVisual(parent.getBounds());
    }

    /**
     * Sets up the visual presentation of the window
     * 
     * @param rect position of parent frame
     */
    protected void setupVisual(Rectangle rect) {
        setLayout(new BorderLayout());
        add(_content, BorderLayout.CENTER);
        
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(new JButton(new AbstractAction("Add group") {
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Enter new group name");
                if (name == null ||
                    name.equals("") ||
                    names.contains(name))
                {
                    return;
                }
                handler.getNodeGroup(name);
                _content.syncGroupList();
            }
        }));
        
        buttons.add(new JButton(new AbstractAction("Remove empty groups") {
            public void actionPerformed(ActionEvent e) {
                handler.clearEmptyGroups();
                _content.syncGroupList();
            }
        }));

        add(buttons, BorderLayout.SOUTH);
        
        setBounds(rect.x + 150,
                         rect.y + 150,
                         rect.width - 50,
                         rect.height - 50);
    }
}
