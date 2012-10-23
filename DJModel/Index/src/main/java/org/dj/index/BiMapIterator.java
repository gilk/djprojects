/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.index;

import com.google.common.collect.BiMap;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author djabry
 */
public class BiMapIterator<T extends Comparable> implements Iterator<T> {
    
    private Iterator<Entry<T,T>> it;
    private Iterator<T> currentIterator;
    
    
    
    public BiMapIterator(BiMap<T,T> m) {
        
        
        it = m.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
        if(!it.hasNext()){
            
            if(currentIterator ==null){
                
                return false;
            }
            
            if(!currentIterator.hasNext()){
                
                return false;
            }
        }
        
        return true;
    }

    @Override
    public T next() {
        
        
        if(!currentIterator.hasNext()){
            Entry<T, T> next = it.next();
            
            Comparable startVal = next.getKey();
            Range<Comparable> closed = Ranges.closed(startVal, next.getValue());
            
            DiscreteDomain dom = DiscreteDomains.integers();
            if(startVal instanceof Long){
                dom = DiscreteDomains.longs();
            }
            
            currentIterator = (Iterator<T>) closed.asSet(dom);

        }
        
        return currentIterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
