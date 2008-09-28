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

import java.util.LinkedList;

/**
 * Object that holds some discrete state. Provided functionality is
 * <ul>
 * <li>holding state,
 * <li>changing state
 * <li>registering {@link TriggerListener}'s for notifying on object's update
 * <li>enabling and disabling notification on update.
 * </ul>
 * 
 * Trigger implements {@link Switch} interface. On/off switching of
 * trigger defines, will trigger notify it's listeners on update or not.
 * Trigger is enabled by default.
 * 
 * @author iappel
 * @param T type of the state
 */
public class Trigger<T> implements Switch {
    private final LinkedList<TriggerListener<? super T>> consumers =
            new LinkedList<TriggerListener<? super T>>();
    private T state;
    private boolean switched = true;

    /**
     * Creates a new stateless trigger.
     */
    public Trigger() {
        state = null;
    }
    
    /**
     * Creates a new trigger with given state.
     * 
     * @param initState initial state
     * @throws IllegalArgumentException when initState parameter is null
     */    
    public Trigger(T initState) {
        if (initState == null) {
            throw new IllegalArgumentException("null initial state is not " +
                    "allowed, use argumentless constructor instead");
        }
        state = initState;
    }
    
    /**
     * Returns state of the object.
     * 
     * @return state.
     * @throws IllegalStateException when state has not been set already.
     */
    public synchronized T getState() {
        if (state == null) {
            throw new IllegalStateException("State is not set yet");
        } else {
            return state;
        }
    }

    /**
     * Sets object's state. Method is protected because not any
     * kind of stateful may support external state update.
     * {@link Trigger#fireUpdate() fires update} in the case when object's
     * state is being changed.
     * 
     * @param newState new state to set.
     * @throws IllegalArgumentException when newState is null
     */
    protected synchronized void setState(T newState) {
        if (newState == null) {
            throw new IllegalArgumentException("Null location not allowed");
        }
        
        if (!newState.equals(state)) {
            state = newState;
            fireUpdate();
        }
    }
    
    /**
     * Unsets trigger's state. Method is protected because not any
     * kind of stateful may support external state changes.
     */
    protected synchronized void unsetState() {
        state = null;
    }

    /**
     * Checks, does this trigger has some state or not.
     * 
     * @return true is this trigger has any set state and false otherwise
     */
    public boolean hasState() {
        return (state != null);
    }
    
    /**
     * Adds a consumer to a collection of consumers that should be notified
     * on stateful's update.
     * 
     * @param consumer consumer to add
     * @throws IllegalArgumentException when consumer is null
     */
    public void addStateConsumer(TriggerListener<? super T> consumer) {
        if (consumer == null) {
            throw new IllegalArgumentException("Null consumer not allowed");
        }
        consumers.add(consumer);
    }

    /**
     * Removes a consumer from a collection of consumers that should be
     * notified on object's state update.
     * 
     * @param consumer consumer to remove
     */
    public void removeStateConsumer(TriggerListener<T> consumer) {
        consumers.remove(consumer);
    }

    /**
     * Notifies consumers, that locator's position have changed.
     */
    protected synchronized void fireUpdate() {
        if (!switched) {
            return;
        }
        TriggerEvent<T> e = new TriggerEvent<T>(this);
        for (TriggerListener<? super T> l : consumers) {
            l.stateChanged(e);
        }
    }
    
    /**
     * Returns a total number of attached {@link TriggerListener}'s.
     * 
     * @return number of attacher consumers
     */
    protected int getConsumerCount() {
        return consumers.size();
    }

    /**
     * Returns trigger's current update notification setting.
     * 
     * @return does trigger fires events on state update or not
     */    
    public synchronized boolean isSwitched() {
        return switched;
    }

    /** {@inheritDoc} */
    public void setSwitched(boolean switching) {
        switched = switching;
    }
}
