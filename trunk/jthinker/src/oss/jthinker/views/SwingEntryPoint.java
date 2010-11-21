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

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import oss.jthinker.interop.VersionChecker;

/**
 * jThinker's entrypoint class + main frame.
 * 
 * @author iappel
 */
public class SwingEntryPoint extends JFrame implements EntryPoint {
    private final String _remoteServer;

    static {
        if ("Mac OS X".equals(System.getProperty("os.name"))) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Test");
        }
    }


    private SwingEntryPoint(String serverName) {
        super("jThinker "+VersionChecker.CURRENT_VERSION);
        
        _remoteServer = serverName;

        setLayout(new BorderLayout());
    }

    /**
     * Entry point of the application.
     * 
     * @param args run command line arguments
     */
    public static void main(String args[]) {
        String serverName = "http://jthinker-server.appspot.com";
        for (String arg : args) {
            if (arg.startsWith("server=")) {
                serverName = arg.substring(7);
            }
        }
        SwingEntryPoint frame = new SwingEntryPoint(serverName);
        ApplicationMain.init(frame);

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ApplicationMain.applicationStop();
            }
        });

        frame.setBounds(100, 100, 700, 500);
        frame.validate();
        frame.setVisible(true);

        if (VersionChecker.checkVersion()) {
            int code = JOptionPane.showConfirmDialog(
                frame,        
                "Would you like to visit project's website?",
                "New version is available",
                JOptionPane.YES_NO_OPTION
            );
            if (code == JOptionPane.YES_OPTION) {
                ApplicationMain.openBrowser();
            }
        }                
    }

    /** {@inheritDoc} */
    public boolean localPersistence() {
        return true;
    }
 
    /** {@inheritDoc} */
    public boolean isTerminatable() {
        return true;
    }

    /** {@inheritDoc} */
    public boolean globalPersistenceWrite() {
        return _remoteServer != null;
    }

    /** {@inheritDoc} */
    public String getServerURL() {
        return _remoteServer;
    }

    /** {@inheritDoc} */
    public void openBrowser(URL url) {
        try {
            Desktop.getDesktop().browse(url.toURI());
        } catch (Throwable t) {
            System.out.println("Unable to display " + url);
        }
    }
}
