/*
 * Copyright (c) 2010, Ivan Appel <ivan.appel@gmail.com>
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
 *
 * @author iappel
 */
public class QuadripoleTriggerConnector<T> implements TriggerListener<T> {
    private final QuadripoleTrigger<T> source;
    private final QuadripoleTriggerListener<T> target;
    private final Trigger<T> left, right;

    public QuadripoleTriggerConnector(QuadripoleTrigger<T> source, QuadripoleTriggerListener<T> target) {
        this.source = source;
        this.target = target;
        left = source.getLeftOutput();
        right = source.getRightOutput();
    }

    public void attach() {
        left.addStateConsumer(this);
        right.addStateConsumer(this);

        target.leftStateChanged(left.getState());
        target.rightStateChanged(right.getState());
    }

    public void stateChanged(TriggerEvent<? extends T> event) {
        if (event.getSource() == left) {
            target.leftStateChanged(event.getState());
        } else if (event.getSource() == right) {
            target.rightStateChanged(event.getState());
        } else {
            throw new IllegalArgumentException("Unknown source");
        }
    }
}
