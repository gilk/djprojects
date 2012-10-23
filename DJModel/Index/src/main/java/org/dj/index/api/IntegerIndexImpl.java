/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.index.api;

import com.google.common.collect.Range;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Iterator;
import org.dj.index.IndexIterator;
import org.dj.index.RangeIterator;

/**
 *
 * @author djabry
 */
public class IntegerIndexImpl implements IntegerIndex,Serializable{
    private int upperLimit;
    private int lowerLimit;
    private final BitSet bS;
    
    public IntegerIndexImpl(){
        this(100);
        
    }
    
    public IntegerIndexImpl(int size){
        
       this(0, size-1);
    }
    
    public IntegerIndexImpl(int lowerLimit, int upperLimit){

        bS = new BitSet(upperLimit-lowerLimit+1);
        this.lowerLimit=lowerLimit;
        this.upperLimit=upperLimit;
    }

    @Override
    public int getCardinality() {
        return bS.cardinality();
    }

    @Override
    public boolean contains(Integer obj) {
        return this.get(obj);
    }
    
    private boolean get(int val){
        if(val>upperLimit||val<lowerLimit){
           return false; 
        }
        return bS.get(val-lowerLimit);
        
    }
    
    

    @Override
    public void put(Integer obj) {
        
        this.set(obj, true);
    }
    
    private void set(Integer obj, boolean tf){
        if(obj>upperLimit||obj<lowerLimit){
           throw new IllegalRangeException(); 
        }
        this.bS.set(obj-lowerLimit, tf);
    }

    @Override
    public void remove(Integer obj) {
        
        this.set(obj, false);
    }

    @Override
    public Iterator<Integer> allItems() {
        return new IndexIterator(this);
    }

    @Override
    public Integer getMaxValue() {
        return this.bS.length()-1+lowerLimit;
    }

    @Override
    public Integer getMinValue() {
        return bS.nextSetBit(0)+lowerLimit;
    }

    @Override
    public int size() {
        return this.upperLimit-lowerLimit+1;
    }

    @Override
    public void putRange(int start, int end) {

        this.set(start,end,true);
    }

    @Override
    public void removeRange(int start, int end) {
       this.set(start,end,false);
    }
    
    private void set(int start, int end, boolean val){
        
        this.bS.set(start-lowerLimit, end-lowerLimit+1, val);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof IntegerIndex){
            if(o instanceof IntegerIndexImpl){
                IntegerIndexImpl obj = (IntegerIndexImpl) o;
                return this.bS.equals(obj.bS);
            }
        }
        return bS.equals(o);
    }

    @Override
    public int hashCode() {
        return bS.hashCode();
    }

    @Override
    public int getLowerLimit() {
        return this.lowerLimit;
    }

    @Override
    public int getUpperLimit() {
        return this.upperLimit;
    }

    @Override
    public void putIndex(IntegerIndex ix) {
        Iterator<Integer> allItems = ix.allItems();
        while(allItems.hasNext()){
            this.put(allItems.next());
        }
    }

    @Override
    public Iterator<Range<Integer>> allRanges() {
        return new RangeIterator(allItems());
    }

    @Override
    public IntegerIndex copy() {
        IntegerIndex c = new IntegerIndexImpl(getLowerLimit(),getUpperLimit());
        c.putIndex(this);
        return c;
    }
    
    
    
    
    
}
