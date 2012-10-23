/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.api;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import java.util.Iterator;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManagerAbstr;
import org.dj.index.api.IntegerIndices;
import org.dj.index.api.RangeOperations;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class TaskRanges extends DJObjectManagerAbstr implements DJNodeObject.Source{

    private final LBLRTMParallelTask t;
    private static final RangeOperations rO = Lookup.getDefault().lookup(RangeOperations.class);
    private static final IntegerIndices iI   = Lookup.getDefault().lookup(IntegerIndices.class);
    public TaskRanges(LBLRTMParallelTask t){
        
        this.t=t;
        ((LBLRTMParallelTaskAbstr)this.t).pullRanges();
        Iterator<Range<Integer>> iterator = t.getRanges().allRanges();
        while(iterator.hasNext()){
            this.createObject(iterator.next());
        }
    }
    
    DJNodeObject createObject(Range<Integer> r){
        //TaskRange rng = new TaskRange(this.t,r);
        //this.addChild(rng);
        //return rng;
        throw new UnsupportedOperationException("");
    }
    
    private Range<Integer> generateNewRange(){
        
        int first = 0;
        int last = 1; 
        Iterator<Range<Integer>> iterator = t.getRanges().allRanges();
        
        while(iterator.hasNext()){
            
           Range<Integer> r = iterator.next();
            
            if(r.lowerEndpoint()>=first){
                
                first = r.upperEndpoint()+1;
                last = first+rO.lengthOf(r)-1;
            }
            
        }
        
        return Ranges.closed(first,last);
    }

    
    @Override
    public DJNodeObject createObject() {
        return createObject(this.generateNewRange());
    }
    
    @Override
    public void removeChild(DJNodeObject obj, Integer dim, boolean notify) {
        super.removeChild(obj, dim, notify);
        
         if(obj instanceof TaskRange){
            TaskRange tR = (TaskRange) obj;
            //t.getRanges().remove(tR.getRange());
        }
    }
    
}
