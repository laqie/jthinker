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

package oss.jthinker.datamodel;

import oss.jthinker.swingutils.WindowUtils;
import java.awt.Color;
import java.awt.Point;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import oss.jthinker.widgets.JLabelBundle;
import oss.jthinker.datamodel.BorderType;

/**
 * A {@link JNode} construction specification.
 * 
 * @author iappel
 */
public class JNodeData extends JSlideData implements XMLStored {
    private final boolean editable;
    private final String content, comment, groupName;

    /**
     * Creates a new JNodeSpec instance.
     * 
     * @param borderType type of border around the node
     * @param editable defines, is node's content editable or not
     * @param content string content of the node
     * @param center desired center point for the slide
     */
    public JNodeData(BorderType borderType, boolean editable, String content,
            Point center) {
        this(borderType, editable, content, center,
                WindowUtils.getDefaultBackground(), "", null);
    }
    
    
    /**
     * Creates a new JNodeSpec instance.
     * 
     * @param borderType type of border around the node
     * @param editable defines, is node's content editable or not
     * @param content string content of the node
     * @param center desired center point for the slide
     * @param color background color for the node
     * @param comment tooltip comment
     * @param nodeGroup group to which current node belongs
     */
    public JNodeData(BorderType borderType, boolean editable, String content,
            Point center, Color color, String comment, String nodeGroup) {
        super(center, borderType, color);
        this.editable = editable;
        String newContent = content == null ? "" : content;
        newContent = borderType == BorderType.ELLIPSE ? "          " : newContent;
        this.content = newContent;
        this.comment = comment == null ? "" : comment;
        this.groupName = nodeGroup;
    }

    /**
     * Returns desired content for the node.
     * 
     * @return desired content for the node.
     */
    public String getContent() {
        return content;
    }

    public String getGroup() {
        return groupName;
    }
    
    public String getComment() {
        return comment;
    }
    
    /**
     * Returns, should node be editable or not.
     * 
     * @return desired modifyability of the node.
     */
    public boolean isEditable() {
        return editable;
    }
    
    /**
     * Returns a component to put into the node.
     * 
     * @return a component to put into the node.
     */
    protected JLabelBundle createComponent() {
        return new JLabelBundle(content);
    }

    /**
     * Creates a new JNodeSpec with the same border type and editability,
     * but with other content and placement.
     * 
     * @param otherContent content for new node
     * @param otherCenter place for new node
     * @return newly cloned JNodeSpec instance
     */
    public JNodeData clone(String otherContent, Point otherCenter) {
        BorderType borderType = super.getBorderType();
        return new JNodeData(borderType, editable, otherContent, otherCenter);
    }
    
    /**
     * Creates a new JNodeSpec with the same border type and editability,
     * but with other content and placement.
     * 
     * @param otherContent content for new node
     * @param otherCenter place for new node
     * @param otherColor color for new node
     * @param otherComment comment for new node
     * @param otherNode node group for new node
     * @return newly cloned JNodeSpec instance
     */
    public JNodeData clone(String otherContent, Point otherCenter, Color otherColor, String otherComment, String otherGroup) {
        BorderType borderType = super.getBorderType();
        return new JNodeData(borderType, editable, otherContent, otherCenter, otherColor, otherComment, otherGroup);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        BorderType borderType = super.getBorderType();
        sb.append(borderType.name() + "." + borderType.ordinal() + ".");
        sb.append(editable + ".");
        Point p = getSlideCenter();
        sb.append(p.x + "." + p.y + " $ ");
        sb.append(content);
        sb.append(" $ " + super.getBackground());
        sb.append(" $ " + comment);
        return sb.toString();
    }

    @Override
    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        if (obj instanceof JNodeData) {
            JNodeData nspec = (JNodeData)obj;
            return nspec.editable == editable &&
                    nspec.content.equals(content) &&
                    nspec.comment.equals(comment) &&
                    super.equals(obj);
        } else {
            return super.equals(obj);
        }
    }

    @Override
    /** {@inheritDoc} */
    public int hashCode() {
        return super.hashCode() + content.hashCode() + comment.hashCode();
    }

    /** {@inheritDoc} */
    public Element saveToXML(Document document) {
        Element result = document.createElement("node");
        result.setAttribute("type", getBorderType().toString());
        result.setAttribute("editable", Boolean.toString(editable));
        Element point = XMLUtils.toXML(getSlideCenter(), document);
        result.appendChild(point);
        
        Element contentNode = document.createElement("content");
        contentNode.setAttribute("text", content);
        result.appendChild(contentNode);

        Element commentNode = document.createElement("comment");
        commentNode.setAttribute("text", comment);
        result.appendChild(commentNode);

        Element colorNode = XMLUtils.toXML(this.getBackground(), document);
        result.appendChild(colorNode);

        if (groupName != null) {
            Element groupNode = document.createElement("group-name");
            groupNode.setAttribute("text", groupName);
            result.appendChild(groupNode);
        }
        
        return result;
    }

    /**
     * Loads a specification from XML data.
     * 
     * @param data XML node to get data from.
     * @return loaded node spec
     */
    public static JNodeData loadInstance(Node data) {
        if (!data.getNodeName().equals("node")) {
            throw new IllegalArgumentException(data.getNodeName());
        }

        NamedNodeMap map = data.getAttributes();
        String typeStr = map.getNamedItem("type").getNodeValue();
        BorderType type = BorderType.valueOf(typeStr);
        String editStr = map.getNamedItem("editable").getNodeValue();
        boolean edit = Boolean.valueOf(editStr);

        NodeList children = data.getChildNodes();

        String content = null;
        Point center = null;
        Color color = null;
        String comment = null;
        String nodeGroup = null;

        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeName().equals("content")) {
                content = n.getAttributes().getNamedItem("text").getNodeValue();
            } else if (n.getNodeName().equals("color")) {
                color = XMLUtils.toColor(n);
            } else if (n.getNodeName().equals("comment")) {
                comment = n.getAttributes().getNamedItem("text").getNodeValue();
            } else if (n.getNodeName().equals("center")) {
                center = XMLUtils.toPoint(n);
            } else if (n.getNodeName().equals("group-name")) {
                nodeGroup = n.getAttributes().getNamedItem("text").getNodeValue();
            }
        }

        if (!edit) {
            content = "";
        }

        return new JNodeData(type, edit, content, center, color, comment, nodeGroup);
    }

    public NodeType getNodeType() {
        BorderType borderType = getBorderType();
        for (NodeType nt : NodeType.values()) {
            if (nt.getBorderType().equals(borderType)) {
                return nt;
            }
        }
        return null;
    }
}
