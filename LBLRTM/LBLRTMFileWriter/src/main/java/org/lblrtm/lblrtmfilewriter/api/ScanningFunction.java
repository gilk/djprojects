/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

/**
 *
 * @author djabry
 */
public enum ScanningFunction {
    
    
    BOXCAR("Boxcar",0),
    TRIANGLE("Triangle",1),
    GAUSS("Gauss",2),
    SINC_SQUARED("Sinc squared",3),
    SINC("Sinc",4),
    BEER("Beer",5),
    HAMMING("Hamming",6),
    HANNING("Hanning",7),
    NORTON_BEER_WEAK("Norton-Beer (Weak)",8),
    NORTON_BEER_MODERATE("Norton-Beer (Moderate)",9),
    NORTON_BEER_STRONG("Norton-Beer (String)",10),
    BRAULT("Brault",11),
    KAISER_BESSEL("Kaiser-Bessel",12),
    KIRUNA("Kiruna",13);
    
    public final int index;
    public final String fullName;
            
    ScanningFunction(String name, int index){
        
        this.fullName= name;
        this.index = index;
    }

    @Override
    public String toString() {
        return this.fullName;
    }
    
    
    
    
            
}
