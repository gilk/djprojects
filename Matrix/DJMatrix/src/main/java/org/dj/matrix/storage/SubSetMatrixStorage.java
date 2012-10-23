/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.storage;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import org.dj.matrix.api.MatrixStorage;

/**
 *
 * @author djabry
 */
public class SubSetMatrixStorage extends SubMatrixStorageAbstr{
    
    private final Iterable<Integer> rows;
    private final Iterable<Integer> cols;
    private boolean rowsColsDiag = false;
    
    public SubSetMatrixStorage(MatrixStorage mS, Iterable<Integer> rows, Iterable<Integer> cols){

        super(mS,Iterables.size(rows),Iterables.size(cols));
        this.rows=rows;
        this.cols=cols;
        this.rowsColsDiag=calculateRowsColsDiag();
        
    }


    @Override
    int translateRow(int r) {
        return Iterables.get(rows, r);
    }

    @Override
    int interpretRow(int r) {
        return Iterables.indexOf(cols, Predicates.equalTo(r));
    }

    @Override
    int translateCol(int c) {
        return Iterables.get(cols, c);
    }
    
    private boolean calculateRowsColsDiag() { 
        return this.isSquare();
    
    }

    @Override
    int interpretCol(int c) {
        
        return Iterables.indexOf(cols, Predicates.equalTo(c));
    }

    @Override
    public boolean isDiagonal() {
        return this.getParentStorage().isDiagonal()&&this.rowsColsDiag;
    }
    
}
