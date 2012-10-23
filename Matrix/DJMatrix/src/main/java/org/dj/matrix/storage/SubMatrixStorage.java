/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.storage;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import org.dj.matrix.api.MatrixStorageAbstr;
import org.dj.matrix.TranslatedIterator;
import org.dj.matrix.api.DJMatrixIterator;
import org.dj.matrix.api.MatrixStorage;


/**
 *
 * @author djabry
 */
public class SubMatrixStorage extends MatrixStorageAbstr implements Serializable, MatrixStorage{
    
    private final MatrixStorage mS;
    
    private final int startRow;
    private final int endRow;
    private final int startCol;
    private final int endCol;
    
    public SubMatrixStorage(MatrixStorage mS, int startRow, int endRow, int startCol, int endCol){
        
        super(1+endRow-startRow,1+endCol-startCol);
        this.mS=mS;
        this.startCol=startCol;
        this.endCol=endCol;
        this.startRow=startRow;
        this.endRow=endRow;
        
    }

    
    private int translateRow(int r){
        
        return this.startRow+r;
    }
    
    private int interpretRow(int r){
        
        return r-this.startRow;
    }
    
    private int translateCol(int c){
        
        return this.startCol+c;
    }
    
    private int interpretCol(int c){
        
        return c-this.startCol;
    }
    
    
    
    private int translateIndex(int index){
        
        int r = this.translateRow(this.rowFromIndex(index));
        int c = this.translateCol(this.columnFromIndex(index));
        return mS.indexFromRC(r, c);

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
        return new IndexIterator(allElements);
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
        int i=0;
        
        DJMatrixIterator iterator = this.iterator(false);
        
        while(iterator.hasNext()){
            
            iterator.next();
            i++;
        }
        
        return i;
    }

    @Override
    public boolean isDiagonal() {
        
        //If submatrix is centred on original matrix and original matrix is diagonal
        if(mS.isDiagonal()){
            
            if(this.startCol==this.startRow){
  
                return true;
            } 
            
        }
        
        return false;
    }

    @Override
    public void close() throws IOException {
        
       
    }
    
   
    
}
