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
package oss.jthinker.util;

/**
 * Simple structure-like event class for notifying
 * {@link TriggerListener}'s that position of the monitored
 * {@link Trigger} has changed.
 * 
 * @param StateType type of state to hold
 * 
 * @author iappel
 */
public class TriggerEvent<StateType> {
    private final Trigger<StateType> source;
    private final StateType state;
    
    /**
     * Creates a new instance of TriggerEvent.
     * 
     * @param stateful stateful object, whose state has changed
     */
    public TriggerEvent(Trigger<StateType> stateful) {
        this(stateful, null);
    }

    /**
     * Creates a new instance of LocationEvent.
     * 
     * @param stateful stateful object, whose state has changed
     * @param t new state of the object
     */    
    public TriggerEvent(Trigger<StateType> stateful, StateType t) {
        if (stateful == null) {
            throw new IllegalArgumentException("Null locator is not allowed");
        } else {
            source = stateful;
        }
        
        if (t == null) {
            state = stateful.getState();
        } else {
            state = t;
        }
    }

    /**
     * Returns the state of the object. It is not guaranteed that
     * event.getLocation() equals to event.getSource().getLocation() cause
     * state of the object could change after initialization of
     * the event.
     * 
     * @return state of the stateful object
     */
    public StateType getState() {
        return state;
    }

    /**
     * Returns the stateful object, on which event was fired.
     * 
     * @return stateful object, on which event is fired
     */    
    public Trigger<StateType> getSource() {
        return source;
    }
}
