/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.index;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import java.util.Iterator;

/**
 *
 * @author djabry
 */
public class RangeIterator implements Iterator<Range<Integer>>{
    
    private final Iterator<Integer> it;
    private boolean hasNext;
    int currentVal;
    int nextVal;
    private Range<Integer> currentRange;
    private Range<Integer> nextRange;
    
    public RangeIterator(Iterator<Integer> it){
        
        this.it=it;
        if(it.hasNext()){
            nextVal = it.next();
        }
        findNextRange();
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public Range<Integer> next() {
        currentRange = nextRange;
        findNextRange();
        return currentRange;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("");
    }
    

    
    private void findNextRange(){
        
        int firstVal = nextVal;
        
        int lastVal = firstVal;
        
        while(it.hasNext()){
            int currVal = nextVal;
            nextVal = it.next();
            
            if(nextVal-currVal!=1){
                
                break;
            }else{
                
                lastVal = nextVal;
            }
        }

        nextRange = Ranges.closed(firstVal, lastVal);
    
        hasNext = !(!it.hasNext() &&(nextVal==firstVal));
    }
    
}
