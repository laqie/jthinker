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

import oss.jthinker.swingutils.WindowUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import oss.jthinker.graphs.NodeBundle;

/**
 * Handler of node group operations.
 * 
 * @author iappel
 */
public class GroupHandler {
    private final Map<JNode, JNodeGroup> grouping;
    private final Map<String, JNodeGroup> groupNames;
    private final NodeBundle<JNode> nodeBundle;
    
    private final JBackground background;
    
    /**
     * Creates a new GroupHandler instance.
     * 
     * @param background pane to host the groups
     * @param nodes available nodes
     */
    public GroupHandler(JBackground background, NodeBundle<JNode> nodes) {
        this.background = background;
        if (nodes == null) {
            throw new NullPointerException();
        }
        nodeBundle = nodes;
        grouping = new HashMap<JNode, JNodeGroup>();
        groupNames = new HashMap<String, JNodeGroup>();
    }

    /**
     * Resolves group by it's name
     * 
     * @param name group's name
     * @return group with the given name
     */
    public JNodeGroup getNodeGroup(String name) {
        JNodeGroup group = groupNames.get(name);
        if (group == null) {
            group = new JNodeGroup(name, background);
            groupNames.put(name, group);
        }
        return group;
    }

    /**
     * Resolves group by it's containee
     * 
     * @param node node, group of which must be resolved
     * @return group of the given node or null if node is
     * not contained in any of the groups
     */
    public JNodeGroup getNodeGroup(JNode node) {
        return grouping.get(node);
    }

    /**
     * Resolves group's name by it's containee
     * 
     * @param node node, group of which must be resolved
     * @return name of the given node's group or null if node is
     * not contained in any of the groups
     */    
    public String getNodeGroupName(JNode node) {
        JNodeGroup group = getNodeGroup(node);
        for (Map.Entry<String, JNodeGroup> item : groupNames.entrySet()) {
            if (group == item.getValue()) {
                return item.getKey();
            }
        }
        return null;
    }
    
    /**
     * Puts node into a node group.
     * 
     * @param name name of the group
     * @param node node to group
     */
    public void group(String name, JNode node) {
        ungroup(node);
        JNodeGroup group = getNodeGroup(name);
        group.addContent(node);
        grouping.put(node, group);
    }

    /**
     * Displays a node selection dialog.
     * 
     * @param node node to group
     */    
    public void selectGroup(JNode node) {
        Collection<String> groups = getGroupList();
        if (groups.isEmpty()) {
            int code = JOptionPane.showConfirmDialog(node,
                    "No groups defined, proceed with creation?",
                    "No groups defined for diagram",
                    JOptionPane.YES_NO_OPTION);
            if (code == JOptionPane.NO_OPTION) {
                return;
            }
            
            displayManagerDialog(WindowUtils.findFrame(node));
            groups = getGroupList();
            if (groups.isEmpty()) {
                return;
            }
        }
        
        JComponent pane;
        final JComboBox combo = new JComboBox();
        for (String s : getGroupList()) {
            combo.addItem(s);
        }
        
        
        final JCheckBox check = new JCheckBox();
        
        String group = getNodeGroupName(node);
        if (group != null) {
            pane = new JPanel(new BorderLayout());
            pane.add(combo, BorderLayout.CENTER);
            String msg = "Remove from '" + group + "'";
            check.setAction(new AbstractAction(msg) {
                public void actionPerformed(ActionEvent e) {
                    combo.setEnabled(!check.isSelected());
                }
            });
            pane.add(check, BorderLayout.SOUTH);
        } else {
            pane = combo;
        }
                
        int code = JOptionPane.showOptionDialog(node, pane,
            "Choose a group for the node",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            null,
            null);
            
        if (code == JOptionPane.CANCEL_OPTION) {
            return;
        }
            
        if (check.isSelected()) {
            ungroup(node);
            return;
        }
        
        group(combo.getSelectedItem().toString(), node);        
    }

    /**
     * Removes a node from the group it's currently in.
     * 
     * @param node node to group
     */
    public void ungroup(JNode node) {
        JNodeGroup group = grouping.get(node);
        if (group != null) {
            grouping.remove(node);
            group.removeContent(node);
        }
    }

    /**
     * Notifies the group border painter that node's position
     * has changed.
     * 
     * @param node node, position of which has changed
     */
    public void updatePosition(JNode node) {
        JNodeGroup group = grouping.get(node);
        if (group != null) {
            group.updatePresentation();
        }
    }

    /**
     * Returns a list of the groups in the handler.
     * 
     * @return list of the groups in the handler.
     */
    public Collection<String> getGroupList() {
        LinkedList<String> result = new LinkedList<String>();
        Set<String> names = groupNames.keySet();
        result.addAll(names);
        return result;
    }

    /**
     * Destroys a group.
     * 
     * @param name group's name
     */
    public void removeGroup(String name) {
        JNodeGroup group = groupNames.get(name);
        if (group == null) {
            return;
        }
        for (JComponent node : group.getContent()) {
            grouping.remove(node);
        }
        groupNames.remove(name);
        group.destroy();
    }
    
    /**
     * Renames a group.
     * 
     * @param nameFrom initial name
     * @param nameTo new name
     * @return true if group was renamed, false if there either no
     * group that matches nameFrom or already a group that matches
     * nameTo
     */    
    public boolean renameGroup(String nameFrom, String nameTo) {
        JNodeGroup group = groupNames.get(nameFrom),
                check = groupNames.get(nameTo);
        if (group == null || check != null) {
            return false;
        }
        group.setTitle(nameTo);
        groupNames.remove(nameFrom);
        groupNames.put(nameTo, group);
        return true;
    }

    /**
     * Destroys all groups that contain no nodes.
     */    
    public void clearEmptyGroups() {
        Set<String> toRemove = new HashSet<String>();
        for (String key : groupNames.keySet()) {
            if (!groupNames.get(key).hasContent()) {
                toRemove.add(key);
            }
        }
        for (String key : toRemove) {
            removeGroup(key);
        }
    }
    
    /**
     * Displays a management dialog.
     * 
     * @param element component, containing frame of which is
     * to be used for calculations of dialog's position and
     * modality.
     */
    public void displayManagerDialog(Component element) {
        Frame frame = WindowUtils.findFrame(element);
        GroupManager manager = new GroupManager(this, nodeBundle, frame);
        manager.setVisible(true);
    }
}
