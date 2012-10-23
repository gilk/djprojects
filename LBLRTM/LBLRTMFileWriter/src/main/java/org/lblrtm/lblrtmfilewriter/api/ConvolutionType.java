/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

/**
 *
 * @author djabry
 */
public enum ConvolutionType {
    
    TRANSMITTANCE("Transmittance",0),
    RADIANCE("Radiance",1);
    
    public final String fullName;
    public final int index;
    
    ConvolutionType(String fullName, int index){
        this.fullName=fullName;
        this.index=index;
        
        
    }

    @Override
    public String toString() {
        return this.fullName;
    }
    
    
        
        
        
    
    
}
