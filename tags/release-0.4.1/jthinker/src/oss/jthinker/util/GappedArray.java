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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * An array-like datatype that binds indexes to values. Differences from
 * an arbitrary list are:
 * 
 * - removal of an item leaves a gap in the array, so that all other entries
 * are still bound to the same indexes
 * - addition of an item is done not into list's end, but to the least
 * non-negative integer index possible, so gaps get filled by consequent
 * additions
 * - any item is unique (like in set), so that it's index can be found
 * 
 * @author iappel
 * @param T type of contained values
 */
public class GappedArray<T> implements Iterable<T> {
    private final ArrayList<T> _content = new ArrayList<T>();

    /**
     * Gets item at the specified index.
     * 
     * @param i index
     * @return value at specified index
     */
    public T get(int i) {
        return _content.get(i);
    }

    /**
     * Finds index of the item.
     * 
     * @param item value to find
     * @return it's index
     */
    public int locate(T item) {
        if (item == null) {
            return locateGap();
        }
        
        for (int i=0;i<_content.size();i++) {
            if (item.equals(_content.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /** 
     * Locates a first gap in the list.
     * 
     * @return first gap index
     */
    public int locateGap() {
        for (int i=0;i<_content.size();i++) {
            if (_content.get(i) == null) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Adds an item to the container. When there's no such item in the
     * container - it's added and insetion's index is returned. When
     * there're such item in the container already - index of that
     * item is returned instead and the container itself is not altered.
     * Common contract is: container.get(container.add(foo)).equals(foo) for
     * any foo.
     * 
     * @param item item to add
     * @return index, where this item is now
     */
    public int add(T item) {
        int t = locate(item);
        if (t != -1) {
            return t;
        }
        for (int i=0;i<_content.size();i++) {
            if (_content.get(i) == null) {
                _content.set(i, item);
                return i;
            }
        }
        _content.add(item);
        return _content.size() - 1;
    }

    /**
     * Removes an item from the container and leaves a gap instead.
     * 
     * @param item entry to remove
     * @return true if some entry was indeed removed and false if there
     * were no such item initially
     */
    public boolean remove(T item) {
        int t = locate(item);
        if (t == -1) {
            return false;
        }
        _content.set(t, null);
        relaxTail();
        return true;
    }
    
    private void relaxTail() {
        int idx = _content.size() - 1;
        while (idx >= 0) {
            if (_content.get(idx) == null) {
                _content.remove(idx);
                idx--;
            } else {
                break;
            }
        }
    }

    /**
     * Returns container's content as a vector with gaps omitted.
     * 
     * @return container's content as a vector with gaps omitted.
     */
    public List<T> getContent() {
        List<T> ret = new LinkedList<T>();
        for (int i=0;i<_content.size();i++) {
            T t = _content.get(i);
            if (t != null) {
                ret.add(t);
            }
        }
        return ret;
    }
    
    /** {@inheritDoc} */
    public Iterator<T> iterator() {
        return getContent().iterator();
    }
    
    /**
     * Removes all gaps from the list.
     */
    public void relax() {
        List<T> vector = getContent();
        _content.clear();
        _content.addAll(vector);
    }

    /**
     * Returns true if item is in the array and false otherwise.
     * 
     * @param item item to chek
     * @return true if item is in the array and false otherwise.
     */
    public boolean contains(T item) {
        return locate(item) != -1;
    }
}
