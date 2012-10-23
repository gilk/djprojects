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
public interface LBLRTMUnits {
    
    public static final Unit RECIPROCAL_CENTIMETER = SI.HECTO(Unit.ONE.divide(SI.METER));
    
    //Spectroscopy definition of spectral radiance (W/[m^2*sr*cm^(-1)])
    public static final Unit SPECTRAL_RADIANCE_SPEC_DEF =   ((SI.WATT.divide(SI.SQUARE_METRE)).divide(SI.STERADIAN)).divide(RECIPROCAL_CENTIMETER);

    


}
