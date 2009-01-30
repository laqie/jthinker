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

import oss.jthinker.resource.VersionChecker;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import oss.jthinker.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

/**
 * jThinker's entrypoint class + main frame.
 * 
 * @author iappel
 */
public class ThinkerMain extends JFrame {

    private static final Logger logger = Logger.getAnonymousLogger();
    private final MasterView masterView;
    private final HelpView helpView;

    private class WindowCloser extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            applicationStop();
        }
    }

    private ThinkerMain() {
        super("jThinker "+VersionChecker.CURRENT_VERSION);

        masterView = new MasterView();
        add(masterView);
        
        helpView = new HelpView();
        
        initApplicationMenuBar();
        setBounds(100, 100, 500, 300);
        addWindowListener(new WindowCloser());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        masterView.contentChanged(null);
    }

    private void initApplicationMenuBar() {
        JMenu fileMenu = masterView.createApplicationFileMenu();
        JMenu helpMenu = helpView.createApplicationHelpMenu();
        JMenu diaoptMenu = masterView.createApplicationDiagramOptionsMenu();
        
        AbstractAction action = new AbstractAction("Exit") {
            public void actionPerformed(ActionEvent e) {
                applicationStop();
            }
        };

        fileMenu.add(new JMenuItem(action));
        
        JMenuBar result = new JMenuBar();
        result.add(fileMenu);
        result.add(diaoptMenu);
        result.add(Box.createHorizontalGlue());
        result.add(helpMenu);
        setJMenuBar(result);
    }
    
    private void applicationStop() {
        if (masterView.closeAll()) {
            System.exit(0);
        }
    }
    
    /**
     * Entry point of the application.
     * 
     * @param args run command line arguments
     */
    public static void main(String args[]) {
        try {
            String system = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(system);
        } catch (Throwable t) {
            logger.log(Level.WARNING, "Unable to initialize system L&F", t);
        }

        ThinkerMain frame = new ThinkerMain();

        frame.setBounds(100, 100, 500, 300);
        frame.validate();
        frame.setVisible(true);

        VersionChecker.checkVersion();        
    }
}
