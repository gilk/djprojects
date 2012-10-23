/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * @author djabry
 */
public enum LBLRTMUnit {
    
    VOLUME_MIXING_RATIO(SI.MEGA(Unit.ONE),
            "A","ppmv","Volume mixing ratio",0.0,1.0e6),
    NUMBER_DENSITY(Unit.ONE.divide(SI.CUBIC_METRE.divide(1e6)),"B","cm\u207B\u00B3","Number density",0.0,1e30),
    MASS_MIXING_RATIO(SI.GRAM.divide(SI.KILOGRAM),"C","g/kg","Mass mixing ratio",0.0,1e3),
    MASS_DENSITY(SI.GRAM.divide(SI.CUBIC_METRE),"D","g m\u207B\u00B3","Mass density",0.0,1e3),
    PARTIAL_PRESSURE(SI.PASCAL.times(100.0),"E","mb","Partial pressure",0,1e4),
    DEW_POINT_K(SI.KELVIN,"F","K","Dew point (K)",0,1e3),
    DEW_POINT_C(SI.CELSIUS,"G","C","Dew point (C)",-273.0,1e3),
    RELATIVE_HUMIDITY(SI.HECTO(Unit.ONE),"H","%","Relative humidity",0,100),

    MILLIBAR(SI.PASCAL.times(100.0),"A","mb","Millibar",0,1e4),
    TORR(SI.PASCAL.times(133.322),"B","Torr","Torr",0,1e4),
    ATM(SI.PASCAL.times(1.01325e5),"C","atm","Atmosphere",0,2),
    KILOMETRE(SI.METRE.times(1000.0),"","km","Kilometre",0,1e3),
    KELVIN(SI.KELVIN,"A","K","Kelvin",0,1e3),
    CELCIUS(SI.CELSIUS,"B","C","Celcius",-273,1e3),
    
    USER( Unit.ONE,"0","User","User-defined"),
    TROPICAL(Unit.ONE, "1","TRP","Tropical"),
    MLS(Unit.ONE,"2","MLS","Mid-latitude summer"),
    MLW(Unit.ONE,"3","MLW","Mid-latitude winter"),
    SAS(Unit.ONE,"4","SAS","Sub-arctic summer"),
    SAW(Unit.ONE,"5","SAW","Sub-arctic winter"),
    US(Unit.ONE,"6","US","US standard 1976");

    LBLRTMUnit(Unit pU, 
            String lString,
            String symbol, 
            String name,
            double min,
            double max){
        
        this.unit=pU;
        this.lBLRTMString =lString;
        this.symbol= symbol;
        this.fullName=name;
        this.min=min;
        this.max= max;
    }
    
    LBLRTMUnit(Unit pU,
            String lString,
            String symbol,
            String name){
        
        this(pU,lString,symbol,name,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY);
    }

    @Override
    public String toString() {
        return this.fullName;
    }
    
    
    
    public final Unit unit;
    public final String lBLRTMString;
    public final String symbol;
    public final String fullName;
    public final double min;
    public final double max;
    
    
    
    
}
