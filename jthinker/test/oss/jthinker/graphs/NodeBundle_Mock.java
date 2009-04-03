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

package oss.jthinker.graphs;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collection;
import oss.jthinker.util.Mapping;

/**
 * Minimalistic mock-emulator for NodeBundle interface.
 * 
 * @author iappel
 * @param T type of contained nodes
 */
public class NodeBundle_Mock<T> implements NodeBundle<T> {

    public Collection<T> getAllNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Dimension getAreaSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<T> getIncomeNodes(T target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Mapping<? super T, Rectangle, ?> getMapping() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<T> getOutcomeNodes(T source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int nodeCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
