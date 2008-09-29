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

package oss.jthinker.widgets;

import oss.jthinker.util.Trigger;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Adapter of {@link Component} to {@link Trigger}
 * interface with {@link Point} as a unit of state. It's important to keep
 * in mind that central point of component is used as it's "point state",
 * not anything else. Another important point is that trigger is using
 * asynchronous AWT event queue as information source, that's why
 * update of component's bounds doesn't cause immediate update of the trigger.
 * 
 * @author iappel
 */
public class ComponentTrigger extends Trigger<Point> {
    private final Component component;

    /**
     * Extension of {@link ComponentAdapter} that fires state update
     * on component's location/size change.
     */
    private class PositionChangeListener extends ComponentAdapter {
        @Override
        public void componentMoved(ComponentEvent e) {
            updateLocation();
        }

        @Override
        public void componentResized(ComponentEvent e) {
            updateLocation();
        }
    }

    /**
     * Creates a new instance of ComponentTrigger around provided component.
     * 
     * @param base component to attach trigger to
     */
    public ComponentTrigger(Component base) {
        if (base == null) {
            throw new IllegalArgumentException("Null component not allowed");
        }
        component = base;
        component.addComponentListener(new PositionChangeListener());
        updateLocation();
    }

    /** 
     * Immediately calculates component's location and updates trigger state
     * accordingly.
     */
    public void updateLocation() {
        Point newValue = GeometryUtils.computeCenterPoint(component);
        setState(newValue);
    }

    @Override
    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        if (obj instanceof ComponentTrigger) {
            ComponentTrigger ct = (ComponentTrigger)obj;
            return component.equals(ct.component);
        } else {
            return false;
        }
    }

    @Override
    /** {@inheritDoc} */
    public int hashCode() {
        if (component == null) {
            return 0;
        } else {
            return component.hashCode();
        }
    }
}
