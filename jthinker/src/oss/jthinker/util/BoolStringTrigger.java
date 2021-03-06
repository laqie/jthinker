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
 * Derived trigger that converts boolean values into string ones,
 * two strings are used as a presentations for true and false.
 * 
 * @author iappel
 */
public class BoolStringTrigger extends DerivedTrigger<Boolean, String> {
    private final String _trueText, _falseText;

    /**
     * Creates a new instance of BoolStringTrigger. Empty string is
     * used to represent false value.
     * 
     * @param input Boolean trigger to be used as input
     * @param trueText text to represent true value
     */
    public BoolStringTrigger(Trigger<Boolean> input, String trueText) {
        this(input, trueText, "");
    }    
    
    /**
     * Creates a new instance of BoolStringTrigger.
     * 
     * @param input Boolean trigger to be used as input
     * @param trueText text to represent true value
     * @param falseText text to represent false value
     */
    public BoolStringTrigger(Trigger<Boolean> input, String trueText, String falseText) {
        super(input);
        _trueText = trueText;
        _falseText = falseText;
        fireUpdate();
    }

    @Override
    /** {@inheritDoc} */
    protected String convertValue(Boolean value) {
        if (_trueText == null) {
            return "";
        }
        return value ? _trueText : _falseText;
    }
}
