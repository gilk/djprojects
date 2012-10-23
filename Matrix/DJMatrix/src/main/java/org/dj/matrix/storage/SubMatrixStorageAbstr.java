/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.storage;

import com.google.common.collect.Iterators;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import org.dj.matrix.TranslatedIterator;
import org.dj.matrix.api.MatrixStorage;
import org.dj.matrix.api.MatrixStorageAbstr;

/**
 *
 * @author djabry
 */
public abstract class SubMatrixStorageAbstr extends MatrixStorageAbstr implements Serializable, MatrixStorage {

    private final MatrixStorage mS;

    public SubMatrixStorageAbstr(MatrixStorage mS, int numRows, int numCols){
        super(numRows,numCols);
        this.mS= mS;
    }


    abstract int translateRow(int r);
    abstract int interpretRow(int r);
    abstract int translateCol(int c);
    abstract int interpretCol(int c);
    
    
    
    private int translateIndex(int index){
        
        int r = this.translateRow(this.rowFromIndex(index));
        int c = this.translateCol(this.columnFromIndex(index));
        return mS.indexFromRC(r, c);

    }
    
    
    public MatrixStorage getParentStorage(){
        
        return this.mS;
    }
    
    private int interpretIndex(int index){
        int r =this.interpretRow(mS.rowFromIndex(index));
        int c = this.interpretCol(mS.columnFromIndex(index));
        return this.indexFromRC(r, c);
    }

    @Override
    public double getValueAt(int index) {
       return mS.getValueAt(this.translateIndex(index));
    }

    @Override
    public void setValueAt(int index, double value) {
        this.mS.setValueAt(this.translateIndex(index), value);
    }

    @Override
    public Iterator<Integer> getIndexIterator(boolean allElements) {
        return new SubMatrixStorageAbstr.IndexIterator(allElements);
    }

    @Override
    public boolean isSerialized() {
        return mS.isSerialized();
    }

    @Override
    public boolean isDense() {
        return mS.isDense();
    }

    @Override
    public void initialiseStorage() {
        
    }

    private class IndexIterator extends TranslatedIterator {
        
        public IndexIterator(boolean allElements){
            
            super(mS.getIndexIterator(allElements));
        }

        @Override
        public int translateIndex(int index) {
            return interpretIndex(index);
        }
 
    }

    @Override
    public int getCardinality() {
        
        return Iterators.size(this.getIndexIterator(false));
    }

    @Override
    public void close() throws IOException {
        
       
    }
    

    
}
