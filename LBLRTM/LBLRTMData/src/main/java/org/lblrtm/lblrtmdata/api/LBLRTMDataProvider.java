/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

import javax.measure.unit.Unit;




/**
 *
 * @author djabry
 */
public interface LBLRTMDataProvider {
    
    Number[] getY();
    Number[] getX();
    
    int getNumberOfPoints();
    
    Unit getXUnits();
    Unit getYUnits();
    
  
    
    
    
}
