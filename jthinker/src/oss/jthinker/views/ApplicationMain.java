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
package oss.jthinker.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import oss.jthinker.diagrams.DiagramView;
import oss.jthinker.diagrams.InteractorActionFactory;

public class ApplicationMain {
    private final MasterView _masterView;
    private final HelpView   _helpView;
    private final EntryPoint _impl;
    private JToolBar _toolbar;

    private static ApplicationMain _singleton;

    public ApplicationMain(EntryPoint impl) {
        _impl = impl;
        _singleton = this;
        _masterView = new MasterView(this);
        _helpView = new HelpView();

        initApplicationMenuBar();

        _impl.add(_masterView, BorderLayout.CENTER);
        _masterView.contentChanged(null);
    }

    protected void updateToolBar(DiagramView diagramView) {
        if (_toolbar != null) {
            _impl.remove(_toolbar);
        }
        _toolbar = InteractorActionFactory.getDiagramToolBar(diagramView);
        if (_toolbar != null) {
            _impl.add(_toolbar, BorderLayout.WEST);
        }
    }

    private void initApplicationMenuBar() {
        JMenu fileMenu   = _masterView.createApplicationFileMenu();
        JMenu helpMenu   = _helpView.createApplicationHelpMenu();
        JMenu diaoptMenu = _masterView.createApplicationDiagramOptionsMenu();

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
        _impl.setJMenuBar(result);
    }

    protected void applicationStop() {
        if (_masterView.closeAll()) {
            _impl.applicationStop();
        }
    }

    protected static boolean localPersistence() {
        return _singleton._impl.localPersistence();
    }

    protected static boolean globalPersistence() {
        return false;
    }
}

