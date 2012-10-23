/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.index.api;

import com.google.common.collect.Range;
import java.io.Serializable;
import java.util.Iterator;
import org.dj.index.IndexIterator;
import org.dj.index.IndexIterator;
import org.dj.index.RangeIterator;
import org.dj.index.api.IllegalRangeException;
import org.dj.index.api.IntegerIndex;

/**
 *
 * @author djabry
 */
public class IntegerIndexImpl2 implements IntegerIndex,Serializable{
    
    private boolean[] arr;
    private int cardinality;

    
    public IntegerIndexImpl2(int size){
        
        this.cardinality=0;
        this.arr=new boolean[size];
        
    }
    
    @Override
    public int getCardinality() {
       return cardinality;
    }

    @Override
    public int size() {
        return arr.length;
    }

    @Override
    public void putRange(int start, int end) throws IllegalRangeException {
        
        for(int i = start;i<=end;i++){
            put(i);
        }
    }

    @Override
    public void removeRange(int start, int end) throws IllegalRangeException {
        for(int i = start;i<=end;i++){
            remove(i);
        }
    }

    @Override
    public boolean contains(Integer obj) {
        if(obj>size()-1||obj<0){
            
            return false;
        }
        
        return arr[obj];
    }

    @Override
    public void put(Integer obj) {
        if(!arr[obj]){
            this.cardinality++;
            arr[obj]=true;
        }
        
    }

    @Override
    public void remove(Integer obj) {
        if(arr[obj]){
            
            this.cardinality--;
            arr[obj]=false;
        }
        
    }

    @Override
    public Iterator<Integer> allItems() {
        return new IndexIterator(this);
    }

    @Override
    public Integer getMaxValue() {
        
        
        int i = size()-1;
        
        while(!contains(i)){
            
            i--;
        }
        
        return i;
    }

    @Override
    public Integer getMinValue() {
       
        int i = 0;
        
        while(!contains(i)){
            
            i++;
        }
        
        return i;
    }

    @Override
    public int getLowerLimit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getUpperLimit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putIndex(IntegerIndex ix) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<Range<Integer>> allRanges() {
        return new RangeIterator(allItems());
    }

    @Override
    public IntegerIndex copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    
    
    
    
}
