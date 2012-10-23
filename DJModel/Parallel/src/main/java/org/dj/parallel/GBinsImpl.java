/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import com.google.common.collect.Sets;
import java.util.Map.Entry;
import java.util.*;
import javolution.util.FastSet;
import org.dj.index.api.IntegerIndex;
import org.dj.index.api.RangeOperations;
import org.dj.remote.api.HostInfo;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */

public class GBinsImpl<H extends HostInfo> implements GBins<GBin<H>>{
    
    private final Set<GBin<H>> bins;
    private int totalCapacity;
    private static final RangeOperations rO = Lookup.getDefault().lookup(RangeOperations.class);
    
    private static final Comparator<GBin> BIN_ASC_ORDER  = new Comparator<GBin>() {

        @Override
        public int compare(GBin t, GBin t1) {
            return ((Integer)t.getRemainingCapacity()).compareTo((Integer)t1.getRemainingCapacity());
        }
    };
    private static final Comparator<Range<Integer>> RANGE_DESC_ORDER = new Comparator<Range<Integer>>() {
        //sorts ranges into descending order
        @Override
        public int compare(Range<Integer> t, Range<Integer> t1) {
            return rO.lengthOf(t1).compareTo(rO.lengthOf(t));
        }
    };
    
    
    
    
    
    public GBinsImpl(Map<H,Integer> capacities) throws IllegalArgumentException{
        
        if(capacities.isEmpty()){
            
            throw new IllegalArgumentException("No bins specified");
        }
        
        totalCapacity = 0;

        
        bins = new FastSet<GBin<H>>(capacities.size());
        
        Iterator<Entry<H, Integer>> iterator = capacities.entrySet().iterator();
        
        while(iterator.hasNext()){
            Entry<H, Integer> next = iterator.next();
            
            Integer cap = next.getValue();
            H hI = next.getKey();
            
            if(Integer.MAX_VALUE-cap<totalCapacity){
                
                throw new IllegalArgumentException("Sum of ranges are too large, must be <= "+Integer.MAX_VALUE);
            }
            
            totalCapacity+=cap;
            bins.add(new GBinImpl<H>(cap,hI));
            
        }
        
    }

    @Override
    public void putRanges(IntegerIndex b) {
        
        Set<Range<Integer>> blocks = Sets.newHashSet(b.allRanges());
        
        
        //This algorithm iterates through the bins ordered by remaining capacity 
        //(smallest first) and places the block into the first available bin 
        //with sufficient capacity. If a space is not found then the block is 
        //split into two pieces, one with the size of the largest remaining 
        //capacity

        //Check that remaining capacity is sufficient
        
        
        //System.out.println("Splittable blocks input size = "+blocks.getSplittables().size());
        
        
        if (getTotalRemainingCapacity() >= rO.lengthOf(blocks)) {

            while (!blocks.isEmpty()) {
                
                Set<Range<Integer>> blocksList = new TreeSet<Range<Integer>>(RANGE_DESC_ORDER);
                blocksList.addAll(blocks);
                
                //Sort bins into ascending order of size;
                Set<GBin<H>> sortedBins = new TreeSet<GBin<H>>(BIN_ASC_ORDER);
                sortedBins.addAll(bins);
                
                Range<Integer> block = blocksList.iterator().next();
                int blockSize = rO.lengthOf(block);
                
                Iterator<GBin<H>> iterator = sortedBins.iterator();

                while (iterator.hasNext()) {

                    GBin<H> bin = iterator.next();

                    if (bin.getRemainingCapacity() >= blockSize) {

                        bin.putRange(block);
                        
                        //System.out.println("Added block to bin: start = "+ block.getStart() +" end = "+block.getEnd() );
                        blocks.remove(block);
                        break;

                    } else if (!iterator.hasNext()) {

                        blocks.remove(block);
                        
                        //Split block into two pieces, the first is the same size as the largest remaining capacity
                        
                        int start = block.lowerEndpoint();
                        int end = block.upperEndpoint();
                        int split = block.lowerEndpoint()+getLargestRemainingCapacity();
                        
                        Range<Integer> b1 = Ranges.closed(start, split-1);
                        Range<Integer> b2 = Ranges.closed(split, end);
                        blocks.add(b1);
                        blocks.add(b2);

                    }
                }
            }
        }
    }

    @Override
    public Set<GBin<H>> getBins() {
        return bins;
    }
    
    
    int getLargestRemainingCapacity(){
        Iterator<GBin<H>> iterator = bins.iterator();
        Integer largestRemCap =0;
        Integer remCap;
        while(iterator.hasNext()){
            
            remCap = iterator.next().getRemainingCapacity();
            if(remCap>largestRemCap){
                
                largestRemCap = remCap;
            }
        }
        
        return largestRemCap;
    }
    
    int getTotalRemainingCapacity(){
        Iterator<GBin<H>> iterator = bins.iterator();
        int remCap = 0;
        while(iterator.hasNext()){
            GBin b = iterator.next();
            remCap+=b.getRemainingCapacity();
        }
        
        return remCap;
        
    }
    
}
