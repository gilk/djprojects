/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;


import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import org.lblrtm.lblrtmdata.api.LBLRTMUnits;

/**
 *
 * @author djabry
 */


public enum VariableInfo {
        
        
        RADIANCE("radiance", LBLRTMUnits.SPECTRAL_RADIANCE_SPEC_DEF),
        
        TRANSMITTANCE("transmittance", Unit.ONE),
        
        OPTICALDEPTH("optical_depth",Unit.ONE),
        
        //VMR
        VOLUME_MIXING_RATIO("mixing_ratio",Unit.ONE),
        
        PPMV("ppmv", Unit.ONE.times(1e6)),
        
        //Measured in km
        ALTITUDE("altitude",SI.METRE.times(1000)),
        
        //Pressure in mb
        PRESSURE("pressure",SI.PASCAL.times(100)),
        
        WAVENUMBER("wavenumber", LBLRTMUnits.RECIPROCAL_CENTIMETER),
        
        TEMPERATURE("temperature", SI.KELVIN);
        
        
        private final String longName;
        private final Unit unit;

        VariableInfo(String longName, Unit unit) {

            this.longName = longName;
            this.unit = unit;
        }

    /**
     * @return the longName
     */
    public String getLongName() {
        return longName;
    }

    /**
     * @return the unit
     */
    public Unit getUnit() {
        return unit;
    }
    }
