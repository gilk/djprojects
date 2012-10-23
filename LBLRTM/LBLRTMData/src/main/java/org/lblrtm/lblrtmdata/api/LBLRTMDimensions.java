/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

import ucar.nc2.Dimension;

/**
 *
 * @author djabry
 */
public interface LBLRTMDimensions {
    
    public static final Dimension V = new Dimension("v",1,true,true,false); 
    public static final Dimension LEVEL = new Dimension("level",1,true, true,false);
    
}
