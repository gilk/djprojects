/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

/**
 *
 * @author djabry
 */
public enum RadianceUnits {
    
    RADIANCE("Spectral radiance"),
    BT("Brightness temperature");
    
    public final String fullName;
    
    RadianceUnits(String fullName){
        
        this.fullName=fullName;
    }

    @Override
    public String toString() {
        return fullName;
    }
    
    
    
    
    
    
}
