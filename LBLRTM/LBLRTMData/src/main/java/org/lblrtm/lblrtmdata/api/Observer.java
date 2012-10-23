/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

import org.dj.domainmodel.api.DJObject;

/**
 *
 * @author djabry
 */
public interface Observer extends DJObject {
    
    public static final String PROP_ALTITUDE = "altitude";
    
    double getAltitude();
    void setAltitude(double altitude);
    
    Observer duplicate();
    
}
