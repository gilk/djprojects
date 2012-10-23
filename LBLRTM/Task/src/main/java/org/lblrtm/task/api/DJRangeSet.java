/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.api;

import java.util.*;

import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManagerAbstr;
import org.dj.parallel.api.ContinuousRange;
import org.dj.parallel.api.CRangeImpl;

/**
 *
 * @author djabry
 */
public class DJRangeSet extends DJObjectManagerAbstr implements DJNodeObject.Source{
    
    private final Set<ContinuousRange> ranges;

    
    public DJRangeSet(Set<ContinuousRange> ranges){
        this.ranges=ranges;
        
        for(ContinuousRange r:ranges){
            
            this.addChild(new DJRangeObject(r)); 
        }
    }

    private ContinuousRange generateNewRange(){
        
        
        double first = 0;
        double last = 1.0; 
        Iterator<ContinuousRange> iterator = ranges.iterator();
        
        while(iterator.hasNext()){
            
            ContinuousRange r = iterator.next();
            
            if(r.getStart()>=first){
                
                first = r.getEnd();
                last = r.getEnd()+r.getSize();
            }
            
        }
        
        return new CRangeImpl(first,last);
    }

    @Override
    public DJNodeObject createObject() {
        ContinuousRange r = generateNewRange();
        
        this.ranges.add(r);
        
        DJRangeObject rO = new DJRangeObject(r);
        this.addChild(rO);
        
        return rO;
    }

    @Override
    public void removeChild(DJNodeObject obj, Integer dim, boolean notify) {
        super.removeChild(obj, dim, notify);
        
         if(obj instanceof DJRangeObject){
            DJRangeObject djRangeObject = (DJRangeObject) obj;
            this.ranges.remove(djRangeObject.getOriginalRange());
        }
    }
    

    
}
