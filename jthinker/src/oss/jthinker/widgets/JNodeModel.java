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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Table model that fills the node editor dialog.
 * 
 * @author iappel
 */
public class JNodeModel implements JAttributeModel {
    private final JNode _edited;
    private final JTextArea _nameField;
    private final JColorComboBox _backColor;
    private final JTextArea _commentField;

    /**
     * Creates a new JNodeModel instance.
     * 
     * @param node node for which the model is created
     */
    public JNodeModel(JNode node) {
        _edited = node;
        _nameField = new JTextArea();
        _nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                _edited.setContent(_nameField.getText());
            }
        });
        _nameField.setRows(7);
        _nameField.setColumns(15);
        _nameField.setWrapStyleWord(true);
        _nameField.setLineWrap(true);
        _nameField.setText(node.getContent());

        _backColor = new JColorComboBox();
        _backColor.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _edited.setColor(_backColor.getItemColor());
            }
        });
        _backColor.setItemColor(node.getColor());
        
        _commentField = new JTextArea();
        _commentField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                _edited.setComment(_commentField.getText());
            }
        });
        _commentField.setRows(7);
        _commentField.setColumns(15);
        _commentField.setWrapStyleWord(true);
        _commentField.setLineWrap(true);
        _commentField.setText(node.getContent());
        _commentField.setText(node.getComment());
    }

    public int getAttributeCount() {
        return 3;
    }

    /**
     * Reloads values from the node.
     */
    public void update() {
        _nameField.setText(_edited.getContent());
        _commentField.setText(_edited.getComment());
        _backColor.setItemColor(_edited.getColor());
    }

    public String getAttributeName(int index) {
        switch (index) {
            case 0:
                return "Name";
            case 1:
                return "Color";
            case 2:
                return "Comment";
            default:
                throw new IndexOutOfBoundsException(index + "/3");
        }
    }

    public JComponent getAttributeEditor(int index) {
        switch (index) {
            case 0:
                return _nameField;
            case 1:
                return _backColor;
            case 2:
                return _commentField;
            default:
                throw new IndexOutOfBoundsException(index + "/3");
        }
    }
}
