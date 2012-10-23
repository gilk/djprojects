/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

import org.lblrtm.lblrtmdata.api.VariableInfo;

/**
 *
 * @author djabry
 */
public enum LBLRTMDimension {
    
    ALTITUDE("altitude",VariableInfo.ALTITUDE),
    LAYER("layer",VariableInfo.ALTITUDE),
    FREQUENCY("frequency",VariableInfo.WAVENUMBER);
    
    
    private final String fullName;
    private VariableInfo info;
    
    LBLRTMDimension(String name,VariableInfo info){
        
        this.fullName=name;
        this.info=info;
    }
    
  
    public String getFullName(){
        
        return this.fullName;
        
    }
    
    public VariableInfo getVariableInfo(){
        
        return this.info;
    }
    
    
}
