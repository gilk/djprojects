/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.api;

import org.lblrtm.lblrtmdata.api.LBLRTMUnit;

/**
 *
 * @author djabry
 */
public interface DJPhysicalProperty extends ValueUnitCell{
    
    public static final String PROP_PHYSICAL_PROPERTY = "physicalProperty";

    
    PhysicalProperty getPhysicalProperty();
    public DJPhysicalProperty duplicate();
 
}
