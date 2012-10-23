/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.api;

import java.util.Set;
import org.dj.domainmodel.api.DJNodeObject;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;

/**
 *
 * @author djabry
 */
public interface ValueUnitCell extends DJNodeObject, Comparable<ValueUnitCell>{
    
    public static final String PROP_VALUE = "value"; 
    public static final String PROP_UNIT  = "unit";  
    public static final String PROP_AVAILABLE_UNITS = "availableUnits";
    public static final String PROP_VARIABLE_NAME = "variableName";
    public static final String PROP_ENUM = "enum";
    
    Set<LBLRTMUnit> getAvailableUnits();
    
    String getVariableName();
    Enum getEnum();
    
    LBLRTMUnit getUnit();
    void setUnit(LBLRTMUnit u);
    
    double getValue();
    void setValue(double val);
    
    public ValueUnitCell duplicate();
    
    public Integer getIndex();
    
}
