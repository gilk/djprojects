/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.storage;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.dj.index.api.IntegerIndex;
import org.dj.index.api.IntegerIndices;
import org.dj.matrix.api.DJMatrix;
import org.dj.matrix.api.MatrixStorageAbstr;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class BlockDiagMatrixStorage extends MatrixStorageAbstr{
    private static final IntegerIndices iI = Lookup.getDefault().lookup(IntegerIndices.class);
    private final List<DJMatrix> childMats;
    private final IntegerIndex ix;
    
    private final int n;
    
    public BlockDiagMatrixStorage(List<DJMatrix> childMats){
        super(childMats.iterator().next().getNumberOfRows()*childMats.size(),childMats.iterator().next().getNumberOfRows()*childMats.size());
        this.childMats=childMats;
        this.n=childMats.iterator().next().getNumberOfRows();
        ix = iI.fromRange(0, this.getNumberOfElements()-1);
        
    }

    @Override
    public void initialiseStorage() {
        
    }
    
    private static boolean validateArgs(List<DJMatrix> mats){
        return true;
        
    }

    @Override
    public double getValueAt(int index) {
  
        int rMatIndex = this.rowFromIndex(index)/n;
        int cMatIndex = this.columnFromIndex(index)/n;
        
        double val=0.0;
        
        if(rMatIndex==cMatIndex){

            int r = this.rowFromIndex(index)%n;
            int c = this.columnFromIndex(index)%n;
            val= childMats.get(rMatIndex).getValueAt(r, c);
            
        }
        
        
        return val;
    }

    @Override
    public void setValueAt(int index, double value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<Integer> getIndexIterator(boolean allElements) {
        return ix.allItems();
    }

    @Override
    public int getCardinality() {
        return this.getNumberOfElements();
    }

    @Override
    public boolean isDiagonal() {
        return false;
    }

    @Override
    public boolean isSerialized() {
        return false;
    }

    @Override
    public boolean isDense() {
        return true;
    }

    @Override
    public void close() throws IOException {
        
    }
    
   
    
    
}
