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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import oss.jthinker.graphs.OrderingLevel;
import oss.jthinker.tocmodel.DiagramDescription;

import oss.jthinker.tocmodel.DiagramType;
import oss.jthinker.widgets.JXPopupMenu;
import static oss.jthinker.tocmodel.DescriptionStorage.getEntity;
import static oss.jthinker.widgets.ThinkerFileChooser.*;

/**
 * (@link DiagramDeck} that's extended with diagram save/load features.
 * 
 * @author iappel
 */
public class MasterView extends DiagramDeck {
    private static final Logger logger = Logger.getAnonymousLogger();
    private final JXPopupMenu popupMenu;
    private final ApplicationMain container;

    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem saveAsItem;
    private JMenuItem jpegExportItem;
    private JMenuItem jpegHtmlExportItem;
    private JMenuItem groupingItem;
    private JCheckBoxMenuItem orderingOffItem;
    private JCheckBoxMenuItem orderingOverlapItem;
    private JCheckBoxMenuItem numberingItem;
    
    private class NewAction extends AbstractAction {
        private final DiagramDescription description;
        
        private NewAction(DiagramType type) {
            super(getEntity(type).getTitle());
            description = getEntity(type);
        }
        
        public void actionPerformed(ActionEvent e) {
            addLinkPane(description);
        }
    }
    
    private class CloseAction extends AbstractAction {
        private CloseAction() {
            super("Close");
        }

        public void actionPerformed(ActionEvent e) {
            Point p = popupMenu.getLastDisplayLocation();
            int i = indexAtLocation(p.x, p.y);
            DiagramPane pane = getDiagram(i);
            if (pane == null || !pane.askedSave()) {
                return;
            }
            removeTabAt(i);
        }
    }

    private class NumberingOptionAction extends AbstractAction {
        private NumberingOptionAction() {
            super("Numbering of statements");
        }
        
        public void actionPerformed(ActionEvent e) {
            DiagramPane pane = getCurrentDiagram();
            pane.getOptions().invertNumbering();
        }
    }

    private class OrderingOffAction extends AbstractAction {
        private OrderingOffAction() {
            super("Off");
        }

        public void actionPerformed(ActionEvent e) {
            getCurrentDiagram().getOptions().setOrderingLevel(OrderingLevel.OFF);
            orderingOffItem.setSelected(true);
            orderingOverlapItem.setSelected(false);
        }
    }

    private class OrderingOverlapAction extends AbstractAction {
        private OrderingOverlapAction() {
            super("Suppress overlapping");
        }

        public void actionPerformed(ActionEvent e) {
            getCurrentDiagram().getOptions().setOrderingLevel(OrderingLevel.SUPPRESS_OVERLAP);
            orderingOffItem.setSelected(false);
            orderingOverlapItem.setSelected(true);
        }
    }
    
    private class GroupManagementAction extends AbstractAction {
        private GroupManagementAction() {
            super("Node groups management");
        }
        
        public void actionPerformed(ActionEvent e) {
            DiagramPane pane = getCurrentDiagram();
            pane.getLinkController().getGroupHandler().displayManagerDialog(MasterView.this);
        }
    }

    /**
     * Creates a new MasterView instance.
     * 
     * @param mainframe application main frame.
     */
    public MasterView(ApplicationMain appMain) {
        popupMenu = new JXPopupMenu();
        popupMenu.add(new CloseAction());
        setComponentPopupMenu(popupMenu);
        container = appMain;
    }

    /**
     * Saves the current diagram.
     * 
     * @param askName should name be asked if already known
     */
    public void saveCurrent(boolean askName) {
        DiagramPane pane = getCurrentDiagram();
        if (pane == null) {
            return;
        }
        pane.saveDiagram(askName);
        contentChanged(pane);
    }

    /**
     * Creates application's "File" menu.
     * 
     * @return newly created application's menu
     */
    public JMenu createApplicationFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenu newSubMenu = new JMenu("New");
        for (DiagramType type : DiagramType.values()) {
            newSubMenu.add(new NewAction(type));
        }
        fileMenu.add(newSubMenu);

        AbstractAction action = new AbstractAction("Open") {
            public void actionPerformed(ActionEvent e) {
                loadNew();
            }
        };
        openItem = new JMenuItem(action);
        
        action = new AbstractAction("Save") {
            public void actionPerformed(ActionEvent e) {
                saveCurrent(false);
            }
        };
        saveItem = new JMenuItem(action);

        action = new AbstractAction("Save as...") {
            public void actionPerformed(ActionEvent e) {
                saveCurrent(true);
            }
        };
        saveAsItem = new JMenuItem(action);

        action = new AbstractAction("Only image") {
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = chooseSave(JPEG_FILES, PNG_FILES);
                    if (file == null) {
                        return;
                    }
                    getCurrentDiagram().getImageMaker().imageExport(file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        jpegExportItem = new JMenuItem(action);

        action = new AbstractAction("Image + HTML") {
            public void actionPerformed(ActionEvent e) {
                try {
                    File imageFile = chooseSave(JPEG_FILES, PNG_FILES);
                    if (imageFile == null) {
                        return;
                    }
                    File htmlFile = chooseSave(HTML_FILES);
                    if (htmlFile == null) {
                        return; 
                    }     
                    getCurrentDiagram().getImageMaker().imageHtmlExport(imageFile, htmlFile);
                } catch (Exception ex) {
                    ex.printStackTrace(); 
                }
            }
        };
        jpegHtmlExportItem = new JMenuItem(action);

        if (container.localPersistence() || 
            container.globalPersistence())
        {
            fileMenu.addSeparator();
            fileMenu.add(openItem);
            fileMenu.add(saveItem);
            fileMenu.add(saveAsItem);

            JMenu exportMenu = new JMenu("Export to JPEG/PNG");
            exportMenu.add(jpegExportItem); 
            exportMenu.add(jpegHtmlExportItem); 
            fileMenu.add(exportMenu);

            fileMenu.addSeparator();
        }
        
        return fileMenu;
    }

    /**
     * Creates application's "Diagram Options" menu.
     * 
     * @return newly created application's menu
     */
    public JMenu createApplicationDiagramOptionsMenu() {
        JMenu diaoptMenu = new JMenu("Diagram Options");
        numberingItem = new JCheckBoxMenuItem(new NumberingOptionAction());

        diaoptMenu.add(numberingItem);
        JMenu ordering = new JMenu("Diagram layout");
        orderingOffItem = new JCheckBoxMenuItem(new OrderingOffAction());
        ordering.add(orderingOffItem);
        orderingOverlapItem = new JCheckBoxMenuItem(new OrderingOverlapAction());
        ordering.add(orderingOverlapItem);
        diaoptMenu.add(ordering);
        
        diaoptMenu.addSeparator();
        
        groupingItem = new JMenuItem(new GroupManagementAction());
        diaoptMenu.add(groupingItem);
        
        return diaoptMenu;
    }
    
    /**
     * Loads a diagram from file.
     */
    public void loadNew() {
        File file = chooseLoad(JTHINKER_FILES);
        if (file == null) {
            return;
        }
        try {
            addLinkPane(file);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Unable to open", t);
        }
    }

    private void updateMenus(boolean flag) {
        numberingItem.setEnabled(flag);
        jpegExportItem.setEnabled(flag);
        saveAsItem.setEnabled(flag);
        groupingItem.setEnabled(flag);
        orderingOffItem.setEnabled(flag);
        orderingOverlapItem.setEnabled(flag);
    }
    
    @Override
    /** {@inheritDoc} */
    protected void contentChanged(DiagramPane pane) {
        if (pane == null) {
            updateMenus(false);
            saveItem.setEnabled(false);
            container.updateToolBar(null);
        } else {
            saveItem.setEnabled(!pane.isSaved());
            numberingItem.setState(pane.getOptions().isNumberingEnabled());
            updateMenus(true);
            
            OrderingLevel level = pane.getOptions().getOrderingLevel();
            orderingOffItem.setState(level == OrderingLevel.OFF);
            orderingOverlapItem.setState(level == OrderingLevel.SUPPRESS_OVERLAP);
            container.updateToolBar(pane);
        }
    }
}
