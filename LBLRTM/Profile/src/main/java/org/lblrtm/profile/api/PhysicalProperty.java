/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javolution.util.FastSet;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;

/**
 *
 * @author djabry
 */
public enum PhysicalProperty {
    
    ALTITUDE_FROM("Altitude (from)",new HashSet(Arrays.asList(
            new LBLRTMUnit[]{LBLRTMUnit.KILOMETRE})), LBLRTMUnit.KILOMETRE, 0.0),
    
    ALTITUDE_TO("Altitude (to)",new HashSet(Arrays.asList(
            new LBLRTMUnit[]{LBLRTMUnit.KILOMETRE})), LBLRTMUnit.KILOMETRE, 0.0),
    
    ALTITUDE("Altitude",new HashSet(Arrays.asList(
            new LBLRTMUnit[]{LBLRTMUnit.KILOMETRE})), LBLRTMUnit.KILOMETRE, 0.0),
    
    PRESSURE("Pressure",new HashSet(Arrays.asList(
            new LBLRTMUnit[]{
                LBLRTMUnit.MILLIBAR,
                LBLRTMUnit.TORR,
                LBLRTMUnit.ATM})), 
            LBLRTMUnit.MILLIBAR, 
            1000.0),
    
    TEMPERATURE("Temperature",new HashSet(Arrays.asList(
            new LBLRTMUnit[]{LBLRTMUnit.KELVIN,LBLRTMUnit.CELCIUS})), LBLRTMUnit.KELVIN, 300.0);
    
   ;
    
    PhysicalProperty(String fullName, Set<LBLRTMUnit> availableUnits,LBLRTMUnit defaultUnit, double defaultValue){
        this.fullName=fullName;
        this.availableUnits=availableUnits;
        this.defaultUnit=defaultUnit;
        this.defaultValue = defaultValue;
        
    }
    
    public final String fullName;
    public final Set<LBLRTMUnit> availableUnits;
    public final LBLRTMUnit defaultUnit;
    public final double defaultValue;
    
}
