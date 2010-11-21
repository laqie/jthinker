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

package oss.jthinker.swingutils;

import oss.jthinker.util.Trigger;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Translator of mouse pointer movement events to trigger-driven
 * stream of {@see Point}s. For that purpose it attaches
 * {@see MouseMotionListener}s to monitored components. Ability
 * to unmonitor is also present.
 * 
 * @author iappel
 */
public class MouseLocator extends Trigger<Point> implements MouseMotionListener {
    private static MouseLocator instance;
    
    private MouseLocator() {
        super(new Point(0,0));
        setSwitched(false);
    }

    /**
     * Returns an instance of MouseLocator singleton.
     * @return an instance of MouseLocator singleton
     */
    public static MouseLocator getInstance() {
        if (instance == null) {
            instance = new MouseLocator();
        }
        instance.setSwitched(true);
        return instance;
    }

    /**
     * Starts monitoring mouse movement events on given component.
     * @param component component to monitor.
     */
    public synchronized void register(Component component) {
        component.addMouseMotionListener(this);
    }

    /**
     * Stops monitoring mouse movement events on given component.
     * @param component component to cancel monitoring.
     */    
    public synchronized void deregister(Component component) {
        component.removeMouseMotionListener(this);
    }

    /**
     * {@inhertiDoc}
     * 
     * @param e event to dispatch
     */
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * {@inhertiDoc}
     * 
     * @param e event to dispatch
     */    
    public synchronized void mouseMoved(MouseEvent e) {
        Component comp = e.getComponent();
        Point place = e.getPoint().getLocation();
        place.translate(comp.getX(), comp.getY());
        setState(place);
    }
}
