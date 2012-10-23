/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import com.google.common.collect.Range;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import javolution.util.FastSet;
import org.dj.index.api.RangeOperations;
import org.dj.remote.api.HostInfo;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class GBinImpl<T extends HostInfo> implements GBin<T>{
    
    private int capacity;
    private int remainingCapacity;
    private final T hI;
    private final Set<Range<Integer>> ranges;
    private static final RangeOperations rC= Lookup.getDefault().lookup(RangeOperations.class);
    private static final Comparator<Range<Integer>> cmp = new Comparator<Range<Integer>>() {

        @Override
        public int compare(Range<Integer> t, Range<Integer> t1) {
            return t.lowerEndpoint().compareTo(t1.lowerEndpoint());
        }
    };
    
    public GBinImpl(int capacity, T hI){
        
        this.capacity=capacity;
        this.remainingCapacity = capacity;
        this.hI=hI;
        this.ranges = new FastSet<Range<Integer>>();
        
                
    }

    @Override
    public Set<Range<Integer>> getRanges() {
        TreeSet<Range<Integer>> rangesOut = new TreeSet<Range<Integer>>(cmp);
        rangesOut.addAll(ranges);
        return rangesOut;
        
    }
    
    

    @Override
    public boolean putRange(Range<Integer> r) {
        int l = rC.lengthOf(r);
        
        if(l>this.remainingCapacity){
            
            return false;
        }
        
        this.remainingCapacity-=l;
        this.ranges.add(r);
        return true;
    }

    @Override
    public int getRemainingCapacity() {
        return this.remainingCapacity;
    }

    @Override
    public int getTotalCapacity() {
        return capacity;
    }

    @Override
    public T getHostInfo() {
        return hI;
    }
    
}
