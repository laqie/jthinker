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

import oss.jthinker.swingutils.WindowUtils;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * {@see JSlide} instance that contains a {@see JLabelBundle} text and
 * maybe linked to other JNodes with {@see JEdge}s.
 * 
 * @author iappel
 */
public class JNode extends JSlide {
    private final HashSet<JComponent> peers = new HashSet<JComponent>();
    private final JNodeCallback host;
    private final JNodeSpec spec;
    private WeakReference<JNodeModel> editorModelRef;

    private String comment, content;
    private boolean numbering;
    
    private void initListeners() {
        final JNode instance = this;
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    host.onLinkingDone(instance);
                }
            }
        });

        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                host.onNodeMoved(JNode.this);
            }
        });
    }

    /**
     * Creates a new JNode instance.
     * 
     * @param nodeHost object manager that should host this node
     * @param spec description of node's interior and content
     */
    protected JNode(JNodeCallback nodeHost, JNodeSpec spec) {
        super(spec);
        host = nodeHost;
        initListeners();
        this.spec = spec;
        setLocation(spec.getSlideCenter());
        setComment(spec.getComment());
        content = spec.getContent();
    }

    /**
     * Adds a peer to the collection of watched components.
     * 
     * @param compo peer to add
     */
    public void watch(JComponent compo) {
        peers.add(compo);
    }

    /**
     * Adds a peer to the collection of watched components.
     * 
     * @param compo peer to add
     */
    public void unwatch(JComponent compo) {
        peers.remove(compo);
    }

    /**
     * Returns a collection of peer components, that this node is attached to.
     * 
     * @return a collection of peers, that this node is attached to.
     */    
    public Set<JComponent> getPeers() {
        HashSet<JComponent> result = new HashSet<JComponent>();
        result.addAll(peers);
        return result;
    }
    
    @Override
    /** {@inheritDoc} */
    public void paint(Graphics g) {
        super.paint(g);
        for (JComponent c : peers) {
            Graphics peerG = g.create();
            peerG.translate(c.getX() - getX(), c.getY() - getY());
            c.paint(peerG);
        }
    }

    /**
     * Returns node's border type.
     * 
     * @return node's border type.
     */
    public BorderType getBorderType() {
        return spec.getBorderType();
    }

    /**
     * Returns node's building specification.
     * 
     * @return node's building specification.
     */    
    public JNodeSpec getNodeSpec() {
        String nodeGroup = host.getGroupHandler().getNodeGroupName(this);
        return spec.clone(content, WindowUtils.computeCenterPoint(this),
                getColor(), comment, nodeGroup);
    }
    
    /**
     * Gets textual content of the node.
     * 
     * @return textual content of the node.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets textual content for the node.
     * 
     * @param content textual content of the node.
     */    
    public void setContent(String content) {
        this.content = content;        
        JLabelBundle bundle = (JLabelBundle)this.getComponent(0);
        bundle.setText(getDisplayContent());
        bundle.setSize(bundle.getPreferredSize());
        setSize(getPreferredSize());
        getParent().validate();
    }

    /**
     * Returns true if node has some non-whitespace content.
     * 
     * @return true if node has some non-whitespace content and
     * false otherwise.
     */
    public boolean hasContent() {
        return content.trim().length() != 0;
    }
    
    /**
     * Default operation of edit (via {@link JOptionPane}).
     * 
     * @return true if node content got modified.
     */
    public boolean defaultEditOperation() {
        host.startEditing(this);
        
        return true;
    }

    /**
     * Sets comment for the node.
     * 
     * @param comment text to be used as a tooltip comment.
     */
    public final void setComment(String comment) {
        setToolTipText(comment);
        this.comment = comment;
    }
    
    /**
     * Returns node's comment.
     * 
     * @return text that's used as a tooltip comment.
     */    
    public String getComment() {
        return comment;
    }

    /**
     * Enables showing statement's number.
     * 
     * @param state true if number should be shown and false otherwise
     */
    public void enableNumbering(boolean state) {
        numbering = state;
        setContent(this.content);
    }

    /**
     * Returns the content the way it's displayed (with number)
     * 
     * @return
     */
    public String getDisplayContent() {
        if (numbering && spec.isEditable() && content.trim().length() != 0) {
            return (host.issueIndex(this) + 1) + ". " + content;
        } else {
            return content;
        }
    }
}
