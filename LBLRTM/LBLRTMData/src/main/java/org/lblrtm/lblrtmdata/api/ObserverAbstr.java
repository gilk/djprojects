/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

import org.dj.domainmodel.api.DJNodeObjectAbstr;

/**
 *
 * @author djabry
 */
public class ObserverAbstr extends DJNodeObjectAbstr implements Observer {
    
    public ObserverAbstr(){
        super();
        setPropertyValue(PROP_ALTITUDE,90.0);
    }
    
    public ObserverAbstr(double alt){
        
        this();
        setAltitude(alt);
    }

    @Override
    public double getAltitude() {
        return (Double)getPropertyValue(PROP_ALTITUDE);
    }

    @Override
    public void setAltitude(double altitude) {
        this.setPropertyValue(PROP_ALTITUDE, altitude);
    }

    @Override
    public Observer duplicate() {
        return new ObserverAbstr(this.getAltitude());
    }
    
    
    
}
