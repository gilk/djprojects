/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.index;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import java.util.Iterator;
import java.util.Set;
import org.dj.index.api.RangeOperations;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service= RangeOperations.class)
public class RangeOperationsImpl implements RangeOperations{

    @Override
    public <C extends Comparable> C lengthOf(Range<C> r) {
       
        C v  = r.upperEndpoint();
       if(v instanceof Integer){
           return (C)intLength((Range<Integer>)r);
       }else if(v instanceof Long){
           return (C)longLength((Range<Long>)r);
       }
       
       throw new UnsupportedOperationException("C must be an integer or long");
       
       
    }
    
    private Long longLength(Range<Long> r){
        return r.upperEndpoint()-r.lowerEndpoint()+1;
        
    }
    
    private Integer intLength(Range<Integer> r){
        
        return r.upperEndpoint()-r.lowerEndpoint()+1;
    }
    

    private <C extends Comparable> C plus(C a, C b){
        
        if(a instanceof Integer){
            
            Integer c  = ((Integer)a)+((Integer)b);
            return (C)c;
        }else if (a instanceof Long){
            Long c  = ((Long)a)+((Long)b);
            return (C)c;
            
        }
        throw new UnsupportedOperationException("C must be an integer or long");
    }
    

    @Override
    public <C extends Comparable> C lengthOf(Iterable<Range<C>> ranges) {
        Iterator<Range<C>> iterator = ranges.iterator();
       
        C sum = null;
        while(iterator.hasNext()){
            Range<C> next = iterator.next();
            C l = lengthOf(next);
            
            if(sum==null){
                
                sum = l;
            }else{
                
                sum = plus(sum,l);
            }
            
        }
        
        if(sum ==null){
            
            return (C)(Integer)0;
            
        }
        
        return sum;
    }

    @Override
    public <C extends Comparable> Range<C> duplicate(Range<C> r) {
        return Ranges.closed(r.lowerEndpoint(), r.upperEndpoint());
    }

    @Override
    public <C extends Comparable> C maxValue(Iterable<Range<C>> ranges) {
        C c=null;
        Iterator<Range<C>> iterator = ranges.iterator();
        while(iterator.hasNext()){
            
            C next = iterator.next().upperEndpoint();
            if(c==null){
                
                c = next;
            }else{
                
                if(next.compareTo(c)==1){
                    
                    c=next;
                }
            }
            
            
        }
        
        return c;
    }

    @Override
    public <C extends Comparable> C minValue(Iterable<Range<C>> ranges) {
         C c=null;
        Iterator<Range<C>> iterator = ranges.iterator();
        while(iterator.hasNext()){
            
            C next = iterator.next().lowerEndpoint();
            if(c==null){
                
                c = next;
            }else{
                
                if(next.compareTo(c)==-1){
                    
                    c=next;
                }
            }
            
            
        }
        
        return c;
    }


    
    
}
