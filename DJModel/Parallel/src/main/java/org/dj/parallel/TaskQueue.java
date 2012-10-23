/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import com.google.common.collect.Range;
import java.util.Iterator;
import org.dj.parallel.GBin;
import org.dj.parallel.api.GSplittableTask;

/**
 *
 * @author djabry
 */
public class TaskQueue<T extends GSplittableTask> implements Iterable<T>{
    
    private final GBin bin;
    private final T tsk;
    
    public TaskQueue(GBin bin, T tsk){
        
        this.bin=bin;
        this.tsk = tsk;
    }
    
    

    @Override
    public Iterator<T> iterator() {
        return new TaskIterator();
    }


    
    private class TaskIterator implements Iterator<T>{
        
        private final Iterator<Range<Integer>> rangeIterator;
        
        public TaskIterator(){
            
            rangeIterator = bin.getRanges().iterator();
        }

        @Override
        public boolean hasNext() {
            return rangeIterator.hasNext();
        }

        @Override
        public T next() {
            Range<Integer> r = rangeIterator.next();
            T t = (T)tsk.duplicate(true);
            t.setRange(r);
            t.setHostInfo(bin.getHostInfo());
            return t;
            
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        
        
    }
    
}
