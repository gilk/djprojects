/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

/**
 *
 * @author djabry
 */
public enum CalculationType {
    
    FFT_OF_APODIZATION("FFT of apodization function",-1),
    AUTO("Automatic",0),
    ANALYTICAL("Analytical",1);
    
    public final String fullName;
    public final int index;
    
    CalculationType(String fullName, int index){
    
        this.fullName = fullName;
        this.index=index;
    
    }

    @Override
    public String toString() {
        return this.fullName;
    }
    
    
    
}
