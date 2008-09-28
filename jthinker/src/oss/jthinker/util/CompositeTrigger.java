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
 * Trigger that integrates output from two separate triggers into
 * one event stream.
 * 
 * @author iappel
 * @param T trigger's state type
 */
public abstract class CompositeTrigger<T> extends Trigger<T>
        implements TriggerListener<T>, Mixer<T> {
    private Trigger<T> first, second;
    
    /**
     * Creates a new instance of the trigger.
     * 
     * @param t1 First trigger to use as a datasource.
     * @param t2 Second trigger to use a a datasource.
     */
    public CompositeTrigger(Trigger<T> t1, Trigger<T> t2) {
        first = t1;
        second = t2;
        t1.addStateConsumer(this);
        t2.addStateConsumer(this);
        setState(mix(t1.getState(), t2.getState()));
    }

    /**
     * Dispatches state change of the datasources.
     * @param event state change event (which is actually ignored).
     */
    public void stateChanged(TriggerEvent<? extends T> event) {
        setState(mix(first.getState(), second.getState()));
    }

    /** {@inheritDoc} */
    public abstract T mix(T arg1, T arg2);
}
