/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.storage;

import org.dj.index.api.IntegerIndexImpl;
import org.dj.index.api.IntegerIndexImpl2;
import org.dj.matrix.api.IndexPool;

/**
 *
 * @author djabry
 */
public class IndexPoolImpl extends IntegerIndexImpl implements IndexPool{
    
    private int rows;
    private int cols;
    
    public IndexPoolImpl(int r, int c){
        super(r*c);
        this.rows=r;
        this.cols=c;
        
    }
    
    private int translate(int r, int c){
        return r*cols+c;
    }

    @Override
    public void put(int row, int col) {
        this.put(translate(row,col));
    }

    @Override
    public void remove(int row, int col) {
       this.remove(translate(row,col));
    }
    
    
    
}
