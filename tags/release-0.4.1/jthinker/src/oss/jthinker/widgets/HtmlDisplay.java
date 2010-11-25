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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

/**
 * Simple dialog window for showing html content. JEditorPane is used
 * for showing the content.
 * 
 * @author iappel
 */
public class HtmlDisplay extends JDialog {
    private final JEditorPane contentPane;
    
    /**
     * Creates a new HtmlDisplay instance.
     * 
     * @param htmlText text to display
     * @param title window title
     */
    public HtmlDisplay(String htmlText, String title) {
        setTitle(title);
        
        setLayout(new BorderLayout());

        contentPane = new JEditorPane("text/html", htmlText);
        
        contentPane.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(contentPane);
        
        add(scrollPane, BorderLayout.CENTER);
        
        scrollPane.setBorder(new LineBorder(Color.DARK_GRAY));
        
        final JDialog instance = this;
        
        JButton button = new JButton(new AbstractAction("OK") {
            public void actionPerformed(ActionEvent e) {
                instance.dispose();
            }
        });
        
        JPanel buttonPane = new JPanel(new FlowLayout());
        buttonPane.add(button);
        
        add(buttonPane, BorderLayout.SOUTH);
    }

    /**
     * Display a dialog.
     * 
     * @param width width of the frame
     * @param height height of the frame
     */
    public void display(int width, int height) {
        setBounds(200, 200, width, height);
        setVisible(true);
    }
}
