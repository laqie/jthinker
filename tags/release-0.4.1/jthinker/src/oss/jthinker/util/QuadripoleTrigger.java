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
 * Two-in two-out trigger circuit. Computes and updates state of outputs
 * from inputs.
 * 
 * @author iappel
 * @param T type of triggers state
 */
public abstract class QuadripoleTrigger<T> implements TriggerListener<T> {
    // Inputs
    private final Trigger<T> inputLeft, inputRight;
    // Outputs
    private final Trigger<T> outputLeft, outputRight;
    //
    private final LinkedList<QuadripoleTriggerListener<? super T>> consumers =
        new LinkedList<QuadripoleTriggerListener<? super T>>();


    /**
     * Creates a new quadripole trigger with two given triggers
     * as inputs.
     * 
     * @param left first input datasource.
     * @param right second input datasource.
     */
    public QuadripoleTrigger(Trigger<T> left, Trigger<T> right) {
        inputLeft = left;
        inputRight = right;
        outputLeft = new MutableTrigger<T>();
        outputRight = new MutableTrigger<T>();
        left.addStateConsumer(this);
        right.addStateConsumer(this);
        left.setSwitched(true);
        try {
            updateOutputs();
        } catch (Throwable t) {
            
        }
    }
    
    /**
     * Actual computation of right output's state.
     * 
     * @param left state of left input.
     * @param right state of right input.
     * @return state for right output
     */
    protected abstract T computeRightState(T left, T right);
    
    /**
     * Actual computation of left output's state.
     * 
     * @param left state of left input.
     * @param right state of right input.
     * @return state for left output
     */
    protected abstract T computeLeftState(T left, T right);
    
    /**
     * Updates data on outputs.
     */
    protected void updateOutputs() {
        T left = inputLeft.getState(), right = inputRight.getState();
        outputLeft.setState(computeLeftState(left, right));
        outputRight.setState(computeRightState(left, right));
    }
    
    /** {@inheritDoc} */
    public void stateChanged(TriggerEvent<? extends T> event) {
        updateOutputs();
    }

    /**
     * Returns the left output.
     * 
     * @return left output trigger.
     */
    public Trigger<T> getLeftOutput() {
        return outputLeft;
    }

    /**
     * Returns the right output.
     * 
     * @return right output trigger.
     */    
    public Trigger<T> getRightOutput() {
        return outputRight;
    }

    /**
     * Returns the left input.
     * 
     * @return left input trigger.
     */    
    public Trigger<T> getLeftInput() {
        return inputLeft;
    }

    /**
     * Returns the right input.
     * 
     * @return right input trigger.
     */    
    public Trigger<T> getRightInput() {
        return inputRight;
    }

    public void addStateConsumer(QuadripoleTriggerListener<T> listener) {
        QuadripoleTriggerConnector<T> connector = new
                QuadripoleTriggerConnector(this, listener);
        connector.attach();
    }
}
