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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import oss.jthinker.graphs.NodeBundle;

/**
 * UI widget for configuring content of a node group.
 * 
 * @author iappel
 */
public class GroupContent extends JPanel implements TableModel, ItemListener {
    private final GroupHandler _groupHandler;
    private final Collection<TableModelListener> _listeners;
    private final JComboBox _groupSelector;
    private final List<JNode> _nodes;
    private final JTable _stateTable;
    private final JScrollPane _stateScroller;
    private String _currentGroup;
    
    /**
     * Creates a new pane for grouping nodes.
     * 
     * @param handler grouping handler
     * @param nodeBundle pack of nodes available for grouping
     */
    public GroupContent(GroupHandler handler, NodeBundle<JNode> nodeBundle) {
        super(new BorderLayout());
        
        _groupHandler = handler;
        _listeners = new LinkedList<TableModelListener>();
        _groupSelector = new JComboBox();
        _nodes = new ArrayList<JNode>(nodeBundle.getAllNodes());

        _stateTable = new JTable(this);
        _stateTable.getColumnModel().getColumn(0).setMaxWidth(100);
        _stateScroller = new JScrollPane(_stateTable);

        JPanel statePane = new JPanel(new BorderLayout());
        statePane.add(_stateScroller, BorderLayout.CENTER);
        statePane.setBorder(new TitledBorder("Group contents"));
        add(statePane, BorderLayout.CENTER);
                
        syncGroupList();
        _groupSelector.addItemListener(this);
    
        JPanel selectorPane = new JPanel(new BorderLayout());
        selectorPane.add(_groupSelector, BorderLayout.CENTER);
        selectorPane.setBorder(new TitledBorder("Node group"));
        add(selectorPane, BorderLayout.NORTH);
    }

    /** {@inheritDoc} */
    public void addTableModelListener(TableModelListener l) {
        _listeners.add(l);
    }

    /** {@inheritDoc} */
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class;
        } else {
            return String.class;
        }
    }

    /** {@inheritDoc} */
    public int getColumnCount() {
        return 2;
    }

    /** {@inheritDoc} */
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return " ";
        } else {
            return "Node";
        }
    }

    /** {@inheritDoc} */
    public int getRowCount() {
        return _nodes.size();
    }

    /** {@inheritDoc} */
    public Object getValueAt(int rowIndex, int columnIndex) {
        JNode n = _nodes.get(rowIndex);
        
        if (columnIndex == 0) {
            String name = _groupHandler.getNodeGroupName(n);
            if (name == null) {
                return false;
            } else {
                return name.equals(_currentGroup);
            }
        } else {
            return n.getDisplayContent();
        }
    }

    /** {@inheritDoc} */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    /** {@inheritDoc} */
    public void removeTableModelListener(TableModelListener l) {
        _listeners.remove(l);
    }

    /** {@inheritDoc} */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            throw new IllegalArgumentException();
        }
        JNode n = _nodes.get(rowIndex);
        if (Boolean.TRUE.equals(aValue)) {
            _groupHandler.group(_currentGroup, n);
        } else {
            _groupHandler.ungroup(n);
        }
    }

    /** {@inheritDoc} */
    public void itemStateChanged(ItemEvent e) {
        _currentGroup = e.getItem().toString();
        TableModelEvent event = new TableModelEvent(this);
        for (TableModelListener l : _listeners) {
            l.tableChanged(event);
        }
    }
    
    /**
     * Updates the list of the available groups using the output
     * from group handler.
     */
    public void syncGroupList() {
        Collection<String> newList = _groupHandler.getGroupList();
        boolean keepSelected = _currentGroup != null &&
                newList.contains(_currentGroup);
        _groupSelector.removeAllItems();
        if (newList.isEmpty()) {
            _groupSelector.setEnabled(false);
            _stateScroller.setVisible(false);
        } else {
            for (String s : newList) {
                _groupSelector.addItem(s);
            }
            if (keepSelected) {
                _groupSelector.setSelectedItem(_currentGroup);
            } else {
                _groupSelector.setSelectedIndex(0);
            }
            _groupSelector.setEnabled(true);
            _stateScroller.setVisible(true);
            _stateTable.validate();
            _stateScroller.validate();
        }
    }
}
