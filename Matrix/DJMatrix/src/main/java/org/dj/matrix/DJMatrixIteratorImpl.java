/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.dj.matrix.api.DJMatrixIterator;
import org.dj.matrix.api.MatrixStorage;



/**
 *
 * @author djabry
 */
public class DJMatrixIteratorImpl implements DJMatrixIterator{
    
    private final MatrixStorage mS;
    private final boolean allElements;
    private final Iterator<Integer> iterator;
    private int currentIndex;
    private double currentVal;
    private int nextIndex;
    private double nextVal;
    private boolean hasNext;
    
    public DJMatrixIteratorImpl(MatrixStorage mS, boolean allElements){
        
        this.mS=mS;
        this.allElements=allElements;
        this.iterator=this.mS.getIndexIterator(allElements);
        findNext();
        
    }

    @Override
        public int getIndex() {
            return currentIndex;
        }

        @Override
        public int getRow() {
            return mS.rowFromIndex(currentIndex);
        }

        @Override
        public int getColumn() {
            return mS.columnFromIndex(currentIndex);
        }

        @Override
        public void set(double val) {
            mS.setValueAt(currentIndex, val);
        }

        @Override
        public boolean isAllElements() {
            return this.allElements;
        }

        @Override
        public boolean hasNext() {
            
             return hasNext;
            
        }
        
        private void findNext(){

                int next = -1;
                
                while(!mS.isInRange(next)&&iterator.hasNext()){
                    
                    next = iterator.next();
                }
                
                if(mS.isInRange(next)){
                    
                    nextIndex = next;
                    nextVal = mS.getValueAt(nextIndex);
                    hasNext = true;
                }else{
                    
                    hasNext=false;
                }
  
            
        }


        @Override
        public Double next() {
            
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            
            currentVal = nextVal;
            currentIndex = nextIndex;
            
            findNext();
            
            return currentVal;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    
}
