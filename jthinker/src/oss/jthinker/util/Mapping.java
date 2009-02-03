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
 * Converter between a value holder and two data types that
 * holder holds.
 * 
 * @author iappel
 * @param holderT class of the container
 * @param primaryInfoT first contained value type
 * @param secondaryInfoT second contained value type
 */
public interface Mapping<holderT, primaryInfoT, secondaryInfoT> {
    /**
     * Fetches primary info from container.
     * 
     * @param holder value holder
     * @return fetched data
     */
    public primaryInfoT fetch(holderT holder);

    /**
     * Converts primary value into secondary.
     * 
     * @param value primary value
     * @return corresponding secondary value
     */
    public secondaryInfoT convert(primaryInfoT value);
    
    /**
     * Assigns primary value to value holder.
     * 
     * General contract for any Mapping<A,B,?> map, any A holder and
     * any B value:
     * <pre>
     *   map.assign(holder, value);
     *   B valueAfter = map.fetch(holder);
     * 
     *   <b>assertEquals</b>(value, valueAfter);
     * </pre>
     * 
     * @param var holder
     * @param value value
     */
    public void assign(holderT var, primaryInfoT value);
    
    /**
     * Injects secondary value into value holder
     * 
     * General contract for any Mapping<A,B,C> map, any A holder and
     * any C value:
     * <pre>
     *   map.inject(holder, value);
     *   B primValueAfter = map.fetch(holder);
     *   C valueAfter = map.convert(primValueAfter);
     * 
     *   <b>assertEquals</b>(value, valueAfter);
     * </pre>
     * 
     * 
     * @param var holder
     * @param value value to inject
     */
    public void inject(holderT var, secondaryInfoT value);
}
