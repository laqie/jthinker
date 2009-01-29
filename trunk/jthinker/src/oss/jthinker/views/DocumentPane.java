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

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import oss.jthinker.util.AssociatedTrigger;
import oss.jthinker.util.BoolStringTrigger;
import oss.jthinker.util.CompositeStringTrigger;
import oss.jthinker.util.FilenameTrigger;
import oss.jthinker.util.MutableTrigger;
import oss.jthinker.util.Trigger;
import oss.jthinker.util.TriggerListener;
import oss.jthinker.widgets.ImageProducer;
import oss.jthinker.widgets.JBackgroundPane;
import oss.jthinker.widgets.MouseLocator;

/**
 * Helper container to assist {@link DiagramPane}. Contains tab
 * title-related triggers and {@link MouseLocator} instrumentation.
 * 
 * @author iappel
 */
public abstract class DocumentPane extends JBackgroundPane
                                   implements TriggerListener<Point> {
    private final MutableTrigger<File> filenameTrigger;
    private final MutableTrigger<Boolean> modifiedTrigger;
    private final Trigger<String> tabTitleTrigger;
    private ImageProducer _imageMaker;
    
    /**
     * Creates a new DocumentPane instance with given string as title.
     * 
     * @param title string to use as a title.
     */
    public DocumentPane(String title) {
        filenameTrigger = new MutableTrigger<File>();
        FilenameTrigger tmp = new FilenameTrigger(filenameTrigger);
        tabTitleTrigger = new AssociatedTrigger<String>(title, tmp);
        modifiedTrigger = new MutableTrigger<Boolean>(false);
        
        MouseLocator.getInstance().register(this);
        MouseLocator.getInstance().addStateConsumer(this);
    }
    
   /**
     * Returns trigger that holds was the pane's diagram modified after
     * the last save or not.
     * 
     * @return trigger that holds was the pane's diagram modified after
     * the last save or not.
     */
    public MutableTrigger<Boolean> getModifiedTrigger() {
        return modifiedTrigger;
    }
    
    /**
     * Returns trigger that holds tab title.
     * 
     * @return trigger that holds tab title.
     */
    public Trigger<String> getTitleTrigger() {
        return tabTitleTrigger;
    }

    /**
     * Marks pane's content as modified.
     * @param saved
     */
    public void markModified(boolean saved) {
        modifiedTrigger.setState(!saved);
    }

    /**
     * Returns trigger that holds tab document filename.
     * 
     * @return trigger that holds tab document filename
     */
    public MutableTrigger<File> getFilenameTrigger() {
        return filenameTrigger;
    }

   /**
     * Dispatches click that happened somewhere on the pane (either on
     * the pane itself or any components that propagate events to 
     * parent).
     * 
     * @param e event to proceed
     */
    public abstract void propagateClick(MouseEvent e);

    /** {@inheritDoc} 
     * @param c component to add
     */
    @Override
    public Component add(Component c) {
        if (c.getParent() == this) {
            return c;
        }
        super.add(c);
        MouseLocator.getInstance().register(c);
        markModified(false);
        return c;
    }

    /** {@inheritDoc} 
     * @param c component to remove
     */
    @Override
    public void remove(Component c) {
        MouseLocator.getInstance().deregister(c);
        super.remove(c);
        validate();
        repaint();
    }
    
    /**
     * Checks either document is saved or not.
     * 
     * @return true is document is saved and false otherwise.
     */
    public boolean isSaved() {
        return !this.getModifiedTrigger().getState();
    }

    /**
     * Creates a trigger that holds document's title. This trigger's
     * state is title with "(*)" added when document is not saved.
     * 
     * @return trigger that holds document's title
     */
    public Trigger<String> makeTitleTrigger() {
        Trigger<String> title = getTitleTrigger();
        Trigger<Boolean> moded = getModifiedTrigger();
        Trigger<String> modedStr = new BoolStringTrigger(moded, "(*)");
        return new CompositeStringTrigger(title, modedStr);
    }

    /**
     * Returns an image producer associated with this component.
     * 
     * @return image producer of this component.
     */
    public ImageProducer getImageMaker() {
        if (_imageMaker == null) {
            _imageMaker = new ImageProducer(this);
        }
        return _imageMaker;
    }
}
