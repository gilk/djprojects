/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

/**
 *
 * @author djabry
 */
public enum BoundaryUnits {
    
    ALTITUDE("Altitude",1),
    PRESSURE("Pressure",-1);
    
    public final String fullName;
    public final int coefficient;
    
    BoundaryUnits(String fullName, int coeff){
        
        this.fullName=fullName;
        this.coefficient=coeff;
    }

    @Override
    public String toString() {
        return fullName;
    }
    
    
    
}
