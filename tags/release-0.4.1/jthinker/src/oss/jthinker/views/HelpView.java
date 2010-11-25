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

import java.awt.event.ActionEvent;
import java.net.URL;
import java.net.MalformedURLException;
import javax.swing.AbstractAction;
import javax.swing.JMenu;

/**
 * Collection of various Help menu-related functions.
 * 
 * @author iappel
 */
public class HelpView {

    private static class ShowHelpAction extends AbstractAction {

        private final URL _url;

        public ShowHelpAction(String name, String url) throws MalformedURLException {
            super(name);
            _url = new URL(url); 
        }

        public void actionPerformed(ActionEvent e) {
            ApplicationMain.openBrowser(_url);
        }
    }

    private static class HelpMenu extends JMenu {
        public HelpMenu() {
            super("Help");
        }

        public void addItem(String name, String url) {
            ShowHelpAction action = null;
            try { 
                action = new ShowHelpAction(name, url);
            } catch (MalformedURLException murex) {
                return;
            }
            add(action);
        }
    }

    /**
     * Creates application's "Help" menu.
     * 
     * @return newly created application's menu
     */
    public JMenu createApplicationHelpMenu() {
        HelpMenu menu = new HelpMenu();
        menu.addItem("How to...", "http://code.google.com/p/jthinker/wiki/HowTo");
        menu.addSeparator();
        menu.addItem("About", "http://code.google.com/p/jthinker/");
        menu.addItem("License", "http://code.google.com/p/jthinker/wiki/License");
        return menu;
    }
}
