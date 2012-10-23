/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile;

import java.util.Set;
import org.dj.domainmodel.api.DJNodeObjectAbstr;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;
import org.lblrtm.profile.api.ValueUnitCell;

/**
 *
 * @author djabry
 */
public abstract class ValueUnitCellAbstr extends DJNodeObjectAbstr implements ValueUnitCell{
    
    public ValueUnitCellAbstr(Enum e, Set<LBLRTMUnit> availableUnits, LBLRTMUnit unit, double value){
        super();
        this.setPropertyValue(PROP_AVAILABLE_UNITS, availableUnits);
        
        this.setPropertyValue(PROP_UNIT, unit);
        this.getProperty(PROP_UNIT).setNotify(true);
        
        this.setPropertyValue(PROP_VALUE, value);
        this.getProperty(PROP_VALUE).setNotify(true);
        
        this.setPropertyValue(PROP_ENUM, e);
        
    }
    

    @Override
    public Enum getEnum(){
        return (Enum)this.getPropertyValue(PROP_ENUM);
        
    }
    
    @Override
    public String getVariableName(){
        
        return getEnum().name();
    }

    @Override
    public Set<LBLRTMUnit> getAvailableUnits() {
        return (Set<LBLRTMUnit>) this.getPropertyValue(PROP_AVAILABLE_UNITS);
    }

    @Override
    public LBLRTMUnit getUnit() {
        return (LBLRTMUnit) this.getPropertyValue(PROP_UNIT);
    }

    @Override
    public void setUnit(LBLRTMUnit u) {
        
       
       refreshUnit(u);
       refreshValue(getValue());
    }
    
    public void refreshUnit(LBLRTMUnit u){
        
        if(this.getAvailableUnits().contains(u)){
            
            this.setPropertyValue(PROP_UNIT, u);
        }
    }

    @Override
    public double getValue() {
        
        return (Double)this.getPropertyValue(PROP_VALUE);
    }
    
    private void refreshValue(double val){
        
        if(getValue()>getMaxVal()){
            
            val = getMaxVal();
        }else if(this.getValue()<getMinVal()){
            
            val = getMinVal();
        }
        
        this.setPropertyValue(PROP_VALUE, val);
    }

    @Override
    public void setValue(double val) {
        
        
            refreshValue(val);
            
        
    }

    @Override
    public int compareTo(ValueUnitCell t) {
        return this.getIndex().compareTo(t.getIndex());
    }
    
    public double getMinVal(){
        return getUnit().min;
    }
    
    public double getMaxVal(){
        
        return getUnit().max;
    }
    

    
    
    
}
