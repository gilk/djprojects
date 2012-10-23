/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.index;

import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import org.dj.index.api.IntegerIndexImpl;
import org.dj.index.api.IntegerIndex;
import org.dj.index.api.IntegerIndices;
import org.dj.index.api.RangeOperations;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service= IntegerIndices.class)
public class IntegerIndicesImpl implements IntegerIndices{
private static final RangeOperations rO = Lookup.getDefault().lookup(RangeOperations.class);
    
    @Override
    public IntegerIndex singleton(int index) {
        IntegerIndexImpl ix = new IntegerIndexImpl(index,index);
        ix.put(index);
        return ix;
        
    }

    @Override
    public IntegerIndex fromRange(int start, int end) {
        IntegerIndex ix = new IntegerIndexImpl(start,end);
        ix.putRange(start, end);
        return ix;
    }

    @Override
    public IntegerIndex empty(int size) {
        return new IntegerIndexImpl(size);
    }

    @Override
    public IntegerIndex empty(int start, int finish) {
        return new IntegerIndexImpl(start,finish);
    }

    @Override
    public IntegerIndex fromRange(Range<Integer> rng) {
        return new IntegerIndexImpl(rng.lowerEndpoint(),rng.upperEndpoint());
    }

    @Override
    public IntegerIndex fromRanges(Iterable<Range<Integer>> rngs) {

        HashSet<Range<Integer>> s = Sets.newHashSet(rngs);
        
        IntegerIndex ix = empty(rO.minValue(rngs),rO.maxValue(rngs));
        
        Iterator<Range<Integer>> iterator = s.iterator();
        while(iterator.hasNext()){
            Range<Integer> next = iterator.next();
            ix.putRange(next.lowerEndpoint(), next.upperEndpoint());
        }
        
        return ix;
        
    }
    
}
