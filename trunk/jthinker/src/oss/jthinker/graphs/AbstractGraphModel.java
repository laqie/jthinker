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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import oss.jthinker.util.Pair;
import oss.jthinker.util.UPair;

/**
 * Implementation of {@see ExtendedNodeBundle}'s methods as an abstract class.
 * 
 * @author iappel
 * @param nodeT type of the managed nodes
 * @param edgeT type of the managed edges
 */
public abstract class AbstractGraphModel<nodeT, edgeT> implements GraphModel<nodeT, edgeT> {
    /** {@inheritDoc} */
    public List<nodeT> getRandomSources(int count) {
        List<nodeT> allSources = getAllSources();
        Random rng = new Random();
        if (allSources.size() < count) {
            return null;
        } else if (allSources.size() == count) {
            return allSources;
        }
        List<nodeT> result = new ArrayList<nodeT>();
        for (int i=0;i<count;i++) {
            int index = rng.nextInt(allSources.size());
            result.add(allSources.remove(index));
        }
        return result;
    }
   
    /** {@inheritDoc} */
    public synchronized Pair<nodeT, List<nodeT>> getRandomIncomings(int count) {
        List<nodeT> nodesList = asList(getAllNodes());
        Random rng = new Random();
        while (!nodesList.isEmpty()) {
            int index = rng.nextInt(nodesList.size());
            nodeT target = nodesList.remove(index);
            List<nodeT> incomeList = asList(getIncomeNodes(target));
            if (incomeList.size() < count) {
                continue;
            }
            List<nodeT> result = pickRandom(incomeList, count);
            return new Pair<nodeT, List<nodeT>>(target, result);
        }
        return null;
    }

    /**
     * Converts a random collection to list by either casting
     * or creating a new list.
     * 
     * @param collection random collection
     * @return list with same set of elements
     */
    public static <Q> List<Q> asList(Collection<Q> collection) {
        if (collection instanceof List) {
            return (List<Q>)collection;
        } else {
            List<Q> result = new ArrayList<Q>();
            result.addAll(collection);
            return result;
        }
    }
    
    /**
     * Picks a required number of elements from collection.
     * 
     * @param stuff collection to pick elements from
     * @param count number of items to pick
     * @return list with <b>count</b> randomly picked elements from
     * <b>stuff</b> or null if there are not enough elements
     */
    public static <Q> List<Q> pickRandom(Collection<Q> stuff, int count) {
        if (stuff.size() < count) {
            return null;
        }
        List<Q> converted = asList(stuff);
        if (stuff.size() == count) {
            return converted;
        }
        List<Q> result = new LinkedList<Q>();
        Random rng = new Random();
        for (int i=0;i<count;i++) {
            int index = rng.nextInt(converted.size());
            result.add(converted.remove(index));
        }
        return result;
    }
    
    /** {@inheritDoc} */
    public synchronized List<nodeT> getAllSources() {
        List<nodeT> result = new ArrayList<nodeT>();
        
        for (nodeT target : getAllNodes()) {
            if (getIncomeNodes(target).isEmpty()) {
                result.add(target);
            }
        }
        
        return result;
    }
    
    /** {@inheritDoc} */    
    public edgeT connection(nodeT node1, nodeT node2) {
        for (edgeT edge : getAllEdges()) {
            UPair<nodeT> peers = endpoints(edge);
            if (peers.first == node1 && peers.second == node2) {
                return edge;
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    public boolean isEdgeModelled(edgeT edge) {
        return getAllEdges().contains(edge);
    }

    /** {@inheritDoc} */
    public boolean isNodeModelled(nodeT node) {
        return getAllNodes().contains(node);
    }
    
    /** {@inheritDoc} */
    public int edgeCount() {
        return getAllEdges().size();
    }

    /** {@inheritDoc} */
    public Collection<nodeT> getRandomNodes(int count) {
        return pickRandom(getAllNodes(), count);
    }
}
