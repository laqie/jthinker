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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Dialog for editing a node's settings.
 * 
 * @author iappel
 */
public class JNodeEditor extends JDialog implements TableCellRenderer, TableCellEditor {
    private final JNodeModel model;
    
    private JNodeEditor(JNodeModel nodeModel) {
        model = nodeModel;
        
        setLayout(new BorderLayout());
        setTitle("Node editor");
        
        JTable table = initTable();
        
        JScrollPane pane = new JScrollPane(table);

        add(pane, BorderLayout.CENTER);
        
        Dimension d = table.getPreferredSize();
        pane.setPreferredSize(d);
        setPreferredSize(new Dimension(d.width + 100, d.height + 100));
        
        JButton done = new JButton(new AbstractAction("Done") {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        JPanel dpane = new JPanel(new FlowLayout());
        
        dpane.add(done);
        
        add(dpane, BorderLayout.SOUTH);
    }

    /** {@inheritDoc}
     * 
     * @return cell renderer component
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Component) {
            return (Component) value;
        } else {
            return new JLabel(value.toString());
        }
    }

    /** {@inheritDpc} */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof JComponent) {
            return (JComponent)value;
        } else {
            return null;
        }
    }

    /** {@inheritDpc} */
    public void addCellEditorListener(CellEditorListener l) {
    }

    /** {@inheritDpc} */
    public void cancelCellEditing() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDpc} */
    public Object getCellEditorValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDpc} */
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    /** {@inheritDpc} */
    public void removeCellEditorListener(CellEditorListener l) {
    }
    
    /** {@inheritDpc} */
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    /** {@inheritDpc} */
    public boolean stopCellEditing() {
        return true;
    }

    /**
     * Displays an editor for given node.
     * 
     * @param node node to start editing.
     */
    public static void startEditing(JNode node) {
        JNodeModel model = node.getEditorModel();
        Point location = WindowUtils.computeAbsoluteCenterPoint(node);
        JNodeEditor editor = new JNodeEditor(model);
        editor.setAlwaysOnTop(true);
        editor.setLocation(location);
        editor.setSize(300, 200);
        editor.setVisible(true);
    }
    
    private JTable initTable() {
        JTable table = new JTable(model);
        table.setDefaultRenderer(String.class, this);
        table.setDefaultRenderer(JComponent.class, this);
        table.setDefaultEditor(JComponent.class, this);

        TableColumnModel colModel = table.getColumnModel();
        TableColumn attr = colModel.getColumn(0), val = colModel.getColumn(1);
        
        attr.setHeaderValue("Attribute");
        attr.setMaxWidth(75);
        val.setHeaderValue("Value");
        
        return table;
    }
}
