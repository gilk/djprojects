/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javolution.util.FastList;
import org.dj.parallel.api.Splittable;

/**
 *
 * @author djabry
 */
public class BinsImpl implements Bins {

    private final FastList<Bin> bins = new FastList<Bin>();
    
    private static final Comparator<Bin> binComparator =new Comparator<Bin>(){

            @Override
            public int compare(Bin o1, Bin o2) {
                return o1.getRemainingCapacity().compareTo(o2.getRemainingCapacity());
            }
 
        };
    
    private static final Comparator<Splittable> blockComparator=new Comparator<Splittable>() {

        @Override
        public int compare(Splittable t, Splittable t1) {
            return ((Double)t1.getSize()).compareTo((Double)t.getSize());
        }
    };
    
    
    private double tolerance;

    public BinsImpl(List<? extends Bin> bins,double tolerance) {
        this.bins.addAll(bins);
        this.tolerance=tolerance;
    }

    @Override
    public List<Bin> getBins() {
        return this.bins.unmodifiable();
    }

    private double getTotalRemainingCapacity() {

        double remainingCapacity = 0.0;
        Iterator<Bin> iterator = this.bins.iterator();

        while (iterator.hasNext()) {
            Bin bin = iterator.next();

            remainingCapacity += bin.getRemainingCapacity();
        }

        return remainingCapacity;

    }

    private double getLargestRemainingCapacity() {

        Double largestCapacity = 0.0;
        Iterator<Bin> iterator = this.bins.iterator();

        while (iterator.hasNext()) {
            Bin bin = iterator.next();

            Double capacity = bin.getRemainingCapacity();

            if (capacity > largestCapacity) {

                largestCapacity = capacity;
            }
        }

        return largestCapacity;

    }

    @Override
    public void putSplittables(SplittablePool blocks) {
        //This algorithm iterates through the bins ordered by remaining capacity 
        //(smallest first) and places the block into the first available bin 
        //with sufficient capacity. If a space is not found then the block is 
        //split into two pieces, one with the size of the largest remaining 
        //capacity

        //Check that remaining capacity is sufficient
        
        
        System.out.println("Splittable blocks input size = "+blocks.getSplittables().size());
        
        
        if (getTotalRemainingCapacity() +getTolerance()>= blocks.getTotalSize()) {

            while (!blocks.getSplittables().isEmpty()) {
                List<Splittable> blocksList = blocks.getSplittables();
                 
                //Sort blocks into descending order of size
                Collections.sort(blocksList, blockComparator);
                
                //Sort bins into ascending order of size
                Collections.sort(bins,binComparator);
                
                Splittable block = blocksList.iterator().next();
                double blockSize = block.getSize();
                
                Iterator<Bin> iterator = bins.iterator();

                while (iterator.hasNext()) {

                    Bin bin = iterator.next();

                    if ((bin.getRemainingCapacity() + getTolerance()) >= blockSize) {

                        bin.putSplittable(block);
                        
                        System.out.println("Added block to bin: start = "+ block.getStart() +" end = "+block.getEnd() );
                        blocks.removeSplittable(block);
                        break;

                    } else if (!iterator.hasNext()) {

                        blocks.removeSplittable(block);
                        
                        Splittable b1 = block.duplicate();
                        Splittable b2 = block.duplicate();
                        
                        double l = getLargestRemainingCapacity();
                         
                        b1.setEnd(block.getStart()+l);
                        double b1Start = b1.getStart();
                        double b1End = b1.getEnd();
                        
                        
                        b2.setStart(b1.getEnd());
                        double b2Start = b2.getStart();
                        double b2End = b2.getEnd();
                        
                        
                        blocks.addSplittable(b1);
                        blocks.addSplittable(b2);

                    }
                }
            }
        }
    }

    /**
     * @return the tolerance
     */
    @Override
    public double getTolerance() {
        return tolerance;
    }

    /**
     * @param tolerance the tolerance to set
     */
    @Override
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }
}
