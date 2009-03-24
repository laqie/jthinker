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

package oss.jthinker.diagrams;

import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.AbstractAction;
import javax.swing.JToolBar;

/**
 * Factory of actions bound to interactor's methods.
 * 
 * @author iappel
 */
public class InteractorActionFactory {
    private final DiagramInteractor _interactor;

    /**
     * Creates a new instance of InteractorActionFactory.
     * 
     * @param interactor wrapped interactor
     */
    public InteractorActionFactory(DiagramInteractor interactor) {
        _interactor = interactor;
    }

    /**
     * Creates a "Suggest reasons" action.
     * 
     * @return a "Suggest reasons" action
     */
    public AbstractAction makeReasonsAction() {
        return new AbstractAction("Suggest reasons") {
            public void actionPerformed(ActionEvent e) {
                _interactor.suggestReasons();
            }
        };
    }

    /**
     * Creates a "Suggest ellipse" action.
     * 
     * @return a "Suggest ellipse" action
     */    
    public AbstractAction makeEllipseAction() {
        return new AbstractAction("Suggest ellipse") {
            public void actionPerformed(ActionEvent e) {
                _interactor.suggestEllipse();
            }
        };
    }

    /**
     * Creates an "Add undesired effect" action.
     * 
     * @return an "Add undesired effect" action
     */    
    public AbstractAction makeUDEAction() {
        return new AbstractAction("Add undesired effect") {
            public void actionPerformed(ActionEvent e) {
                _interactor.addUndesiredEffect();
            }
        };
    }
    
    private static JToolBar createToolBar(DiagramView diagram) {
        DiagramInteractor interactor = new DiagramInteractor(diagram);
        InteractorActionFactory actionFactory = interactor.getActionFactory();
        switch(diagram.getDiagramType()) {
            case CURRENT_REALITY_TREE:
                JToolBar toolBar = new JToolBar();
                toolBar.setOrientation(JToolBar.VERTICAL);
                toolBar.add(actionFactory.makeUDEAction());
                toolBar.addSeparator();;
                toolBar.add(actionFactory.makeReasonsAction());
                toolBar.addSeparator();;
                toolBar.add(actionFactory.makeEllipseAction());
                return toolBar;
            default:
        }
        return null;
    }

    private static final Map<DiagramView, JToolBar> viewToolbarMap =
        new WeakHashMap<DiagramView, JToolBar>();
    
    /**
     * Returns a toolbar for common actions with the diagram view.
     * 
     * @param diagram diagram to attach toolbar to
     * @return diagram's toolbar
     */
    public static JToolBar getDiagramToolBar(DiagramView diagram) {
        if (diagram == null) {
            return null;
        }
        JToolBar toolBar = viewToolbarMap.get(diagram);
        if (toolBar == null) {
            toolBar = createToolBar(diagram);
            if (toolBar == null) {
                return null;
            }
            viewToolbarMap.put(diagram, toolBar);
        }
        return toolBar;
    }
}
