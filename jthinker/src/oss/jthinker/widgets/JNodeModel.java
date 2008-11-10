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
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

/**
 * Table model that fills the node editor dialog.
 * 
 * @author iappel
 */
public class JNodeModel extends AbstractTableModel  {
    private final JNode _edited;
    private final JTextField _nameField;
    private final JColorComboBox _backColor;
    private final JTextField _commentField;

    /**
     * Creates a new JNodeModel instance.
     * 
     * @param node node for which the model is created
     */
    public JNodeModel(JNode node) {
        _edited = node;
        _nameField = new JTextField();
        _nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                _edited.setContent(_nameField.getText());
            }
        });

        _backColor = new JColorComboBox();
        _backColor.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _edited.setColor(_backColor.getItemColor());
            }
        });
        
        _commentField = new JTextField();
        _commentField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                _edited.setComment(_commentField.getText());
            }
        });
    }

    /** {@inheritDpc} */
    public int getColumnCount() {
        return 2;
    }

    /** {@inheritDpc} */
    public int getRowCount() {
        return 3;
    }

    /** {@inheritDpc} */
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex + rowIndex * 2) {
            case 0:
                return "Name";
            case 1:
                return _nameField;
            case 2:
                return "Color";
            case 3:
                return _backColor;
            case 4:
                return "Comment";
            case 5:
                return _commentField;
        }
        throw new IllegalArgumentException();
    }

    @Override
    /** {@inheritDpc} */    
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return JComponent.class;
        }
        throw new IllegalArgumentException();
    }

    @Override
    /** {@inheritDpc} */
    public String getColumnName(int column) {
        return super.getColumnName(column);
    }

    @Override
    /** {@inheritDpc} */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }

    /**
     * Reloads values from the node.
     */
    public void update() {
        _nameField.setText(_edited.getContent());
        _commentField.setText(_edited.getComment());
        _backColor.setItemColor(_edited.getColor());
    }
}
