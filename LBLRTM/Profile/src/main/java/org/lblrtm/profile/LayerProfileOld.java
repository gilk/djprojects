/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile;

import java.util.List;
import org.dj.parallel.api.ContinuousRange;

/**
 *
 * @author djabry
 */
public interface LayerProfileOld extends LBLRTMProfile{
    
    public static final String PROP_ALTITUDE_RANGES = "altitudeRanges";
    public static final String PROP_ALTITUDE_FROM = "altitudeFrom";
    public static final String PROP_ALTITUDE_TO = "altitudeTo";
    
    List<ContinuousRange> getAltitudeRanges();
    
}
