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
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import oss.jthinker.tocmodel.DiagramDescription;

import oss.jthinker.tocmodel.DiagramType;
import oss.jthinker.widgets.JXPopupMenu;
import static oss.jthinker.tocmodel.DescriptionStorage.getEntity;

/**
 * (@link DiagramDeck} that's extended with diagram save/load features.
 * 
 * @author iappel
 */
public class MasterView extends DiagramDeck {
    private static final Logger logger = Logger.getAnonymousLogger();
    private final JXPopupMenu popupMenu;

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
            if (!pane.askedSave()) {
                return;
            }
            removeTabAt(i);
        }
    }

    /**
     * Creates a new MasterView instance.
     */
    public MasterView() {
        popupMenu = new JXPopupMenu();
        popupMenu.add(new CloseAction());
        setComponentPopupMenu(popupMenu);
    }

    /**
     * Saves the current diagram.
     */
    public void saveCurrent() {
        DiagramPane pane = getCurrentDiagram();
        pane.saveDiagram();
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

        AbstractAction action = new AbstractAction("Save") {
            public void actionPerformed(ActionEvent e) {
                saveCurrent();
            }
        };

        fileMenu.add(new JMenuItem(action));

        action = new AbstractAction("Load") {
            public void actionPerformed(ActionEvent e) {
                loadNew();
            }
        };

        fileMenu.add(new JMenuItem(action));

        return fileMenu;
    }

    /**
     * Loads a diagram from file.
     */
    public void loadNew() {
        JFileChooser chooser = new JFileChooser();
        int code = chooser.showOpenDialog(this);
        if (code != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = chooser.getSelectedFile();
        try {
            addLinkPane(f);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Unable to open", t);
        }
    }
}
