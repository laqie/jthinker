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

package oss.jthinker.views;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import oss.jthinker.diagrams.ComponentManager;
import oss.jthinker.diagrams.DiagramOptionSpec;
import oss.jthinker.diagrams.DiagramSpec;
import oss.jthinker.graphs.GraphEngine;
import oss.jthinker.tocmodel.DiagramType;
import oss.jthinker.diagrams.DiagramView;
import oss.jthinker.diagrams.FileDiagramSpec;
import oss.jthinker.tocmodel.DescriptionStorage;
import oss.jthinker.tocmodel.DiagramDescription;
import oss.jthinker.tocmodel.NodeType;
import oss.jthinker.util.TriggerEvent;
import oss.jthinker.widgets.AbstractEdge;
import oss.jthinker.widgets.JEdge;
import oss.jthinker.widgets.JLeg;
import oss.jthinker.widgets.JNode;
import oss.jthinker.widgets.JNodeAdjuster;
import oss.jthinker.widgets.JWire;
import oss.jthinker.widgets.MouseLocator;
import oss.jthinker.widgets.JXPopupMenu;

import static oss.jthinker.widgets.ThinkerFileChooser.*;

/**
 * {@link DocumentPane}-based implementation of {@link DiagramView}.
 * 
 * @author iappel

 */
public class DiagramPane extends DocumentPane implements DiagramView {
    private static Logger logger = Logger.getAnonymousLogger();
    
    private JWire mouseEdge = null;
    private final JXPopupMenu menu;
    private final ComponentManager linker;
    private final DiagramType type;
    private final DiagramOptions options;
    
    /**
     * Creates a new DiagramPane. Provided diagram description specifies, which
     * types of nodes and connections are allowed in such diagram.
     * 
     * @param description description of the diagram
     */
    public DiagramPane(DiagramDescription description) {
        this(description, description.getTitle(), new DiagramOptionSpec());
    }

    /**
     * Creates a new DiagramPane. Provided diagram description specifies, which
     * types of nodes and connections are allowed in such diagram.
     * 
     * @param description description of the diagram
     * @param title title to use
     * @param optionSpec description of diagram's settings
     */    
    public DiagramPane(DiagramDescription description, String title,
            DiagramOptionSpec optionSpec) {
        super(title);
        
        menu = new JXPopupMenu();
        for (NodeType nk : description.possibleNodes()) {
            menu.add(new AddAction(this, menu, nk));
        }
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                propagateClick(e);
            }
        });
        setLayout(null);
        type = description.getType();
        linker = new ComponentManager(this, type);
        options = new DiagramOptions(this, optionSpec);
        linker.enableNodeNumbering(options.isNumberingEnabled());
    }

    /**
     * Creates a new instance and applies {@link FileDiagramSpec} to it.
     * 
     * @param fspec diagram specification to apply.
     */    
    public DiagramPane(FileDiagramSpec fspec) {
        this(DescriptionStorage.getEntity(fspec.type),
                fspec.file.toString(), fspec.options);        
        linker.setDiagramSpec(fspec);
        markModified(true);
        getFilenameTrigger().setState(fspec.file);
    }
    
    /** {@inheritDoc} */
    public void dispatchMove() {
        int x = 0, y = 0;

        for (JNode n : linker.getAllNodes()) {
            x = Math.max(x, n.getX() + n.getWidth());
            y = Math.max(y, n.getY() + n.getHeight());
        }

        setPreferredSize(new Dimension(x, y));
        getParent().getParent().validate();
    }
    
    /**
     * Turns edges blue when they're pointed and black when they're not
     * pointed any more.
     * 
     * @param event coordinate update from {@link MouseLocator}
     */
    public void stateChanged(TriggerEvent<? extends Point> event) {
        Point p = event.getState();
        boolean flag = true;
        for (AbstractEdge edge : linker.getAllWires()) {
            edge.setSwitched(false);
            if ((edge.distanceToPoint(p) < 5) && flag) {
                edge.setSwitched(true);
                flag = false;
            }
        }
    }

    /**
     * Dispatches click that happened somewhere on the pane (either on
     * the pane itself or any components that propagate events to 
     * parent).
     * 
     * @param e event to proceed
     */
    public void propagateClick(MouseEvent e) {
        AbstractEdge activeEdge = null;
        for (AbstractEdge edge : linker.getAllWires()) {
            if (edge.isSwitched()) {
                activeEdge = edge;
                break;
            }
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (activeEdge == null) {
                return;
            }
            if (activeEdge instanceof JEdge) {
                linker.endLinking((JEdge)activeEdge);
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            JPopupMenu menuToShow = menu;
            if (activeEdge != null) {
                menuToShow = activeEdge.getPopupMenu();
            }
            Point p = e.getComponent().getLocation();
            p.translate(e.getX(), e.getY());
            menuToShow.show(this, p.x, p.y);
        }
    }

    /** {@inheritDoc} */
    public void enableMouseEdge(JNode c) {
        mouseEdge = new JWire(JNodeAdjuster.makeTrigger(c,
                MouseLocator.getInstance()), false);
        super.add(mouseEdge, 0);
        c.watch(mouseEdge);
        mouseEdge.setVisible(true);
    }
    
    /** {@inheritDoc} */
    public void disableMouseEdge(JNode start) {
        mouseEdge.setVisible(false);
        super.remove(mouseEdge);
        start.unwatch(mouseEdge);
        mouseEdge = null;
    }

    /**
     * Returns controller of the view.
     * 
     * @return controller of the view
     */
    public ComponentManager getLinkController() {
        return linker;
    }

    /** {@inheritDoc} */
    public void add(JEdge edge) {
        edge.addMouseListener(new MouseWatcher(edge, this));
        super.add(edge);
        repaint();
    }
    
    /** {@inheritDoc} */
    public void remove(JEdge edge) {
        super.remove(edge);
    }

    /** {@inheritDoc} */
    public void add(final JLeg leg) {
        leg.addMouseListener(new MouseWatcher(leg, this));
        super.add(leg);
    }
    
    /** {@inheritDoc} */
    public void remove(JLeg leg) {
        super.remove(leg);
    }

    /** {@inheritDoc} */
    public void add(JNode node) {
        super.add(node, 0);
        node.validate();
        node.setPreferredSize(node.getPreferredSize());
        validate();
    }
    
    /** {@inheritDoc} */
    public void remove(JNode node) {
        super.remove(node);
    }

    /** {@inheritDoc} */
    public void editNode(JNode node) {
        if (node.defaultEditOperation()) {
            markModified(false);
        }
    }
    
    /**
     * Attempts to save diagram.
     * 
     * @return true if diagram was really saved
     */
    protected boolean askedSave() {
        if (isSaved()) {
            return true;
        }
       
        if (!ApplicationMain.localPersistence() && 
            !ApplicationMain.globalPersistence()) 
        {
            JOptionPane.showMessageDialog(this,
                "It's not possible to save the diagram in demo mode, your changes will be lost...",
                "Sorry...",
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
 
        int option = JOptionPane.showConfirmDialog(this, "Do you want to save "
                + getTitleTrigger().getState());
        
        switch (option) {
            case JOptionPane.YES_OPTION:
                saveDiagram(false);
            case JOptionPane.NO_OPTION:
                return true;
            case JOptionPane.CANCEL_OPTION:
            default:
                return false;
        }
    }
    
    /**
     * Saves the diagram.
     * 
     * @param askName should name be asked if it is already knows
     * @return true if diagram was saved and false otherwise
     */
    protected boolean saveDiagram(boolean askName) {
        if (this.isSaved() && !askName) {
            return true;
        }
        if (!getFilenameTrigger().hasState() || askName) {
            File file = chooseSave(JTHINKER_FILES);
            if (file == null) {
                return false;
            }
            getFilenameTrigger().setState(file);
        }

        File file = getFilenameTrigger().getState();
        DiagramSpec spec = linker.getDiagramSpec();
        spec.options.fill(options.getSpec());
        
        try {
            spec.save(file);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Exception", t);
            JOptionPane.showMessageDialog(this, "Unable to save",
                    "Unable to save", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        getModifiedTrigger().setState(false);
        return true;
    }

    /**
     * Returns the holder of diagram's optional settings.
     * 
     * @return holder of diagram's optional settings.
     */
    public DiagramOptions getOptions() {
        return options;
    }

    /** {@inheritDoc} */
    public DiagramType getDiagramType() {
        return type;
    }
    
    /** {@inheritDoc} */
    public Dimension getAreaSize() {
        return this.getSize();
    }

    /** {@inheritDoc} */
    public GraphEngine<JNode> getGraphEngine() {
        return getLinkController().getGraphEngine();
    }
}
