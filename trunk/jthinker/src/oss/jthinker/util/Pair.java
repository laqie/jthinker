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

package oss.jthinker.util;

/**
 * Just a pair of objects.
 * 
 * @author iappel
 * @param TA first object's type
 * @param TB second object's type
 */
public class Pair<TA, TB> {
    public final TA first;
    public final TB second;

    /**
     * Creates a new instance of Pair.
     * 
     * @param a first object
     * @param b second object
     */
    public Pair(TA a, TB b) {
        first = a;
        second = b;
    }

    @Override
    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            Pair pObj = (Pair)obj;
            return _equals(first, pObj.first) &&
                   _equals(second, pObj.second);
        } else {
            return super.equals(obj);
        }
    }

    private boolean _equals(Object a, Object b) {
        if (a == null) {
            return b==null;
        } else {
            return a.equals(b);
        }
    }
    
    private int _code(Object obj) {
        if (obj==null) {
            return 0;
        } else {
            return obj.hashCode();
        }
    }
    
    @Override
    /** {@inheritDoc} */    
    public int hashCode() {
        return _code(first) + _code(second)*424242;
    }

    @Override
    /** {@inheritDoc} */
    public String toString() {
        return "Pair ("+first.toString()+", "+second.toString()+")";
    }
}
