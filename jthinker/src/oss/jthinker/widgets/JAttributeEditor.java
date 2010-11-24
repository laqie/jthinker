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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 * Dialog for editing a node's settings.
 * 
 * @author iappel
 */
public class JAttributeEditor extends JPanel {
    private final JNodeModel model;
    
    private JAttributeEditor(JNodeModel nodeModel, final JAttributeEditorContainer container) {
        model = nodeModel;
        
        setLayout(new BorderLayout());
//        setBorder(new TitledBorder("Node editor"));

        JPanel componentsPanel = new JPanel(new BorderLayout());

        JPanel widgetsPanel = initWidgets();
        
        componentsPanel.add(widgetsPanel, BorderLayout.CENTER);
                
        JButton done = new JButton(new AbstractAction("Done") {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        container.hideEditor();
                    }
                });
            }
        });
        
        JPanel dpane = new JPanel(new FlowLayout());
        
        dpane.add(done);
        
        componentsPanel.add(dpane, BorderLayout.SOUTH);

        add(componentsPanel, BorderLayout.NORTH);
    }

    /**
     * Displays an editor for given node.
     * 
     * @param node node to start editing.
     */
    public static void startEditing(JNode node, final JAttributeEditorContainer holder) {
        final JAttributeEditor editor = new JAttributeEditor(new JNodeModel(node), holder);
        editor.setSize(300, 500);
        editor.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                holder.displayEditor(editor);
            }
        });
    }
    
    private JPanel initWidgets() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        for (int i=0; i<model.getAttributeCount(); i++) {
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.add(model.getAttributeEditor(i));
            wrapper.setBorder(new TitledBorder(model.getAttributeName(i)));
            gbc.gridy = i;
            panel.add(wrapper, gbc);
        }

        return panel;
    }
}
