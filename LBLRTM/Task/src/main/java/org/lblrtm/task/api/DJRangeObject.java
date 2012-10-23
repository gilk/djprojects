/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.api;

import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJNodeObjectAbstr;
import org.dj.parallel.api.ContinuousRange;
import org.dj.property.api.DJProperty;
import org.dj.property.api.PropertyAbstr;

/**
 *
 * @author djabry
 */

//Allows Range object to be edited by user
public class DJRangeObject extends DJNodeObjectAbstr {
    
    public static final String PROP_START = "start";
    public static final String PROP_END="end";
    private final ContinuousRange r;
    
    public DJRangeObject(ContinuousRange range){
        super();
        
        for(DJProperty p: this.getAllProperties()){
            
            p.setCanRead(false);
 
        }
        
        this.r=range;
        
        DJProperty<Double> startProp = new PropertyAbstr<Double>(PROP_START, r.getStart()){

            @Override
            public Double getValue() {
                return r.getStart();
            }

            @Override
            public boolean setValue(Double value) {
                r.setStart(value);
                return true;
            }
            
        };
        
        startProp.setDisplayName("Start");
        
        this.putProperty(startProp);
        
        
         DJProperty<Double> endProp = new PropertyAbstr<Double>(PROP_END, r.getEnd()){

            @Override
            public Double getValue() {
                return r.getEnd();
            }

            @Override
            public boolean setValue(Double value) {
                r.setEnd(value);
                return true;
            }
            
        };
        
        endProp.setDisplayName("End");
        
        this.putProperty(endProp);

    }


    
    public ContinuousRange getOriginalRange(){
        
        return r;
    }
    


    public double getStart() {
        return r.getStart();
    }


    public double getEnd() {
        return r.getEnd();
    }


    public double getSize() {
        return r.getSize();
    }


    public void setStart(double start) {
       
        this.setPropertyValue(PROP_START, start);
        
    }


    public void setEnd(double end) {
        this.setPropertyValue(PROP_END, end);
        
    }


    
}
