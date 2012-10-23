/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.editor;

import java.util.Iterator;
import org.dj.domainmodel.api.DJNodeObjectAbstr;
import org.dj.property.api.DJProperty;

/**
 *
 * @author djabry
 */
public class DoubleEntry extends DJNodeObjectAbstr{
    
    public static final String PROP_DOUBLE_VALUE = "doubleValue";
    
    
    public DoubleEntry(Double d){
        Iterator<DJProperty> iterator = this.getAllProperties().iterator();
        while(iterator.hasNext()){
            DJProperty next = iterator.next();
            next.setCanRead(false);
        }

        DJProperty<Double> dValProp = getPropertyFactory().createProperty(PROP_DOUBLE_VALUE, d);
        dValProp.setDisplayName("Value");
        this.putProperty(dValProp);
        
    }
    
    public double getDoubleValue(){
        
        return (Double)this.getPropertyValue(PROP_DOUBLE_VALUE);
    }
    
    public void setDoubleValue(double val){
        
        this.setPropertyValue(PROP_DOUBLE_VALUE, val);
    }
    
    
    
}
