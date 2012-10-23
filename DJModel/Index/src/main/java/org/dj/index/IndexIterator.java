/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.index;

import java.util.Iterator;
import org.dj.index.api.IntegerIndex;


/**
 *
 * @author djabry
 */
public class IndexIterator implements Iterator<Integer>{
    
    private final IntegerIndex ix;
    private int limit;
    
    public IndexIterator(IntegerIndex ix){
        
        this.ix=ix;
        limit = ix.getMaxValue();
        nextIndex =-1;
        findNext();
    }

    
        
        private int currentIndex;
        private int nextIndex;
        private boolean hasNext;
        

        @Override
        public boolean hasNext() {
            return this.hasNext;
        }

        @Override
        public Integer next() {
            currentIndex = nextIndex;
            findNext();
            return currentIndex;
        }

        @Override
        public void remove() {
            ix.remove(currentIndex);
        }
        
        private void findNext(){

            nextIndex++;
            while(!ix.contains(nextIndex)&&nextIndex<limit){
                
                nextIndex++;
            }
            
            this.hasNext=nextIndex<=limit&&ix.contains(nextIndex);
            
        }

   
    
}
