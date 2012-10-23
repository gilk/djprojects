/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.storage;

import java.io.IOException;
import java.util.Iterator;
import org.dj.matrix.api.MatrixStorageAbstr;
import ucar.ma2.Array;

/**
 *
 * @author djabry
 */
public class ArrayMatrixStorage extends MatrixStorageAbstr{
    
    private final Array arr;
    
    public ArrayMatrixStorage(Array arr){
        
        super(arr.getShape()[0], arr.getRank()>1? arr.getShape()[1]:1);
        
        if(arr.getRank()>2){
            
            throw new IllegalArgumentException("Array must not have more than two dimensions");
        }
        
        this.arr=arr;
        
        
        
    }

    @Override
    public void initialiseStorage() {
        
    }

    @Override
    public double getValueAt(int index) {
        return this.arr.getDouble(index);
    }

    @Override
    public void setValueAt(int index, double value) {
        this.arr.setDouble(index, value);
    }

    @Override
    public Iterator<Integer> getIndexIterator(boolean allElements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCardinality() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDiagonal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSerialized() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDense() {
        return true;
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
}
